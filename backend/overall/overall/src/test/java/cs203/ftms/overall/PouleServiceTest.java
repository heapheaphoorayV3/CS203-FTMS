package cs203.ftms.overall;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

import cs203.ftms.overall.dto.CreatePoulesDTO;
import cs203.ftms.overall.dto.PouleResultsDTO;
import cs203.ftms.overall.dto.PouleTableDTO;
import cs203.ftms.overall.dto.SinglePouleTableDTO;
import cs203.ftms.overall.dto.clean.CleanMatchDTO;
import cs203.ftms.overall.dto.clean.CleanPouleDTO;
import cs203.ftms.overall.dto.clean.CleanTournamentFencerDTO;
import cs203.ftms.overall.model.tournamentrelated.Event;
import cs203.ftms.overall.model.tournamentrelated.Poule;
import cs203.ftms.overall.model.tournamentrelated.PouleMatch;
import cs203.ftms.overall.model.tournamentrelated.Tournament;
import cs203.ftms.overall.model.tournamentrelated.TournamentFencer;
import cs203.ftms.overall.model.userrelated.Fencer;
import cs203.ftms.overall.repository.tournamentrelated.EventRepository;
import cs203.ftms.overall.repository.tournamentrelated.MatchRepository;
import cs203.ftms.overall.repository.tournamentrelated.PouleRepository;
import cs203.ftms.overall.repository.tournamentrelated.TournamentFencerRepository;
import cs203.ftms.overall.service.event.EventService;
import cs203.ftms.overall.service.match.MatchService;
import cs203.ftms.overall.service.match.PouleService;

public class PouleServiceTest {

    @Mock
    private PouleRepository pouleRepository;

    @Mock
    private TournamentFencerRepository tournamentFencerRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private MatchService matchService;

    @Mock
    private EventService eventService;

    @InjectMocks
    private PouleService pouleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getCleanPouleDTO_MenSabre() {

        // Arrange
        Event event = new Event();
        event.setId(1);

        int pouleId = 1;
        int fencer1Id = 1;
        int fencer2Id = 2;
        String fencer1Name = "John Doe";
        String fencer2Name = "Jane Smith";

        Fencer fencer1 = new Fencer();
        fencer1.setId(fencer1Id);
        fencer1.setName(fencer1Name);

        Fencer fencer2 = new Fencer();
        fencer2.setId(fencer2Id);
        fencer2.setName(fencer2Name);

        TournamentFencer tFencer1 = new TournamentFencer();
        tFencer1.setId(fencer1Id);
        tFencer1.setFencer(fencer1);

        TournamentFencer tFencer2 = new TournamentFencer();
        tFencer2.setId(fencer2Id);
        tFencer2.setFencer(fencer2);

        PouleMatch pouleMatch1 = new PouleMatch();
        pouleMatch1.setId(1);
        pouleMatch1.setFencer1(fencer1Id);
        pouleMatch1.setFencer2(fencer2Id);

        Set<PouleMatch> pouleMatches = new HashSet<>();
        pouleMatches.add(pouleMatch1);

        Poule poule = new Poule();
        poule.setId(pouleId);
        poule.setFencers(new HashSet<>(Arrays.asList(tFencer1, tFencer2)));
        poule.setPouleMatches(pouleMatches);
        poule.setEvent(event);

        event.setPoules(new HashSet<>(Arrays.asList(poule)));
        event.setGender('M');
        event.setWeapon('S');

        CleanTournamentFencerDTO cleanFencer1 = new CleanTournamentFencerDTO(fencer1Id, fencer1Id, fencer1Name, fencer1Name, fencer1Name, 'L', fencer1Id, fencer1Id, fencer1Id, fencer1Id, 0);
        CleanTournamentFencerDTO cleanFencer2 = new CleanTournamentFencerDTO(fencer2Id, fencer2Id, fencer2Name, fencer2Name, fencer2Name, 'R', fencer2Id, fencer2Id, fencer2Id, fencer2Id, 0);

        when(pouleRepository.findById(pouleId)).thenReturn(Optional.of(poule));
        when(tournamentFencerRepository.findById(fencer1Id)).thenReturn(Optional.of(tFencer1));
        when(tournamentFencerRepository.findById(fencer2Id)).thenReturn(Optional.of(tFencer2));
        when(matchService.getCleanMatchDTO(pouleMatch1, 'P')).thenReturn(new CleanMatchDTO());
        when(eventService.getCleanTournamentFencerDTO(tFencer1)).thenReturn(cleanFencer1);
        when(eventService.getCleanTournamentFencerDTO(tFencer2)).thenReturn(cleanFencer2);

        // Act
        CleanPouleDTO result = pouleService.getCleanPouleDTO(poule);

        // Assert
        assertEquals(pouleId, result.getId());
        List<CleanTournamentFencerDTO> cleanFencers = result.getFencers();
        assertEquals(2, cleanFencers.size());
    }

