package cs203.ftms.overall;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.web.bind.MethodArgumentNotValidException;

import cs203.ftms.overall.dto.CreateEventDTO;
import cs203.ftms.overall.dto.UpdateEventDTO;
import cs203.ftms.overall.dto.clean.CleanEventDTO;
import cs203.ftms.overall.dto.clean.CleanFencerDTO;
import cs203.ftms.overall.dto.clean.CleanTournamentFencerDTO;
import cs203.ftms.overall.exception.EntityDoesNotExistException;
import cs203.ftms.overall.exception.EventAlreadyExistsException;
import cs203.ftms.overall.exception.EventCannotEndException;
import cs203.ftms.overall.exception.FencerAlreadyRegisteredForEventException;
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
import cs203.ftms.overall.service.event.EventService;
import cs203.ftms.overall.service.fencer.FencerService;

public class EventServiceTest {

    @InjectMocks
    private EventService eventService;

    @Mock
    private TournamentRepository tournamentRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private DirectEliminationMatchRepository directEliminationMatchRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FencerService fencerService;

    @Mock
    private TournamentFencerRepository tournamentFencerRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getCleanEventDTO_EventIsNull() {
        // Act
        CleanEventDTO result = eventService.getCleanEventDTO(null);

        // Assert
        assertNull(result);
    }
    
    @Test
    public void getCleanEventDTO_ValidEvent_ReturnCleanEventDTO() {
        // Arrange
        Tournament tournament = new Tournament();
        tournament.setName("National Tournament");

        Event event = new Event();
        event.setId(1);
        // event.setName("Men's Foil");
        event.setGender('M');
        event.setWeapon('F');
        event.setTournament(tournament);
        event.setMinParticipants(10);
        event.setParticipantCount(5);
        event.setDate(LocalDate.now());
        event.setStartTime(LocalTime.now());
        event.setEndTime(LocalTime.now().plusHours(2));

        Fencer fencer = new Fencer();
        TournamentFencer tf = new TournamentFencer();
        tf.setFencer(fencer);
        when(fencerService.getCleanFencerDTO(fencer)).thenReturn(new CleanFencerDTO(0, null, null, null, null, null, 'L', 'S', null, 0, 0, 'M'));

        event.setFencers(new HashSet<>(Arrays.asList(tf)));

        // Act
        CleanEventDTO result = eventService.getCleanEventDTO(event);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals('M', result.getGender());
        assertEquals('F', result.getWeapon());
        assertEquals("National Tournament", result.getTournamentName());
        assertEquals(10, result.getMinParticipants());
        assertEquals(5, result.getParticipantCount());
        assertEquals(1, result.getFencers().size());
    }

    @Test
    void getCleanTournamentFencerDTO() {
        // Arrange
        Fencer fencer = new Fencer();
        fencer.setId(1);
        fencer.setName("John Doe");
        fencer.setClub("Best Club");
        fencer.setCountry("USA");
        fencer.setDominantArm('R');

        Event event = new Event();
        event.setId(1);

        TournamentFencer tournamentFencer = new TournamentFencer();
        tournamentFencer.setId(1);
        tournamentFencer.setFencer(fencer);
        tournamentFencer.setTournamentRank(1);
        tournamentFencer.setEvent(event);
        tournamentFencer.setPouleWins(5);
        tournamentFencer.setPoulePoints(10);

        // Act
        CleanTournamentFencerDTO result = eventService.getCleanTournamentFencerDTO(tournamentFencer);

        // Assert
        assertEquals(1, result.getTournamentFencerId());
        assertEquals(1, result.getFencerId());
        assertEquals("John Doe", result.getFencerName());
        assertEquals("Best Club", result.getFencerClub());
        assertEquals("USA", result.getCountry());
        assertEquals('R', result.getDominantArm());
        assertEquals(1, result.getTournamentRank());
        assertEquals(1, result.getEventId());
        assertEquals(5, result.getPouleWins());
        assertEquals(10, result.getPoulePoints());
    }

    @Test
    public void createEvent_TournamentNotFound_ReturnNull() throws MethodArgumentNotValidException {
        // Arrange
        int tid = 1;
        Organiser organiser = new Organiser();
        CreateEventDTO dto = new CreateEventDTO('F', 'S', 10, LocalDate.of(2024, 12, 12), LocalTime.now(), LocalTime.now().plusHours(3));

        when(tournamentRepository.findById(tid)).thenReturn(Optional.empty());

        // Act
        List<Event> result = eventService.createEvent(tid, organiser, Arrays.asList(dto));

        // Assert
        assertEquals(Arrays.asList(), result);
    }

