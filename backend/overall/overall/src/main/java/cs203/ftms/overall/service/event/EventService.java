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
import cs203.ftms.overall.exception.SignUpDateOverException;
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

/**
 * Service class for managing tournament events.
 * Handles operations such as event creation, updates, deletion, registration, 
 * and rank calculation for tournament fencers.
 */
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
                        FencerService fencerService, DirectEliminationMatchRepository directEliminationMatchRepository, 
                        TournamentFencerRepository tournamentFencerRepository) {
        this.tournamentRepository = tournamentRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.fencerService = fencerService; 
        this.directEliminationMatchRepository = directEliminationMatchRepository; 
        this.tournamentFencerRepository = tournamentFencerRepository;
    }

    /**
     * Retrieves future events for a specific gender and weapon.
     *
     * @param gender the gender of the events to retrieve
     * @param weapon the weapon type of the events to retrieve
     * @return a list of future events matching the criteria
     */
    public List<Event> getFutureEventsByGenderAndWeapon(char gender, char weapon) {
        List<Event> events = eventRepository.findByGenderAndWeapon(gender, weapon);
        List<Event> eventsToReturn = new ArrayList<>();
        for (Event e : events) {
            if (e.getTournament().getStartDate().isAfter(LocalDate.now())) {
                eventsToReturn.add(e);
            }
        }
        return eventsToReturn;
    }

    /**
     * Retrieves an event by its ID.
     *
     * @param eid the ID of the event to retrieve
     * @return the Event entity
     * @throws EntityDoesNotExistException if the event does not exist
     */
    public Event getEvent(int eid) {
        return eventRepository.findById(eid).orElseThrow(() -> new EntityDoesNotExistException("Event does not exist!"));
    }

    /**
     * Converts an Event entity into a CleanEventDTO.
     *
     * @param e the Event entity to convert
     * @return a CleanEventDTO containing the event details
     * @throws EntityDoesNotExistException if the event does not exist
     */
    public CleanEventDTO getCleanEventDTO(Event e) {
        if (e == null) throw new EntityDoesNotExistException("Event does not exist!");
        
        List<CleanFencerDTO> cleanFencers = new ArrayList<>(); 
        for (TournamentFencer f : e.getFencers()) {
            cleanFencers.add(fencerService.getCleanFencerDTO(f.getFencer()));
        }

        return new CleanEventDTO(e.getId(), e.getGender(), e.getWeapon(), e.getTournament().getName(), cleanFencers, e.getMinParticipants(), e.getParticipantCount(), e.getDate(), e.getStartTime(), e.getEndTime(), e.getTournament().getSignupEndDate(), e.isOver());
    }

    /**
     * Converts a TournamentFencer entity into a CleanTournamentFencerDTO.
     *
     * @param tf the TournamentFencer entity to convert
     * @return a CleanTournamentFencerDTO containing the fencer details
     */
    public CleanTournamentFencerDTO getCleanTournamentFencerDTO(TournamentFencer tf) {
        return new CleanTournamentFencerDTO(tf.getId(), tf.getFencer().getId(), tf.getFencer().getName(), tf.getFencer().getClub(), tf.getFencer().getCountry(), tf.getFencer().getDominantArm(), tf.getTournamentRank(), tf.getEvent().getId(), tf.getPouleWins(), tf.getPoulePoints(), tf.getPointsAfterEvent());
    }

    /**
     * Creates multiple events for a tournament.
     *
     * @param tid       the ID of the tournament
     * @param organiser the organiser performing the operation
     * @param eventDTOs a list of event creation data transfer objects
     * @return a list of created Event objects
     * @throws MethodArgumentNotValidException if any event data is invalid
     * @throws EventAlreadyExistsException if an event with the same attributes already exists
     */
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
                throw new EventAlreadyExistsException("Event already exists!");
            }

            Event event = buildEvent(tournament, dto);
            OtherValidations.validEventDate(event, tournament);
            eventRepository.save(event);
            events.add(event);
        }
        return events;
    }

    /**
     * Deletes an event from the tournament, unregistering all participants and ensuring no matches or poules exist.
     *
     * @param eid       the ID of the event to delete
     * @param organiser the organiser performing the operation
     * @throws EventCannotEndException if the event contains matches or poules
     */
    @Transactional
    public void deleteEvent(int eid, Organiser organiser) {
        Event event = getEvent(eid);
        validateOrganiser(event, organiser);
        if (event.getDirectEliminationMatches().size() != 0 || event.getPoules().size() != 0) {
            throw new EventCannotEndException("Event cannot be deleted as it has matches or poules!");
        }
        Tournament tournament = event.getTournament();
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

    // Helper method to validate the tournament
    private Tournament validateTournament(int tid, Organiser organiser) {
        Tournament tournament = tournamentRepository.findById(tid).orElse(null);
        if (tournament == null || !tournament.getOrganiser().equals(organiser)) {
            return null;
        }
        return tournament;
    }

    // Helper method to check if an event already exists
    private boolean eventExists(Tournament tournament, CreateEventDTO dto) {
        return eventRepository.findByTournamentAndGenderAndWeapon(tournament, dto.getGender(), dto.getWeapon()).isPresent();
    }

    // Helper method to build an event object
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

    /**
     * Updates an existing event with new details.
     *
     * @param eid       the ID of the event to update
     * @param organiser the organiser performing the operation
     * @param dto       the event update data transfer object
     * @return the updated Event object
     * @throws MethodArgumentNotValidException if the update data is invalid
     */
    @Transactional
    public Event updateEvent(int eid, Organiser organiser, UpdateEventDTO dto) throws MethodArgumentNotValidException {
        Event event = getEvent(eid);
        validateOrganiser(event, organiser);
        validateEventOver(event);
        OtherValidations.validUpdateEventDate(dto.getDate(), event.getTournament());
        updateEventDetails(event, dto);
        return eventRepository.save(event);
    }

    /**
     * Validates if the organiser is the same as the organiser of the tournament hosting the event.
     *
     * @param event     the event to validate
     * @param organiser the organiser performing the operation
     * @throws IllegalArgumentException if the organiser does not match
     */
    public void validateOrganiser(Event event, Organiser organiser) {
        Tournament tournament = event.getTournament();
        if (!tournament.getOrganiser().equals(organiser)) {
            throw new IllegalArgumentException("Organiser does not match the tournament organiser.");
        }
    }

    // Helper method to validate if the event is over
    private void validateEventOver(Event event) {
        if (event.isOver()) {
            throw new IllegalArgumentException("Event is over!");
        }
    }

    // Helper method to update event details
    private void updateEventDetails(Event event, UpdateEventDTO dto) {
        event.setMinParticipants(dto.getMinParticipants());
        event.setDate(dto.getDate());
        event.setStartTime(dto.getStartTime());
        event.setEndTime(dto.getEndTime());
    }

    /**
     * Registers a fencer for a specific event.
     *
     * @param eid the ID of the event
     * @param f   the fencer to register
     * @return true if the registration is successful, false otherwise
     * @throws SignUpDateOverException if the signup deadline for the event has passed
     * @throws FencerAlreadyRegisteredForEventException if the fencer is already registered for the event
     * @throws FencerProfileMismatchException if the fencer's profile (gender or weapon) does not match the event
     */
    @Transactional
    public boolean registerEvent(int eid, Fencer f) {
        Event event = getEvent(eid);

        if (event.getTournament().getSignupEndDate().isBefore(LocalDate.now())) {
            throw new SignUpDateOverException("Sign up date is over!");
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

        TournamentFencer tf = new TournamentFencer(f, event);
        Set<TournamentFencer> fencers = event.getFencers();
        fencers.add(tf);
        event.setFencers(fencers);
        event.setParticipantCount(event.getParticipantCount() + 1);

        Set<TournamentFencer> fencerTFs = f.getTournamentFencerProfiles();
        fencerTFs.add(tf);
        f.setTournamentFencerProfiles(fencerTFs);

        Fencer nf = userRepository.save(f);

        if (nf != null) {
            Event tc = eventRepository.save(event);
            return tc != null;
        }
        return false;
    }

    /**
     * Unregisters a fencer from a specific event.
     *
     * @param eid the ID of the event
     * @param f   the fencer to unregister
     * @return true if the unregistration is successful, false otherwise
     * @throws SignUpDateOverException if the signup deadline for the event has passed
     */
    @Transactional
    public boolean unregisterEvent(int eid, Fencer f) {
        Event event = getEvent(eid);
        if (event.getTournament().getSignupEndDate().isBefore(LocalDate.now())) {
            throw new SignUpDateOverException("Sign up date is over!");
        }
        Set<TournamentFencer> fencers = event.getFencers();
        fencers.removeIf(tf -> tf.getFencer().equals(f));
        event.setFencers(fencers);
        event.setParticipantCount(event.getParticipantCount() - 1);

        Set<TournamentFencer> fencerTFs = f.getTournamentFencerProfiles();
        fencerTFs.removeIf(tf -> tf.getEvent().equals(event));
        f.setTournamentFencerProfiles(fencerTFs);

        tournamentFencerRepository.delete(tournamentFencerRepository.findByFencerAndEvent(f, event));

        Fencer nf = userRepository.save(f);

        if (nf != null) {
            Event tc = eventRepository.save(event);
            return tc != null;
        }
        return false;
    }

    /**
     * Retrieves a list of tournament fencers sorted by their rank in the event.
     *
     * @param eid the ID of the event
     * @return a sorted list of TournamentFencer objects
     * @throws EntityDoesNotExistException if the event does not exist
     */
    public List<TournamentFencer> getTournamentRanks(int eid) {
        Event event = eventRepository.findById(eid)
                .orElseThrow(() -> new EntityDoesNotExistException("Event does not exist!"));
        List<TournamentFencer> tfs = new ArrayList<>(event.getFencers());
        Collections.sort(tfs, new TournamentFencerComparator());
        return tfs;
    }

    /**
     * Marks a tournament event as completed, ensuring all prerequisites are met.
     *
     * @param eid the ID of the event
     * @param o   the organiser performing the operation
     * @throws EventCannotEndException if the event cannot be ended due to unfulfilled prerequisites
     */
    public void endTournamentEvent(int eid, Organiser o) throws EventCannotEndException {
        Event event = eventRepository.findById(eid)
                .orElseThrow(() -> new EntityDoesNotExistException("Event does not exist!"));
        validateOrganiser(event, o);
        if (event.getDate().isAfter(LocalDate.now())) {
            throw new EventCannotEndException("Event has not started yet!");
        }
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

    /**
     * Updates the international rank of all participants in an event.
     *
     * @param event the event for which ranks are updated
     */
    @Transactional
    public void updateInternationalRank(Event event) {
        List<TournamentFencer> tfs = getTournamentRanks(event.getId());
        double numOfFencersThatGetPoints = tfs.size() * 0.8;
        int totalPoints = getPointsForDistribution(event.getFencers());
        for (int i = 0; i < (int) numOfFencersThatGetPoints; i++) {
            int points = calculatePoints(i, totalPoints, (int) numOfFencersThatGetPoints);
            TournamentFencer tf = tfs.get(i);
            Fencer fencer = tf.getFencer();
            fencer.setPoints(points + fencer.getPoints());
            tf.setPointsAfterEvent(fencer.getPoints());
            userRepository.save(fencer);
        }
    }

    /**
     * Calculates the total points to be distributed among fencers in the event.
     *
     * @param tfencers the set of tournament fencers
     * @return the total points available for distribution
     */
    public int getPointsForDistribution(Set<TournamentFencer> tfencers) {
        int total = 0;
        for (TournamentFencer f : tfencers) {
            total += f.getFencer().getPoints();
        }
        return total / 5;
    }

    /**
     * Calculates the points for a specific fencer based on their rank and the total points available.
     *
     * @param rank          the rank of the fencer
     * @param totalPoints   the total points available for distribution
     * @param totalFencers  the total number of fencers receiving points
     * @return the calculated points for the fencer
     */
    public int calculatePoints(int rank, int totalPoints, int totalFencers) {
        double numerator = totalPoints * Math.pow(totalFencers - rank + 1, 2);
        double denominator = sumOfPowers(totalFencers, 2);
        return (int) (numerator / denominator);
    }

    // Helper method to calculate the sum of powers
    private static double sumOfPowers(int n, int exponent) {
        double sum = 0;
        for (int i = 1; i <= n; i++) {
            sum += Math.pow(i, exponent);
        }
        return sum;
    }
}
