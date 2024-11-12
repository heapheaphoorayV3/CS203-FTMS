package cs203.ftms.overall;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import cs203.ftms.overall.dto.DirectEliminationBracketDTO;
import cs203.ftms.overall.dto.DirectEliminationBracketFencerDTO;
import cs203.ftms.overall.dto.UpdateDirectEliminationMatchDTO;
import cs203.ftms.overall.exception.EntityDoesNotExistException;
import cs203.ftms.overall.model.tournamentrelated.DirectEliminationMatch;
import cs203.ftms.overall.model.tournamentrelated.Event;
import cs203.ftms.overall.model.tournamentrelated.Tournament;
import cs203.ftms.overall.model.tournamentrelated.TournamentFencer;
import cs203.ftms.overall.model.userrelated.Fencer;
import cs203.ftms.overall.model.userrelated.Organiser;
import cs203.ftms.overall.repository.tournamentrelated.DirectEliminationMatchRepository;
import cs203.ftms.overall.repository.tournamentrelated.EventRepository;
import cs203.ftms.overall.repository.tournamentrelated.MatchRepository;
import cs203.ftms.overall.repository.tournamentrelated.TournamentFencerRepository;
import cs203.ftms.overall.repository.userrelated.FencerRepository;
import cs203.ftms.overall.service.event.EventService;
import cs203.ftms.overall.service.match.DirectEliminationService;
import cs203.ftms.overall.service.match.MatchService;
import cs203.ftms.overall.service.match.PouleService;

public class DirectEliminationServiceTest {

    @Mock
    private EventService eventService;

    @Mock
    private PouleService pouleService;

    @Mock
    private DirectEliminationMatchRepository directEliminationMatchRepository;

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private FencerRepository fencerRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private TournamentFencerRepository tournamentFencerRepository;

    @Mock
    private MatchService matchService;

    @InjectMocks
    private DirectEliminationService directEliminationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void noOfDEMatches_NoMatches() {
        // Arrange
        int eid = 1;
        Tournament tournament = new Tournament();
        tournament.setAdvancementRate(80);

        Event event = new Event();
        event.setId(eid);
        event.setTournament(tournament);

        when(eventService.getEvent(eid)).thenReturn(event);
        when(directEliminationMatchRepository.findByEvent(event)).thenReturn(Collections.emptyList());

        // Act
        int result = directEliminationService.noOfDEMatches(event);

        // Assert
        assertEquals(0, result);
    }

    @Test
    void noOfDEMatches_WithMatches() {
        // Arrange
        int eid = 1;
        Tournament tournament = new Tournament();
        tournament.setAdvancementRate(80);

        Event event = new Event();
        event.setId(eid);
        event.setTournament(tournament);
        event.setParticipantCount(10);

        when(eventService.getEvent(eid)).thenReturn(event);

        // Act
        int result = directEliminationService.noOfDEMatches(event);

        // Assert
        assertEquals(7, result);
    }
    @Test
    void noOfDEMatches_With15Matches() {
        // Arrange
        int eid = 1;
        Tournament tournament = new Tournament();
        tournament.setAdvancementRate(100);

        Event event = new Event();
        event.setId(eid);
        event.setTournament(tournament);
        event.setParticipantCount(10);

        when(eventService.getEvent(eid)).thenReturn(event);

        // Act
        int result = directEliminationService.noOfDEMatches(event);

        // Assert
        assertEquals(15, result);
    }

    @Test
    void noOfDEMatches_WithNonPowerOf2Fencers() {
        // Arrange
        int eid = 1;
        Tournament tournament = new Tournament();
        tournament.setAdvancementRate(80);

        Event event = new Event();
        event.setId(eid);
        event.setTournament(tournament);
        event.setParticipantCount(12);

        when(eventService.getEvent(eid)).thenReturn(event);

        // Act
        int result = directEliminationService.noOfDEMatches(event);

        // Assert
        assertEquals(15, result);
    }

// @Test
// void createAllDEMatches() {
//     // Arrange
//     Tournament tournament = new Tournament();
//     tournament.setAdvancementRate(80);

//     int eid = 1;
//     Event event = new Event();
//     event.setId(eid);
//     event.setTournament(tournament);

//     Set<Event> events = new HashSet<>();
//     events.add(event);
//     tournament.setEvents(events);

//     // Initialize poule structure
//     Poule poule = new Poule();
//     poule.setId(1);
//     poule.setEvent(event);
//     poule.setPouleMatches(new HashSet<>());
//     poule.setFencers(new HashSet<>());
    
//     // Create completed poule match
//     PouleMatch pouleMatch = new PouleMatch();
//     pouleMatch.setId(1);
//     pouleMatch.setPoule(poule);
//     pouleMatch.setWinner(0);
    
//     // Set up poule relationships
//     poule.getPouleMatches().add(pouleMatch);
    
//     Set<Poule> poules = new HashSet<>();
//     poules.add(poule);
//     event.setPoules(poules);

//     // Set up fencers with rankings
//     Set<TournamentFencer> fencers = new HashSet<>();
//     for (int i = 0; i < 32; i++) {
//         TournamentFencer tf = new TournamentFencer();
//         tf.setId(i + 1);
//         tf.setEvent(event);
        
//         Fencer fencer = new Fencer();
//         fencer.setId(i + 1);
//         fencer.setPoints(100); // Set some points for the fencer
//         tf.setFencer(fencer);
        
//         fencers.add(tf);
//     }
//     event.setFencers(fencers);
//     poule.setFencers(fencers);

//     // Mock service calls
//     when(eventService.getEvent(eid)).thenReturn(event);
//     Map<String, List<TournamentFencer>> fencersMap = Collections.singletonMap("fencers", new ArrayList<>(fencers));
//     when(pouleService.getFencersAfterPoules(event)).thenReturn(fencersMap);

//     // Mock repository calls
//     for (TournamentFencer f : fencers) {
//         when(tournamentFencerRepository.findById(f.getId())).thenReturn(Optional.of(f));
//         when(tournamentFencerRepository.save(f)).thenReturn(f);
//     }

//     List<DirectEliminationMatch> matches = IntStream.range(0, 15)
//             .mapToObj(i -> {
//                 DirectEliminationMatch match = new DirectEliminationMatch();
//                 match.setId(i); // Ensure unique IDs
//                 return match;
//             })
//             .collect(Collectors.toList());

//     for (DirectEliminationMatch match : matches) {
//         when(matchRepository.save(match)).thenReturn(match);
//         when(matchRepository.findById(match.getId())).thenReturn(Optional.of(match));
//     }

//     // Act
//     directEliminationService.createAllDEMatches(eid);

//     // Assert
//     verify(matchRepository, times(29)).save(any(DirectEliminationMatch.class));
// }

