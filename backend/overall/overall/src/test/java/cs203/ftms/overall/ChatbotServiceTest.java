package cs203.ftms.overall;

import java.time.LocalDate;
import java.util.Arrays;
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
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import cs203.ftms.overall.exception.EntityDoesNotExistException;
import cs203.ftms.overall.model.tournamentrelated.Event;
import cs203.ftms.overall.model.tournamentrelated.Tournament;
import cs203.ftms.overall.model.tournamentrelated.TournamentFencer;
import cs203.ftms.overall.model.userrelated.Fencer;
import cs203.ftms.overall.repository.tournamentrelated.EventRepository;
import cs203.ftms.overall.repository.tournamentrelated.TournamentRepository;
import cs203.ftms.overall.repository.userrelated.FencerRepository;
import cs203.ftms.overall.service.chatbot.ChatbotService;

class ChatbotServiceTest {

    @Mock
    private TournamentRepository tournamentRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private FencerRepository fencerRepository;

    @InjectMocks
    private ChatbotService chatbotService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getRecommendedTournaments_ExperienceGreaterThan5() {
        // Arrange
        Fencer fencer = new Fencer();
        fencer.setId(11);
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

        List<Tournament> tournaments = createMockTournaments();
        when(tournamentRepository.findAll()).thenReturn(tournaments);
        when(fencerRepository.findById(1)).thenReturn(Optional.of(fencer));
        when(eventRepository.findById(1)).thenReturn(Optional.of(createMockEvent(1, 'M', 'F')));
        when(eventRepository.findById(2)).thenReturn(Optional.of(createMockEvent(2, 'M', 'F')));
        when(eventRepository.findById(3)).thenReturn(Optional.of(createMockEvent(3, 'M', 'F')));

        ChatbotService spyChatbotService = Mockito.spy(chatbotService);

        // Act
        List<Tournament> result = spyChatbotService.getRecommendedTournaments(fencer);

        // Assert
        assertEquals(3, result.size());
        assertEquals('A', result.get(0).getDifficulty());
        assertEquals('B', result.get(1).getDifficulty());
        assertEquals('I', result.get(2).getDifficulty());
    }

    @Test
    void getRecommendedTournaments_ExperienceGreaterThan3() {
        // Arrange
        Fencer fencer = new Fencer();
        fencer.setId(11);
        fencer.setName("John Doe");
        fencer.setEmail("john@example.com");
        fencer.setContactNo("123456789");
        fencer.setCountry("USA");
        fencer.setDateOfBirth(LocalDate.of(1990, 1, 1));
        fencer.setDominantArm('R');
        fencer.setWeapon('F');
        fencer.setClub("Best Club");
        fencer.setPoints(100);
        fencer.setDebutYear(2020); // Experience is 5 years
        fencer.setGender('M');

        List<Tournament> tournaments = createMockTournaments();
        when(tournamentRepository.findAll()).thenReturn(tournaments);
        when(fencerRepository.findById(1)).thenReturn(Optional.of(fencer));
        when(eventRepository.findById(1)).thenReturn(Optional.of(createMockEvent(1, 'M', 'F')));
        when(eventRepository.findById(2)).thenReturn(Optional.of(createMockEvent(2, 'M', 'F')));
        when(eventRepository.findById(3)).thenReturn(Optional.of(createMockEvent(3, 'M', 'F')));

        ChatbotService spyChatbotService = Mockito.spy(chatbotService);

        // Act
        List<Tournament> result = spyChatbotService.getRecommendedTournaments(fencer);

        // Assert
        assertEquals(2, result.size());
        assertEquals('B', result.get(0).getDifficulty());
        assertEquals('I', result.get(1).getDifficulty());

    }

    @Test
    void getRecommendedTournaments_ExperienceLesserThan3() {
        // Arrange
        Fencer fencer = new Fencer();
        fencer.setId(11);
        fencer.setName("John Doe");
        fencer.setEmail("john@example.com");
        fencer.setContactNo("123456789");
        fencer.setCountry("USA");
        fencer.setDateOfBirth(LocalDate.of(1990, 1, 1));
        fencer.setDominantArm('R');
        fencer.setWeapon('F');
        fencer.setClub("Best Club");
        fencer.setPoints(100);
        fencer.setDebutYear(2023); // Experience is less than 3 years
        fencer.setGender('M');

        List<Tournament> tournaments = createMockTournaments();
        when(tournamentRepository.findAll()).thenReturn(tournaments);
        when(fencerRepository.findById(1)).thenReturn(Optional.of(fencer));
        when(eventRepository.findById(1)).thenReturn(Optional.of(createMockEvent(1, 'M', 'F')));
        when(eventRepository.findById(2)).thenReturn(Optional.of(createMockEvent(2, 'M', 'F')));
        when(eventRepository.findById(3)).thenReturn(Optional.of(createMockEvent(3, 'M', 'F')));

        ChatbotService spyChatbotService = Mockito.spy(chatbotService);

        // Act
        List<Tournament> result = spyChatbotService.getRecommendedTournaments(fencer);

        // Assert
        assertEquals(1, result.size());
        assertEquals('B', result.get(0).getDifficulty());
    }

