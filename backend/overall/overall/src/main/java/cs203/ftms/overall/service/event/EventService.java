package cs203.ftms.overall.service.event;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import cs203.ftms.overall.dto.CreateEventDTO;
import cs203.ftms.overall.dto.clean.CleanEventDTO;
import cs203.ftms.overall.dto.clean.CleanFencerDTO;
import cs203.ftms.overall.model.tournamentrelated.Event;
import cs203.ftms.overall.model.tournamentrelated.Tournament;
import cs203.ftms.overall.model.userrelated.Fencer;
import cs203.ftms.overall.model.userrelated.Organiser;
import cs203.ftms.overall.repository.tournamentrelated.EventRepository;
import cs203.ftms.overall.repository.tournamentrelated.TournamentRepository;
import cs203.ftms.overall.repository.userrelated.UserRepository;
import cs203.ftms.overall.service.fencer.FencerService;
import static cs203.ftms.overall.validation.OtherValidations.validEventDate;
import jakarta.transaction.Transactional;


@Service
public class EventService {
    private final TournamentRepository tournamentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final FencerService fencerService;

    @Autowired
    public EventService(TournamentRepository tournamentRepository, EventRepository eventRepository, 
    UserRepository userRepository, FencerService fencerService) {
        this.tournamentRepository = tournamentRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.fencerService = fencerService; 
    }

    public CleanEventDTO getCleanEventDTO(Event e) {
        if (e == null) return null;

        Set<CleanFencerDTO> cleanFencers = new HashSet<>(); 
        for (Fencer f : e.getFencers()) {
            cleanFencers.add(fencerService.getCleanFencerDTO(f));
        }

        return new CleanEventDTO(e.getId(), e.getName(), e.getTournament().getName(), cleanFencers, e.getMinParticipants(), e.getParticipantCount(), e.getDate(), e.getStartTime(), e.getEndTime());
    } 

    public Event getEvent(int id) {
        return eventRepository.findById(id).orElse(null);
    }

    @Transactional
    public List<Event> createEvent(int tid, Organiser o, List<CreateEventDTO> dto) throws MethodArgumentNotValidException {
        Tournament tournament = tournamentRepository.findById(tid).orElse(null);
        if (tournament == null) {
            return null;
        }    
        
        if (!tournament.getOrganiser().equals(o)) {
            return null;
        }
        List<Event> events = new ArrayList<>();
        for (CreateEventDTO e : dto) {
            Event event = new Event(tournament, e.getGender(), e.getWeapon(), e.getMinParticipants(), e.getDate(), e.getStartTime(), e.getEndTime());
            validEventDate(event, tournament);
            eventRepository.save(event);
            events.add(event);
        }
        return events;
    }

    public boolean registerEvent(int tcid, Fencer f) {
        Event event = eventRepository.findById(tcid).orElse(null);
        if (event == null) return false;

        Set<Fencer> fencers = event.getFencers(); 
        fencers.add(f);
        event.setParticipantCount(event.getParticipantCount()+1);

        Set<Event> ftc = f.getEventsPart(); 
        ftc.add(event);
        f.setEventsPart(ftc);
        
        Fencer nf = userRepository.save(f);
        if (nf != null) {
            Event tc = eventRepository.save(event);
            if (tc != null) {
                return true;
            }
        }
        return false; 
    }

}
