package cs203.ftms.overall.service.event;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import cs203.ftms.overall.comparator.TournamentFencerComparator;
import cs203.ftms.overall.dto.CreateEventDTO;
import cs203.ftms.overall.dto.UpdateEventDTO;
import cs203.ftms.overall.dto.clean.CleanEventDTO;
import cs203.ftms.overall.dto.clean.CleanFencerDTO;
import cs203.ftms.overall.dto.clean.CleanTournamentFencerDTO;
import cs203.ftms.overall.exception.EntityDoesNotExistException;
import cs203.ftms.overall.exception.EventAlreadyExistsException;
import cs203.ftms.overall.exception.EventCannotEndException;
import cs203.ftms.overall.exception.FencerAlreadyRegisteredForEventException;
import cs203.ftms.overall.exception.FencerProfileMismatchException;
import cs203.ftms.overall.exception.SignUpDateOverExcpetion;
import cs203.ftms.overall.model.tournamentrelated.DirectEliminationMatch;
import cs203.ftms.overall.model.tournamentrelated.Event;
import cs203.ftms.overall.model.tournamentrelated.Tournament;
import cs203.ftms.overall.model.tournamentrelated.TournamentFencer;
import cs203.ftms.overall.model.userrelated.Fencer;
import cs203.ftms.overall.model.userrelated.Organiser;
import cs203.ftms.overall.repository.tournamentrelated.DirectEliminationMatchRepository;
import cs203.ftms.overall.repository.tournamentrelated.EventRepository;
import cs203.ftms.overall.repository.tournamentrelated.TournamentFencerRepository;
import cs203.ftms.overall.repository.tournamentrelated.TournamentRepository;
import cs203.ftms.overall.repository.userrelated.UserRepository;
import cs203.ftms.overall.service.fencer.FencerService;
import cs203.ftms.overall.validation.OtherValidations;
import jakarta.transaction.Transactional;


@Service
public class EventService {
    private final TournamentRepository tournamentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final FencerService fencerService;
    private final DirectEliminationMatchRepository directEliminationMatchRepository; 
    private final TournamentFencerRepository tournamentFencerRepository;

    @Autowired
    public EventService(TournamentRepository tournamentRepository, EventRepository eventRepository, UserRepository userRepository, 
    FencerService fencerService, DirectEliminationMatchRepository directEliminationMatchRepository, TournamentFencerRepository tournamentFencerRepository) {
        this.tournamentRepository = tournamentRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.fencerService = fencerService; 
        this.directEliminationMatchRepository = directEliminationMatchRepository; 
        this.tournamentFencerRepository = tournamentFencerRepository;
    }

    public Event getEvent(int eid) {
        return eventRepository.findById(eid).orElseThrow(() -> new EntityDoesNotExistException("Event does not exist!"));
    }

    public CleanEventDTO getCleanEventDTO(Event e) {
        if (e == null) throw new EntityDoesNotExistException("Event does not exist!");
        
        List<CleanFencerDTO> cleanFencers = new ArrayList<>(); 
        for (TournamentFencer f : e.getFencers()) {
            cleanFencers.add(fencerService.getCleanFencerDTO(f.getFencer()));
        }

        return new CleanEventDTO(e.getId(), e.getGender(), e.getWeapon(), e.getTournament().getName(), cleanFencers, e.getMinParticipants(), e.getParticipantCount(), e.getDate(), e.getStartTime(), e.getEndTime(), e.getTournament().getSignupEndDate(), e.isOver());
    } 

    public CleanTournamentFencerDTO getCleanTournamentFencerDTO(TournamentFencer tf) {
        return new CleanTournamentFencerDTO(tf.getId(), tf.getFencer().getId(), tf.getFencer().getName(), tf.getFencer().getClub(), tf.getFencer().getCountry(), tf.getFencer().getDominantArm(), tf.getTournamentRank(), tf.getEvent().getId(), tf.getPouleWins(), tf.getPoulePoints(), tf.getPointsAfterEvent());
    }

