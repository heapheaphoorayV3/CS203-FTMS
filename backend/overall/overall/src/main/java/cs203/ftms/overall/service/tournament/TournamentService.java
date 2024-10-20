package cs203.ftms.overall.service.tournament;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import cs203.ftms.overall.dto.CreateTournamentDTO;
import cs203.ftms.overall.dto.clean.CleanEventDTO;
import cs203.ftms.overall.dto.clean.CleanTournamentDTO;
import cs203.ftms.overall.model.tournamentrelated.Event;
import cs203.ftms.overall.model.tournamentrelated.Tournament;
import cs203.ftms.overall.model.userrelated.Organiser;
import cs203.ftms.overall.repository.tournamentrelated.EventRepository;
import cs203.ftms.overall.repository.tournamentrelated.TournamentRepository;
import cs203.ftms.overall.repository.userrelated.UserRepository;
import cs203.ftms.overall.service.event.EventService;
import cs203.ftms.overall.service.fencer.FencerService;
import static cs203.ftms.overall.validation.OtherValidations.validTournamentDates;
import static cs203.ftms.overall.validation.OtherValidations.validTournamentSignUpEndDate;

@Service
public class TournamentService {
    private final TournamentRepository tournamentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final FencerService fencerService;
    private final EventService eventService;

    @Autowired
    public TournamentService(TournamentRepository tournamentRepository, EventRepository eventRepository, 
    UserRepository userRepository, FencerService fencerService, EventService eventService) {
        this.tournamentRepository = tournamentRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.fencerService = fencerService; 
        this.eventService = eventService;
    }

    public CleanTournamentDTO getCleanTournamentDTO(Tournament t) {
        if (t==null) return null;

        Set<CleanEventDTO> cleanEvents = new HashSet<>();
        for (Event e : t.getEvents()) {
            cleanEvents.add(eventService.getCleanEventDTO(e));
        }

        return new CleanTournamentDTO(t.getId(), t.getName(), t.getOrganiser().getName(), t.getSignupEndDate(), t.getStartDate(), t.getEndDate(), t.getLocation(), t.getDescription(), t.getRules(), cleanEvents);
    }

    public Tournament getTournament(int id) {
        return tournamentRepository.findById(id).orElse(null);
    }

    public Tournament createTournament(CreateTournamentDTO t, Organiser o) throws MethodArgumentNotValidException {
        Tournament tournament = new Tournament(t.getName(), o, t.getSignupEndDate(), t.getAdvancementRate(), t.getStartDate(), t.getEndDate(), t.getLocation(), t.getDescription(), t.getRules());
        validTournamentSignUpEndDate(tournament);
        validTournamentDates(tournament);
        return tournamentRepository.save(tournament);
    }

}
