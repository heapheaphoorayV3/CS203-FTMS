package cs203.ftms.overall;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;

import cs203.ftms.overall.dto.CompleteFencerProfileDTO;
import cs203.ftms.overall.dto.CreateEventDTO;
import cs203.ftms.overall.dto.CreatePoulesDTO;
import cs203.ftms.overall.dto.CreateTournamentDTO;
import cs203.ftms.overall.dto.PouleResultsDTO;
import cs203.ftms.overall.dto.PouleTableDTO;
import cs203.ftms.overall.dto.RegisterAdminDTO;
import cs203.ftms.overall.dto.RegisterFencerDTO;
import cs203.ftms.overall.dto.RegisterOrganiserDTO;
import cs203.ftms.overall.dto.SinglePouleTableDTO;
import cs203.ftms.overall.dto.clean.CleanTournamentFencerDTO;
import cs203.ftms.overall.model.userrelated.Fencer;
import cs203.ftms.overall.model.userrelated.Organiser;
import cs203.ftms.overall.repository.tournamentrelated.EventRepository;
import cs203.ftms.overall.repository.tournamentrelated.PouleRepository;
import cs203.ftms.overall.repository.tournamentrelated.TournamentRepository;
import cs203.ftms.overall.repository.userrelated.UserRepository;
import cs203.ftms.overall.service.authentication.AuthenticationService;
import cs203.ftms.overall.service.event.EventService;
import cs203.ftms.overall.service.fencer.FencerService;
import cs203.ftms.overall.service.match.PouleService;
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
    private final PouleService pouleService;

    private final UserRepository userRepository;
    private final TournamentRepository tournamentRepository;
    private final EventRepository eventRepository;
    private final PouleRepository pouleRepository; 

    @Autowired
    public PopulateData(AuthenticationService authenticationService, TournamentService tournamentService, EventService eventService, FencerService fencerService, PouleService poulesService, UserRepository userRepository, TournamentRepository tournamentRepository, EventRepository eventRepository, PouleRepository pouleRepository) {
        this.authenticationService = authenticationService;
        this.tournamentService = tournamentService;
        this.eventService = eventService;
        this.fencerService = fencerService;
        this.pouleService = poulesService;
        this.userRepository = userRepository;
        this.tournamentRepository = tournamentRepository;
        this.eventRepository = eventRepository;
        this.pouleRepository = pouleRepository;
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

    public void createMenSabreFencer() throws MethodArgumentNotValidException {
        for (int i = 1; i <= FENCER_COUNT; i++) {
            Fencer f = (Fencer) authenticationService.createFencer(new RegisterFencerDTO("MSFencer", "" + i, "MSfencer" + i + "@gmail.com", "Abcd1234!", "+6591234567", "Singapore", LocalDate.of(1999,1,1)));
            fencerService.completeProfile(f, new CompleteFencerProfileDTO('L', 'S', 'M', "Club", 2021));
            // f.setPoints(random.nextInt(1000));
            f.setPoints(i * 100);
        }
    }
    public void createWomenSabreFencer() throws MethodArgumentNotValidException {
        for (int i = 1; i <= FENCER_COUNT; i++) {
            Fencer f = (Fencer) authenticationService.createFencer(new RegisterFencerDTO("WSFencer", "" + i, "WSfencer" + i + "@gmail.com", "Abcd1234!", "+6591234567", "Singapore", LocalDate.of(1999,1,1)));
            fencerService.completeProfile(f, new CompleteFencerProfileDTO('L', 'S', 'W', "Club", 2021));
            // f.setPoints(random.nextInt(1000));
            f.setPoints(i * 100);
        }
    }
    public void createMenEpeeFencer() throws MethodArgumentNotValidException {
        for (int i = 1; i <= FENCER_COUNT; i++) {
            Fencer f = (Fencer) authenticationService.createFencer(new RegisterFencerDTO("MEFencer", "" + i, "MEfencer" + i + "@gmail.com", "Abcd1234!", "+6591234567", "Singapore", LocalDate.of(1999,1,1)));
            fencerService.completeProfile(f, new CompleteFencerProfileDTO('L', 'E', 'M', "Club", 2021));
            // f.setPoints(random.nextInt(1000));
            f.setPoints(i * 100);
        }
    }
    public void createWomenEpeeFencer() throws MethodArgumentNotValidException {
        for (int i = 1; i <= FENCER_COUNT; i++) {
            Fencer f = (Fencer) authenticationService.createFencer(new RegisterFencerDTO("WEFencer", "" + i, "WEfencer" + i + "@gmail.com", "Abcd1234!", "+6591234567", "Singapore", LocalDate.of(1999,1,1)));
            fencerService.completeProfile(f, new CompleteFencerProfileDTO('L', 'E', 'W', "Club", 2021));
            // f.setPoints(random.nextInt(1000));
            f.setPoints(i * 100);
        }
    }
    public void createMenFoilFencer() throws MethodArgumentNotValidException {
        for (int i = 1; i <= FENCER_COUNT; i++) {
            Fencer f = (Fencer) authenticationService.createFencer(new RegisterFencerDTO("MFFencer", "" + i, "MFfencer" + i + "@gmail.com", "Abcd1234!", "+6591234567", "Singapore", LocalDate.of(1999,1,1)));
            fencerService.completeProfile(f, new CompleteFencerProfileDTO('L', 'F', 'M', "Club", 2021));
            // f.setPoints(random.nextInt(1000));
            f.setPoints(i * 100);
        }
    }
    public void createWomenFoilFencer() throws MethodArgumentNotValidException {
        for (int i = 1; i <= FENCER_COUNT; i++) {
            Fencer f = (Fencer) authenticationService.createFencer(new RegisterFencerDTO("WFFencer", "" + i, "WFfencer" + i + "@gmail.com", "Abcd1234!", "+6591234567", "Singapore", LocalDate.of(1999,1,1)));
            fencerService.completeProfile(f, new CompleteFencerProfileDTO('L', 'F', 'W', "Club", 2021));
            // f.setPoints(random.nextInt(1000));
            f.setPoints(i * 100);
        }
    }

    public void createTournament() throws MethodArgumentNotValidException {
        tournamentService.createTournament(new CreateTournamentDTO("Tournament0", LocalDate.of(2024, 12, 10), 100, LocalDate.of(2024, 12, 20), LocalDate.of(2024, 12, 30), "location", "description", "rules", 'B'), (Organiser) userRepository.findByEmail("organiser1@xyz.com").get());
        tournamentService.createTournament(new CreateTournamentDTO("Tournament1", LocalDate.of(2024, 12, 10), 100, LocalDate.of(2024, 12, 20), LocalDate.of(2024, 12, 30), "location", "description", "rules", 'I'), (Organiser) userRepository.findByEmail("organiser1@xyz.com").get());
        tournamentService.createTournament(new CreateTournamentDTO("Tournament2", LocalDate.of(2024, 12, 10), 100, LocalDate.of(2024, 12, 20), LocalDate.of(2024, 12, 30), "location", "description", "rules", 'A'), (Organiser) userRepository.findByEmail("organiser1@xyz.com").get());

    }

    public void createEvent() throws MethodArgumentNotValidException {
        for(int i = 0; i < 3; i++){
            eventService.createEvent(tournamentRepository.findByName("Tournament" + i).get().getId(), (Organiser) userRepository.findByEmail("organiser1@xyz.com").get(), List.of(new CreateEventDTO('M', 'S', 10, LocalDate.of(2024, 12, 30), LocalTime.of(10, 0, 0), LocalTime.of(17, 0, 0))));
        }
    }

    public void registerFencerForEvent() {
        for(int i = 0; i < 3; i++){
            for (int j = 1; j <= FENCER_COUNT; j++) {
                eventService.registerEvent(eventRepository.findByTournamentAndGenderAndWeapon(tournamentRepository.findByName("Tournament" + i).get(), 'M', 'S').get().getId(), (Fencer) userRepository.findByEmail("MSfencer" + j + "@gmail.com").get());
            }
        }
    }

    public void createPoules() {
        Set<String> recommendation = pouleService.recommendPoules(tournamentRepository.findByName("Tournament").get().getId());
        String[] recommendationArray = recommendation.toArray(new String[0]);
        int poulesCount = recommendationArray[recommendationArray.length - 1].charAt(0) - '0';
        pouleService.createPoules(eventRepository.findByTournamentAndGenderAndWeapon(tournamentRepository.findByName("Tournament").get(), 'M', 'S').get().getId(), new CreatePoulesDTO(poulesCount));
    }

    public void createPouleMatches() {
        PouleTableDTO dto = pouleService.getPouleTable(eventRepository.findByTournamentAndGenderAndWeapon(tournamentRepository.findByName("Tournament").get(), 'M', 'S').get().getId(), true);
        for (Map<String,String> line : dto.getPouleTable()) {
            for (String key : line.keySet()) {
                System.out.println(key + " " + line.get(key));
            }
        }
        System.out.println("end of poule table display");
    }

    public void updatePouleTable() throws MethodArgumentNotValidException {
        Map<String, String> singlePoule1 = new LinkedHashMap<>();
        singlePoule1.put("9 Fencer (Singapore) -- 9", "-1,5,5,5,5");
        singlePoule1.put("7 Fencer (Singapore) -- 7", "0,-1,5,5,5");
        singlePoule1.put("5 Fencer (Singapore) -- 5", "0,2,-1,5,5");
        singlePoule1.put("3 Fencer (Singapore) -- 3", "0,3,2,-1,5");
        singlePoule1.put("1 Fencer (Singapore) -- 1", "0,3,1,3,-1");
        pouleService.updatePouleTable(eventRepository.findByTournamentAndGenderAndWeapon(tournamentRepository.findByName("Tournament").get(), 'M', 'S').get().getId(), new SinglePouleTableDTO(2, singlePoule1));
        Map<String, String> singlePoule2 = new LinkedHashMap<>();
        singlePoule2.put("10 Fencer (Singapore) -- 10", "-1,5,5,5,5");
        singlePoule2.put("8 Fencer (Singapore) -- 8", "0,-1,5,5,5");
        singlePoule2.put("6 Fencer (Singapore) -- 6", "0,2,-1,5,5");
        singlePoule2.put("4 Fencer (Singapore) -- 4", "0,3,2,-1,5");
        singlePoule2.put("2 Fencer (Singapore) -- 2", "0,3,1,3,-1");
        pouleService.updatePouleTable(eventRepository.findByTournamentAndGenderAndWeapon(tournamentRepository.findByName("Tournament").get(), 'M', 'S').get().getId(), new SinglePouleTableDTO(1, singlePoule2));
    }

    public void printUpdatedPouleTable() {
        PouleTableDTO dto = pouleService.getPouleTable(eventRepository.findByTournamentAndGenderAndWeapon(tournamentRepository.findByName("Tournament").get(), 'M', 'S').get().getId(), false);
        for (Map<String,String> line : dto.getPouleTable()) {
            for (String key : line.keySet()) {
                System.out.println(key + " " + line.get(key));
            }
        }
    }

    public void printPouleResult() {
        PouleResultsDTO pouleResults = pouleService.poulesResult(eventRepository.findByTournamentAndGenderAndWeapon(tournamentRepository.findByName("Tournament").get(), 'M', 'S').get().getId());
        System.out.println("Bypass Fencers:");
        for (CleanTournamentFencerDTO ctf : pouleResults.getBypassFencers()) {
            System.out.println(ctf.getFencerName() + " - Poule wins:" + ctf.getPouleWins() + ", Poule points:" + ctf.getPoulePoints());
        }
        System.out.println("FenceOff Fencers:");
        for (CleanTournamentFencerDTO ctf : pouleResults.getFenceOffFencers()) {
            System.out.println(ctf.getFencerName() + " - Poule wins:" + ctf.getPouleWins() + ", Poule points:" + ctf.getPoulePoints());
        }
        System.out.println("Eliminated Fencers:");
        for (CleanTournamentFencerDTO ctf : pouleResults.getEliminatedFencers()) {
            System.out.println(ctf.getFencerName() + " - Poule wins:" + ctf.getPouleWins() + ", Poule points:" + ctf.getPoulePoints());
        }
    }

    // public void createDirectEliminationMatches() {
    //     eventService.createAllDEMatches(eventRepository.findByTournamentAndGenderAndWeapon(tournamentRepository.findByName("Tournament").get(), 'M', 'S').get().getId());
    // }

    @EventListener(ContextRefreshedEvent.class)
    @Transactional
    public void populateData() throws MethodArgumentNotValidException {
        if (userRepository.count() == 0) {
            createAdmin();
            createOrganiser();
            createMenSabreFencer();
            createWomenSabreFencer();
            createMenEpeeFencer();
            createWomenEpeeFencer();
            createMenFoilFencer();
            createWomenFoilFencer();
            createTournament();
            createEvent();
            registerFencerForEvent();
            return;
        }

        if (pouleRepository.count() == 0) {
            createPoules();
            createPouleMatches();
            updatePouleTable();
            printUpdatedPouleTable();
            printPouleResult();
        }

        // createDirectEliminationMatches();
    }
}