    @Test
    void updateDEMatch() {
        // Arrange
        Organiser organiser = new Organiser();
        Tournament tournament = new Tournament();
        tournament.setOrganiser(organiser);
        int eid = 1;
        int matchId = 1;
        int score1 = 15;
        int score2 = 10;

        Event event = new Event();
        event.setId(eid);
        event.setTournament(tournament);

        DirectEliminationMatch match = new DirectEliminationMatch();
        match.setId(matchId);
        match.setEvent(event);
        DirectEliminationMatch nextMatch = new DirectEliminationMatch();   
        nextMatch.setId(2);
        match.setNextMatchId(2);

        TournamentFencer fencer1 = new TournamentFencer();
        fencer1.setId(1);

        TournamentFencer fencer2 = new TournamentFencer();
        fencer2.setId(2);

        UpdateDirectEliminationMatchDTO dto = new UpdateDirectEliminationMatchDTO(0, 0, 0);
        dto.setMatchId(matchId);
        dto.setScore1(score1);
        dto.setScore2(score2);

        when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));
        when(matchRepository.findById(match.getNextMatchId())).thenReturn(Optional.of(nextMatch));
        when(eventService.getEvent(eid)).thenReturn(event);
        when(matchService.getFencer1(match)).thenReturn(fencer1);
        when(matchService.getFencer2(match)).thenReturn(fencer2);
        when(tournamentFencerRepository.findById(fencer1.getId())).thenReturn(Optional.of(fencer1));
        when(tournamentFencerRepository.findById(fencer2.getId())).thenReturn(Optional.of(fencer2));

        // Act
        directEliminationService.updateDEMatch(eid, dto, organiser);

        // Assert
        verify(matchRepository).save(match);
        assertEquals(score1, match.getScore1());
        assertEquals(score2, match.getScore2());
    }

    @Test
    void updateDEMatch_MatchDoesNotExist() {
        // Arrange
        Organiser organiser = new Organiser();
        Tournament tournament = new Tournament();
        tournament.setOrganiser(organiser);
        Event event = new Event();
        event.setId(1);
        event.setTournament(tournament);
        int eid = 1;
        int matchId = 1;
        int score1 = 15;
        int score2 = 10;

        UpdateDirectEliminationMatchDTO dto = new UpdateDirectEliminationMatchDTO(score2, score2, score2);
        dto.setMatchId(matchId);
        dto.setScore1(score1);
        dto.setScore2(score2);

        when(matchRepository.findById(matchId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityDoesNotExistException.class, () -> {
            directEliminationService.updateDEMatch(eid, dto, organiser);
        });
    }


    @Test
    void updateDEMatch_MatchNotInEvent() {
        // Arrange
        Organiser organiser = new Organiser();
        Tournament tournament = new Tournament();
        tournament.setOrganiser(organiser);
        int eid = 1;
        int matchId = 1;
        int score1 = 15;
        int score2 = 10;

        Event event = new Event();
        event.setId(eid);
        event.setTournament(tournament);

        Event differentEvent = new Event();
        differentEvent.setId(2);

        DirectEliminationMatch match = new DirectEliminationMatch();
        match.setId(matchId);
        match.setEvent(differentEvent);

        UpdateDirectEliminationMatchDTO dto = new UpdateDirectEliminationMatchDTO(score2, score2, score2);
        dto.setMatchId(matchId);
        dto.setScore1(score1);
        dto.setScore2(score2);

        when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));
        when(eventService.getEvent(eid)).thenReturn(event);

        // Act & Assert
        assertThrows(EntityDoesNotExistException.class, () -> {
            directEliminationService.updateDEMatch(eid, dto, organiser);
        });

    }


    @Test
    void updateDEMatch_TournamentFencerDoesNotExist() {
        // Arrange
        Organiser organiser = new Organiser();
        Tournament tournament = new Tournament();
        tournament.setOrganiser(organiser);
        int eid = 1;
        int matchId = 1;
        int score1 = 15;
        int score2 = 10;

        Event event = new Event();
        event.setId(eid);
        event.setTournament(tournament);

        DirectEliminationMatch match = new DirectEliminationMatch();
        match.setId(matchId);
        match.setEvent(event);

        TournamentFencer fencer1 = new TournamentFencer();
        fencer1.setId(1);

        TournamentFencer fencer2 = new TournamentFencer();
        fencer2.setId(2);

        UpdateDirectEliminationMatchDTO dto = new UpdateDirectEliminationMatchDTO(score2, score2, score2);
        dto.setMatchId(matchId);
        dto.setScore1(score1);
        dto.setScore2(score2);

        when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));
        when(eventService.getEvent(eid)).thenReturn(event);
        when(matchService.getFencer1(match)).thenReturn(fencer1);
        when(matchService.getFencer2(match)).thenReturn(fencer2);
        when(tournamentFencerRepository.findById(fencer1.getId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityDoesNotExistException.class, () -> {
            directEliminationService.updateDEMatch(eid, dto, organiser);
        });
    }

    @Test
    void updateDEMatch_NextMatchDoesNotExist() {
        // Arrange
        Organiser organiser = new Organiser();
        Tournament tournament = new Tournament();
        tournament.setOrganiser(organiser);
        int eid = 1;
        int matchId = 1;
        int score1 = 15;
        int score2 = 10;

        Event event = new Event();
        event.setId(eid);
        event.setTournament(tournament);

        DirectEliminationMatch match = new DirectEliminationMatch();
        match.setId(matchId);
        match.setEvent(event);
        match.setNextMatchId(2);

        TournamentFencer fencer1 = new TournamentFencer();
        fencer1.setId(1);

        TournamentFencer fencer2 = new TournamentFencer();
        fencer2.setId(2);

        UpdateDirectEliminationMatchDTO dto = new UpdateDirectEliminationMatchDTO(score2, score2, score2);
        dto.setMatchId(matchId);
        dto.setScore1(score1);
        dto.setScore2(score2);

        when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));
        when(matchRepository.findById(match.getNextMatchId())).thenReturn(Optional.empty());
        when(eventService.getEvent(eid)).thenReturn(event);
        when(matchService.getFencer1(match)).thenReturn(fencer1);
        when(matchService.getFencer2(match)).thenReturn(fencer2);
        when(tournamentFencerRepository.findById(fencer1.getId())).thenReturn(Optional.of(fencer1));
        when(tournamentFencerRepository.findById(fencer2.getId())).thenReturn(Optional.of(fencer2));

        // Act & Assert
        assertThrows(EntityDoesNotExistException.class, () -> {
            directEliminationService.updateDEMatch(eid, dto, organiser);
        });
    }

    @Test
    void updateDEMatch_Fencer2WinsWithLowerRank() {
        // Arrange
        Organiser organiser = new Organiser();
        Tournament tournament = new Tournament();
        tournament.setOrganiser(organiser);
        int eid = 1;
        int matchId = 1;
        int score1 = 10;
        int score2 = 15;

        Event event = new Event();
        event.setId(eid);
        event.setTournament(tournament);

        DirectEliminationMatch match = new DirectEliminationMatch();
        match.setId(matchId);
        match.setEvent(event);
        DirectEliminationMatch nextMatch = new DirectEliminationMatch();   
        nextMatch.setId(2);
        match.setNextMatchId(2);

        TournamentFencer fencer1 = new TournamentFencer();
        fencer1.setId(1);
        fencer1.setTournamentRank(1);

        TournamentFencer fencer2 = new TournamentFencer();
        fencer2.setId(2);
        fencer2.setTournamentRank(2);

        UpdateDirectEliminationMatchDTO dto = new UpdateDirectEliminationMatchDTO(score2, score2, score2);
        dto.setMatchId(matchId);
        dto.setScore1(score1);
        dto.setScore2(score2);

        when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));
        when(matchRepository.findById(match.getNextMatchId())).thenReturn(Optional.of(nextMatch));
        when(eventService.getEvent(eid)).thenReturn(event);
        when(matchService.getFencer1(match)).thenReturn(fencer1);
        when(matchService.getFencer2(match)).thenReturn(fencer2);
        when(tournamentFencerRepository.findById(fencer1.getId())).thenReturn(Optional.of(fencer1));
        when(tournamentFencerRepository.findById(fencer2.getId())).thenReturn(Optional.of(fencer2));

        // Act
        directEliminationService.updateDEMatch(eid, dto, organiser);

        // Assert
        verify(matchRepository).save(match);
        verify(matchRepository).save(nextMatch);
        assertEquals(score1, match.getScore1());
        assertEquals(score2, match.getScore2());
        assertEquals(fencer2.getId(), nextMatch.getFencer1());
    }

    @Test
    void updateDEMatch_Fencer1WinsAndNextMatchFencer1IsSet() {
        // Arrange
        Organiser organiser = new Organiser();
        Tournament tournament = new Tournament();
        tournament.setOrganiser(organiser);
        int eid = 1;
        int matchId = 1;
        int score1 = 15;
        int score2 = 10;

        Event event = new Event();
        event.setId(eid);
        event.setTournament(tournament);

        DirectEliminationMatch match = new DirectEliminationMatch();
        match.setId(matchId);
        match.setEvent(event);
        DirectEliminationMatch nextMatch = new DirectEliminationMatch();   
        nextMatch.setId(2);
        nextMatch.setFencer1(3); // Already set
        match.setNextMatchId(2);

        TournamentFencer fencer1 = new TournamentFencer();
        fencer1.setId(1);
        fencer1.setTournamentRank(1);

        TournamentFencer fencer2 = new TournamentFencer();
        fencer2.setId(2);
        fencer2.setTournamentRank(2);

        UpdateDirectEliminationMatchDTO dto = new UpdateDirectEliminationMatchDTO(score2, score2, score2);
        dto.setMatchId(matchId);
        dto.setScore1(score1);
        dto.setScore2(score2);

        when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));
        when(matchRepository.findById(match.getNextMatchId())).thenReturn(Optional.of(nextMatch));
        when(eventService.getEvent(eid)).thenReturn(event);
        when(matchService.getFencer1(match)).thenReturn(fencer1);
        when(matchService.getFencer2(match)).thenReturn(fencer2);
        when(tournamentFencerRepository.findById(fencer1.getId())).thenReturn(Optional.of(fencer1));
        when(tournamentFencerRepository.findById(fencer2.getId())).thenReturn(Optional.of(fencer2));

        // Act
        directEliminationService.updateDEMatch(eid, dto, organiser);

        // Assert
        verify(matchRepository).save(match);
        verify(matchRepository).save(nextMatch);
        assertEquals(score1, match.getScore1());
        assertEquals(score2, match.getScore2());
        assertEquals(fencer1.getId(), nextMatch.getFencer2());
    }

    @Test
    void updateDEMatch_Fencer2WinsAndNextMatchFencer1IsSet() {
        // Arrange
        Organiser organiser = new Organiser();
        Tournament tournament = new Tournament();
        tournament.setOrganiser(organiser);
        int eid = 1;
        int matchId = 1;
        int score1 = 10;
        int score2 = 15;

        Event event = new Event();
        event.setId(eid);
        event.setTournament(tournament);

        DirectEliminationMatch match = new DirectEliminationMatch();
        match.setId(matchId);
        match.setEvent(event);
        DirectEliminationMatch nextMatch = new DirectEliminationMatch();   
        nextMatch.setId(2);
        nextMatch.setFencer1(3); // Already set
        match.setNextMatchId(2);

        TournamentFencer fencer1 = new TournamentFencer();
        fencer1.setId(1);
        fencer1.setTournamentRank(1);

        TournamentFencer fencer2 = new TournamentFencer();
        fencer2.setId(2);
        fencer2.setTournamentRank(2);

        UpdateDirectEliminationMatchDTO dto = new UpdateDirectEliminationMatchDTO(score2, score2, score2);
        dto.setMatchId(matchId);
        dto.setScore1(score1);
        dto.setScore2(score2);

        when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));
        when(matchRepository.findById(match.getNextMatchId())).thenReturn(Optional.of(nextMatch));
        when(eventService.getEvent(eid)).thenReturn(event);
        when(matchService.getFencer1(match)).thenReturn(fencer1);
        when(matchService.getFencer2(match)).thenReturn(fencer2);
        when(tournamentFencerRepository.findById(fencer1.getId())).thenReturn(Optional.of(fencer1));
        when(tournamentFencerRepository.findById(fencer2.getId())).thenReturn(Optional.of(fencer2));

        // Act
        directEliminationService.updateDEMatch(eid, dto, organiser);

        // Assert
        verify(matchRepository).save(match);
        verify(matchRepository).save(nextMatch);
        assertEquals(score1, match.getScore1());
        assertEquals(score2, match.getScore2());
        assertEquals(fencer2.getId(), nextMatch.getFencer2());
    }


    @Test
    void updateDEMatch_Fencer1WinsAndNextMatchFencer1IsUnset() {
        // Arrange
        Organiser organiser = new Organiser();
        Tournament tournament = new Tournament();
        tournament.setOrganiser(organiser);
        int eid = 1;
        int matchId = 1;
        int score1 = 15;
        int score2 = 10;

        Event event = new Event();
        event.setId(eid);
        event.setTournament(tournament);

        DirectEliminationMatch match = new DirectEliminationMatch();
        match.setId(matchId);
        match.setEvent(event);
        DirectEliminationMatch nextMatch = new DirectEliminationMatch();   
        nextMatch.setId(2);
        nextMatch.setFencer1(-1); // Unset
        match.setNextMatchId(2);

        TournamentFencer fencer1 = new TournamentFencer();
        fencer1.setId(1);
        fencer1.setTournamentRank(1);

        TournamentFencer fencer2 = new TournamentFencer();
        fencer2.setId(2);
        fencer2.setTournamentRank(2);

        UpdateDirectEliminationMatchDTO dto = new UpdateDirectEliminationMatchDTO(score2, score2, score2);
        dto.setMatchId(matchId);
        dto.setScore1(score1);
        dto.setScore2(score2);

        when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));
        when(matchRepository.findById(match.getNextMatchId())).thenReturn(Optional.of(nextMatch));
        when(eventService.getEvent(eid)).thenReturn(event);
        when(matchService.getFencer1(match)).thenReturn(fencer1);
        when(matchService.getFencer2(match)).thenReturn(fencer2);
        when(tournamentFencerRepository.findById(fencer1.getId())).thenReturn(Optional.of(fencer1));
        when(tournamentFencerRepository.findById(fencer2.getId())).thenReturn(Optional.of(fencer2));

        // Act
        directEliminationService.updateDEMatch(eid, dto, organiser);

        // Assert
        verify(matchRepository).save(match);
        verify(matchRepository).save(nextMatch);
        assertEquals(score1, match.getScore1());
        assertEquals(score2, match.getScore2());
        assertEquals(fencer1.getId(), nextMatch.getFencer1());
    }

    @Test
    void updateDEMatch_Fencer1WinsAndNextMatchFencer1IsFencer1() {
        // Arrange
        Organiser organiser = new Organiser();
        Tournament tournament = new Tournament();
        tournament.setOrganiser(organiser);
        int eid = 1;
        int matchId = 1;
        int score1 = 15;
        int score2 = 10;

        Event event = new Event();
        event.setId(eid);
        event.setTournament(tournament);

        DirectEliminationMatch match = new DirectEliminationMatch();
        match.setId(matchId);
        match.setEvent(event);
        DirectEliminationMatch nextMatch = new DirectEliminationMatch();   
        nextMatch.setId(2);
        nextMatch.setFencer1(1); // Already set to fencer1
        match.setNextMatchId(2);

        TournamentFencer fencer1 = new TournamentFencer();
        fencer1.setId(1);
        fencer1.setTournamentRank(1);

        TournamentFencer fencer2 = new TournamentFencer();
        fencer2.setId(2);
        fencer2.setTournamentRank(2);

        UpdateDirectEliminationMatchDTO dto = new UpdateDirectEliminationMatchDTO(score2, score2, score2);
        dto.setMatchId(matchId);
        dto.setScore1(score1);
        dto.setScore2(score2);

        when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));
        when(matchRepository.findById(match.getNextMatchId())).thenReturn(Optional.of(nextMatch));
        when(eventService.getEvent(eid)).thenReturn(event);
        when(matchService.getFencer1(match)).thenReturn(fencer1);
        when(matchService.getFencer2(match)).thenReturn(fencer2);
        when(tournamentFencerRepository.findById(fencer1.getId())).thenReturn(Optional.of(fencer1));
        when(tournamentFencerRepository.findById(fencer2.getId())).thenReturn(Optional.of(fencer2));

        // Act
        directEliminationService.updateDEMatch(eid, dto, organiser);

        // Assert
        verify(matchRepository).save(match);
        verify(matchRepository).save(nextMatch);
        assertEquals(score1, match.getScore1());
        assertEquals(score2, match.getScore2());
        assertEquals(fencer1.getId(), nextMatch.getFencer1());
    }

    @Test
    void updateDEMatch_Fencer1WinsAndNextMatchFencer1IsFencer2() {
        // Arrange
        Organiser organiser = new Organiser();
        Tournament tournament = new Tournament();
        tournament.setOrganiser(organiser);
        int eid = 1;
        int matchId = 1;
        int score1 = 15;
        int score2 = 10;

        Event event = new Event();
        event.setId(eid);
        event.setTournament(tournament);

        DirectEliminationMatch match = new DirectEliminationMatch();
        match.setId(matchId);
        match.setEvent(event);
        DirectEliminationMatch nextMatch = new DirectEliminationMatch();   
        nextMatch.setId(2);
        nextMatch.setFencer1(2); // Already set to fencer2
        match.setNextMatchId(2);

        TournamentFencer fencer1 = new TournamentFencer();
        fencer1.setId(1);
        fencer1.setTournamentRank(1);

        TournamentFencer fencer2 = new TournamentFencer();
        fencer2.setId(2);
        fencer2.setTournamentRank(2);

        UpdateDirectEliminationMatchDTO dto = new UpdateDirectEliminationMatchDTO(score2, score2, score2);
        dto.setMatchId(matchId);
        dto.setScore1(score1);
        dto.setScore2(score2);

        when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));
        when(matchRepository.findById(match.getNextMatchId())).thenReturn(Optional.of(nextMatch));
        when(eventService.getEvent(eid)).thenReturn(event);
        when(matchService.getFencer1(match)).thenReturn(fencer1);
        when(matchService.getFencer2(match)).thenReturn(fencer2);
        when(tournamentFencerRepository.findById(fencer1.getId())).thenReturn(Optional.of(fencer1));
        when(tournamentFencerRepository.findById(fencer2.getId())).thenReturn(Optional.of(fencer2));

        // Act
        directEliminationService.updateDEMatch(eid, dto, organiser);

        // Assert
        verify(matchRepository).save(match);
        verify(matchRepository).save(nextMatch);
        assertEquals(score1, match.getScore1());
        assertEquals(score2, match.getScore2());
        assertEquals(fencer1.getId(), nextMatch.getFencer1());
    }

    @Test
    void updateDEMatch_Fencer1WinsAndNextMatchFencer2IsUnset() {
        // Arrange
        Organiser organiser = new Organiser();
        Tournament tournament = new Tournament();
        tournament.setOrganiser(organiser);
        int eid = 1;
        int matchId = 1;
        int score1 = 15;
        int score2 = 10;

        Event event = new Event();
        event.setId(eid);
        event.setTournament(tournament);

        DirectEliminationMatch match = new DirectEliminationMatch();
        match.setId(matchId);
        match.setEvent(event);
        DirectEliminationMatch nextMatch = new DirectEliminationMatch();   
        nextMatch.setId(2);
        nextMatch.setFencer1(3); // Already set
        nextMatch.setFencer2(-1); // Unset
        match.setNextMatchId(2);

        TournamentFencer fencer1 = new TournamentFencer();
        fencer1.setId(1);
        fencer1.setTournamentRank(1);

        TournamentFencer fencer2 = new TournamentFencer();
        fencer2.setId(2);
        fencer2.setTournamentRank(2);

        UpdateDirectEliminationMatchDTO dto = new UpdateDirectEliminationMatchDTO(score2, score2, score2);
        dto.setMatchId(matchId);
        dto.setScore1(score1);
        dto.setScore2(score2);

        when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));
        when(matchRepository.findById(match.getNextMatchId())).thenReturn(Optional.of(nextMatch));
        when(eventService.getEvent(eid)).thenReturn(event);
        when(matchService.getFencer1(match)).thenReturn(fencer1);
        when(matchService.getFencer2(match)).thenReturn(fencer2);
        when(tournamentFencerRepository.findById(fencer1.getId())).thenReturn(Optional.of(fencer1));
        when(tournamentFencerRepository.findById(fencer2.getId())).thenReturn(Optional.of(fencer2));

        // Act
        directEliminationService.updateDEMatch(eid, dto, organiser);

        // Assert
        verify(matchRepository).save(match);
        verify(matchRepository).save(nextMatch);
        assertEquals(score1, match.getScore1());
        assertEquals(score2, match.getScore2());
        assertEquals(fencer1.getId(), nextMatch.getFencer2());
    }

    @Test
    void updateDEMatch_Fencer1WinsAndNextMatchFencer2IsFencer1() {
        // Arrange
        Organiser organiser = new Organiser();
        Tournament tournament = new Tournament();
        tournament.setOrganiser(organiser);
        int eid = 1;
        int matchId = 1;
        int score1 = 15;
        int score2 = 10;

        Event event = new Event();
        event.setId(eid);
        event.setTournament(tournament);

        DirectEliminationMatch match = new DirectEliminationMatch();
        match.setId(matchId);
        match.setEvent(event);
        DirectEliminationMatch nextMatch = new DirectEliminationMatch();   
        nextMatch.setId(2);
        nextMatch.setFencer1(3); // Already set to fencer3
        nextMatch.setFencer2(1); // Already set to fencer1
        match.setNextMatchId(2);

        TournamentFencer fencer1 = new TournamentFencer();
        fencer1.setId(1);
        fencer1.setTournamentRank(1);

        TournamentFencer fencer2 = new TournamentFencer();
        fencer2.setId(2);
        fencer2.setTournamentRank(2);

        UpdateDirectEliminationMatchDTO dto = new UpdateDirectEliminationMatchDTO(score2, score2, score2);
        dto.setMatchId(matchId);
        dto.setScore1(score1);
        dto.setScore2(score2);

        when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));
        when(matchRepository.findById(match.getNextMatchId())).thenReturn(Optional.of(nextMatch));
        when(eventService.getEvent(eid)).thenReturn(event);
        when(matchService.getFencer1(match)).thenReturn(fencer1);
        when(matchService.getFencer2(match)).thenReturn(fencer2);
        when(tournamentFencerRepository.findById(fencer1.getId())).thenReturn(Optional.of(fencer1));
        when(tournamentFencerRepository.findById(fencer2.getId())).thenReturn(Optional.of(fencer2));

        // Act
        directEliminationService.updateDEMatch(eid, dto, organiser);

        // Assert
        verify(matchRepository).save(match);
        verify(matchRepository).save(nextMatch);
        assertEquals(score1, match.getScore1());
        assertEquals(score2, match.getScore2());
        assertEquals(fencer1.getId(), nextMatch.getFencer2());
    }

    @Test
    void updateDEMatch_Fencer1WinsAndNextMatchFencer2IsFencer2() {
        // Arrange
        Organiser organiser = new Organiser();
        Tournament tournament = new Tournament();
        tournament.setOrganiser(organiser);
        int eid = 1;
        int matchId = 1;
        int score1 = 15;
        int score2 = 10;

        Event event = new Event();
        event.setId(eid);
        event.setTournament(tournament);

        DirectEliminationMatch match = new DirectEliminationMatch();
        match.setId(matchId);
        match.setEvent(event);
        DirectEliminationMatch nextMatch = new DirectEliminationMatch();   
        nextMatch.setId(2);
        nextMatch.setFencer1(3);
        nextMatch.setFencer2(2); // Already set to fencer2
        match.setNextMatchId(2);

        TournamentFencer fencer1 = new TournamentFencer();
        fencer1.setId(1);
        fencer1.setTournamentRank(1);

        TournamentFencer fencer2 = new TournamentFencer();
        fencer2.setId(2);
        fencer2.setTournamentRank(2);

        UpdateDirectEliminationMatchDTO dto = new UpdateDirectEliminationMatchDTO(score2, score2, score2);
        dto.setMatchId(matchId);
        dto.setScore1(score1);
        dto.setScore2(score2);

        when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));
        when(matchRepository.findById(match.getNextMatchId())).thenReturn(Optional.of(nextMatch));
        when(eventService.getEvent(eid)).thenReturn(event);
        when(matchService.getFencer1(match)).thenReturn(fencer1);
        when(matchService.getFencer2(match)).thenReturn(fencer2);
        when(tournamentFencerRepository.findById(fencer1.getId())).thenReturn(Optional.of(fencer1));
        when(tournamentFencerRepository.findById(fencer2.getId())).thenReturn(Optional.of(fencer2));

        // Act
        directEliminationService.updateDEMatch(eid, dto, organiser);

        // Assert
        verify(matchRepository).save(match);
        verify(matchRepository).save(nextMatch);
        assertEquals(score1, match.getScore1());
        assertEquals(score2, match.getScore2());
        assertEquals(fencer1.getId(), nextMatch.getFencer2());
    }

    @Test
    void updateDEMatch_Fencer1WinsAndNextMatchBothFencerSet() {
        // Arrange
        Organiser organiser = new Organiser();
        Tournament tournament = new Tournament();
        tournament.setOrganiser(organiser);
        int eid = 1;
        int matchId = 1;
        int score1 = 15;
        int score2 = 10;

        Event event = new Event();
        event.setId(eid);
        event.setTournament(tournament);

        DirectEliminationMatch match = new DirectEliminationMatch();
        match.setId(matchId);
        match.setEvent(event);
        DirectEliminationMatch nextMatch = new DirectEliminationMatch();   
        nextMatch.setId(2);
        nextMatch.setFencer1(3);
        nextMatch.setFencer2(4); // Already set to fencer2
        match.setNextMatchId(2);

        TournamentFencer fencer1 = new TournamentFencer();
        fencer1.setId(1);
        fencer1.setTournamentRank(1);

        TournamentFencer fencer2 = new TournamentFencer();
        fencer2.setId(2);
        fencer2.setTournamentRank(2);

        UpdateDirectEliminationMatchDTO dto = new UpdateDirectEliminationMatchDTO(score2, score2, score2);
        dto.setMatchId(matchId);
        dto.setScore1(score1);
        dto.setScore2(score2);

        when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));
        when(matchRepository.findById(match.getNextMatchId())).thenReturn(Optional.of(nextMatch));
        when(eventService.getEvent(eid)).thenReturn(event);
        when(matchService.getFencer1(match)).thenReturn(fencer1);
        when(matchService.getFencer2(match)).thenReturn(fencer2);
        when(tournamentFencerRepository.findById(fencer1.getId())).thenReturn(Optional.of(fencer1));
        when(tournamentFencerRepository.findById(fencer2.getId())).thenReturn(Optional.of(fencer2));

        // Act
        directEliminationService.updateDEMatch(eid, dto, organiser);

        // Assert
        verify(matchRepository).save(match);
        verify(matchRepository).save(nextMatch);
        assertEquals(score1, match.getScore1());
        assertEquals(score2, match.getScore2());

    }

    @Test
    void updateDEMatch_RoundOf2() {
        // Arrange
        Organiser organiser = new Organiser();
        Tournament tournament = new Tournament();
        tournament.setOrganiser(organiser);
        int eid = 1;
        int matchId = 1;
        int score1 = 15;
        int score2 = 10;

        Event event = new Event();
        event.setId(eid);
        event.setTournament(tournament);

        DirectEliminationMatch match = new DirectEliminationMatch();
        match.setId(matchId);
        match.setEvent(event);
        match.setRoundOf(2); // Set roundOf to 2

        TournamentFencer fencer1 = new TournamentFencer();
        fencer1.setId(1);
        fencer1.setTournamentRank(1);

        TournamentFencer fencer2 = new TournamentFencer();
        fencer2.setId(2);
        fencer2.setTournamentRank(2);

        UpdateDirectEliminationMatchDTO dto = new UpdateDirectEliminationMatchDTO(score2, score2, score2);
        dto.setMatchId(matchId);
        dto.setScore1(score1);
        dto.setScore2(score2);

        when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));
        when(eventService.getEvent(eid)).thenReturn(event);
        when(matchService.getFencer1(match)).thenReturn(fencer1);
        when(matchService.getFencer2(match)).thenReturn(fencer2);
        when(tournamentFencerRepository.findById(fencer1.getId())).thenReturn(Optional.of(fencer1));
        when(tournamentFencerRepository.findById(fencer2.getId())).thenReturn(Optional.of(fencer2));

        // Act
        directEliminationService.updateDEMatch(eid, dto, organiser);

        // Assert
        verify(matchRepository).save(match);
        assertEquals(score1, match.getScore1());
        assertEquals(score2, match.getScore2());
        assertEquals(fencer1.getId(), match.getWinner());
    }
    
    @Test
    void getDirectEliminationBracketDTO() {
        // Arrange
        int matchId = 1;
        int nextMatchId = 2;
        int roundOf = 16;

        DirectEliminationMatch match = new DirectEliminationMatch();
        match.setId(matchId);
        match.setNextMatchId(nextMatchId);
        match.setRoundOf(roundOf);

        when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));

        // Act
        DirectEliminationBracketDTO result = directEliminationService.getDirectEliminationBracketDTO(match);

        // Assert
        assertEquals(matchId, result.getId());
        assertEquals(nextMatchId, result.getNextMatchId());
        assertEquals("Top 16", result.getTournamentRoundText());
    }

    @Test
    void getDirectEliminationBracketDTO_RoundOf2() {
        // Arrange
        int matchId = 1;
        int nextMatchId = 2;
        int roundOf = 2;

        DirectEliminationMatch match = new DirectEliminationMatch();
        match.setId(matchId);
        match.setNextMatchId(nextMatchId);
        match.setRoundOf(roundOf);

        when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));

        // Act
        DirectEliminationBracketDTO result = directEliminationService.getDirectEliminationBracketDTO(match);

        // Assert
        assertEquals(matchId, result.getId());
        assertEquals(nextMatchId, result.getNextMatchId());
        assertEquals("Finals", result.getTournamentRoundText());
    }

    @Test
    void getDirectEliminationBracketDTO_RoundOf4() {
        // Arrange
        int matchId = 1;
        int nextMatchId = 2;
        int roundOf = 4;

        DirectEliminationMatch match = new DirectEliminationMatch();
        match.setId(matchId);
        match.setNextMatchId(nextMatchId);
        match.setRoundOf(roundOf);

        when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));

        // Act
        DirectEliminationBracketDTO result = directEliminationService.getDirectEliminationBracketDTO(match);

        // Assert
        assertEquals(matchId, result.getId());
        assertEquals(nextMatchId, result.getNextMatchId());
        assertEquals("Semi-Finals", result.getTournamentRoundText());
    }

    @Test
    void getDirectEliminationBracketDTO_RoundOf8() {
        // Arrange
        int matchId = 1;
        int nextMatchId = 2;
        int roundOf = 8;

        DirectEliminationMatch match = new DirectEliminationMatch();
        match.setId(matchId);
        match.setNextMatchId(nextMatchId);
        match.setRoundOf(roundOf);

        when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));

        // Act
        DirectEliminationBracketDTO result = directEliminationService.getDirectEliminationBracketDTO(match);

        // Assert
        assertEquals(matchId, result.getId());
        assertEquals(nextMatchId, result.getNextMatchId());
        assertEquals("Quarter-Finals", result.getTournamentRoundText());
    }

    @Test
    void getDirectEliminationBracketFencerDTO() {
        // Arrange
        int fencerId = 1;
        String fencerName = "John Doe";
        int fencerRank = 1;
        String resultText = "15";
        boolean isWinner = true;
        String status = "Active";

        Fencer fencer = new Fencer();
        fencer.setId(fencerId);
        fencer.setName(fencerName); 

        TournamentFencer tFencer = new TournamentFencer();
        tFencer.setId(fencerId);
        tFencer.setTournamentRank(fencerRank);
        tFencer.setFencer(fencer);

        DirectEliminationMatch match = new DirectEliminationMatch();
        match.setFencer1(fencerId);

        when(tournamentFencerRepository.findById(fencerId)).thenReturn(Optional.of(tFencer));
        when(fencerRepository.findById(fencerId)).thenReturn(Optional.of(fencer));


        // Act
        DirectEliminationBracketFencerDTO result = directEliminationService.getDirectEliminationBracketFencerDTO(tFencer, 1, 15);

        // Assert
        assertEquals(fencerId, result.getId());
        assertEquals(fencerName, result.getName());
        assertEquals(resultText, result.getResultText());
        assertEquals(isWinner, result.getIsWinner());
    }

    @Test
    void getDirectEliminationBracketDTO_WithFencers() {
        // Arrange
        int matchId = 1;
        int nextMatchId = 2;
        int roundOf = 16;
        int fencer1Id = 1;
        int fencer2Id = 2;
        String fencer1Name = "John Doe";
        String fencer2Name = "Jane Smith";
        int fencer1Rank = 1;
        int fencer2Rank = 2;
        String resultText = "15";
        String resultText2 = "10";
        boolean isWinner = true;
        String status = "Active";

        DirectEliminationMatch match = new DirectEliminationMatch();
        match.setId(matchId);
        match.setNextMatchId(nextMatchId);
        match.setRoundOf(roundOf);
        match.setFencer1(fencer1Id);
        match.setFencer2(fencer2Id);
        match.setWinner(fencer1Id);
        match.setScore1(15);
        match.setScore2(10);

        Fencer fencer1 = new Fencer();
        fencer1.setId(fencer1Id);
        fencer1.setName(fencer1Name);

        Fencer fencer2 = new Fencer();
        fencer2.setId(fencer2Id);
        fencer2.setName(fencer2Name);

        TournamentFencer tFencer1 = new TournamentFencer();
        tFencer1.setId(fencer1Id);
        tFencer1.setTournamentRank(fencer1Rank);
        tFencer1.setFencer(fencer1);

        TournamentFencer tFencer2 = new TournamentFencer();
        tFencer2.setId(fencer2Id);
        tFencer2.setTournamentRank(fencer2Rank);
        tFencer2.setFencer(fencer2);

        when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));
        when(tournamentFencerRepository.findById(fencer1Id)).thenReturn(Optional.of(tFencer1));
        when(tournamentFencerRepository.findById(fencer2Id)).thenReturn(Optional.of(tFencer2));
        when(fencerRepository.findById(fencer1Id)).thenReturn(Optional.of(fencer1));
        when(fencerRepository.findById(fencer2Id)).thenReturn(Optional.of(fencer2));
        when(matchService.getFencersInMatch(match)).thenReturn(Arrays.asList(tFencer1, tFencer2));

        // Act
        DirectEliminationBracketDTO result = directEliminationService.getDirectEliminationBracketDTO(match);

        // Assert
        assertEquals(matchId, result.getId());
        assertEquals(nextMatchId, result.getNextMatchId());
        assertEquals("Top 16", result.getTournamentRoundText());


        DirectEliminationBracketFencerDTO[] fencersDTO = result.getParticipants();
        assertEquals(2, fencersDTO.length);

        DirectEliminationBracketFencerDTO fencerDTO1 = fencersDTO[0];
        assertEquals(fencer1Id, fencerDTO1.getId());
        assertEquals(fencer1Name, fencerDTO1.getName());
        assertEquals(resultText, fencerDTO1.getResultText());
        assertEquals(isWinner, fencerDTO1.getIsWinner());

        DirectEliminationBracketFencerDTO fencerDTO2 = fencersDTO[1];
        assertEquals(fencer2Id, fencerDTO2.getId());
        assertEquals(fencer2Name, fencerDTO2.getName());
        assertEquals(resultText2, fencerDTO2.getResultText());
        assertEquals(!isWinner, fencerDTO2.getIsWinner());
    }


    @Test
    void generateDirectEliminationBracketDTOs() {
        // Arrange
        int eventId = 1;
        int matchId1 = 1;
        int matchId2 = 2;
        int nextMatchId = 3;
        int roundOf = 16;
        int fencer1Id = 1;
        int fencer2Id = 2;
        int fencer3Id = 3;
        int fencer4Id = 4;
        String fencer1Name = "John Doe";
        String fencer2Name = "Jane Smith";
        String fencer3Name = "Alice Johnson";
        String fencer4Name = "Bob Brown";
        int fencer1Rank = 1;
        int fencer2Rank = 2;
        int fencer3Rank = 3;
        int fencer4Rank = 4;
        String resultText = "15";
        boolean isWinner = true;
        String status = "Active";

        Event event = new Event();
        event.setId(eventId);

        DirectEliminationMatch match1 = new DirectEliminationMatch();
        match1.setId(matchId1);
        match1.setNextMatchId(nextMatchId);
        match1.setRoundOf(roundOf);
        match1.setFencer1(fencer1Id);
        match1.setFencer2(fencer2Id);
        match1.setScore1(15);
        match1.setScore2(10);
        match1.setWinner(fencer1Id);

        DirectEliminationMatch match2 = new DirectEliminationMatch();
        match2.setId(matchId2);
        match2.setNextMatchId(nextMatchId);
        match2.setRoundOf(roundOf);
        match2.setFencer1(fencer3Id);
        match2.setFencer2(fencer4Id);
        match2.setScore1(12);
        match2.setScore2(15);
        match2.setWinner(fencer4Id);

        Fencer fencer1 = new Fencer();
        fencer1.setId(fencer1Id);
        fencer1.setName(fencer1Name);

        Fencer fencer2 = new Fencer();
        fencer2.setId(fencer2Id);
        fencer2.setName(fencer2Name);

        Fencer fencer3 = new Fencer();
        fencer3.setId(fencer3Id);
        fencer3.setName(fencer3Name);

        Fencer fencer4 = new Fencer();
        fencer4.setId(fencer4Id);
        fencer4.setName(fencer4Name);

        TournamentFencer tFencer1 = new TournamentFencer();
        tFencer1.setId(fencer1Id);
        tFencer1.setTournamentRank(fencer1Rank);
        tFencer1.setFencer(fencer1);

        TournamentFencer tFencer2 = new TournamentFencer();
        tFencer2.setId(fencer2Id);
        tFencer2.setTournamentRank(fencer2Rank);
        tFencer2.setFencer(fencer2);

        TournamentFencer tFencer3 = new TournamentFencer();
        tFencer3.setId(fencer3Id);
        tFencer3.setTournamentRank(fencer3Rank);
        tFencer3.setFencer(fencer3);

        TournamentFencer tFencer4 = new TournamentFencer();
        tFencer4.setId(fencer4Id);
        tFencer4.setTournamentRank(fencer4Rank);
        tFencer4.setFencer(fencer4);

        when(eventService.getEvent(eventId)).thenReturn(event);
        when(directEliminationMatchRepository.findByEvent(event)).thenReturn(Arrays.asList(match1, match2));
        when(tournamentFencerRepository.findById(fencer1Id)).thenReturn(Optional.of(tFencer1));
        when(tournamentFencerRepository.findById(fencer2Id)).thenReturn(Optional.of(tFencer2));
        when(tournamentFencerRepository.findById(fencer3Id)).thenReturn(Optional.of(tFencer3));
        when(tournamentFencerRepository.findById(fencer4Id)).thenReturn(Optional.of(tFencer4));
        when(fencerRepository.findById(fencer1Id)).thenReturn(Optional.of(fencer1));
        when(fencerRepository.findById(fencer2Id)).thenReturn(Optional.of(fencer2));
        when(fencerRepository.findById(fencer3Id)).thenReturn(Optional.of(fencer3));
        when(fencerRepository.findById(fencer4Id)).thenReturn(Optional.of(fencer4));
        when(matchService.getFencersInMatch(match1)).thenReturn(Arrays.asList(tFencer1, tFencer2));
        when(matchService.getFencersInMatch(match2)).thenReturn(Arrays.asList(tFencer3, tFencer4));

        // Act
        List<DirectEliminationBracketDTO> result = directEliminationService.generateDirectEliminationBracketDTOs(eventId);

        // Assert
        assertEquals(2, result.size());

        DirectEliminationBracketDTO dto1 = result.get(0);
        assertEquals(matchId1, dto1.getId());
        assertEquals(nextMatchId, dto1.getNextMatchId());
        assertEquals("Top 16", dto1.getTournamentRoundText());

        DirectEliminationBracketFencerDTO[] fencersDTO1 = dto1.getParticipants();
        assertEquals(2, fencersDTO1.length);

        DirectEliminationBracketFencerDTO fencerDTO1_1 = fencersDTO1[0];
        assertEquals(fencer1Id, fencerDTO1_1.getId());
        assertEquals(fencer1Name, fencerDTO1_1.getName());
        assertEquals(resultText, fencerDTO1_1.getResultText());
        assertEquals(isWinner, fencerDTO1_1.getIsWinner());

        DirectEliminationBracketFencerDTO fencerDTO1_2 = fencersDTO1[1];
        assertEquals(fencer2Id, fencerDTO1_2.getId());
        assertEquals(fencer2Name, fencerDTO1_2.getName());
        assertEquals("10", fencerDTO1_2.getResultText());
        assertEquals(!isWinner, fencerDTO1_2.getIsWinner());

        DirectEliminationBracketDTO dto2 = result.get(1);
        assertEquals(matchId2, dto2.getId());
        assertEquals(nextMatchId, dto2.getNextMatchId());
        assertEquals("Top 16", dto2.getTournamentRoundText());

        DirectEliminationBracketFencerDTO[] fencersDTO2 = dto2.getParticipants();
        assertEquals(2, fencersDTO2.length);

        DirectEliminationBracketFencerDTO fencerDTO2_1 = fencersDTO2[0];
        assertEquals(fencer3Id, fencerDTO2_1.getId());
        assertEquals(fencer3Name, fencerDTO2_1.getName());
        assertEquals("12", fencerDTO2_1.getResultText());
        assertEquals(!isWinner, fencerDTO2_1.getIsWinner());

        DirectEliminationBracketFencerDTO fencerDTO2_2 = fencersDTO2[1];
        assertEquals(fencer4Id, fencerDTO2_2.getId());
        assertEquals(fencer4Name, fencerDTO2_2.getName());
        assertEquals(resultText, fencerDTO2_2.getResultText());
        assertEquals(isWinner, fencerDTO2_2.getIsWinner());
    }

    @Test
    void getTournamentRanks() {
        // Arrange
        int eventId = 1;
        int fencer1Id = 1;
        int fencer2Id = 2;
        String fencer1Name = "John Doe";
        String fencer2Name = "Jane Smith";
        int fencer1Rank = 1;
        int fencer2Rank = 2;

        Fencer fencer1 = new Fencer();
        fencer1.setId(fencer1Id);
        fencer1.setName(fencer1Name);

        Fencer fencer2 = new Fencer();
        fencer2.setId(fencer2Id);
        fencer2.setName(fencer2Name);

        TournamentFencer tFencer1 = new TournamentFencer();
        tFencer1.setId(fencer1Id);
        tFencer1.setTournamentRank(fencer1Rank);
        tFencer1.setFencer(fencer1);

        TournamentFencer tFencer2 = new TournamentFencer();
        tFencer2.setId(fencer2Id);
        tFencer2.setTournamentRank(fencer2Rank);
        tFencer2.setFencer(fencer2);

        Event event = new Event();
        event.setId(eventId);
        Set<TournamentFencer> fencers = new HashSet<>();
        fencers.add(tFencer1);
        fencers.add(tFencer2);
        event.setFencers(fencers);

        when(eventService.getEvent(eventId)).thenReturn(event);
        when(tournamentFencerRepository.findByEvent(event)).thenReturn(Arrays.asList(tFencer1, tFencer2));

        // Act
        List<TournamentFencer> result = directEliminationService.getTournamentRanks(eventId);

        // Assert
        assertEquals(2, result.size());

        TournamentFencer rank1 = result.get(0);
        assertEquals(fencer1Id, rank1.getId());
        assertEquals(fencer1Name, rank1.getFencer().getName());
        assertEquals(fencer1Rank, rank1.getTournamentRank());

        TournamentFencer rank2 = result.get(1);
        assertEquals(fencer2Id, rank2.getId());
        assertEquals(fencer2Name, rank2.getFencer().getName());
        assertEquals(fencer2Rank, rank2.getTournamentRank());
    }


    
}