    @Test
    void getCleanPouleDTO_WomenFoil() {

        // Arrange
        Event event = new Event();
        event.setId(1);

        int pouleId = 1;
        int fencer1Id = 1;
        int fencer2Id = 2;
        String fencer1Name = "John Doe";
        String fencer2Name = "Jane Smith";

        Fencer fencer1 = new Fencer();
        fencer1.setId(fencer1Id);
        fencer1.setName(fencer1Name);

        Fencer fencer2 = new Fencer();
        fencer2.setId(fencer2Id);
        fencer2.setName(fencer2Name);

        TournamentFencer tFencer1 = new TournamentFencer();
        tFencer1.setId(fencer1Id);
        tFencer1.setFencer(fencer1);

        TournamentFencer tFencer2 = new TournamentFencer();
        tFencer2.setId(fencer2Id);
        tFencer2.setFencer(fencer2);

        PouleMatch pouleMatch1 = new PouleMatch();
        pouleMatch1.setId(1);
        pouleMatch1.setFencer1(fencer1Id);
        pouleMatch1.setFencer2(fencer2Id);

        Set<PouleMatch> pouleMatches = new HashSet<>();
        pouleMatches.add(pouleMatch1);

        Poule poule = new Poule();
        poule.setId(pouleId);
        poule.setFencers(new HashSet<>(Arrays.asList(tFencer1, tFencer2)));
        poule.setPouleMatches(pouleMatches);
        poule.setEvent(event);

        event.setPoules(new HashSet<>(Arrays.asList(poule)));
        event.setGender('W');
        event.setWeapon('F');

        CleanTournamentFencerDTO cleanFencer1 = new CleanTournamentFencerDTO(fencer1Id, fencer1Id, fencer1Name, fencer1Name, fencer1Name, 'L', fencer1Id, fencer1Id, fencer1Id, fencer1Id, 0);
        CleanTournamentFencerDTO cleanFencer2 = new CleanTournamentFencerDTO(fencer2Id, fencer2Id, fencer2Name, fencer2Name, fencer2Name, 'R', fencer2Id, fencer2Id, fencer2Id, fencer2Id, 0);

        when(pouleRepository.findById(pouleId)).thenReturn(Optional.of(poule));
        when(tournamentFencerRepository.findById(fencer1Id)).thenReturn(Optional.of(tFencer1));
        when(tournamentFencerRepository.findById(fencer2Id)).thenReturn(Optional.of(tFencer2));
        when(matchService.getCleanMatchDTO(pouleMatch1, 'P')).thenReturn(new CleanMatchDTO());
        when(eventService.getCleanTournamentFencerDTO(tFencer1)).thenReturn(cleanFencer1);
        when(eventService.getCleanTournamentFencerDTO(tFencer2)).thenReturn(cleanFencer2);

        // Act
        CleanPouleDTO result = pouleService.getCleanPouleDTO(poule);

        // Assert
        assertEquals(pouleId, result.getId());
        List<CleanTournamentFencerDTO> cleanFencers = result.getFencers();
        assertEquals(2, cleanFencers.size());
    }

    @Test
    void getCleanPouleDTO_MenEpee() {

        // Arrange
        Event event = new Event();
        event.setId(1);

        int pouleId = 1;
        int fencer1Id = 1;
        int fencer2Id = 2;
        String fencer1Name = "John Doe";
        String fencer2Name = "Jane Smith";

        Fencer fencer1 = new Fencer();
        fencer1.setId(fencer1Id);
        fencer1.setName(fencer1Name);

        Fencer fencer2 = new Fencer();
        fencer2.setId(fencer2Id);
        fencer2.setName(fencer2Name);

        TournamentFencer tFencer1 = new TournamentFencer();
        tFencer1.setId(fencer1Id);
        tFencer1.setFencer(fencer1);

        TournamentFencer tFencer2 = new TournamentFencer();
        tFencer2.setId(fencer2Id);
        tFencer2.setFencer(fencer2);

        PouleMatch pouleMatch1 = new PouleMatch();
        pouleMatch1.setId(1);
        pouleMatch1.setFencer1(fencer1Id);
        pouleMatch1.setFencer2(fencer2Id);

        Set<PouleMatch> pouleMatches = new HashSet<>();
        pouleMatches.add(pouleMatch1);

        Poule poule = new Poule();
        poule.setId(pouleId);
        poule.setFencers(new HashSet<>(Arrays.asList(tFencer1, tFencer2)));
        poule.setPouleMatches(pouleMatches);
        poule.setEvent(event);

        event.setPoules(new HashSet<>(Arrays.asList(poule)));
        event.setGender('M');
        event.setWeapon('E');

        CleanTournamentFencerDTO cleanFencer1 = new CleanTournamentFencerDTO(fencer1Id, fencer1Id, fencer1Name, fencer1Name, fencer1Name, 'L', fencer1Id, fencer1Id, fencer1Id, fencer1Id, 0);
        CleanTournamentFencerDTO cleanFencer2 = new CleanTournamentFencerDTO(fencer2Id, fencer2Id, fencer2Name, fencer2Name, fencer2Name, 'R', fencer2Id, fencer2Id, fencer2Id, fencer2Id, 0);

        when(pouleRepository.findById(pouleId)).thenReturn(Optional.of(poule));
        when(tournamentFencerRepository.findById(fencer1Id)).thenReturn(Optional.of(tFencer1));
        when(tournamentFencerRepository.findById(fencer2Id)).thenReturn(Optional.of(tFencer2));
        when(matchService.getCleanMatchDTO(pouleMatch1, 'P')).thenReturn(new CleanMatchDTO());
        when(eventService.getCleanTournamentFencerDTO(tFencer1)).thenReturn(cleanFencer1);
        when(eventService.getCleanTournamentFencerDTO(tFencer2)).thenReturn(cleanFencer2);

        // Act
        CleanPouleDTO result = pouleService.getCleanPouleDTO(poule);

        // Assert
        assertEquals(pouleId, result.getId());
        List<CleanTournamentFencerDTO> cleanFencers = result.getFencers();
        assertEquals(2, cleanFencers.size());
    }