    @Transactional
    public List<Event> createEvent(int tid, Organiser organiser, List<CreateEventDTO> eventDTOs) 
            throws MethodArgumentNotValidException, EventAlreadyExistsException {

        Tournament tournament = validateTournament(tid, organiser);
        if (tournament == null) {
            return new ArrayList<>();
        }

        List<Event> events = new ArrayList<>();
        for (CreateEventDTO dto : eventDTOs) {
            if (eventExists(tournament, dto)) {
                throw new EventAlreadyExistsException();
            }

            Event event = buildEvent(tournament, dto);
            OtherValidations.validEventDate(event, tournament);
            eventRepository.save(event);
            events.add(event);
        }
        return events;
    }

    @Transactional
    public void deleteEvent(int eid, Organiser organiser) {
        Event event = getEvent(eid);
        if (event.getDirectEliminationMatches().size() != 0 || event.getPoules().size() != 0) {
            throw new EventCannotEndException("Event cannot be deleted as it has matches or poules!");
        }
        Tournament tournament = event.getTournament();
        validateOrganiser(event, organiser);
        Set<TournamentFencer> fencersCopy = new HashSet<>(event.getFencers());
        for (TournamentFencer tf : fencersCopy) {
            Fencer fencer = tf.getFencer();
            unregisterEvent(eid, fencer);
        }
        Set<Event> events = tournament.getEvents();
        events.remove(event);
        tournament.setEvents(events);
        tournamentRepository.save(tournament);
        eventRepository.delete(event);
    }


    private Tournament validateTournament(int tid, Organiser organiser) {
        Tournament tournament = tournamentRepository.findById(tid).orElse(null);
        if (tournament == null || !tournament.getOrganiser().equals(organiser)) {
            return null;
        }
        return tournament;
    }

    private boolean eventExists(Tournament tournament, CreateEventDTO dto) {
        return eventRepository.findByTournamentAndGenderAndWeapon(tournament, dto.getGender(), dto.getWeapon()).isPresent();
    }

    private Event buildEvent(Tournament tournament, CreateEventDTO dto) {
        return new Event(
            tournament,
            dto.getGender(),
            dto.getWeapon(),
            dto.getMinParticipants(),
            dto.getDate(),
            dto.getStartTime(),
            dto.getEndTime()
        );
    }

    @Transactional
    public Event updateEvent(int eid, Organiser organiser, UpdateEventDTO dto) throws MethodArgumentNotValidException {
        Event event = getEvent(eid);
        validateOrganiser(event, organiser);
        OtherValidations.validUpdateEventDate(dto.getDate(), event.getTournament());
        updateEventDetails(event, dto);
        return eventRepository.save(event);
    }

    private void validateOrganiser(Event event, Organiser organiser) {
        Tournament tournament = event.getTournament();
        if (!tournament.getOrganiser().equals(organiser)) {
            throw new IllegalArgumentException("Organiser does not match the tournament organiser.");
        }
    }

    private void updateEventDetails(Event event, UpdateEventDTO dto) {
        event.setMinParticipants(dto.getMinParticipants());
        event.setDate(dto.getDate());
        event.setStartTime(dto.getStartTime());
        event.setEndTime(dto.getEndTime());
    }

    // fencer register for event
    @Transactional
    public boolean registerEvent(int eid, Fencer f) {
        Event event = getEvent(eid);
        
        if (event.getTournament().getSignupEndDate().isBefore(LocalDate.now())) {
            throw new SignUpDateOverExcpetion("Sign up date is over!");
        }

        if (tournamentFencerRepository.findByFencerAndEvent(f, event) != null) {
            throw new FencerAlreadyRegisteredForEventException("Fencer already registered for event!");
        }

        if (event.getWeapon() != f.getWeapon()) {
            throw new FencerProfileMismatchException("Fencer's weapon does not match the event's weapon!");
        }

        if (event.getGender() != f.getGender()) {
            throw new FencerProfileMismatchException("Fencer's gender does not match the event's gender!");
        }

        // handle relevant relationships
        TournamentFencer tf = new TournamentFencer(f, event);
        
        Set<TournamentFencer> fencers = event.getFencers(); 
        fencers.add(tf);
        event.setFencers(fencers);
        event.setParticipantCount(event.getParticipantCount()+1);

        Set<TournamentFencer> fencerTFs = f.getTournamentFencerProfiles();
        fencerTFs.add(tf);
        f.setTournamentFencerProfiles(fencerTFs);
        
        Fencer nf = userRepository.save(f);

        if (nf != null) {
            Event tc = eventRepository.save(event);
            if (tc != null) {
                return true;
            }
        }
        return false; 
    }