    @Test
    public void createEvent_InvalidOrganiser_ReturnNull() throws MethodArgumentNotValidException {
        // Arrange
        Organiser organiser = new Organiser("Organizer One", "organizer.one@example.com", "password", "+6599999999", "Singapore");
        Organiser differentOrganiser = new Organiser("Organizer two", "organizer.two@example.com", "password", "+6599999998", "Singapore");
        Tournament tournament = new Tournament("hi", differentOrganiser, LocalDate.of(2024, 12, 10), 60, LocalDate.of(2024, 12, 12), LocalDate.of(2024, 12, 15), "singapore", "hi", "hi", 'B');
        tournamentRepository.save(tournament);

        // supposed to have different ids, but sometimes it is both 0 due to database flushing possibly
        organiser.setId(1);
        differentOrganiser.setId(2);

        when(tournamentRepository.findById(any(Integer.class))).thenReturn(Optional.of(tournament));

        // Act
        List<Event> result = eventService.createEvent(tournament.getId(), organiser, Arrays.asList(new CreateEventDTO('F', 'S', 10, LocalDate.of(2024, 12, 12), LocalTime.now(), LocalTime.now().plusHours(3))));

        // Assert
        assertEquals(Arrays.asList(), result);
    }

    @Test
    public void createEvent_ValidEvent_ReturnEventList() throws MethodArgumentNotValidException {
        // Arrange
        int tid = 1;
        Organiser organiser = new Organiser("Organizer One", "organizer.one@example.com", "password", "+6599999999", "Singapore");
        Tournament tournament = new Tournament("hi", organiser, LocalDate.of(2024, 12, 10), 60, LocalDate.of(2024, 12, 12), LocalDate.of(2024, 12, 15), "singapore", "hi", "hi", 'B');
        tournament.setOrganiser(organiser);

        CreateEventDTO dto = new CreateEventDTO('F', 'S', 10, LocalDate.of(2024, 12, 12), LocalTime.now(), LocalTime.now().plusHours(3));

        when(tournamentRepository.findById(tid)).thenReturn(Optional.of(tournament));

        // Act
        List<Event> result = eventService.createEvent(tid, organiser, Arrays.asList(dto));

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    public void registerEvent_ValidEventAndFencer_ReturnTrue() {
        // Arrange
        int tcid = 1;
        Fencer fencer = new Fencer("DOE John", "john.doe@example.com", "password"
        , "+6594949499", "Singapore", LocalDate.of(2000, 1, 1));
        Organiser organiser = new Organiser("Organizer One", "organizer.one@example.com", "password", "+6599999999", "Singapore");
        
        Tournament tournament = new Tournament("hi", organiser, LocalDate.of(2024, 12, 10), 60, LocalDate.of(2024, 12, 12), LocalDate.of(2024, 12, 15), "singapore", "hi", "hi", 'B');
        Event event = new Event(tournament, 'F', 'S', 10, LocalDate.of(2024, 12, 12), LocalTime.now(), LocalTime.now().plusHours(3));


        when(eventRepository.findById(tcid)).thenReturn(Optional.of(event));
        when(userRepository.save(fencer)).thenReturn(fencer);
        when(eventRepository.save(event)).thenReturn(event);

        // Act
        boolean result = eventService.registerEvent(tcid, fencer);

        // Assert
        assertTrue(result);
        assertEquals(1, event.getParticipantCount());
        assertEquals(1, event.getFencers().size());
    }

    @Test
    public void registerEvent_EventNotFound_ReturnFalse() {
        // Arrange
        int tcid = 1;
        Fencer fencer = new Fencer("DOE John", "john.doe@example.com", "password"
        , "+6599999999", "Singapore", LocalDate.of(2000, 1, 1));

        when(eventRepository.findById(tcid)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(EntityDoesNotExistException.class, () -> eventService.registerEvent(tcid, fencer));
    }


    @Test
    void createEvent_EventAlreadyExists() throws MethodArgumentNotValidException {
        // Arrange
        int tid = 1;
        Organiser organiser = new Organiser();
        organiser.setId(1);

        CreateEventDTO dto = new CreateEventDTO('F', 'S', 10, LocalDate.of(2024, 12, 12), LocalTime.now(), LocalTime.now().plusHours(3));

        Tournament tournament = new Tournament();
        tournament.setId(tid);
        tournament.setOrganiser(organiser);

        Event existingEvent = new Event();
        existingEvent.setTournament(tournament);
        existingEvent.setGender('F');
        existingEvent.setWeapon('S');
        existingEvent.setDate(LocalDate.of(2024, 12, 12));
        existingEvent.setStartTime(LocalTime.now());
        existingEvent.setEndTime(LocalTime.now().plusHours(3));

        when(tournamentRepository.findById(tid)).thenReturn(Optional.of(tournament));
        when(eventRepository.findByTournamentAndGenderAndWeapon(
                tournament, 'F', 'S'))
                .thenReturn(Optional.of(existingEvent));

        // Act & Assert
        assertThrows(EventAlreadyExistsException.class, () -> {
            eventService.createEvent(tid, organiser, Arrays.asList(dto));
        });
    }

    @Test
    void updateEvent_ValidOrganiser() throws MethodArgumentNotValidException {
        // Arrange
        Organiser organiser = new Organiser();
        organiser.setId(1);

        UpdateEventDTO dto = new UpdateEventDTO(0, null, null, null);
        dto.setMinParticipants(20);
        dto.setDate(LocalDate.now().plusDays(10));
        dto.setStartTime(LocalTime.now());
        dto.setEndTime(LocalTime.now().plusHours(3));

        Tournament tournament = new Tournament();
        tournament.setOrganiser(organiser);
        tournament.setStartDate(LocalDate.now().plusDays(9));
        tournament.setEndDate(LocalDate.now().plusDays(20));

        Event existingEvent = new Event();
        existingEvent.setId(1);
        existingEvent.setTournament(tournament);

        when(eventRepository.findById(1)).thenReturn(Optional.of(existingEvent));
        when(eventRepository.save(existingEvent)).thenReturn(existingEvent);

        // Act
        Event result = eventService.updateEvent(1, organiser, dto);

        // Assert
        assertEquals(existingEvent, result);
        verify(eventRepository).save(existingEvent);
    }

    @Test
    void updateEvent_InvalidOrganiser() {
        // Arrange
        Organiser organiser = new Organiser();
        organiser.setId(1);

        Organiser differentOrganiser = new Organiser();
        differentOrganiser.setId(2);

        UpdateEventDTO dto = new UpdateEventDTO(0, null, null, null);
        dto.setMinParticipants(20);
        dto.setDate(LocalDate.now().plusDays(10));
        dto.setStartTime(LocalTime.now());
        dto.setEndTime(LocalTime.now().plusHours(3));

        Tournament tournament = new Tournament();
        tournament.setOrganiser(differentOrganiser);

        Event existingEvent = new Event();
        existingEvent.setId(1);
        existingEvent.setTournament(tournament);

        when(eventRepository.findById(1)).thenReturn(Optional.of(existingEvent));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            eventService.updateEvent(1, organiser, dto);
        });

        assertEquals("Organiser does not match the tournament organiser.", exception.getMessage());
    }


