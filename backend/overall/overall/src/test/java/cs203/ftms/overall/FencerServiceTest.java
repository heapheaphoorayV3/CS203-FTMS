package cs203.ftms.overall;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.MethodArgumentNotValidException;

import cs203.ftms.overall.dto.CompleteFencerProfileDTO;
import cs203.ftms.overall.dto.UpdateFencerProfileDTO;
import cs203.ftms.overall.dto.clean.CleanFencerDTO;
import cs203.ftms.overall.dto.clean.CleanTournamentFencerDTO;
import cs203.ftms.overall.model.tournamentrelated.Event;
import cs203.ftms.overall.model.tournamentrelated.TournamentFencer;
import cs203.ftms.overall.model.userrelated.Fencer;
import cs203.ftms.overall.repository.tournamentrelated.EventRepository;
import cs203.ftms.overall.repository.userrelated.FencerRepository;
import cs203.ftms.overall.repository.userrelated.UserRepository;
import cs203.ftms.overall.service.authentication.AuthenticationService;
import cs203.ftms.overall.service.event.EventService;
import cs203.ftms.overall.service.fencer.FencerService;


class FencerServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private FencerRepository fencerRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private FencerService fencerService;

    @Mock
    private EventService eventService;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authenticationService = new AuthenticationService(userRepository, authenticationManager, passwordEncoder, null);
    }

    /**
     * Test case to verify that getting a clean tournament fencer DTO returns null when the input is null.
     */
    @Test
    void getCleanTournamentFencerDTO_TournamentFencerIsNull() {
        // Act
        CleanTournamentFencerDTO result = fencerService.getCleanTournamentFencerDTO(null);

        // Assert
        assertNull(result);
    }

    /**
     * Test case to verify that a clean tournament fencer DTO is correctly created when the tournament fencer is not null.
     */
    @Test
    void getCleanTournamentFencerDTO_TournamentFencerIsNotNull() {
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
        CleanTournamentFencerDTO result = fencerService.getCleanTournamentFencerDTO(tournamentFencer);

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

    /**
     * Test case to verify that a clean fencer DTO is correctly created from a valid fencer.
     */
    @Test
    void getCleanFencerDTO_ValidFencer_ReturnCleanFencer() {
        // Given
        Fencer fencer = new Fencer();
        fencer.setId(1);
        fencer.setName("John Doe");
        fencer.setEmail("john@example.com");
        fencer.setContactNo("123456789");
        fencer.setCountry("USA");
        fencer.setDateOfBirth(LocalDate.of(1990, 1, 1));
        fencer.setDominantArm('R');
        fencer.setWeapon('F');
        fencer.setClub("Best Club");
        fencer.setPoints(100);
        fencer.setDebutYear(2008);
        fencer.setGender('M');

        // When
        CleanFencerDTO cleanFencerDTO = fencerService.getCleanFencerDTO(fencer);

        // Then
        assertNotNull(cleanFencerDTO);
        assertEquals(fencer.getId(), cleanFencerDTO.getId());
        assertEquals(fencer.getName(), cleanFencerDTO.getName());
        assertEquals(fencer.getEmail(), cleanFencerDTO.getEmail());
        assertEquals(fencer.getDebutYear(), cleanFencerDTO.getDebutYear());
    }

    /**
     * Test case to verify that getting a clean fencer DTO returns null for an invalid fencer.
     */
    @Test
    void getCleanFencerDTO_InvalidFencer_ReturnNull() {
        // When
        CleanFencerDTO cleanFencerDTO = fencerService.getCleanFencerDTO(null);

        // Then
        assertNull(cleanFencerDTO);
    }

    /**
     * Test case to verify that all fencers are correctly retrieved from the repository.
     */
    @Test
    void getAllFencers() {
        // Arrange
        Fencer fencer1 = new Fencer();
        fencer1.setId(1);
        fencer1.setName("John Doe");

        Fencer fencer2 = new Fencer();
        fencer2.setId(2);
        fencer2.setName("Jane Doe");

        List<Fencer> fencers = Arrays.asList(fencer1, fencer2);
        when(fencerRepository.findAll()).thenReturn(fencers);

        // Act
        List<Fencer> result = fencerService.getAllFencers();

        // Assert
        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getName());
        assertEquals("Jane Doe", result.get(1).getName());
    }

    /**
     * Test case to verify that completing a fencer profile with valid input updates the fencer details.
     */
    @Test
    void completeProfile_ValidInput_ReturnFencer() throws MethodArgumentNotValidException {
        // Given
        Fencer fencer = new Fencer();
        fencer.setDateOfBirth(LocalDate.of(1990, 1, 1));

        CompleteFencerProfileDTO dto = new CompleteFencerProfileDTO(null, null, null, null, 0);
        dto.setClub("Best Club");
        dto.setDebutYear(2010);
        dto.setDominantArm('R');
        dto.setGender('M');
        dto.setWeapon('F');

        when(userRepository.save(any(Fencer.class))).thenReturn(fencer);

        // When
        Fencer updatedFencer = fencerService.completeProfile(fencer, dto);

        // Then
        assertNotNull(updatedFencer);
        assertEquals("Best Club", updatedFencer.getClub());
        assertEquals(2010, updatedFencer.getDebutYear());
        assertEquals('R', updatedFencer.getDominantArm());
        assertEquals('M', updatedFencer.getGender());
        verify(userRepository, times(1)).save(fencer);
    }

    /**
     * Test case to verify that completing a fencer profile with an invalid debut year throws an exception.
     */
    @Test
    void completeProfile_InvalidDebutYear_ReturnException() {
        // Given
        Fencer fencer = new Fencer();
        fencer.setDateOfBirth(LocalDate.of(2010, 1, 1));

        CompleteFencerProfileDTO dto = new CompleteFencerProfileDTO('L', 'S', 'M', "SMU", 2002);
        dto.setClub("Best Club");
        dto.setDebutYear(2015); // Invalid: less than 8 years after DoB
        dto.setDominantArm('R');
        dto.setGender('M');
        dto.setWeapon('F');

        // Expect exception
        assertThrows(MethodArgumentNotValidException.class, () -> {
            fencerService.completeProfile(fencer, dto);
        });

        verify(userRepository, times(0)).save(any(Fencer.class));
    }

    /**
     * Test case to verify the international ranking of male sabre fencers based on their points.
     */
    @Test
    void testGetInternationalRank_MenSabre_ReturnsSortedFencers() {
        // Arrange
        Fencer fencer1 = new Fencer();
        fencer1.setId(1);
        fencer1.setPoints(50);
        fencer1.setGender('M');
        fencer1.setWeapon('S');
        Fencer fencer2 = new Fencer();
        fencer2.setId(2);
        fencer2.setPoints(100);
        fencer2.setGender('M');
        fencer2.setWeapon('S');
        Fencer fencer3 = new Fencer();
        fencer3.setId(3);
        fencer3.setPoints(75);
        fencer3.setGender('M');
        fencer3.setWeapon('S');
        
        List<Fencer> fencers = Arrays.asList(fencer1, fencer2, fencer3);
        when(fencerRepository.findAll()).thenReturn(fencers);

        // Act
        int result1 = fencerService.getInternationalRank(fencer1);
        int result2 = fencerService.getInternationalRank(fencer2);
        int result3 = fencerService.getInternationalRank(fencer3);

        // Assert
        assertEquals(3, result1);   // Lowest points
        assertEquals(1, result2);  // Highest points
        assertEquals(2, result3);  // Second highest points
        verify(fencerRepository, times(3)).findAll();
    }

    /**
     * Test case to verify that updating a fencer's profile applies all changes correctly.
     */
    @Test
    void updateProfile() {
        // Arrange
        Fencer fencer = new Fencer();
        fencer.setId(1);
        fencer.setClub("Old Club");
        fencer.setContactNo("123456789");
        fencer.setCountry("Old Country");
        fencer.setEmail("old@example.com");
        fencer.setName("Old Name");
        fencer.setDominantArm('R');

        UpdateFencerProfileDTO dto = new UpdateFencerProfileDTO(null, null, null, null, 'R', null);
        dto.setClub("New Club");
        dto.setContactNo("987654321");
        dto.setCountry("New Country");
        dto.setEmail("new@example.com");
        dto.setName("New Name");
        dto.setDominantArm('L');

        // Act
        fencerService.updateProfile(fencer, dto);

        // Assert
        assertEquals("New Club", fencer.getClub());
        assertEquals("987654321", fencer.getContactNo());
        assertEquals("New Country", fencer.getCountry());
        assertEquals("new@example.com", fencer.getEmail());
        assertEquals("New Name", fencer.getName());
        assertEquals('L', fencer.getDominantArm());
        verify(userRepository).save(fencer);
    }

    /**
     * Test case to verify that only past events are retrieved from a fencer's profile.
     */
    @Test
    void getFencerPastEventsProfiles() {
        // Arrange
        Fencer fencer = new Fencer();
        fencer.setId(1);

        Event pastEvent = new Event();
        pastEvent.setId(1);
        pastEvent.setDate(LocalDate.now().minusDays(10));

        Event futureEvent = new Event();
        futureEvent.setId(2);
        futureEvent.setDate(LocalDate.now().plusDays(10));

        TournamentFencer pastTournamentFencer = new TournamentFencer();
        pastTournamentFencer.setEvent(pastEvent);

        TournamentFencer futureTournamentFencer = new TournamentFencer();
        futureTournamentFencer.setEvent(futureEvent);

        Set<TournamentFencer> tournamentFencers = new HashSet<>();
        tournamentFencers.add(pastTournamentFencer);
        tournamentFencers.add(futureTournamentFencer);

        fencer.setTournamentFencerProfiles(tournamentFencers);

        // Act
        List<TournamentFencer> result = fencerService.getFencerPastEventsProfiles(fencer);

        // Assert
        assertEquals(1, result.size());
        assertEquals(pastEvent.getId(), result.get(0).getEvent().getId());
    }

    /**
     * Test case to verify that all events (past and upcoming) associated with a fencer are retrieved.
     */
    @Test
    void getFencerEvents() {
        // Arrange
        Fencer fencer = new Fencer();
        fencer.setId(1);

        Event event1 = new Event();
        event1.setId(1);
        event1.setDate(LocalDate.now().minusDays(10));

        Event event2 = new Event();
        event2.setId(2);
        event2.setDate(LocalDate.now().plusDays(9));

        TournamentFencer tournamentFencer1 = new TournamentFencer();
        tournamentFencer1.setEvent(event1);

        TournamentFencer tournamentFencer2 = new TournamentFencer();
        tournamentFencer2.setEvent(event2);

        Set<TournamentFencer> tournamentFencers = new HashSet<>();
        tournamentFencers.add(tournamentFencer1);
        tournamentFencers.add(tournamentFencer2);

        fencer.setTournamentFencerProfiles(tournamentFencers);

        when(eventRepository.findById(1)).thenReturn(Optional.of(event1));
        when(eventRepository.findById(2)).thenReturn(Optional.of(event2));

        // Act
        List<Event> result = fencerService.getFencerEvents(fencer);

        // Assert
        assertEquals(2, result.size());
        assertEquals(event1.getId(), result.get(0).getId());
        assertEquals(event2.getId(), result.get(1).getId());
    }

    /**
     * Test case to verify that only upcoming events are retrieved from a fencer's profile.
     */
    @Test
    void getFencerUpcomingEvents() {
        // Arrange
        Fencer fencer = new Fencer();
        fencer.setId(1);

        Event pastEvent = new Event();
        pastEvent.setId(1);
        pastEvent.setDate(LocalDate.now().minusDays(10));

        Event futureEvent = new Event();
        futureEvent.setId(2);
        futureEvent.setDate(LocalDate.now().plusDays(10));

        TournamentFencer pastTournamentFencer = new TournamentFencer();
        pastTournamentFencer.setEvent(pastEvent);

        TournamentFencer futureTournamentFencer = new TournamentFencer();
        futureTournamentFencer.setEvent(futureEvent);

        Set<TournamentFencer> tournamentFencers = new HashSet<>();
        tournamentFencers.add(pastTournamentFencer);
        tournamentFencers.add(futureTournamentFencer);

        fencer.setTournamentFencerProfiles(tournamentFencers);

        when(eventRepository.findById(1)).thenReturn(Optional.of(pastEvent));
        when(eventRepository.findById(2)).thenReturn(Optional.of(futureEvent));

        // Act
        List<Event> result = fencerService.getFencerUpcomingEvents(fencer);

        // Assert
        assertEquals(1, result.size());
        assertEquals(futureEvent.getId(), result.get(0).getId());
    }

    /**
     * Test case to verify that only past events associated with a fencer are retrieved.
     */
    @Test
    void getFencerPastEvents() {
        // Arrange
        Fencer fencer = new Fencer();
        fencer.setId(1);

        Event pastEvent = new Event();
        pastEvent.setId(1);
        pastEvent.setDate(LocalDate.now().minusDays(10)); // Past date

        Event futureEvent = new Event();
        futureEvent.setId(2);
        futureEvent.setDate(LocalDate.now().plusDays(10)); // Future date

        TournamentFencer pastTournamentFencer = new TournamentFencer();
        pastTournamentFencer.setEvent(pastEvent);

        TournamentFencer futureTournamentFencer = new TournamentFencer();
        futureTournamentFencer.setEvent(futureEvent);

        Set<TournamentFencer> tournamentFencers = new HashSet<>();
        tournamentFencers.add(pastTournamentFencer);
        tournamentFencers.add(futureTournamentFencer);

        fencer.setTournamentFencerProfiles(tournamentFencers);

        when(eventRepository.findById(1)).thenReturn(Optional.of(pastEvent));
        when(eventRepository.findById(2)).thenReturn(Optional.of(futureEvent));

        // Act
        List<Event> result = fencerService.getFencerPastEvents(fencer);

        // Assert
        assertEquals(1, result.size()); // Only one past event should be returned
        assertEquals(pastEvent.getId(), result.get(0).getId()); // Verify the returned event is the past event
    }

    /**
     * Test case to verify that fencer rankings can be filtered by weapon type and gender.
     */
    @Test
    void getFilterdInternationalRank() {
        // Arrange
        Fencer fencer1 = new Fencer();
        fencer1.setId(1);
        fencer1.setWeapon('E'); // Eligible weapon
        fencer1.setGender('M'); // Eligible gender
        fencer1.setPoints(100);

        Fencer fencer2 = new Fencer();
        fencer2.setId(2);
        fencer2.setWeapon('E'); // Eligible weapon
        fencer2.setGender('F'); // Ineligible gender
        fencer2.setPoints(200);

        Fencer fencer3 = new Fencer();
        fencer3.setId(3);
        fencer3.setWeapon('S'); // Ineligible weapon
        fencer3.setGender('M'); // Eligible gender
        fencer3.setPoints(150);

        List<Fencer> fencers = Arrays.asList(fencer1, fencer2, fencer3);
        when(fencerRepository.findAll()).thenReturn(fencers);

        // Act
        List<Fencer> result = fencerService.getFilterdInternationalRank('E', 'M'); // Filter by weapon 'E' and gender 'M'

        // Assert
        assertEquals(1, result.size()); // Only one fencer matches the criteria
        assertEquals(fencer1.getId(), result.get(0).getId()); // Verify the correct fencer is returned
    }

    /**
     * Test case to verify that past events are sorted and converted into DTOs for a fencer.
     */
    @Test
    void getFencerPastEventsPoints_ShouldReturnSortedCleanTournamentFencerDTOs() {
        // Arrange
        Fencer fencer = new Fencer();
        Fencer fencer2 = new Fencer();
        Set<TournamentFencer> tournamentFencers = new HashSet<>();

        TournamentFencer tf1 = new TournamentFencer();
        tf1.setId(1);
        Event event1 = new Event();
        event1.setId(1);
        event1.setDate(new Date(1000000000L).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()); // Set a date
        tf1.setEvent(event1);
        tournamentFencers.add(tf1);
        tf1.setFencer(fencer);

        TournamentFencer tf2 = new TournamentFencer();
        tf2.setId(2);
        Event event2 = new Event();
        event2.setId(2);
        event2.setDate(new Date(2000000000L).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()); // Set a later date
        tf2.setEvent(event2);
        tournamentFencers.add(tf2);
        tf2.setFencer(fencer2);

        fencer.setTournamentFencerProfiles(tournamentFencers);

        CleanTournamentFencerDTO cleanTF1 = new CleanTournamentFencerDTO(tf1.getId(), tf1.getFencer().getId(), tf1.getFencer().getName(), tf1.getFencer().getClub(), tf1.getFencer().getCountry(), 'R', tf1.getTournamentRank(), tf1.getEvent().getId(), tf1.getPouleWins(), tf1.getPoulePoints(), 0);
        CleanTournamentFencerDTO cleanTF2 = new CleanTournamentFencerDTO(tf2.getId(), tf2.getFencer().getId(), tf2.getFencer().getName(), tf2.getFencer().getClub(), tf2.getFencer().getCountry(), 'R', tf2.getTournamentRank(), tf2.getEvent().getId(), tf2.getPouleWins(), tf2.getPoulePoints(), 0);

        when(eventService.getCleanTournamentFencerDTO(tf1)).thenReturn(cleanTF1);
        when(eventService.getCleanTournamentFencerDTO(tf2)).thenReturn(cleanTF2);

        // Act
        List<CleanTournamentFencerDTO> result = fencerService.getFencerPastEventsPoints(fencer);

        // Assert
        assertEquals(2, result.size());
        assertEquals(event1.getId(), result.get(0).getEventId());
        assertEquals(event2.getId(), result.get(1).getEventId());
    }
}
