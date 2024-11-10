package cs203.ftms.overall;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import cs203.ftms.overall.dto.clean.CleanMatchDTO;
import cs203.ftms.overall.dto.clean.CleanTournamentFencerDTO;
import cs203.ftms.overall.exception.EntityDoesNotExistException;
import cs203.ftms.overall.model.tournamentrelated.Match;
import cs203.ftms.overall.model.tournamentrelated.TournamentFencer;
import cs203.ftms.overall.model.userrelated.Fencer;
import cs203.ftms.overall.repository.tournamentrelated.TournamentFencerRepository;
import cs203.ftms.overall.service.event.EventService;
import cs203.ftms.overall.service.match.MatchService;

class MatchServiceTest {

    @Mock
    private TournamentFencerRepository tournamentFencerRepository;

    @Mock
    private EventService eventService;

    @InjectMocks
    private MatchService matchService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getFencersInMatch_ShouldReturnFencers() {
        // Arrange
        Match match = new Match();
        match.setFencer1(1);
        match.setFencer2(2);

        TournamentFencer fencer1 = new TournamentFencer();
        fencer1.setId(1);
        Fencer f1 = new Fencer();
        f1.setName("Fencer 1");
        fencer1.setFencer(f1);

        TournamentFencer fencer2 = new TournamentFencer();
        fencer2.setId(2);
        Fencer f2 = new Fencer();
        f2.setName("Fencer 2");
        fencer2.setFencer(f2);

        when(tournamentFencerRepository.findById(1)).thenReturn(Optional.of(fencer1));
        when(tournamentFencerRepository.findById(2)).thenReturn(Optional.of(fencer2));

        // Act
        List<TournamentFencer> result = matchService.getFencersInMatch(match);

        // Assert
        assertEquals(2, result.size());
        assertEquals(fencer1, result.get(0));
        assertEquals(fencer2, result.get(1));
    }

        @Test
    void getFencer1_ShouldReturnFencer() {
        // Arrange
        Match match = new Match();
        match.setFencer1(1);

        TournamentFencer fencer1 = new TournamentFencer();
        fencer1.setId(1);
        Fencer f1 = new Fencer();
        f1.setName("Fencer 1");
        fencer1.setFencer(f1);

        when(tournamentFencerRepository.findById(1)).thenReturn(Optional.of(fencer1));

        // Act
        TournamentFencer result = matchService.getFencer1(match);

        // Assert
        assertEquals(fencer1, result);
    }

    @Test
    void getFencer1_ShouldThrowException_WhenFencerDoesNotExist() {
        // Arrange
        Match match = new Match();
        match.setFencer1(1);

        when(tournamentFencerRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityDoesNotExistException.class, () -> matchService.getFencer1(match));
    }

    @Test
    void getFencer2_ShouldReturnFencer() {
        // Arrange
        Match match = new Match();
        match.setFencer2(2);

        TournamentFencer fencer2 = new TournamentFencer();
        fencer2.setId(2);
        Fencer f2 = new Fencer();
        f2.setName("Fencer 2");
        fencer2.setFencer(f2);

        when(tournamentFencerRepository.findById(2)).thenReturn(Optional.of(fencer2));

        // Act
        TournamentFencer result = matchService.getFencer2(match);

        // Assert
        assertEquals(fencer2, result);
    }

    @Test
    void getFencer2_ShouldThrowException_WhenFencerDoesNotExist() {
        // Arrange
        Match match = new Match();
        match.setFencer2(2);

        when(tournamentFencerRepository.findById(2)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityDoesNotExistException.class, () -> matchService.getFencer2(match));
    }

