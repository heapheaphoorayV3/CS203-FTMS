package cs203.ftms.overall;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.bind.MethodArgumentNotValidException;

import cs203.ftms.overall.dto.clean.CleanOrganiserDTO;
import cs203.ftms.overall.model.tournamentrelated.Tournament;
import cs203.ftms.overall.model.userrelated.Organiser;
import cs203.ftms.overall.repository.tournamentrelated.TournamentRepository;
import cs203.ftms.overall.repository.userrelated.UserRepository;
import cs203.ftms.overall.service.organiser.OrganiserService;

class OrganiserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TournamentRepository tournamentRepository;

    @InjectMocks
    private OrganiserService organiserService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // 1. Tests for getCleanOrganiserDTO
    @Test
    void testGetCleanOrganiserDTO_ReturnsDTO_WhenOrganiserIsNotNull() {
        // Arrange
        Organiser organiser = new Organiser("Organiser1", "organiser1@xyz.com", "Abcd1234!", "+6591234567", "Singapore");
        organiser.setId(1);

        // Act
        CleanOrganiserDTO result = organiserService.getCleanOrganiserDTO(organiser);

        // Assert
        assertNotNull(result);
        assertEquals(organiser.getId(), result.getId());
        assertEquals(organiser.isVerified(), result.isVerified());
        assertEquals(organiser.getName(), result.getName());
        assertEquals(organiser.getEmail(), result.getEmail());
        assertEquals(organiser.getContactNo(), result.getContactNo());
        assertEquals(organiser.getCountry(), result.getCountry());
    }

    @Test
    void testGetCleanOrganiserDTO_ReturnsNull_WhenOrganiserIsNull() {
        // Act
        CleanOrganiserDTO result = organiserService.getCleanOrganiserDTO(null);

        // Assert
        assertNull(result);
    }

    // 2. Tests for getOrganiserTournaments
    @Test
    void testGetOrganiserTournaments_ReturnsTournamentList_WhenTournamentsExist() {
        // Arrange
        Organiser organiser = new Organiser("Organiser1", "organiser1@xyz.com", "Abcd1234!", "+6591234567", "Singapore");
        Tournament tournament1 = new Tournament();
        Tournament tournament2 = new Tournament();
        List<Tournament> tournaments = Arrays.asList(tournament1, tournament2);

        when(tournamentRepository.findByOrganiserId(organiser.getId())).thenReturn(tournaments);

        // Act
        List<Tournament> result = organiserService.getOrganiserTournaments(organiser);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(tournamentRepository, times(1)).findByOrganiserId(organiser.getId());
    }

    @Test
    void testGetOrganiserTournaments_ReturnsNull_WhenNoTournamentsExist() {
        // Arrange
        Organiser organiser = new Organiser("Organiser1", "organiser1@xyz.com", "Abcd1234!", "+6591234567", "Singapore");
        
        when(tournamentRepository.findByOrganiserId(organiser.getId())).thenReturn(new ArrayList<>());

        // Act
        List<Tournament> result = organiserService.getOrganiserTournaments(organiser);

        // Assert
        assertNull(result);
        verify(tournamentRepository, times(1)).findByOrganiserId(organiser.getId());
    }
}
