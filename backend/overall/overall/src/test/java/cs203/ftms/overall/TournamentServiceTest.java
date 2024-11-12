package cs203.ftms.overall;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.web.bind.MethodArgumentNotValidException;

import cs203.ftms.overall.dto.CreateTournamentDTO;
import cs203.ftms.overall.dto.clean.CleanEventDTO;
import cs203.ftms.overall.dto.clean.CleanTournamentDTO;
import cs203.ftms.overall.model.tournamentrelated.Event;
import cs203.ftms.overall.model.tournamentrelated.Tournament;
import cs203.ftms.overall.model.userrelated.Organiser;
import cs203.ftms.overall.repository.tournamentrelated.TournamentRepository;
import cs203.ftms.overall.service.event.EventService;
import cs203.ftms.overall.service.tournament.TournamentService;
import cs203.ftms.overall.validation.OtherValidations;
import jakarta.transaction.Transactional;

public class TournamentServiceTest {

    @InjectMocks
    private TournamentService tournamentService;

    @Mock
    private TournamentRepository tournamentRepository;

    @Mock
    private EventService eventService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getCleanTournamentDTO_ValidTournamnet_ReturnCleanTournament() {
        // Arrange
        Tournament tournament = new Tournament();
        tournament.setId(1);
        tournament.setName("National Tournament");
        Organiser organiser = new Organiser();
        organiser.setName("John Doe");
        tournament.setOrganiser(organiser);

        Event event = new Event();
        Set<Event> events = new HashSet<>();
        events.add(event);
        tournament.setEvents(events);

        when(eventService.getCleanEventDTO(event)).thenReturn(new CleanEventDTO(0, 'W', 'F', null, null, 0, 0, null, null, null, null, false));

        // Act
        CleanTournamentDTO result = tournamentService.getCleanTournamentDTO(tournament);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("National Tournament", result.getName());
        assertEquals("John Doe", result.getOrganiserName());
        assertEquals(1, result.getEvents().size());
    }

    @Test
    public void getTournament_TournamentExists_ReturnTournament() {
        // Arrange
        int tournamentId = 1;
        Tournament tournament = new Tournament();
        tournament.setId(tournamentId);

        when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.of(tournament));

        // Act
        Tournament result = tournamentService.getTournament(tournamentId);

        // Assert
        assertNotNull(result);
        assertEquals(tournamentId, result.getId());
    }

// @Test
//     public void getTournament_TournamentNotFound_ReturnNull() {
//         // Arrange
//         int tournamentId = 1;

//         when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.empty());

//         // Act
//         Tournament result = tournamentService.getTournament(tournamentId);

//         // Assert
//         assertNull(result);
//     }

//     @Test
//     public void createTournament_ValidTournament_ReturnTournament() throws MethodArgumentNotValidException {
//         // Arrange
//         Organiser organiser = new Organiser();
//         CreateTournamentDTO createTournamentDTO = new CreateTournamentDTO();
//         createTournamentDTO.setName("National Tournament");
//         createTournamentDTO.setSignupEndDate(LocalDate.of(2024, 9, 30));
//         createTournamentDTO.setStartDate(LocalDate.of(2024, 10, 5));
//         createTournamentDTO.setEndDate(LocalDate.of(2024, 10, 10));

//         Tournament tournament = new Tournament(
//             createTournamentDTO.getName(),
//             organiser,
//             createTournamentDTO.getSignupEndDate(),
//             createTournamentDTO.getAdvancementRate(),
//             createTournamentDTO.getStartDate(),
//             createTournamentDTO.getEndDate(),
//             createTournamentDTO.getLocation(),
//             createTournamentDTO.getDescription(),
//             createTournamentDTO.getRules(),
//             createTournamentDTO.getDifficulty()
//         );

//         when(tournamentRepository.save(any(Tournament.class))).thenReturn(tournament);

//         // Act
//         Tournament result = tournamentService.createTournament(createTournamentDTO, organiser);

