package cs203.ftms.overall;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.transaction.annotation.Transactional;

import cs203.ftms.overall.dto.CreateEventDTO;
import cs203.ftms.overall.dto.CreatePoulesDTO;
import cs203.ftms.overall.dto.CreateTournamentDTO;
import cs203.ftms.overall.dto.RegisterAdminDTO;
import cs203.ftms.overall.dto.RegisterFencerDTO;
import cs203.ftms.overall.dto.RegisterOrganiserDTO;
import cs203.ftms.overall.dto.CompleteFencerProfileDTO;
import cs203.ftms.overall.model.userrelated.Fencer;
import cs203.ftms.overall.model.userrelated.Organiser;
import cs203.ftms.overall.model.tournamentrelated.*;
import cs203.ftms.overall.repository.tournamentrelated.EventRepository;
import cs203.ftms.overall.repository.tournamentrelated.TournamentRepository;
import cs203.ftms.overall.repository.userrelated.UserRepository;
import cs203.ftms.overall.service.authentication.AuthenticationService;
import cs203.ftms.overall.service.event.EventService;
import cs203.ftms.overall.service.fencer.FencerService;
import cs203.ftms.overall.service.tournament.TournamentService;

@Component
public class PopulateData {
    private final Random random = new Random();
    private static final int ADMIN_COUNT = 1;
    private static final int ORGANISER_COUNT = 1;
    private static final int FENCER_COUNT = 10;

    private final AuthenticationService authenticationService;
    private final TournamentService tournamentService;
    private final EventService eventService;
    private final FencerService fencerService;

    private final UserRepository userRepository;
    private final TournamentRepository tournamentRepository;
    private final EventRepository eventRepository;

    @Autowired
    public PopulateData(AuthenticationService authenticationService, TournamentService tournamentService, EventService eventService, FencerService fencerService, UserRepository userRepository, TournamentRepository tournamentRepository, EventRepository eventRepository) {
        this.authenticationService = authenticationService;
        this.tournamentService = tournamentService;
        this.eventService = eventService;
        this.fencerService = fencerService;
        this.userRepository = userRepository;
        this.tournamentRepository = tournamentRepository;
        this.eventRepository = eventRepository;
    }

    public void createAdmin() {
        for (int i = 1; i <= ADMIN_COUNT; i++) {
            authenticationService.createAdmin(new RegisterAdminDTO("Admin", "admin" + i + "@xyz.com", "+6591234567", "Abcd1234!", "Singapore"));
        }
    }
    
    public void createOrganiser() {
        for (int i = 1; i <= ORGANISER_COUNT; i++) {
            Organiser o = (Organiser) authenticationService.createOrganiser(new RegisterOrganiserDTO("Organiser", "organiser" + i + "@xyz.com", "+6591234567", "Abcd1234!", "Singapore"));
            o.setVerified(true);
        }
    }

    public void createFencer() throws MethodArgumentNotValidException {
        for (int i = 1; i <= FENCER_COUNT; i++) {
            Fencer f = (Fencer) authenticationService.createFencer(new RegisterFencerDTO("Fencer", "" + i, "fencer" + i + "@gmail.com", "Abcd1234!", "+6591234567", "Singapore", LocalDate.of(1999,1,1)));
            fencerService.completeProfile(f, new CompleteFencerProfileDTO('L', 'S', 'M', "Club", 2021));
            f.setPoints(random.nextInt(1000));
        }
    }

    public void createTournament() throws MethodArgumentNotValidException {
        Tournament t = tournamentService.createTournament(new CreateTournamentDTO("Tournament", LocalDate.of(2024, 12, 10), 70, LocalDate.of(2024, 12, 20), LocalDate.of(2024, 12, 30), "location", "description", "rules"), (Organiser) userRepository.findByEmail("organiser1@xyz.com").get());
    }

    public void createEvent() throws MethodArgumentNotValidException {
        List<Event> eList = eventService.createEvent(tournamentRepository.findByName("Tournament").get().getId(), (Organiser) userRepository.findByEmail("organiser1@xyz.com").get(), List.of(new CreateEventDTO('M', 'S', 10, LocalDate.of(2024, 12, 30), LocalTime.of(10, 0, 0), LocalTime.of(17, 0, 0))));
        // Event e = eList.get(0);
        // e.setDate(LocalDate.of(2024, 10, 8));
        // eventRepository.save(e);
    }

    public void registerFencerForEvent() {
        for (int i = 1; i <= FENCER_COUNT; i++) {
            eventService.registerEvent(eventRepository.findByTournamentAndGenderAndWeapon(tournamentRepository.findByName("Tournament").get(), 'M', 'S').get().getId(), (Fencer) userRepository.findByEmail("fencer" + i + "@gmail.com").get());
        }
    }

    // public void createPoules() {
    //     Set<String> recommendation = eventService.recommendPoules(tournamentRepository.findByName("Tournament").get().getId());
    //     String[] recommendationArray = recommendation.toArray(new String[0]);
    //     int poulesCount = recommendationArray[recommendationArray.length - 1].charAt(0) - '0';
    //     eventService.createPoules(tournamentRepository.findByName("Tournament").get().getId(), new CreatePoulesDTO(poulesCount));
    // }

    @EventListener(ContextRefreshedEvent.class)
    @Transactional
    public void populateData() throws MethodArgumentNotValidException {
        if (userRepository.count() > 0) {
            return;
        }

        createAdmin();
        createOrganiser();
        createFencer();
        createTournament();
        createEvent();
        registerFencerForEvent();
        // createPoules();
    }
}