    @Test
    void getCleanPouleDTO_NoWeaponAndGender() {

        // Arrange
        Event event = new Event();
        event.setId(1);

        int pouleId = 1;
        int fencer1Id = 1;
        int fencer2Id = 2;
        String fencer1Name = "John Doe";
        String fencer2Name = "Jane Smith";

        Fencer fencer1 = new Fencer();
        fencer1.setId(fencer1Id);
        fencer1.setName(fencer1Name);

        Fencer fencer2 = new Fencer();
        fencer2.setId(fencer2Id);
        fencer2.setName(fencer2Name);

        TournamentFencer tFencer1 = new TournamentFencer();
        tFencer1.setId(fencer1Id);
        tFencer1.setFencer(fencer1);

        TournamentFencer tFencer2 = new TournamentFencer();
        tFencer2.setId(fencer2Id);
        tFencer2.setFencer(fencer2);

        PouleMatch pouleMatch1 = new PouleMatch();
        pouleMatch1.setId(1);
        pouleMatch1.setFencer1(fencer1Id);
        pouleMatch1.setFencer2(fencer2Id);

        Set<PouleMatch> pouleMatches = new HashSet<>();
        pouleMatches.add(pouleMatch1);

        Poule poule = new Poule();
        poule.setId(pouleId);
        poule.setFencers(new HashSet<>(Arrays.asList(tFencer1, tFencer2)));
        poule.setPouleMatches(pouleMatches);
        poule.setEvent(event);

        event.setPoules(new HashSet<>(Arrays.asList(poule)));

        CleanTournamentFencerDTO cleanFencer1 = new CleanTournamentFencerDTO(fencer1Id, fencer1Id, fencer1Name, fencer1Name, fencer1Name, 'L', fencer1Id, fencer1Id, fencer1Id, fencer1Id, 0);
        CleanTournamentFencerDTO cleanFencer2 = new CleanTournamentFencerDTO(fencer2Id, fencer2Id, fencer2Name, fencer2Name, fencer2Name, 'R', fencer2Id, fencer2Id, fencer2Id, fencer2Id, 0);

        when(pouleRepository.findById(pouleId)).thenReturn(Optional.of(poule));
        when(tournamentFencerRepository.findById(fencer1Id)).thenReturn(Optional.of(tFencer1));
        when(tournamentFencerRepository.findById(fencer2Id)).thenReturn(Optional.of(tFencer2));
        when(matchService.getCleanMatchDTO(pouleMatch1, 'P')).thenReturn(new CleanMatchDTO());
        when(eventService.getCleanTournamentFencerDTO(tFencer1)).thenReturn(cleanFencer1);
        when(eventService.getCleanTournamentFencerDTO(tFencer2)).thenReturn(cleanFencer2);

        // Act
        CleanPouleDTO result = pouleService.getCleanPouleDTO(poule);

        // Assert
        assertEquals(pouleId, result.getId());
        List<CleanTournamentFencerDTO> cleanFencers = result.getFencers();
        assertEquals(2, cleanFencers.size());
    }

    @Test
    void recommendPoules_10Fencers() {
        // Arrange
        int eventId = 1;
        Event event = new Event();
        event.setParticipantCount(10);

        when(eventService.getEvent(eventId)).thenReturn(event);

        // Act
        Set<String> result = pouleService.recommendPoules(eventId);

        // Assert
        Set<String> expected = new LinkedHashSet<>();
        expected.add("1 Poules: 1 poules of 10 fencers");
        expected.add("2 Poules: 2 poules of 5 fencers");
        assertEquals(expected, result);
    }

    @Test
    void recommendPoules_16Fencers() {
        // Arrange
        int eventId = 1;
        Event event = new Event();
        event.setParticipantCount(16);

        when(eventService.getEvent(eventId)).thenReturn(event);

        // Act
        Set<String> result = pouleService.recommendPoules(eventId);

        // Assert
        Set<String> expected = new LinkedHashSet<>();
        expected.add("2 Poules: 2 poules of 8 fencers");
        expected.add("3 Poules: 2 poules of 5 fencers and 1 poule(s) of 6 fencers");

        assertEquals(expected, result);
    }