    @Test
    void registerEvent_SignUpDateOver() {
        // Arrange
        int eid = 1;
        Fencer fencer = new Fencer();
        fencer.setId(1);

        Tournament tournament = new Tournament();
        tournament.setSignupEndDate(LocalDate.now().minusDays(1));

        Event event = new Event();
        event.setId(eid);
        event.setTournament(tournament);

        when(eventRepository.findById(eid)).thenReturn(Optional.of(event));

        // Act & Assert
        assertThrows(SignUpDateOverExcpetion.class, () -> {
            eventService.registerEvent(eid, fencer);
        });
    }

    @Test
    void registerEvent_FencerAlreadyRegistered() {
        // Arrange
        int eid = 1;
        Fencer fencer = new Fencer();
        fencer.setId(1);

        Tournament tournament = new Tournament();
        tournament.setSignupEndDate(LocalDate.now().plusDays(1));

        Event event = new Event();
        event.setId(eid);
        event.setTournament(tournament);

        TournamentFencer existingTournamentFencer = new TournamentFencer();
        existingTournamentFencer.setFencer(fencer);
        existingTournamentFencer.setEvent(event);

        when(eventRepository.findById(eid)).thenReturn(Optional.of(event));
        when(tournamentFencerRepository.findByFencerAndEvent(fencer, event)).thenReturn(existingTournamentFencer);

        // Act & Assert
        assertThrows(FencerAlreadyRegisteredForEventException.class, () -> {
            eventService.registerEvent(eid, fencer);
        });
    }