    @Test
    void getCleanMatchDTO_ShouldReturnCleanMatchDTO() {
        // Arrange
        Match match = new Match();
        match.setId(1);
        match.setFencer1(1);
        match.setFencer2(2);
        match.setScore1(15);
        match.setScore2(10);
        match.setWinner(1);

        TournamentFencer fencer1 = new TournamentFencer();
        fencer1.setId(1);
        Fencer f1 = new Fencer();
        f1.setName("Fencer 1");
        fencer1.setFencer(f1);

        TournamentFencer fencer2 = new TournamentFencer();
        fencer2.setId(2);
        Fencer f2 = new Fencer();
        f2.setName("Fencer 2");
        fencer2.setFencer(f2);

        CleanTournamentFencerDTO cleanFencer1 = new CleanTournamentFencerDTO(fencer1.getId(), f1.getId(), f1.getName(), f1.getClub(), f1.getCountry(), 'R' , fencer1.getTournamentRank(), 1, fencer1.getPouleWins(), fencer1.getPoulePoints(), 0);
        CleanTournamentFencerDTO cleanFencer2 = new CleanTournamentFencerDTO(fencer2.getId(), f2.getId(), f2.getName(), f2.getClub(), f2.getCountry(), 'R' , fencer2.getTournamentRank(), 1, fencer2.getPouleWins(), fencer2.getPoulePoints(), 0);
        when(tournamentFencerRepository.findById(1)).thenReturn(Optional.of(fencer1));
        when(tournamentFencerRepository.findById(2)).thenReturn(Optional.of(fencer2));
        when(eventService.getCleanTournamentFencerDTO(fencer1)).thenReturn(cleanFencer1);
        when(eventService.getCleanTournamentFencerDTO(fencer2)).thenReturn(cleanFencer2);

        // Act
        CleanMatchDTO result = matchService.getCleanMatchDTO(match, 'A');

        // Assert
        assertEquals(match.getId(), result.getId());
        assertEquals(cleanFencer1, result.getFencer1());
        assertEquals(match.getScore1(), result.getScore1());
        assertEquals(cleanFencer2, result.getFencer2());
        assertEquals(match.getScore2(), result.getScore2());
        assertEquals(match.getWinner(), result.getWinner());
        assertEquals('A', result.getMatchType());
    }
    @Test
    void getCleanMatchDTO_ShouldReturnNullFencers_WhenFencerListIsEmpty() {
        // Arrange
        Match match = new Match();
        match.setId(1);
        match.setFencer1(-1); // Invalid fencer ID
        match.setFencer2(-1); // Invalid fencer ID
        match.setScore1(15);
        match.setScore2(10);
        match.setWinner(1);

        when(tournamentFencerRepository.findById(-1)).thenReturn(Optional.empty());

        // Act
        CleanMatchDTO result = matchService.getCleanMatchDTO(match, 'A');

        // Assert
        assertEquals(match.getId(), result.getId());
        assertNull(result.getFencer1());
        assertEquals(match.getScore1(), result.getScore1());
        assertNull(result.getFencer2());
        assertEquals(match.getScore2(), result.getScore2());
        assertEquals(match.getWinner(), result.getWinner());
        assertEquals('A', result.getMatchType());
    }

    @Test
    void getCleanMatchDTO_ShouldThrowException_WhenFencer1DoesNotExist() {
        // Arrange
        Match match = new Match();
        match.setId(1);
        match.setFencer1(1); // Non-existent fencer ID
        match.setFencer2(2);

        when(tournamentFencerRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityDoesNotExistException.class, () -> matchService.getCleanMatchDTO(match, 'A'));
    }

    @Test
    void getCleanMatchDTO_ShouldThrowException_WhenFencer2DoesNotExist() {
        // Arrange
        Match match = new Match();
        match.setId(1);
        match.setFencer1(1);
        match.setFencer2(2); // Non-existent fencer ID

        TournamentFencer fencer1 = new TournamentFencer();
        fencer1.setId(1);
        when(tournamentFencerRepository.findById(1)).thenReturn(Optional.of(fencer1));
        when(tournamentFencerRepository.findById(2)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityDoesNotExistException.class, () -> matchService.getCleanMatchDTO(match, 'A'));
    }

    @Test
    void getCleanMatchDTO_ShouldThrowException_WhenBothFencersDoNotExist() {
        // Arrange
        Match match = new Match();
        match.setId(1);
        match.setFencer1(1); // Non-existent fencer ID
        match.setFencer2(2); // Non-existent fencer ID

        when(tournamentFencerRepository.findById(1)).thenReturn(Optional.empty());
        when(tournamentFencerRepository.findById(2)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityDoesNotExistException.class, () -> matchService.getCleanMatchDTO(match, 'A'));
    }
}