    @Test
    void recommendPoules_15Fencers() {
        // Arrange
        int eventId = 1;
        Event event = new Event();
        event.setParticipantCount(15);

        when(eventService.getEvent(eventId)).thenReturn(event);

        // Act
        Set<String> result = pouleService.recommendPoules(eventId);

        // Assert
        Set<String> expected = new LinkedHashSet<>();
        expected.add("1 Poules: 1 poules of 15 fencers");
        expected.add("2 Poules: 1 poules of 7 fencers and 1 poule(s) of 8 fencers");
        expected.add("3 Poules: 3 poules of 5 fencers");

        assertEquals(expected, result);
    }

    @Test
    void createPoules_ShouldCreateAndReturnPoules() {
        // Arrange
        int eventId = 1;
        CreatePoulesDTO dto = new CreatePoulesDTO();
        dto.setPouleCount(2);

        Event event = new Event();
        event.setId(eventId);
        event.setPoules(Collections.emptySet());

        Set<TournamentFencer> fencers = new HashSet<>();
        for (int i = 0; i < 5; i++) {
            TournamentFencer tournamentFencer = new TournamentFencer();
            Fencer fencer = new Fencer();
            fencer.setPoints(100); // Set some points for the fencer
            tournamentFencer.setFencer(fencer);
            fencers.add(tournamentFencer);
        }
        event.setFencers(fencers);

        when(eventService.getEvent(eventId)).thenReturn(event);
        when(eventRepository.save(event)).thenReturn(event);


        // Act
        Set<CleanPouleDTO> result = pouleService.createPoules(eventId, dto);

        // Assert
        verify(eventService).getEvent(eventId);
        assertNotNull(result);
    }
    
    @Test
    void createPoules_PoulesAlreadyExist() {
        // Arrange
        int eventId = 1;
        CreatePoulesDTO dto = new CreatePoulesDTO();
        dto.setPouleCount(2);

        Event event = new Event();
        event.setId(eventId);
        event.setGender('M');
        event.setWeapon('S');

        Set<TournamentFencer> fencers = new HashSet<>();
        for (int i = 0; i < 5; i++) {
            TournamentFencer tournamentFencer = new TournamentFencer();
            Fencer fencer = new Fencer();
            fencer.setPoints(100); // Set some points for the fencer
            tournamentFencer.setFencer(fencer);
            fencers.add(tournamentFencer);
        }

        Set<PouleMatch> pouleMatches = new HashSet<>();

        event.setFencers(fencers);
        Set<Poule> poules = new HashSet<>();
        Poule poule = new Poule();
        poule.setPouleMatches(pouleMatches);
        poule.setFencers(fencers);
        poules.add(poule);
        event.setPoules(poules);
        poule.setPouleMatches(pouleMatches);
        poule.setEvent(event);

        when(eventService.getEvent(eventId)).thenReturn(event);
        when(eventRepository.save(event)).thenReturn(event);


        // Act
        Set<CleanPouleDTO> result = pouleService.createPoules(eventId, dto);

        // Assert
        verify(eventService).getEvent(eventId);
        assertNotNull(result);
    }

    @Test
    void getPoulesOfEvent_ShouldReturnCleanPoules() {
        // Arrange
        int eventId = 1;

        Event event = new Event();
        event.setId(eventId);

        Set<TournamentFencer> fencers = new HashSet<>();
        for (int i = 0; i < 5; i++) {
            TournamentFencer tournamentFencer = new TournamentFencer();
            Fencer fencer = new Fencer();
            fencer.setPoints(100); // Set some points for the fencer
            tournamentFencer.setFencer(fencer);
            fencers.add(tournamentFencer);
        }
        event.setFencers(fencers);

        Set<Poule> poules = new HashSet<>();
        Poule poule = new Poule();
        poule.setEvent(event); // Ensure the poule has a non-null event
        poule.setFencers(fencers);
        poule.setPouleMatches(new HashSet<>()); // Ensure poule matches are not null
        poules.add(poule);
        event.setPoules(poules);

        when(eventService.getEvent(eventId)).thenReturn(event);

        // Act
        Set<CleanPouleDTO> result = pouleService.getPoulesOfEvent(eventId);

        // Assert
        assertEquals(1, result.size());
        // Add more assertions as needed to verify the behavior
    }


    @Test
    void getPouleTable_ShouldReturnPouleTable_WhenPoulesExist() {
        // Arrange
        int eventId = 1;
        boolean createPM = true;

        Event event = new Event();
        event.setId(eventId);

        Set<TournamentFencer> fencers = new HashSet<>();
        for (int i = 0; i < 5; i++) {
            TournamentFencer tournamentFencer = new TournamentFencer();
            tournamentFencer.setId(i);
            Fencer fencer = new Fencer();
            fencer.setPoints(100 + i); // Set some points for the fencer
            fencer.setName("Fencer " + i);
            fencer.setCountry("Country " + i);
            tournamentFencer.setFencer(fencer);
            fencers.add(tournamentFencer);
        }
        event.setFencers(fencers);

        List<Poule> poules = new ArrayList<>();
        Poule poule = new Poule();
        poule.setId(1);
        poule.setEvent(event); // Ensure the poule has a non-null event
        poule.setFencers(fencers);
        Set<PouleMatch> pouleMatches = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            PouleMatch pouleMatch = new PouleMatch();
            pouleMatch.setId(i);
            pouleMatch.setFencer1(i);
            pouleMatch.setFencer2(i + 1);
            pouleMatches.add(pouleMatch);
        }
        poule.setPouleMatches(new HashSet<>()); // Ensure poule matches are not null
        poules.add(poule);