//         // Assert
//         assertNotNull(result);
//         assertEquals("National Tournament", result.getName());
//         verify(tournamentRepository, times(1)).save(any(Tournament.class));
//     }

    @Test
    public void createTournament_InValidTournamentSignUpEndDate_ReturnException() {
        // Arrange
        Tournament tournament = new Tournament();
        tournament.setStartDate(LocalDate.of(2024, 10, 5));
        tournament.setSignupEndDate(LocalDate.of(2024, 10, 4)); // Less than 1 day before start

        // Act & Assert
        MethodArgumentNotValidException exception = assertThrows(MethodArgumentNotValidException.class, () -> {
            OtherValidations.validTournamentSignUpEndDate(tournament);
        });

        assertNotNull(exception);
        assertEquals("Sign-up end date must be at least one day before the event start date.", 
                     exception.getBindingResult().getFieldError("signUpEndDate").getDefaultMessage());
    }

    @Test
    public void createTournament_ValidTournamentSignUpEndDate_Return() {
        // Arrange
        Tournament tournament = new Tournament();
        tournament.setStartDate(LocalDate.of(2024, 10, 5));
        tournament.setSignupEndDate(LocalDate.of(2024, 9, 30)); // Valid date

        // Act & Assert
        assertDoesNotThrow(() -> {
            OtherValidations.validTournamentSignUpEndDate(tournament);
        });
    }

    @Test
    void testGetAllTournaments_ReturnsListOfTournaments() {
        // Arrange
        Tournament tournament1 = new Tournament();
        tournament1.setName("Tournament 1");
        Tournament tournament2 = new Tournament();
        tournament2.setName("Tournament 2");
        List<Tournament> tournaments = Arrays.asList(tournament1, tournament2);

        when(tournamentRepository.findAll()).thenReturn(tournaments);

        // Act
        List<Tournament> result = tournamentService.getAllTournaments();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Tournament 1", result.get(0).getName());
        assertEquals("Tournament 2", result.get(1).getName());
        verify(tournamentRepository, times(1)).findAll();
    }

    @Test
    void testGetAllTournaments_ReturnsEmptyListWhenNoTournamentsFound() {
        // Arrange
        when(tournamentRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Tournament> result = tournamentService.getAllTournaments();

        // Assert
        assertTrue(result.isEmpty());
        verify(tournamentRepository, times(1)).findAll();
    }

    @Test
    void testGetCleanOrganiserDTO_ReturnsNull_WhenOrganiserIsNull() {
        // Act
        CleanTournamentDTO result = tournamentService.getCleanTournamentDTO(null);

        // Assert
        assertNull(result);
    }


    @Test
    void createTournament() throws Exception {
        // Arrange
        Organiser organiser = new Organiser();
        organiser.setId(1);

        CreateTournamentDTO dto = new CreateTournamentDTO();
        dto.setName("Tournament Name");
        dto.setSignupEndDate(LocalDate.now().plusDays(10));
        dto.setAdvancementRate(70);
        dto.setStartDate(LocalDate.now().plusDays(20));
        dto.setEndDate(LocalDate.now().plusDays(30));
        dto.setLocation("Location");
        dto.setDescription("Description");
        dto.setRules("Rules");
        dto.setDifficulty('A');

        Tournament tournament = new Tournament(dto.getName(), organiser, dto.getSignupEndDate(), dto.getAdvancementRate(), dto.getStartDate(), dto.getEndDate(), dto.getLocation(), dto.getDescription(), dto.getRules(), dto.getDifficulty());
        when(tournamentRepository.save(tournament)).thenReturn(tournament);

        // Act
        Tournament result = tournamentService.createTournament(dto, organiser);

        // Assert
        assertEquals(tournament, result);
        verify(tournamentRepository).save(tournament);
    }

    @Test
    @Transactional
    void updateTournament() throws Exception {
        // Arrange
        Organiser organiser = new Organiser();
        organiser.setId(1);

        CreateTournamentDTO dto = new CreateTournamentDTO();
        dto.setName("Updated Tournament Name");
        dto.setSignupEndDate(LocalDate.now().plusDays(10));
        dto.setAdvancementRate(70);
        dto.setStartDate(LocalDate.now().plusDays(20));
        dto.setEndDate(LocalDate.now().plusDays(30));
        dto.setLocation("Updated Location");
        dto.setDescription("Updated Description");
        dto.setRules("Updated Rules");
        dto.setDifficulty('B');

        Tournament existingTournament = new Tournament("Old Tournament Name", organiser, LocalDate.now().plusDays(5), 70, LocalDate.now().plusDays(15), LocalDate.now().plusDays(25), "Old Location", "Old Description", "Old Rules", 'A');
        existingTournament.setId(1);

        when(tournamentRepository.findById(1)).thenReturn(Optional.of(existingTournament));
        when(tournamentRepository.save(existingTournament)).thenReturn(existingTournament);

        // Act
        Tournament result = tournamentService.updateTournament(1, dto, organiser);

        // Assert
        assertEquals(existingTournament, result);
        verify(tournamentRepository).save(existingTournament);
    }

    @Test
    @Transactional
    void updateTournament_ValidOrganiser() throws Exception {
        // Arrange
        Organiser organiser = new Organiser();
        organiser.setId(1);

        CreateTournamentDTO dto = new CreateTournamentDTO();
        dto.setName("Updated Tournament Name");
        dto.setSignupEndDate(LocalDate.now().plusDays(10));
        dto.setAdvancementRate(70);
        dto.setStartDate(LocalDate.now().plusDays(20));
        dto.setEndDate(LocalDate.now().plusDays(30));
        dto.setLocation("Updated Location");
        dto.setDescription("Updated Description");
        dto.setRules("Updated Rules");
        dto.setDifficulty('B');

        Tournament existingTournament = new Tournament("Old Tournament Name", organiser, LocalDate.now().plusDays(5), 70, LocalDate.now().plusDays(15), LocalDate.now().plusDays(25), "Old Location", "Old Description", "Old Rules", 'A');
        existingTournament.setId(1);

        when(tournamentRepository.findById(1)).thenReturn(Optional.of(existingTournament));
        when(tournamentRepository.save(existingTournament)).thenReturn(existingTournament);

        // Act
        Tournament result = tournamentService.updateTournament(1, dto, organiser);

        // Assert
        assertEquals(existingTournament, result);
        verify(tournamentRepository).save(existingTournament);
    }

    @Test
    @Transactional
    void updateTournament_InvalidOrganiser() {
        // Arrange
        Organiser organiser = new Organiser();
        organiser.setId(1);

        Organiser differentOrganiser = new Organiser();
        differentOrganiser.setId(2);

        CreateTournamentDTO dto = new CreateTournamentDTO();
        dto.setName("Updated Tournament Name");
        dto.setSignupEndDate(LocalDate.now().plusDays(10));
        dto.setAdvancementRate(70);
        dto.setStartDate(LocalDate.now().plusDays(20));
        dto.setEndDate(LocalDate.now().plusDays(30));
        dto.setLocation("Updated Location");
        dto.setDescription("Updated Description");
        dto.setRules("Updated Rules");
        dto.setDifficulty('B');

        Tournament existingTournament = new Tournament("Old Tournament Name", organiser, LocalDate.now().plusDays(5), 70, LocalDate.now().plusDays(15), LocalDate.now().plusDays(25), "Old Location", "Old Description", "Old Rules", 'A');
        existingTournament.setId(1);

        when(tournamentRepository.findById(1)).thenReturn(Optional.of(existingTournament));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            tournamentService.updateTournament(1, dto, differentOrganiser);
        });

        assertEquals("Organiser does not match the tournament organiser.", exception.getMessage());
    }

    @Test
    @Transactional
    void updateTournament_ValidEventDates() throws Exception {
        // Arrange
        Organiser organiser = new Organiser();
        organiser.setId(1);

        CreateTournamentDTO dto = new CreateTournamentDTO();
        dto.setName("Updated Tournament Name");
        dto.setSignupEndDate(LocalDate.now().plusDays(10));
        dto.setAdvancementRate(70);
        dto.setStartDate(LocalDate.now().plusDays(20));
        dto.setEndDate(LocalDate.now().plusDays(30));
        dto.setLocation("Updated Location");
        dto.setDescription("Updated Description");
        dto.setRules("Updated Rules");
        dto.setDifficulty('B');

        Event event = new Event();
        event.setDate(LocalDate.now().plusDays(21));
        Set<Event> events = new HashSet<>();
        events.add(event);

        Tournament existingTournament = new Tournament("Old Tournament Name", organiser, LocalDate.now().plusDays(5), 70, LocalDate.now().plusDays(15), LocalDate.now().plusDays(25), "Old Location", "Old Description", "Old Rules", 'A');
        existingTournament.setId(1);
        existingTournament.setEvents(events);

        when(tournamentRepository.findById(1)).thenReturn(Optional.of(existingTournament));
        when(tournamentRepository.save(existingTournament)).thenReturn(existingTournament);

        // Act
        Tournament result = tournamentService.updateTournament(1, dto, organiser);

        // Assert
        assertEquals(existingTournament, result);
        verify(tournamentRepository).save(existingTournament);
    }

    @Test
    @Transactional
    void updateTournament_InvalidEventDates() {
        // Arrange
        Organiser organiser = new Organiser();
        organiser.setId(1);

        CreateTournamentDTO dto = new CreateTournamentDTO();
        dto.setName("Updated Tournament Name");
        dto.setSignupEndDate(LocalDate.now().plusDays(10));
        dto.setAdvancementRate(80);
        dto.setStartDate(LocalDate.now().plusDays(20));
        dto.setEndDate(LocalDate.now().plusDays(30));
        dto.setLocation("Updated Location");
        dto.setDescription("Updated Description");
        dto.setRules("Updated Rules");
        dto.setDifficulty('B');

        Event event = new Event();
        event.setDate(LocalDate.now().plusDays(31));
        Set<Event> events = new HashSet<>();
        events.add(event);

        Tournament existingTournament = new Tournament("Old Tournament Name", organiser, LocalDate.now().plusDays(5), 80, LocalDate.now().plusDays(15), LocalDate.now().plusDays(25), "Old Location", "Old Description", "Old Rules", 'A');
        existingTournament.setId(1);
        existingTournament.setEvents(events);

        when(tournamentRepository.findById(1)).thenReturn(Optional.of(existingTournament));

        // Act & Assert
        assertThrows(MethodArgumentNotValidException.class, () -> {
            tournamentService.updateTournament(1, dto, organiser);
        });
    }

    @Test
    void getUpcomingTournaments() {
        // Arrange
        Tournament pastTournament = new Tournament();
        pastTournament.setId(1);
        pastTournament.setStartDate(LocalDate.now().minusDays(10));

        Tournament futureTournament = new Tournament();
        futureTournament.setId(2);
        futureTournament.setStartDate(LocalDate.now().plusDays(10));

        List<Tournament> tournaments = Arrays.asList(pastTournament, futureTournament);
        when(tournamentRepository.findAll()).thenReturn(tournaments);

        // Act
        List<Tournament> result = tournamentService.getUpcomingTournaments();

        // Assert
        assertEquals(1, result.size());
        assertEquals(futureTournament.getId(), result.get(0).getId());
    }

    @Test
    void getPastTournaments() {
        // Arrange
        Tournament pastTournament = new Tournament();
        pastTournament.setId(1);
        pastTournament.setStartDate(LocalDate.now().minusDays(10));

        Tournament futureTournament = new Tournament();
        futureTournament.setId(2);
        futureTournament.setStartDate(LocalDate.now().plusDays(10));

        List<Tournament> tournaments = Arrays.asList(pastTournament, futureTournament);
        when(tournamentRepository.findAll()).thenReturn(tournaments);

        // Act
        List<Tournament> result = tournamentService.getPastTournaments();

        // Assert
        assertEquals(1, result.size());
        assertEquals(pastTournament.getId(), result.get(0).getId());
    }
}