    @Test
    void getRecommendedTournaments_NoSuitableEventForWeaponType() {
        // Arrange
        Fencer fencer = new Fencer();
        fencer.setId(11);
        fencer.setName("John Doe");
        fencer.setEmail("john@example.com");
        fencer.setContactNo("123456789");
        fencer.setCountry("USA");
        fencer.setDateOfBirth(LocalDate.of(1990, 1, 1));
        fencer.setDominantArm('R');
        fencer.setWeapon('S');
        fencer.setClub("Best Club");
        fencer.setPoints(100);
        fencer.setDebutYear(2023); // Experience is less than 3 years
        fencer.setGender('M');

        List<Tournament> tournaments = createMockTournaments();
        when(tournamentRepository.findAll()).thenReturn(tournaments);
        when(fencerRepository.findById(1)).thenReturn(Optional.of(fencer));
        when(eventRepository.findById(1)).thenReturn(Optional.of(createMockEvent(1, 'M', 'F')));
        when(eventRepository.findById(2)).thenReturn(Optional.of(createMockEvent(2, 'M', 'F')));
        when(eventRepository.findById(3)).thenReturn(Optional.of(createMockEvent(3, 'M', 'F')));

        ChatbotService spyChatbotService = Mockito.spy(chatbotService);

        // Act
        List<Tournament> result = spyChatbotService.getRecommendedTournaments(fencer);

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    void getRecommendedTournaments_NoSuitableEventForGender() {
        // Arrange
        Fencer fencer = new Fencer();
        fencer.setId(11);
        fencer.setName("John Doe");
        fencer.setEmail("john@example.com");
        fencer.setContactNo("123456789");
        fencer.setCountry("USA");
        fencer.setDateOfBirth(LocalDate.of(1990, 1, 1));
        fencer.setDominantArm('R');
        fencer.setWeapon('F');
        fencer.setClub("Best Club");
        fencer.setPoints(100);
        fencer.setDebutYear(2023); // Experience is less than 3 years
        fencer.setGender('W');

        List<Tournament> tournaments = createMockTournaments();
        when(tournamentRepository.findAll()).thenReturn(tournaments);
        when(fencerRepository.findById(1)).thenReturn(Optional.of(fencer));
        when(eventRepository.findById(1)).thenReturn(Optional.of(createMockEvent(1, 'M', 'F')));
        when(eventRepository.findById(2)).thenReturn(Optional.of(createMockEvent(2, 'M', 'F')));
        when(eventRepository.findById(3)).thenReturn(Optional.of(createMockEvent(3, 'M', 'F')));

        ChatbotService spyChatbotService = Mockito.spy(chatbotService);

        // Act
        List<Tournament> result = spyChatbotService.getRecommendedTournaments(fencer);

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    void getRecommendedTournaments_OnlyReturnBeginnerTournament() {
        // Arrange
        Fencer fencer = new Fencer();
        fencer.setId(11);
        fencer.setName("John Doe");
        fencer.setEmail("john@example.com");
        fencer.setContactNo("123456789");
        fencer.setCountry("USA");
        fencer.setDateOfBirth(LocalDate.of(1990, 1, 1));
        fencer.setDominantArm('R');
        fencer.setWeapon('F');
        fencer.setClub("Best Club");
        fencer.setPoints(1);
        fencer.setDebutYear(2023); // Experience is less than 3 years
        fencer.setGender('M');

        List<Tournament> tournaments = createMockTournaments();
        when(tournamentRepository.findAll()).thenReturn(tournaments);
        when(fencerRepository.findById(1)).thenReturn(Optional.of(fencer));
        when(eventRepository.findById(1)).thenReturn(Optional.of(createMockEvent(1, 'M', 'F')));
        when(eventRepository.findById(2)).thenReturn(Optional.of(createMockEvent(2, 'M', 'F')));
        when(eventRepository.findById(3)).thenReturn(Optional.of(createMockEvent(3, 'M', 'F')));

        ChatbotService spyChatbotService = Mockito.spy(chatbotService);

        // Act
        List<Tournament> result = spyChatbotService.getRecommendedTournaments(fencer);

        // Assert
        assertEquals(1, result.size());
    }


    private List<Tournament> createMockTournaments() {
        Event event1 = createMockEvent(1, 'M', 'F');
        Tournament tournament1 = new Tournament();
        tournament1.setDifficulty('A');
        Set<Event> events1 = new HashSet<>();
        events1.add(event1);
        tournament1.setEvents(events1);
        event1.setTournament(tournament1);
    
        Event event2 = createMockEvent(2, 'M', 'F');
        Tournament tournament2 = new Tournament();
        tournament2.setDifficulty('B');
        Set<Event> events2 = new HashSet<>();
        events2.add(event2);
        tournament2.setEvents(events2);
        event2.setTournament(tournament2);
    
        Event event3 = createMockEvent(3, 'M', 'F');
        Tournament tournament3 = new Tournament();
        tournament3.setDifficulty('I');
        Set<Event> events3 = new HashSet<>();
        events3.add(event3);
        tournament3.setEvents(events3);
        event3.setTournament(tournament3);
    
        return Arrays.asList(tournament1, tournament2, tournament3);
    }
    
    private Event createMockEvent(int id, char gender, char weapon) {
        Event event = new Event();
        event.setId(id);
        event.setGender(gender);
        event.setWeapon(weapon);
        Set<TournamentFencer> fencers = new HashSet<>();
        for (int i = 1; i <= 10; i++) {
            Fencer fencer = new Fencer();
            fencer.setId(i);
            fencer.setName("Fencer " + i);
            fencer.setEmail("fencer" + i + "@example.com");
            fencer.setContactNo("123456789" + i);
            fencer.setCountry("Country " + i);
            fencer.setDateOfBirth(LocalDate.of(1990, 1, 1));
            fencer.setDominantArm('R');
            fencer.setWeapon(weapon);
            fencer.setClub("Club " + i);
            fencer.setPoints(99);
            fencer.setDebutYear(2008 + i);
            fencer.setGender(gender);
    
            TournamentFencer tournamentFencer = new TournamentFencer();
            tournamentFencer.setFencer(fencer);
            fencers.add(tournamentFencer);
        }
        event.setFencers(fencers); // Ensure fencers list is initialized
        return event;
    }

    @Test
    void getProjectedPointsEarned() {
        // Arrange
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

        Event event = createMockEvent(1, 'M', 'F');
        when(eventRepository.findById(1)).thenReturn(Optional.of(event));

        ChatbotService spyChatbotService = Mockito.spy(chatbotService);


        // Act
        int result = spyChatbotService.getProjectedPointsEarned(1, fencer);

        // Assert
        assertEquals(62, result);
    }

    @Test
    void getWinrate_HighChanceOfWinning() {
        // Arrange
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
        fencer.setPoints(1000);
        fencer.setDebutYear(2008);
        fencer.setGender('M');

        Event event = createMockEvent(1, 'M', 'F');
        when(eventRepository.findById(1)).thenReturn(Optional.of(event));

        ChatbotService spyChatbotService = Mockito.spy(chatbotService);

        // Act
        String result = spyChatbotService.getWinrate(1, fencer);

        // Assert
        assertEquals("High chance of winning!", result);
    }

    @Test
    void getWinrate_GoodChanceOfWinning() {
        // Arrange
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
        fencer.setPoints(99);
        fencer.setDebutYear(2008);
        fencer.setGender('M');

        Event event = createMockEvent(1, 'M', 'F');
        when(eventRepository.findById(1)).thenReturn(Optional.of(event));

        ChatbotService spyChatbotService = Mockito.spy(chatbotService);
        Mockito.doReturn(5).when(spyChatbotService).expectedRank(event, fencer);

        // Act
        String result = spyChatbotService.getWinrate(1, fencer);

        // Assert
        assertEquals("Good chance of winning!", result);
    }

    @Test
    void getWinrate_ToughFight() {
        // Arrange
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
        fencer.setPoints(98);
        fencer.setDebutYear(2008);
        fencer.setGender('M');

        Event event = createMockEvent(1, 'M', 'F');
        when(eventRepository.findById(1)).thenReturn(Optional.of(event));

        ChatbotService spyChatbotService = Mockito.spy(chatbotService);

        // Act
        String result = spyChatbotService.getWinrate(1, fencer);

        // Assert
        assertEquals("It will be a tough fight!", result);
    }

    // @Test
    // void getWinrate_NoFencers() {
    //     // Arrange
    //     Fencer fencer = new Fencer();
    //     fencer.setId(1);
    //     fencer.setName("John Doe");
    //     fencer.setEmail("john@example.com");
    //     fencer.setContactNo("123456789");
    //     fencer.setCountry("USA");
    //     fencer.setDateOfBirth(LocalDate.of(1990, 1, 1));
    //     fencer.setDominantArm('R');
    //     fencer.setWeapon('F');
    //     fencer.setClub("Best Club");
    //     fencer.setPoints(100);
    //     fencer.setDebutYear(2008);
    //     fencer.setGender('M');

    //     Event event = createMockEventWithNoFencers(1, 'M', 'F');
    //     when(eventRepository.findById(1)).thenReturn(Optional.of(event));

    //     // Mock expectedRank to return a value
    //     ChatbotService spyChatbotService = Mockito.spy(chatbotService);
    //     Mockito.doReturn(1).when(spyChatbotService).expectedRank(event, fencer);

    //     // Act
    //     String result = spyChatbotService.getWinrate(1, fencer);

    //     // Assert
    //     assertEquals("High chance of winning!", result);
    // }

    private Event createMockEventWithNoFencers(int id, char gender, char weapon) {
        Event event = new Event();
        event.setId(id);
        event.setGender(gender);
        event.setWeapon(weapon);
        event.setFencers(new HashSet<>()); // Ensure fencers list is initialized but empty
        return event; 
    }
    @Test
    void getProjectedPointsEarned_NormalCase() {
        // Arrange
        Fencer fencer = createMockFencer(1, 100);
        Event event = createMockEvent(1, 'M', 'F');
        when(eventRepository.findById(1)).thenReturn(Optional.of(event));

        // Mock methods used in getProjectedPointsEarned
        ChatbotService spyChatbotService = Mockito.spy(chatbotService);
        Mockito.doReturn(1).when(spyChatbotService).expectedRank(event, fencer);
        Mockito.doReturn(100).when(spyChatbotService).getPointsForDistribution(event.getFencers());
        Mockito.doReturn(20).when(spyChatbotService).calculatePoints(1, 100, 8);

        // Act
        int result = spyChatbotService.getProjectedPointsEarned(1, fencer);

        // Assert
        assertEquals(20, result);
    }

    @Test
    void getProjectedPointsEarned_NoFencers() {
        // Arrange
        Fencer fencer = createMockFencer(1, 100);
        Event event = createMockEventWithNoFencers(1, 'M', 'F');
        when(eventRepository.findById(1)).thenReturn(Optional.of(event));

        // Mock methods used in getProjectedPointsEarned
        ChatbotService spyChatbotService = Mockito.spy(chatbotService);
        Mockito.doReturn(1).when(spyChatbotService).expectedRank(event, fencer);
        Mockito.doReturn(0).when(spyChatbotService).getPointsForDistribution(event.getFencers());
        Mockito.doReturn(0).when(spyChatbotService).calculatePoints(1, 0, 0);

        // Act
        int result = spyChatbotService.getProjectedPointsEarned(1, fencer);

        // Assert
        assertEquals(0, result);
    }

    @Test
    void getProjectedPointsEarned_ExpectedRankGreaterThanTotalFencers() {
        // Arrange
        Fencer fencer = createMockFencer(1, 100);
        Event event = createMockEvent(1, 'M', 'F');
        when(eventRepository.findById(1)).thenReturn(Optional.of(event));

        // Mock methods used in getProjectedPointsEarned
        ChatbotService spyChatbotService = Mockito.spy(chatbotService);
        Mockito.doReturn(11).when(spyChatbotService).expectedRank(event, fencer); // Rank greater than total fencers
        Mockito.doReturn(100).when(spyChatbotService).getPointsForDistribution(event.getFencers());
        Mockito.doReturn(0).when(spyChatbotService).calculatePoints(11, 100, 8);

        // Act
        int result = spyChatbotService.getProjectedPointsEarned(1, fencer);

        // Assert
        assertEquals(0, result);
    }

    @Test
    void getProjectedPointsEarned_EventDoesNotExist() {
        // Arrange
        Fencer fencer = createMockFencer(1, 100);
        when(eventRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityDoesNotExistException.class, () -> {
            chatbotService.getProjectedPointsEarned(1, fencer);
        });
    }

    private Fencer createMockFencer(int id, int points) {
        Fencer fencer = new Fencer();
        fencer.setId(id);
        fencer.setName("Fencer " + id);
        fencer.setEmail("fencer" + id + "@example.com");
        fencer.setContactNo("123456789" + id);
        fencer.setCountry("Country " + id);
        fencer.setDateOfBirth(LocalDate.of(1990, 1, 1));
        fencer.setDominantArm('R');
        fencer.setWeapon('F');
        fencer.setClub("Club " + id);
        fencer.setPoints(points);
        fencer.setDebutYear(2008 + id);
        fencer.setGender('M');
        return fencer;
    }
    @Test
    void getWinrate_EventDoesNotExist() {
        // Arrange
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

        when(eventRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityDoesNotExistException.class, () -> {
            chatbotService.getWinrate(1, fencer);
        });
    }
}