package cs203.ftms.overall.service.tournament;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import cs203.ftms.overall.dto.CreateTournamentDTO;
import cs203.ftms.overall.dto.clean.CleanEventDTO;
import cs203.ftms.overall.dto.clean.CleanTournamentDTO;
import cs203.ftms.overall.exception.EntityDoesNotExistException;
import cs203.ftms.overall.exception.TournamentAlreadyStartedException;
import cs203.ftms.overall.model.tournamentrelated.Event;
import cs203.ftms.overall.model.tournamentrelated.Tournament;
import cs203.ftms.overall.model.tournamentrelated.TournamentFencer;
import cs203.ftms.overall.model.userrelated.Fencer;
import cs203.ftms.overall.model.userrelated.Organiser;
import cs203.ftms.overall.repository.tournamentrelated.EventRepository;
import cs203.ftms.overall.repository.tournamentrelated.TournamentFencerRepository;
import cs203.ftms.overall.repository.tournamentrelated.TournamentRepository;
import cs203.ftms.overall.service.event.EventService;
import cs203.ftms.overall.validation.OtherValidations;
import jakarta.transaction.Transactional;

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

        return new CleanTournamentDTO(t.getId(), t.getName(), t.getOrganiser().getName(), t.getSignupEndDate(), t.getStartDate(), t.getEndDate(), t.getLocation(), t.getDescription(), t.getRules(), cleanEvents, t.getDifficulty(), t.getAdvancementRate());
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

    @Transactional
    public Tournament updateTournament(int tid, CreateTournamentDTO dto, Organiser o) throws MethodArgumentNotValidException {
        Tournament tournament = getTournament(tid);
        validateOrganiser(tournament, o);
        validateTournamentDates(dto);
        validateEventsDates(tournament, dto);

        updateTournamentDetails(tournament, dto);
        return tournamentRepository.save(tournament);
    }

    private void validateOrganiser(Tournament tournament, Organiser organiser) {
        if (tournament.getOrganiser().getId() != organiser.getId()) {
            throw new IllegalArgumentException("Organiser does not match the tournament organiser.");
        }
    }

    private void validateTournamentDates(CreateTournamentDTO dto) throws MethodArgumentNotValidException {
        OtherValidations.validTournamentSignUpEndDate(dto.getStartDate(), dto.getSignupEndDate());
        OtherValidations.validTournamentDates(dto.getStartDate(), dto.getEndDate());
    }

    private void validateEventsDates(Tournament tournament, CreateTournamentDTO dto) throws MethodArgumentNotValidException {
        for (Event event : tournament.getEvents()) {
            OtherValidations.validUpdateTournamentDate(event, dto.getStartDate(), dto.getEndDate());
        }
    }

    private void updateTournamentDetails(Tournament tournament, CreateTournamentDTO dto) {
        tournament.setName(dto.getName());
        tournament.setSignupEndDate(dto.getSignupEndDate());
        tournament.setAdvancementRate(dto.getAdvancementRate());
        tournament.setStartDate(dto.getStartDate());
        tournament.setEndDate(dto.getEndDate());
        tournament.setLocation(dto.getLocation());
        tournament.setDescription(dto.getDescription());
        tournament.setRules(dto.getRules());
        tournament.setDifficulty(dto.getDifficulty());
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

    @Transactional
public void deleteTournament(Organiser organiser, int tournamentId) {
    Tournament tournament = getTournament(tournamentId);
    validateOrganiser(tournament, organiser);
    validateTournamentNotStarted(tournament);
    unregisterAllFencers(tournament);
    removeFromOrganiser(tournament, organiser);
    deleteTournamentAndEvents(tournament);
}

private void validateOrganiser(Tournament tournament, Organiser organiser) {
    if (tournament.getOrganiser().getId() != organiser.getId()) {
        throw new EntityDoesNotExistException("Tournament does not exist!");
    }
}

private void validateTournamentNotStarted(Tournament tournament) {
    for (Event event : tournament.getEvents()) {
        if (event.getPoules() != null && !event.getPoules().isEmpty()) {
            throw new TournamentAlreadyStartedException("Cannot delete tournament that has already started!");
        }
    }
}

private void unregisterAllFencers(Tournament tournament) {
    for (Event event : tournament.getEvents()) {
        for (TournamentFencer tf : event.getFencers()) {
            Fencer fencer = tf.getFencer();
            eventService.unregisterEvent(event.getId(), fencer);
        }
    }
}

private void removeFromOrganiser(Tournament tournament, Organiser organiser) {
    Set<Tournament> tourHost = organiser.getTourHost();
    tourHost.removeIf(t -> t.getId() == tournament.getId());
    organiser.setTourHost(tourHost);
}

private void deleteTournamentAndEvents(Tournament tournament) {
    for (Event event : tournament.getEvents()) {
        event.setTournament(null);
        eventRepository.delete(event);
    }
    tournament.setOrganiser(null);
    tournament.setEvents(null);
    tournamentRepository.deleteTournamentById(tournament.getId());
}

    // @Transactional
    // public void deleteTournament(Organiser o, int tid) {
    //     Tournament t = getTournament(tid);
    //     if (t.getOrganiser().getId() != o.getId()) {
    //         throw new EntityDoesNotExistException("Tournament does not exist!");
    //     }
        
    //     for (Event e : t.getEvents()) {
    //         if (e.getPoules() != null && !e.getPoules().isEmpty()) {
    //             throw new TournamentAlreadyStartedException("Cannot delete tournament that has already started!");
    //         }
    //         for (TournamentFencer tf : e.getFencers()) {
    //             Fencer f = tf.getFencer();
    //             eventService.unregisterEvent(tid, f);
    //         }
    //         e.setTournament(null); 
    //         eventRepository.delete(e);
    //     }

    //     Set<Tournament> tourHost = o.getTourHost();
    //     tourHost.removeIf(tournament -> tournament.getId() == tid);
    //     o.setTourHost(tourHost);
    //     t.setOrganiser(null);
    //     t.setEvents(null);
    //     tournamentRepository.deleteTournamentById(tid);
    // }


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
