package cs203.ftms.overall;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.bind.MethodArgumentNotValidException;

import cs203.ftms.overall.dto.CreateEventDTO;
import cs203.ftms.overall.dto.clean.CleanEventDTO;
import cs203.ftms.overall.dto.clean.CleanFencerDTO;
import cs203.ftms.overall.exception.EntityDoesNotExistException;
import cs203.ftms.overall.model.tournamentrelated.Event;
import cs203.ftms.overall.model.tournamentrelated.Tournament;
import cs203.ftms.overall.model.tournamentrelated.TournamentFencer;
import cs203.ftms.overall.model.userrelated.Fencer;
import cs203.ftms.overall.model.userrelated.Organiser;
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
    public void createEvent_TournamentNotFound_ReturnNull() throws MethodArgumentNotValidException {
        // Arrange
        int tid = 1;
        Organiser organiser = new Organiser();
        CreateEventDTO dto = new CreateEventDTO('F', 'S', 10, LocalDate.of(2024, 12, 12), LocalTime.now(), LocalTime.now().plusHours(3));

        when(tournamentRepository.findById(tid)).thenReturn(Optional.empty());

        // Act
        List<Event> result = eventService.createEvent(tid, organiser, Arrays.asList(dto));

        // Assert
        assertNull(result);
    }

    @Test
    public void createEvent_InvalidOrganiser_ReturnNull() throws MethodArgumentNotValidException {
        // Arrange
        Organiser organiser = new Organiser("Organizer One", "organizer.one@example.com", "password", "+6599999999", "Singapore");
        Organiser differentOrganiser = new Organiser("Organizer two", "organizer.two@example.com", "password", "+6599999998", "Singapore");
        Tournament tournament = new Tournament("hi", differentOrganiser, LocalDate.of(2024, 12, 10), 60, LocalDate.of(2024, 12, 12), LocalDate.of(2024, 12, 15), "singapore", "hi", "hi");
        tournamentRepository.save(tournament);

        // supposed to have different ids, but sometimes it is both 0 due to database flushing possibly
        organiser.setId(1);
        differentOrganiser.setId(2);

        when(tournamentRepository.findById(any(Integer.class))).thenReturn(Optional.of(tournament));

        // Act
        List<Event> result = eventService.createEvent(tournament.getId(), organiser, Arrays.asList(new CreateEventDTO('F', 'S', 10, LocalDate.of(2024, 12, 12), LocalTime.now(), LocalTime.now().plusHours(3))));

        // Assert
        assertNull(result);
    }

    @Test
    public void createEvent_ValidEvent_ReturnEventList() throws MethodArgumentNotValidException {
        // Arrange
        int tid = 1;
        Organiser organiser = new Organiser("Organizer One", "organizer.one@example.com", "password", "+6599999999", "Singapore");
        Tournament tournament = new Tournament("hi", organiser, LocalDate.of(2024, 12, 10), 60, LocalDate.of(2024, 12, 12), LocalDate.of(2024, 12, 15), "singapore", "hi", "hi");
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
        
        Tournament tournament = new Tournament("hi", organiser, LocalDate.of(2024, 12, 10), 60, LocalDate.of(2024, 12, 12), LocalDate.of(2024, 12, 15), "singapore", "hi", "hi");
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
    public void createPoules_Test_ReturnTrue() {
        // Arange
        int tcid = 1;
        Organiser organiser = new Organiser("Organizer One", "organizer.one@example.com", "password", "+6599999999", "Singapore");
        
        Tournament tournament = new Tournament("hi", organiser, LocalDate.of(2024, 12, 10), 60, LocalDate.of(2024, 12, 12), LocalDate.of(2024, 12, 15), "singapore", "hi", "hi");
        Event event = new Event(tournament, 'F', 'S', 10, LocalDate.of(2024, 12, 12), LocalTime.now(), LocalTime.now().plusHours(3));

        when(eventRepository.findById(tcid)).thenReturn(Optional.of(event));
        when(eventRepository.save(event)).thenReturn(event);

        for(int i = 1; i < 11;i++){
        Fencer fencer = new Fencer("DOE John" + String.valueOf(i), "john.doe" + String.valueOf(i) + "@example.com", "password"
        , "+6594949499", "Singapore", LocalDate.of(2000, 1, 1));
        when(userRepository.save(fencer)).thenReturn(fencer);
        }


        // Act

        // Assert

    }

    @Test
    public void recommendPoules_Test(){

    }
}
