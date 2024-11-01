package cs203.ftms.overall.service.tournament;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import cs203.ftms.overall.dto.CreateTournamentDTO;
import cs203.ftms.overall.dto.clean.CleanEventDTO;
import cs203.ftms.overall.dto.clean.CleanTournamentDTO;
import cs203.ftms.overall.exception.EntityDoesNotExistException;
import cs203.ftms.overall.model.tournamentrelated.Event;
import cs203.ftms.overall.model.tournamentrelated.Tournament;
import cs203.ftms.overall.model.userrelated.Organiser;
import cs203.ftms.overall.repository.tournamentrelated.EventRepository;
import cs203.ftms.overall.repository.tournamentrelated.TournamentFencerRepository;
import cs203.ftms.overall.repository.tournamentrelated.TournamentRepository;
import cs203.ftms.overall.service.event.EventService;
import cs203.ftms.overall.validation.OtherValidations;

@Service
public class TournamentService {
    private final TournamentRepository tournamentRepository;
    private final EventService eventService;
    private final EventRepository eventRepository;
    private final TournamentFencerRepository tournamentFencerRepository;

    @Autowired
    public TournamentService(TournamentRepository tournamentRepository, EventService eventService, EventRepository eventRepository, TournamentFencerRepository tournamentFencerRepository) {
        this.tournamentRepository = tournamentRepository;
        this.eventService = eventService;
        this.eventRepository = eventRepository;
        this.tournamentFencerRepository = tournamentFencerRepository;
    }

    public CleanTournamentDTO getCleanTournamentDTO(Tournament t) {
        if (t==null) return null;

        List<CleanEventDTO> cleanEvents = new ArrayList<>();
        for (Event e : t.getEvents()) {
            cleanEvents.add(eventService.getCleanEventDTO(e));
        }

        return new CleanTournamentDTO(t.getId(), t.getName(), t.getOrganiser().getName(), t.getSignupEndDate(), t.getStartDate(), t.getEndDate(), t.getLocation(), t.getDescription(), t.getRules(), cleanEvents);
    }

    public Tournament getTournament(int id) {
        return tournamentRepository.findById(id).orElseThrow(() -> new EntityDoesNotExistException("Tournament does not exist!"));
    }

    public List<Tournament> getAllTournaments() {
        return tournamentRepository.findAll();
    }

    public Tournament createTournament(CreateTournamentDTO t, Organiser o) throws MethodArgumentNotValidException {
        Tournament tournament = new Tournament(t.getName(), o, t.getSignupEndDate(), t.getAdvancementRate(), t.getStartDate(), t.getEndDate(), t.getLocation(), t.getDescription(), t.getRules(), t.getDifficulty());
        OtherValidations.validTournamentSignUpEndDate(tournament);
        OtherValidations.validTournamentDates(tournament);
        return tournamentRepository.save(tournament);
    }
    
    public List<Tournament> getUpcomingTournaments() {
        List<Tournament> tList = tournamentRepository.findAll();
        List<Tournament> upcomingTournaments = new ArrayList<>();
        for (Tournament t : tList) {
            if (t.getStartDate().isAfter(LocalDate.now())) {
                upcomingTournaments.add(t);
            }
        }
        return upcomingTournaments;
    }

    public List<Tournament> getPastTournaments() {
        List<Tournament> tList = tournamentRepository.findAll();
        List<Tournament> pastTournaments = new ArrayList<>();
        for (Tournament t : tList) {
            if (t.getStartDate().isBefore(LocalDate.now())) {
                pastTournaments.add(t);
            }
        }
        return pastTournaments;
    }

    // @Transactional
    // public void deleteTournament(Organiser o, int tid) {
    //     Tournament t = getTournament(tid);
    //     if (t.getOrganiser().getId() != o.getId()) {
    //         throw new EntityDoesNotExistException("Tournament does not exist!");
    //     }
    //     for (Event e : t.getEvents()) {
    //         deleteEvent(e);
    //     }
    //     tournamentRepository.delete(t);
    // }

    // @Transactional
    // private void deleteEvent(Event event) {
    //     // Event event = eventRepository.findById(eid).orElseThrow(() -> new EntityDoesNotExistException("Event does not exist!"));
    //     Tournament t = event.getTournament();
    //     if (event.isOver()) {
    //         throw new EntityDoesNotExistException("Cannot delete completed event!");
    //     }
    //     Set<TournamentFencer> tournamentFencers = event.getFencers();
    //     event.setFencers(null);
    //     for (TournamentFencer tf : tournamentFencers) {
    //         tf.setEvent(null);
    //         tournamentFencerRepository.delete(tf);
    //     }
    //     Set<Event> events = t.getEvents();
    //     events.remove(event);
    //     t.setEvents(events);
    //     tournamentRepository.save(t);
    //     eventRepository.delete(event);
    // }
}