    @Test
    void registerEvent_NfIsNull() {
        // Arrange
        int eid = 1;
        Fencer fencer = new Fencer();
        fencer.setId(1);
        fencer.setTournamentFencerProfiles(new HashSet<>()); // Initialize the set

        Tournament tournament = new Tournament();
        tournament.setSignupEndDate(LocalDate.now().plusDays(1));

        Event event = new Event();
        event.setId(eid);
        event.setTournament(tournament);
        event.setFencers(new HashSet<>()); // Initialize the set

        when(eventRepository.findById(eid)).thenReturn(Optional.of(event));
        when(tournamentFencerRepository.findByFencerAndEvent(fencer, event)).thenReturn(null);
        when(userRepository.save(fencer)).thenReturn(null);

        // Act
        boolean result = eventService.registerEvent(eid, fencer);

        // Assert
        assertFalse(result);
    }

    @Test
    void registerEvent_TcIsNull() {
        // Arrange
        int eid = 1;
        Fencer fencer = new Fencer();
        fencer.setId(1);
        fencer.setTournamentFencerProfiles(new HashSet<>()); // Initialize the set

        Tournament tournament = new Tournament();
        tournament.setSignupEndDate(LocalDate.now().plusDays(1));

        Event event = new Event();
        event.setId(eid);
        event.setTournament(tournament);
        event.setFencers(new HashSet<>()); // Initialize the set

        when(eventRepository.findById(eid)).thenReturn(Optional.of(event));
        when(tournamentFencerRepository.findByFencerAndEvent(fencer, event)).thenReturn(null);
        when(userRepository.save(fencer)).thenReturn(fencer);
        when(eventRepository.save(event)).thenReturn(null);

        // Act
        boolean result = eventService.registerEvent(eid, fencer);

        // Assert
        assertFalse(result);
    }

    @Test
    void unregisterEvent() {
        // Arrange
        int eid = 1;
        Fencer fencer = new Fencer();
        fencer.setId(1);
        fencer.setTournamentFencerProfiles(new HashSet<>()); // Initialize the set

        Event event = new Event();
        event.setId(eid);

        TournamentFencer tournamentFencer = new TournamentFencer();
        tournamentFencer.setFencer(fencer);
        tournamentFencer.setEvent(event);

        Set<TournamentFencer> fencers = new HashSet<>();
        fencers.add(tournamentFencer);
        event.setFencers(fencers);

        when(eventRepository.findById(eid)).thenReturn(Optional.of(event));
        when(tournamentFencerRepository.findByFencerAndEvent(fencer, event)).thenReturn(tournamentFencer);
        when(userRepository.save(fencer)).thenReturn(fencer);
        when(eventRepository.save(event)).thenReturn(event);

        // Act
        boolean result = eventService.unregisterEvent(eid, fencer);

        // Assert
        verify(tournamentFencerRepository).delete(tournamentFencer);
        assertEquals(0, event.getFencers().size());
        assertTrue(result);
    }



    @Test
    void unregisterEvent_NfIsNull() {
        // Arrange
        int eid = 1;
        Fencer fencer = new Fencer();
        fencer.setId(1);
        fencer.setTournamentFencerProfiles(new HashSet<>()); // Initialize the set

        Event event = new Event();
        event.setId(eid);
        event.setFencers(new HashSet<>()); // Initialize the set

        TournamentFencer tournamentFencer = new TournamentFencer();
        tournamentFencer.setFencer(fencer);
        tournamentFencer.setEvent(event);

        event.getFencers().add(tournamentFencer);
        fencer.getTournamentFencerProfiles().add(tournamentFencer);

        when(eventRepository.findById(eid)).thenReturn(Optional.of(event));
        when(tournamentFencerRepository.findByFencerAndEvent(fencer, event)).thenReturn(tournamentFencer);
        when(userRepository.save(fencer)).thenReturn(null);

        // Act
        boolean result = eventService.unregisterEvent(eid, fencer);

        // Assert
        assertFalse(result);
    }



