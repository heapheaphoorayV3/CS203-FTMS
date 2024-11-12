package cs203.ftms.overall;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import cs203.ftms.overall.dto.UpdateOrganiserProfileDTO;
import cs203.ftms.overall.dto.clean.CleanOrganiserDTO;
import cs203.ftms.overall.model.tournamentrelated.Tournament;
import cs203.ftms.overall.model.userrelated.Organiser;
import cs203.ftms.overall.repository.tournamentrelated.TournamentRepository;
import cs203.ftms.overall.repository.userrelated.OrganiserRepository;
import cs203.ftms.overall.repository.userrelated.UserRepository;
import cs203.ftms.overall.service.organiser.OrganiserService;
import jakarta.persistence.EntityNotFoundException;

class OrganiserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TournamentRepository tournamentRepository;

    @Mock
    private OrganiserRepository organiserRepository;

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
        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            organiserService.getCleanOrganiserDTO(null);
        });
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
        assertEquals(0, result.size());
        verify(tournamentRepository, times(1)).findByOrganiserId(organiser.getId());
    }

    @Test
    void updateProfile() {
        // Arrange
        Organiser organiser = new Organiser();
        organiser.setId(1);
        organiser.setContactNo("123456789");
        organiser.setCountry("Old Country");
        organiser.setEmail("old@example.com");
        organiser.setName("Old Name");

        UpdateOrganiserProfileDTO dto = new UpdateOrganiserProfileDTO(null, null, null, null);
        dto.setContactNo("987654321");
        dto.setCountry("New Country");
        dto.setEmail("new@example.com");
        dto.setName("New Name");

        // Act
        organiserService.updateProfile(organiser, dto);

        // Assert
        assertEquals("987654321", organiser.getContactNo());
        assertEquals("New Country", organiser.getCountry());
        assertEquals("new@example.com", organiser.getEmail());
        assertEquals("New Name", organiser.getName());
        verify(userRepository).save(organiser);
    }

        @Test
    void getOrganiserUpcomingTournaments() {
        // Arrange
        Organiser organiser = new Organiser();
        organiser.setId(1);

        Tournament pastTournament = new Tournament();
        pastTournament.setId(1);
        pastTournament.setStartDate(LocalDate.now().minusDays(10));

        Tournament futureTournament = new Tournament();
        futureTournament.setId(2);
        futureTournament.setStartDate(LocalDate.now().plusDays(10));

        List<Tournament> tournaments = Arrays.asList(pastTournament, futureTournament);
        when(tournamentRepository.findByOrganiserId(organiser.getId())).thenReturn(tournaments);

        // Act
        List<Tournament> result = organiserService.getOrganiserUpcomingTournaments(organiser);

        // Assert
        assertEquals(1, result.size());
        assertEquals(futureTournament.getId(), result.get(0).getId());
    }

    @Test
    void getOrganiserPastTournaments() {
        // Arrange
        Organiser organiser = new Organiser();
        organiser.setId(1);

        Tournament pastTournament = new Tournament();
        pastTournament.setId(1);
        pastTournament.setStartDate(LocalDate.now().minusDays(10));

        Tournament futureTournament = new Tournament();
        futureTournament.setId(2);
        futureTournament.setStartDate(LocalDate.now().plusDays(10));

        List<Tournament> tournaments = Arrays.asList(pastTournament, futureTournament);
        when(tournamentRepository.findByOrganiserId(organiser.getId())).thenReturn(tournaments);

        // Act
        List<Tournament> result = organiserService.getOrganiserPastTournaments(organiser);

        // Assert
        assertEquals(1, result.size());
        assertEquals(pastTournament.getId(), result.get(0).getId());
    }

    @Test
    void getAllOrganisers_ShouldReturnAllOrganisers() {
        // Arrange
        List<Organiser> organisers = new ArrayList<>();
        Organiser organiser1 = new Organiser();
        organiser1.setId(1);
        organiser1.setName("Organiser 1");
        organisers.add(organiser1);

        Organiser organiser2 = new Organiser();
        organiser2.setId(2);
        organiser2.setName("Organiser 2");
        organisers.add(organiser2);

        when(organiserRepository.findAll()).thenReturn(organisers);

        // Act
        List<Organiser> result = organiserService.getAllOrganisers();

        // Assert
        assertEquals(2, result.size());
        assertEquals(organiser1, result.get(0));
        assertEquals(organiser2, result.get(1));
    }
}
