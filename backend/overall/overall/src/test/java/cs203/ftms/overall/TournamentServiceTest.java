package cs203.ftms.overall;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

        when(eventService.getCleanEventDTO(event)).thenReturn(new CleanEventDTO(0, null, null, null, 0, 0, null, null, null));

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

@Test
    public void getTournament_TournamentNotFound_ReturnNull() {
        // Arrange
        int tournamentId = 1;

        when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.empty());

        // Act
        Tournament result = tournamentService.getTournament(tournamentId);

        // Assert
        assertNull(result);
    }

    @Test
    public void createTournament_ValidTournament_ReturnTournament() throws MethodArgumentNotValidException {
        // Arrange
        Organiser organiser = new Organiser();
        CreateTournamentDTO createTournamentDTO = new CreateTournamentDTO();
        createTournamentDTO.setName("National Tournament");
        createTournamentDTO.setSignupEndDate(LocalDate.of(2024, 9, 30));
        createTournamentDTO.setStartDate(LocalDate.of(2024, 10, 5));
        createTournamentDTO.setEndDate(LocalDate.of(2024, 10, 10));

        Tournament tournament = new Tournament(
            createTournamentDTO.getName(),
            organiser,
            createTournamentDTO.getSignupEndDate(),
            createTournamentDTO.getAdvancementRate(),
            createTournamentDTO.getStartDate(),
            createTournamentDTO.getEndDate(),
            createTournamentDTO.getLocation(),
            createTournamentDTO.getDescription(),
            createTournamentDTO.getRules()
        );

        when(tournamentRepository.save(any(Tournament.class))).thenReturn(tournament);

        // Act
        Tournament result = tournamentService.createTournament(createTournamentDTO, organiser);

        // Assert
        assertNotNull(result);
        assertEquals("National Tournament", result.getName());
        verify(tournamentRepository, times(1)).save(any(Tournament.class));
    }

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
}