    @Test
    void unregisterEvent_TcIsNull() {
        // Arrange
        int eid = 1;
        Fencer fencer = new Fencer();
        fencer.setId(1);
        fencer.setTournamentFencerProfiles(new HashSet<>()); // Initialize the set

        Event event = new Event();
        event.setId(eid);
        event.setFencers(new HashSet<>()); // Initialize the set

        TournamentFencer tournamentFencer = new TournamentFencer();
        tournamentFencer.setFencer(fencer);
        tournamentFencer.setEvent(event);

        event.getFencers().add(tournamentFencer);
        fencer.getTournamentFencerProfiles().add(tournamentFencer);

        when(eventRepository.findById(eid)).thenReturn(Optional.of(event));
        when(tournamentFencerRepository.findByFencerAndEvent(fencer, event)).thenReturn(tournamentFencer);
        when(userRepository.save(fencer)).thenReturn(fencer);
        when(eventRepository.save(event)).thenReturn(null);

        // Act
        boolean result = eventService.unregisterEvent(eid, fencer);

        // Assert
        assertFalse(result);
    }

    @Test
    void getTournamentRanks() {
        // Arrange
        int eid = 1;

        Event event = new Event();
        event.setId(eid);

        TournamentFencer tournamentFencer1 = new TournamentFencer();
        tournamentFencer1.setTournamentRank(1);

        TournamentFencer tournamentFencer2 = new TournamentFencer();
        tournamentFencer2.setTournamentRank(2);

        Set<TournamentFencer> fencers = new HashSet<>();
        fencers.add(tournamentFencer1);
        fencers.add(tournamentFencer2);
        event.setFencers(fencers);

        when(eventRepository.findById(eid)).thenReturn(Optional.of(event));

        // Act
        List<TournamentFencer> result = eventService.getTournamentRanks(eid);

        // Assert
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getTournamentRank());
        assertEquals(2, result.get(1).getTournamentRank());
    }

    @Test
    void getTournamentRanks_EventDoesNotExist() {
        // Arrange
        int eid = 1;

        when(eventRepository.findById(eid)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityDoesNotExistException.class, () -> {
            eventService.getTournamentRanks(eid);
        });
    }

    @Test
    void endTournamentEvent() {
        // Arrange
        int eid = 1;

        Event event = new Event();
        event.setId(eid);

        DirectEliminationMatch match = new DirectEliminationMatch();
        match.setWinner(1);

        Fencer fencer1 = new Fencer();
        fencer1.setId(1);
        fencer1.setPoints(100);

        Fencer fencer2 = new Fencer();
        fencer2.setId(2);
        fencer2.setPoints(200);

        TournamentFencer tournamentFencer1 = new TournamentFencer();
        tournamentFencer1.setFencer(fencer1);
        tournamentFencer1.setTournamentRank(1);

        TournamentFencer tournamentFencer2 = new TournamentFencer();
        tournamentFencer2.setFencer(fencer2);
        tournamentFencer2.setTournamentRank(2);

        Set<TournamentFencer> fencers = new HashSet<>();
        fencers.add(tournamentFencer1);
        fencers.add(tournamentFencer2);
        event.setFencers(fencers);

        when(eventRepository.findById(eid)).thenReturn(Optional.of(event));
        when(directEliminationMatchRepository.findByEventAndRoundOf(event, 2)).thenReturn(Arrays.asList(match));
        when(tournamentFencerRepository.findByEvent(event)).thenReturn(Arrays.asList(tournamentFencer1, tournamentFencer2));

        // Act
        eventService.endTournamentEvent(eid);

        // Assert
        verify(eventRepository).save(event);
        assertEquals(true, event.isOver());
    }

    @Test
    void endTournamentEvent_EventDoesNotExist() {
        // Arrange
        int eid = 1;

        when(eventRepository.findById(eid)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityDoesNotExistException.class, () -> {
            eventService.endTournamentEvent(eid);
        });
    }

    @Test
    void endTournamentEvent_FinalMatchNotCompleted() {
        // Arrange
        int eid = 1;

        Event event = new Event();
        event.setId(eid);

        DirectEliminationMatch match = new DirectEliminationMatch();
        match.setWinner(-1);

        when(eventRepository.findById(eid)).thenReturn(Optional.of(event));
        when(directEliminationMatchRepository.findByEventAndRoundOf(event, 2)).thenReturn(Arrays.asList(match));

        // Act & Assert
        assertThrows(EventCannotEndException.class, () -> {
            eventService.endTournamentEvent(eid);
        });
    }

    @Test
    void endTournamentEvent_EventAlreadyEnded() {
        // Arrange
        int eid = 1;

        Event event = new Event();
        event.setId(eid);
        event.setOver(true); // Mark the event as already ended

        when(eventRepository.findById(eid)).thenReturn(Optional.of(event));

        // Act & Assert
        assertThrows(EventCannotEndException.class, () -> {
            eventService.endTournamentEvent(eid);
        });
    }
}