        when(eventService.getEvent(eventId)).thenReturn(event);
        when(pouleRepository.findByEvent(event)).thenReturn(poules);
        when(pouleRepository.findById(1)).thenReturn(Optional.of(poule));
        when(tournamentFencerRepository.findById(0)).thenReturn(Optional.of(fencers.iterator().next()));
        when(tournamentFencerRepository.findById(1)).thenReturn(Optional.of(fencers.iterator().next()));
        when(tournamentFencerRepository.findById(2)).thenReturn(Optional.of(fencers.iterator().next()));
        when(tournamentFencerRepository.findById(3)).thenReturn(Optional.of(fencers.iterator().next()));
        when(tournamentFencerRepository.findById(4)).thenReturn(Optional.of(fencers.iterator().next()));
        when(matchRepository.save(pouleMatches.iterator().next())).thenReturn(pouleMatches.iterator().next());

        // Act
        PouleTableDTO result = pouleService.getPouleTable(eventId, createPM);
        // Assert
        verify(eventService).getEvent(eventId);
        assertNotNull(result);
    }

    @Test
    void getPouleTable_ShouldReturnPouleTable_WhenCreatePMIsFalse() {
        // Arrange
        int eventId = 1;
        boolean createPM = false;

        Event event = new Event();
        event.setId(eventId);

        Set<TournamentFencer> fencers = new HashSet<>();
        for (int i = 0; i < 5; i++) {
            TournamentFencer tournamentFencer = new TournamentFencer();
            tournamentFencer.setId(i);
            Fencer fencer = new Fencer();
            fencer.setPoints(100 + i); // Set some points for the fencer
            fencer.setName("Fencer " + i);
            fencer.setCountry("Country " + i);
            tournamentFencer.setFencer(fencer);
            fencers.add(tournamentFencer);
        }
        event.setFencers(fencers);

        List<Poule> poules = new ArrayList<>();
        Poule poule = new Poule();
        poule.setId(1);
        poule.setEvent(event); // Ensure the poule has a non-null event
        poule.setFencers(fencers);
        Set<PouleMatch> pouleMatches = new HashSet<>();
        for (TournamentFencer tf1 : fencers) {
            for (TournamentFencer tf2 : fencers) {
                if (!tf1.equals(tf2) && !pouleMatches.contains(tf2.getId() + tf1.getId()) && !pouleMatches.contains(tf1.getId() + tf2.getId())) {
                    PouleMatch pouleMatch = new PouleMatch();
                    pouleMatch.setFencer1(tf1.getId());
                    pouleMatch.setFencer2(tf2.getId());
                    pouleMatches.add(pouleMatch);

                    when(matchService.getFencer1(pouleMatch)).thenReturn(tf1);
                    when(matchService.getFencer2(pouleMatch)).thenReturn(tf2);
                }
            }
        }
        poule.setPouleMatches(pouleMatches); // Ensure poule matches are not null
        poules.add(poule);

        when(eventService.getEvent(eventId)).thenReturn(event);
        when(pouleRepository.findByEvent(event)).thenReturn(poules);
        when(pouleRepository.findById(1)).thenReturn(Optional.of(poule));
        for (TournamentFencer fencer : fencers) {
            when(tournamentFencerRepository.findById(fencer.getId())).thenReturn(Optional.of(fencer));
        }
        for (PouleMatch pouleMatch : pouleMatches) {
            when(matchRepository.save(pouleMatch)).thenReturn(pouleMatch);
        }
        // Act
        PouleTableDTO result = pouleService.getPouleTable(eventId, createPM);

        // Assert
        verify(eventService).getEvent(eventId);
        assertNotNull(result);
        // Add more assertions as needed to verify the behavior
    }

    @Test
    void updatePouleTable_ShouldUpdatePouleTable() throws MethodArgumentNotValidException {
         // Arrange
         int eventId = 1;
         boolean createPM = true;
 
         Event event = new Event();
         event.setId(eventId);
 
         Set<TournamentFencer> fencers = new HashSet<>();
         for (int i = 0; i < 5; i++) {
             TournamentFencer tournamentFencer = new TournamentFencer();
             tournamentFencer.setId(i);
             Fencer fencer = new Fencer();
             fencer.setPoints(100 + i); // Set some points for the fencer
             fencer.setName("Fencer " + i);
             fencer.setCountry("Country " + i);
             tournamentFencer.setFencer(fencer);
             fencers.add(tournamentFencer);
         }
         event.setFencers(fencers);

         Set<PouleMatch> pouleMatches = new HashSet<>();
         for (int i = 0; i < 10; i++) {
             PouleMatch pouleMatch = new PouleMatch();
             pouleMatch.setId(i);
             pouleMatch.setFencer1(i);
             pouleMatch.setFencer2(i + 1);
             pouleMatches.add(pouleMatch);
         }
 
         List<Poule> poules = new ArrayList<>();
         Poule poule = new Poule();
         poule.setId(1);
         poule.setPouleNumber(1);
         poule.setEvent(event); // Ensure the poule has a non-null event
         poule.setFencers(fencers);
         poule.setPouleMatches(new HashSet<>()); // Ensure poule matches are not null
         poules.add(poule);
 
         when(eventService.getEvent(eventId)).thenReturn(event);
         when(pouleRepository.findByEvent(event)).thenReturn(poules);
         when(pouleRepository.findById(1)).thenReturn(Optional.of(poule));
         when(pouleRepository.findByEventAndPouleNumber(event, 1)).thenReturn(Collections.singletonList(poule));
         for (TournamentFencer fencer : fencers) {
            when(tournamentFencerRepository.findById(fencer.getId())).thenReturn(Optional.of(fencer));
        }
         when(matchRepository.save(pouleMatches.iterator().next())).thenReturn(pouleMatches.iterator().next());
         for (TournamentFencer tf1 : fencers) {
            for (TournamentFencer tf2 : fencers) {
                if (!tf1.equals(tf2) && !pouleMatches.contains(tf2.getId() + tf1.getId()) && !pouleMatches.contains(tf1.getId() + tf2.getId())) {
                    PouleMatch pouleMatch = new PouleMatch();
                    pouleMatch.setFencer1(tf1.getId());
                    pouleMatch.setFencer2(tf2.getId());
                    pouleMatches.add(pouleMatch);

                    when(matchService.getFencer1(pouleMatch)).thenReturn(tf1);
                    when(matchService.getFencer2(pouleMatch)).thenReturn(tf2);
                }
            }
        }
    
        PouleTableDTO initial = pouleService.getPouleTable(eventId, createPM);
        

        SinglePouleTableDTO singlePouleTableDTO = new SinglePouleTableDTO(1, initial.getPouleTable().get(0));
        String updated = singlePouleTableDTO.getSingleTable().get("Fencer 4 (Country 4) -- 4").replace('0', '5');
        singlePouleTableDTO.getSingleTable().put("Fencer 4 (Country 4) -- 4", updated);

        // Act - Update the poule table
        boolean result = pouleService.updatePouleTable(eventId, singlePouleTableDTO);

        // Assert
        verify(pouleRepository).findByEventAndPouleNumber(event, 1);
        assertTrue(result);
        // Add more assertions as needed to verify the behavior
    }



    @Test
    void updatePouleTable_UpdatePouleTableFencer1win() throws MethodArgumentNotValidException {
        // Arrange
        int eventId = 1;
        boolean createPM = true;

        Event event = new Event();
        event.setId(eventId);

        Set<TournamentFencer> fencers = new HashSet<>();
        for (int i = 0; i < 5; i++) {
            TournamentFencer tournamentFencer = new TournamentFencer();
            tournamentFencer.setId(i);
            Fencer fencer = new Fencer();
            fencer.setPoints(100 + i); // Set some points for the fencer
            fencer.setName("Fencer " + i);
            fencer.setCountry("Country " + i);
            tournamentFencer.setFencer(fencer);
            fencers.add(tournamentFencer);
        }
        event.setFencers(fencers);

        Set<PouleMatch> pouleMatches = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            PouleMatch pouleMatch = new PouleMatch();
            pouleMatch.setId(i);
            pouleMatch.setFencer1(i % 5);
            pouleMatch.setFencer2((i + 1) % 5);
            pouleMatch.setScore1(5); // Set score1 greater than score2
            pouleMatch.setScore2(3); // Set score2 less than score1
            pouleMatches.add(pouleMatch);
        }

        List<Poule> poules = new ArrayList<>();
        Poule poule = new Poule();
        poule.setId(1);
        poule.setPouleNumber(1);
        poule.setEvent(event); // Ensure the poule has a non-null event
        poule.setFencers(fencers);
        poule.setPouleMatches(pouleMatches); // Ensure poule matches are not null
        poules.add(poule);

        when(eventService.getEvent(eventId)).thenReturn(event);
        when(pouleRepository.findByEvent(event)).thenReturn(poules);
        when(pouleRepository.findById(1)).thenReturn(Optional.of(poule));
        when(pouleRepository.findByEventAndPouleNumber(event, 1)).thenReturn(Collections.singletonList(poule));
        for (TournamentFencer fencer : fencers) {
            when(tournamentFencerRepository.findById(fencer.getId())).thenReturn(Optional.of(fencer));
        }
        for (PouleMatch pouleMatch : pouleMatches) {
            when(matchRepository.save(pouleMatch)).thenReturn(pouleMatch);
            when(matchService.getFencer1(pouleMatch)).thenReturn(fencers.stream().filter(f -> f.getId() == pouleMatch.getFencer1()).findFirst().orElse(null));
            when(matchService.getFencer2(pouleMatch)).thenReturn(fencers.stream().filter(f -> f.getId() == pouleMatch.getFencer2()).findFirst().orElse(null));
        }

        PouleTableDTO initial = pouleService.getPouleTable(eventId, createPM);

        SinglePouleTableDTO singlePouleTableDTO = new SinglePouleTableDTO(1, initial.getPouleTable().get(0));
        String updated = singlePouleTableDTO.getSingleTable().get("Fencer 3 (Country 3) -- 3").replace('0', '5');
        singlePouleTableDTO.getSingleTable().put("Fencer 3 (Country 3) -- 3", updated);
        String updated2 = singlePouleTableDTO.getSingleTable().get("Fencer 4 (Country 4) -- 4").replace('0', '4');
        singlePouleTableDTO.getSingleTable().put("Fencer 4 (Country 4) -- 4", updated2);

        // Act - Update the poule table
        boolean result = pouleService.updatePouleTable(eventId, singlePouleTableDTO);

        // Assert
        verify(pouleRepository).findByEventAndPouleNumber(event, 1);
        assertTrue(result);
    }

    @Test
    void getFencersAfterPoules_ShouldReturnCorrectMappings() {
        // Arrange
        Tournament tournament = new Tournament();
        tournament.setId(1);
        tournament.setAdvancementRate(100);

        int eventId = 1;
        boolean createPM = true;

        Event event = new Event();
        event.setId(eventId);
        event.setParticipantCount(5);
        Set<Event> events = new HashSet<>();
        events.add(event);
        tournament.setEvents(events);
        event.setTournament(tournament);

        Set<TournamentFencer> fencers = new HashSet<>();
        for (int i = 0; i < 5; i++) {
            TournamentFencer tournamentFencer = new TournamentFencer();
            tournamentFencer.setId(i);
            Fencer fencer = new Fencer();
            fencer.setPoints(100 + i); // Set some points for the fencer
            fencer.setName("Fencer " + i);
            fencer.setCountry("Country " + i);
            tournamentFencer.setFencer(fencer);
            fencers.add(tournamentFencer);
        }
        event.setFencers(fencers);

        Set<PouleMatch> pouleMatches = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            PouleMatch pouleMatch = new PouleMatch();
            pouleMatch.setId(i);
            pouleMatch.setFencer1(i);
            pouleMatch.setFencer2(i + 1);
            pouleMatches.add(pouleMatch);
        }

        List<Poule> poules = new ArrayList<>();
        Poule poule = new Poule();
        poule.setId(1);
        poule.setPouleNumber(1);
        poule.setEvent(event); // Ensure the poule has a non-null event
        poule.setFencers(fencers);
        poule.setPouleMatches(new HashSet<>()); // Ensure poule matches are not null
        poules.add(poule);

        when(eventService.getEvent(eventId)).thenReturn(event);
        when(pouleRepository.findByEvent(event)).thenReturn(poules);
        when(pouleRepository.findById(1)).thenReturn(Optional.of(poule));
        when(pouleRepository.findByEventAndPouleNumber(event, 1)).thenReturn(Collections.singletonList(poule));
        for (TournamentFencer fencer : fencers) {
            when(tournamentFencerRepository.findById(fencer.getId())).thenReturn(Optional.of(fencer));
        }
        when(matchRepository.save(pouleMatches.iterator().next())).thenReturn(pouleMatches.iterator().next());
        for (TournamentFencer tf1 : fencers) {
            for (TournamentFencer tf2 : fencers) {
                if (!tf1.equals(tf2) && !pouleMatches.contains(tf2.getId() + tf1.getId()) && !pouleMatches.contains(tf1.getId() + tf2.getId())) {
                    PouleMatch pouleMatch = new PouleMatch();
                    pouleMatch.setFencer1(tf1.getId());
                    pouleMatch.setFencer2(tf2.getId());
                    pouleMatches.add(pouleMatch);

                    when(matchService.getFencer1(pouleMatch)).thenReturn(tf1);
                    when(matchService.getFencer2(pouleMatch)).thenReturn(tf2);
                }
            }
        }
        when(tournamentFencerRepository.findByEvent(event)).thenReturn(new ArrayList<>(fencers));

        // Act
        Map<String, List<TournamentFencer>> result = pouleService.getFencersAfterPoules(event);

        // Assert
        assertEquals(3, result.get("Bypass").size()); // nearest lower power of 2 for 5 is 4, so bypass is 1
        assertEquals(2, result.get("FenceOff").size()); // 50% of 10 is 5, so fence off is 4 - 1 = 3
        assertEquals(0, result.get("Eliminated").size()); // remaining fencers are eliminated
    }

    @Test
    void getFencersAfterPoules_ShouldReturnCorrectMappingsNearestPower2() {
        // Arrange
        Tournament tournament = new Tournament();
        tournament.setId(1);
        tournament.setAdvancementRate(80);

        int eventId = 1;
        boolean createPM = true;

        Event event = new Event();
        event.setId(eventId);
        event.setParticipantCount(10);
        Set<Event> events = new HashSet<>();
        events.add(event);
        tournament.setEvents(events);
        event.setTournament(tournament);

        Set<TournamentFencer> fencers = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            TournamentFencer tournamentFencer = new TournamentFencer();
            tournamentFencer.setId(i);
            Fencer fencer = new Fencer();
            fencer.setPoints(100 + i); // Set some points for the fencer
            fencer.setName("Fencer " + i);
            fencer.setCountry("Country " + i);
            tournamentFencer.setFencer(fencer);
            fencers.add(tournamentFencer);
        }
        event.setFencers(fencers);

        Set<PouleMatch> pouleMatches = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            PouleMatch pouleMatch = new PouleMatch();
            pouleMatch.setId(i);
            pouleMatch.setFencer1(i);
            pouleMatch.setFencer2(i + 1);
            pouleMatches.add(pouleMatch);


        }

        List<Poule> poules = new ArrayList<>();
        Poule poule = new Poule();
        poule.setId(1);
        poule.setPouleNumber(1);
        poule.setEvent(event); // Ensure the poule has a non-null event
        poule.setFencers(fencers);
        poule.setPouleMatches(new HashSet<>()); // Ensure poule matches are not null
        poules.add(poule);

        when(eventService.getEvent(eventId)).thenReturn(event);
        when(pouleRepository.findByEvent(event)).thenReturn(poules);
        when(pouleRepository.findById(1)).thenReturn(Optional.of(poule));
        when(pouleRepository.findByEventAndPouleNumber(event, 1)).thenReturn(Collections.singletonList(poule));
        for (TournamentFencer fencer : fencers) {
            when(tournamentFencerRepository.findById(fencer.getId())).thenReturn(Optional.of(fencer));
        }
        when(matchRepository.save(pouleMatches.iterator().next())).thenReturn(pouleMatches.iterator().next());
        for (TournamentFencer tf1 : fencers) {
            for (TournamentFencer tf2 : fencers) {
                if (!tf1.equals(tf2) && !pouleMatches.contains(tf2.getId() + tf1.getId()) && !pouleMatches.contains(tf1.getId() + tf2.getId())) {
                    PouleMatch pouleMatch = new PouleMatch();
                    pouleMatch.setFencer1(tf1.getId());
                    pouleMatch.setFencer2(tf2.getId());
                    pouleMatches.add(pouleMatch);

                    when(matchService.getFencer1(pouleMatch)).thenReturn(tf1);
                    when(matchService.getFencer2(pouleMatch)).thenReturn(tf2);
                }
            }
        }
        when(tournamentFencerRepository.findByEvent(event)).thenReturn(new ArrayList<>(fencers));

        // Act
        Map<String, List<TournamentFencer>> result = pouleService.getFencersAfterPoules(event);

        // Assert
        assertEquals(0, result.get("Bypass").size()); // nearest lower power of 2 for 5 is 4, so bypass is 1
        assertEquals(8, result.get("FenceOff").size()); // 50% of 10 is 5, so fence off is 4 - 1 = 3
        assertEquals(2, result.get("Eliminated").size()); // remaining fencers are eliminated
    }

    @Test
    void updateTournamentFencerRanks_ShouldUpdateRanksCorrectly() {
        // Arrange
        List<TournamentFencer> fencers = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            TournamentFencer tournamentFencer = new TournamentFencer();
            tournamentFencer.setId(i);
            tournamentFencer.setTournamentRank(0); // Initial rank
            fencers.add(tournamentFencer);
        }

        // Act
        pouleService.updateTournamentFencerRanks(fencers);

        // Assert
        for (int i = 0; i < fencers.size(); i++) {
            assertEquals(i + 1, fencers.get(i).getTournamentRank());
            verify(tournamentFencerRepository, times(1)).save(fencers.get(i));
        }
    }



    @Test
    void poulesResult_ShouldReturnPouleResultsDTO() {
        // Arrange
        int eventId = 1;
        Event event = new Event();
        event.setId(eventId);

        Tournament tournament = new Tournament();
        tournament.setAdvancementRate(50); // 50% advancement rate
        event.setTournament(tournament);

        Set<TournamentFencer> fencers = new HashSet<>();
        List<TournamentFencer> fencerList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            TournamentFencer tournamentFencer = new TournamentFencer();
            tournamentFencer.setId(i);
            Fencer fencer = new Fencer();
            fencer.setPoints(100 + i);
            fencer.setName("Fencer " + i);
            fencer.setCountry("Country " + i);
            tournamentFencer.setFencer(fencer);
            fencers.add(tournamentFencer);
            fencerList.add(tournamentFencer);
        }
        event.setFencers(fencers);

        Map<String, List<TournamentFencer>> mappings = new HashMap<>();
        mappings.put("Bypass", fencerList.subList(0, 2));
        mappings.put("FenceOff", fencerList.subList(2, 5));
        mappings.put("Eliminated", fencerList.subList(5, 10));

        when(eventService.getEvent(eventId)).thenReturn(event);

        // Act
        PouleResultsDTO result = pouleService.poulesResult(eventId);

        // Assert
        verify(eventService).getEvent(eventId);
        assertNotNull(result);
        // Add more assertions as needed to verify the behavior
    }
}
