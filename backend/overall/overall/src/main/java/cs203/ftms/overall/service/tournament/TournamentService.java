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
import cs203.ftms.overall.repository.tournamentrelated.TournamentRepository;
import cs203.ftms.overall.repository.userrelated.UserRepository;
import cs203.ftms.overall.service.event.EventService;
import cs203.ftms.overall.validation.OtherValidations;
import jakarta.transaction.Transactional;

/**
 * Service class for managing tournament-related operations.
 * Provides methods for creating, updating, retrieving, and deleting tournaments.
 */
@Service
public class TournamentService {
    private final TournamentRepository tournamentRepository;
    private final EventService eventService;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Autowired
    public TournamentService(TournamentRepository tournamentRepository, EventService eventService,
                             EventRepository eventRepository, UserRepository userRepository) {
        this.tournamentRepository = tournamentRepository;
        this.eventService = eventService;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    /**
     * Converts a Tournament entity into a CleanTournamentDTO.
     *
     * @param t the Tournament entity
     * @return a CleanTournamentDTO containing cleaned tournament details
     * @throws EntityDoesNotExistException if the tournament is null
     */
    public CleanTournamentDTO getCleanTournamentDTO(Tournament t) {
        if (t == null) {
            throw new EntityDoesNotExistException("Tournament cannot be null");
        }

        List<CleanEventDTO> cleanEvents = new ArrayList<>();
        for (Event e : t.getEvents()) {
            cleanEvents.add(eventService.getCleanEventDTO(e));
        }

        return new CleanTournamentDTO(t.getId(), t.getName(), t.getOrganiser().getName(), t.getSignupEndDate(),
                                      t.getStartDate(), t.getEndDate(), t.getLocation(), t.getDescription(),
                                      t.getRules(), cleanEvents, t.getDifficulty(), t.getAdvancementRate());
    }

    /**
     * Retrieves a Tournament entity by its ID.
     *
     * @param id the ID of the tournament
     * @return the Tournament entity
     * @throws EntityDoesNotExistException if the tournament does not exist
     */
    public Tournament getTournament(int id) {
        return tournamentRepository.findById(id)
                .orElseThrow(() -> new EntityDoesNotExistException("Tournament does not exist!"));
    }

    /**
     * Retrieves all tournaments.
     *
     * @return a list of Tournament entities
     */
    public List<Tournament> getAllTournaments() {
        return tournamentRepository.findAll();
    }

    /**
     * Creates a new tournament.
     *
     * @param t the DTO containing tournament details
     * @param o the Organiser creating the tournament
     * @return the created Tournament entity
     * @throws MethodArgumentNotValidException if validation of tournament details fails
     */
    public Tournament createTournament(CreateTournamentDTO t, Organiser o) throws MethodArgumentNotValidException {
        Tournament tournament = new Tournament(t.getName(), o, t.getSignupEndDate(), t.getAdvancementRate(),
                                                t.getStartDate(), t.getEndDate(), t.getLocation(), t.getDescription(),
                                                t.getRules(), t.getDifficulty());
        OtherValidations.validTournamentSignUpEndDate(tournament);
        OtherValidations.validTournamentDates(tournament);
        return tournamentRepository.save(tournament);
    }

    /**
     * Updates an existing tournament.
     *
     * @param tid the ID of the tournament to update
     * @param dto the DTO containing updated tournament details
     * @param o the Organiser requesting the update
     * @return the updated Tournament entity
     * @throws MethodArgumentNotValidException if validation of updated details fails
     */
    @Transactional
    public Tournament updateTournament(int tid, CreateTournamentDTO dto, Organiser o)
            throws MethodArgumentNotValidException {
        Tournament tournament = getTournament(tid);
        validateOrganiser(tournament, o);
        validateTournamentDates(dto);
        validateEventsDates(tournament, dto);

        updateTournamentDetails(tournament, dto);
        return tournamentRepository.save(tournament);
    }

    /**
     * Retrieves upcoming tournaments.
     *
     * @return a list of upcoming Tournament entities
     */
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

    /**
     * Retrieves past tournaments.
     *
     * @return a list of past Tournament entities
     */
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

    /**
     * Deletes a tournament.
     *
     * @param organiser the Organiser requesting the deletion
     * @param tournamentId the ID of the tournament to delete
     */
    @Transactional
    public void deleteTournament(Organiser organiser, int tournamentId) {
        Tournament tournament = getTournament(tournamentId);
        validateOrganiser(tournament, organiser);
        validateTournamentNotStarted(tournament);
        unregisterAllFencers(tournament);
        removeFromOrganiser(tournament, organiser);
        deleteTournamentAndEvents(tournament);
    }

    // Validates that the organiser matches the tournament organiser
    private void validateOrganiser(Tournament tournament, Organiser organiser) {
        if (tournament.getOrganiser().getId() != organiser.getId()) {
            throw new IllegalArgumentException("Organiser does not match the tournament organiser.");
        }
    }

    // Validates the tournament dates
    private void validateTournamentDates(CreateTournamentDTO dto) throws MethodArgumentNotValidException {
        OtherValidations.validTournamentSignUpEndDate(dto.getStartDate(), dto.getSignupEndDate());
        OtherValidations.validTournamentDates(dto.getStartDate(), dto.getEndDate());
    }

    // Validates the dates of all events within the tournament
    private void validateEventsDates(Tournament tournament, CreateTournamentDTO dto)
            throws MethodArgumentNotValidException {
        for (Event event : tournament.getEvents()) {
            OtherValidations.validUpdateTournamentDate(event, dto.getStartDate(), dto.getEndDate());
        }
    }

    // Updates tournament details
    private void updateTournamentDetails(Tournament tournament, CreateTournamentDTO dto)
            throws MethodArgumentNotValidException {
        tournament.setName(dto.getName());
        tournament.setSignupEndDate(dto.getSignupEndDate());
        tournament.setAdvancementRate(dto.getAdvancementRate());
        tournament.setStartDate(dto.getStartDate());
        tournament.setEndDate(dto.getEndDate());
        tournament.setLocation(dto.getLocation());
        tournament.setDescription(dto.getDescription());
        tournament.setRules(dto.getRules());
        tournament.setDifficulty(dto.getDifficulty());
        OtherValidations.validTournamentSignUpEndDate(tournament);
        OtherValidations.validTournamentDates(tournament);
        tournamentRepository.save(tournament);
    }

    // Validates that the tournament has not started
    private void validateTournamentNotStarted(Tournament tournament) {
        for (Event event : tournament.getEvents()) {
            if (event.getPoules() != null && !event.getPoules().isEmpty()) {
                throw new TournamentAlreadyStartedException("Cannot delete tournament that has already started!");
            }
        }
    }

    // Unregisters all fencers from the tournament
    private void unregisterAllFencers(Tournament tournament) {
        List<Event> eventsCopy = new ArrayList<>(tournament.getEvents());
        for (Event event : eventsCopy) {
            Set<TournamentFencer> fencersCopy = new HashSet<>(event.getFencers());
            for (TournamentFencer tf : fencersCopy) {
                Fencer fencer = tf.getFencer();
                eventService.unregisterEvent(event.getId(), fencer);
            }
        }
    }

    // Removes the tournament from the organiser
    private void removeFromOrganiser(Tournament tournament, Organiser organiser) {
        Set<Tournament> tourHost = organiser.getTourHost();
        tourHost.removeIf(t -> t.getId() == tournament.getId());
        organiser.setTourHost(tourHost);
        userRepository.save(organiser);
    }

    // Deletes the tournament and its associated events
    @Transactional
    private void deleteTournamentAndEvents(Tournament tournament) {
        List<Event> eventsCopy = new ArrayList<>(tournament.getEvents());
        for (Event event : eventsCopy) {
            event.setTournament(null);
            eventRepository.delete(event);
        }
        tournament.getEvents().clear();
        tournament.setOrganiser(null);
        tournament.setEvents(null);
        tournamentRepository.delete(tournament);
    }
}