    // fencer unregister for event
    @Transactional
    public boolean unregisterEvent(int eid, Fencer f) {
        Event event = getEvent(eid);

        Set<TournamentFencer> fencers = event.getFencers(); 
        fencers.removeIf(tf -> tf.getFencer().equals(f));
        event.setFencers(fencers);
        event.setParticipantCount(event.getParticipantCount()-1);

        Set<TournamentFencer> fencerTFs = f.getTournamentFencerProfiles();
        fencerTFs.removeIf(tf -> tf.getEvent().equals(event));
        f.setTournamentFencerProfiles(fencerTFs);

        tournamentFencerRepository.delete(tournamentFencerRepository.findByFencerAndEvent(f, event));
        
        Fencer nf = userRepository.save(f);

        if (nf != null) {
            Event tc = eventRepository.save(event);
            if (tc != null) {
                return true;
            }
        }
        return false; 
    }

    public List<TournamentFencer> getTournamentRanks(int eid) {
        Event event = eventRepository.findById(eid).orElseThrow(() -> new EntityDoesNotExistException("Event does not exist!"));
        List<TournamentFencer> tfs = new ArrayList<>(event.getFencers());
        Collections.sort(tfs, new TournamentFencerComparator());
        return tfs;
    }

    public void endTournamentEvent(int eid) throws EventCannotEndException {
        Event event = eventRepository.findById(eid).orElseThrow(() -> new EntityDoesNotExistException("Event does not exist!"));
        if (event.isOver()) {
            throw new EventCannotEndException("Event has already been ended!");
        }
        if (event.getPoules().size() == 0) {
            throw new EventCannotEndException("Poules have not been completed!");
        }
        List<DirectEliminationMatch> matches = directEliminationMatchRepository.findByEventAndRoundOf(event, 2);
        if (matches.size() == 0) {
            throw new EventCannotEndException("Direct Elimination matches have not been completed!");
        }
        if (matches.get(0).getWinner() == -1) {
            throw new EventCannotEndException("Final match has not been completed!");
        } 
        updateInternationalRank(event);
        event.setOver(true);
        eventRepository.save(event);
    }

    @Transactional
    public void updateInternationalRank(Event event) {
        List<TournamentFencer> tfs = getTournamentRanks(event.getId());
        double numOfFencersThatGetPoints = tfs.size() * 0.8;
        int totalPoints = getPointsForDistribution(event.getFencers());
        for(int i = 0; i < (int) numOfFencersThatGetPoints; i++){
            int points = calculatePoints(i, totalPoints, (int) numOfFencersThatGetPoints);
            TournamentFencer tf = tfs.get(i);
            Fencer fencer = tf.getFencer();
            fencer.setPoints(points + fencer.getPoints());
            tf.setPointsAfterEvent(points + fencer.getPoints());
            userRepository.save(fencer);
        }
    }

    public int getPointsForDistribution(Set<TournamentFencer> tfencers) {
        int total = 0;
        for(TournamentFencer f :tfencers){
            total += f.getFencer().getPoints();
        }
        return total / 5;
    }

    public int calculatePoints(int rank, int totalPoints, int totalFencers){
        double numerator = totalPoints * Math.pow(totalFencers - rank + 1, 2);
        double denominator = sumOfPowers(totalFencers, 2);
        return (int) (numerator / denominator);
    }

    private static double sumOfPowers(int n, int exponent) {
        double sum = 0;
        for (int i = 1; i <= n; i++) {
            sum += Math.pow(i, exponent);
        }
        return sum;
    }
}
