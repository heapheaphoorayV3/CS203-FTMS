package cs203.ftms.overall;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;

import cs203.ftms.overall.dto.AuthenticationDTO;
import cs203.ftms.overall.dto.CompleteFencerProfileDTO;
import cs203.ftms.overall.dto.CreateEventDTO;
import cs203.ftms.overall.dto.CreatePoulesDTO;
import cs203.ftms.overall.dto.CreateTournamentDTO;
import cs203.ftms.overall.dto.DirectEliminationBracketDTO;
import cs203.ftms.overall.dto.JwtDTO;
import cs203.ftms.overall.dto.PouleTableDTO;
import cs203.ftms.overall.dto.RegisterFencerDTO;
import cs203.ftms.overall.dto.RegisterOrganiserDTO;
import cs203.ftms.overall.dto.SinglePouleTableDTO;
import cs203.ftms.overall.dto.UpdateDirectEliminationMatchDTO;
import cs203.ftms.overall.model.tournamentrelated.DirectEliminationMatch;
import cs203.ftms.overall.model.tournamentrelated.Event;
import cs203.ftms.overall.model.tournamentrelated.Tournament;
import cs203.ftms.overall.model.userrelated.Fencer;
import cs203.ftms.overall.model.userrelated.Organiser;
import cs203.ftms.overall.model.userrelated.User;
import cs203.ftms.overall.repository.tournamentrelated.DirectEliminationMatchRepository;
import cs203.ftms.overall.repository.tournamentrelated.EventRepository;
import cs203.ftms.overall.repository.tournamentrelated.MatchRepository;
import cs203.ftms.overall.repository.tournamentrelated.TournamentFencerRepository;
import cs203.ftms.overall.repository.tournamentrelated.TournamentRepository;
import cs203.ftms.overall.repository.userrelated.FencerRepository;
import cs203.ftms.overall.repository.userrelated.UserRepository;
import cs203.ftms.overall.security.repository.RefreshTokenRepository;
import cs203.ftms.overall.security.service.JwtService;
import cs203.ftms.overall.service.fencer.FencerService;

/**
 * Integration test suite for Spring Boot application.
 * Tests end-to-end functionality with a real database connection.
 * 
 * Features tested:
 * - REST endpoints
 * - Database operations
 * - Service layer integration
 * - Authentication and authorization
 * - Data persistence
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class SpringBootIntegrationTest {

    /**
     * Random port assigned by Spring Boot for testing.
     */
    @LocalServerPort
    private int port;

    /**
     * Base URL for REST endpoints.
     */
    private final String baseUrl = "http://localhost:";

    /**
     * REST template for making HTTP requests in tests.
     */
    @Autowired
    private TestRestTemplate restTemplate;

    /**
     * Repository dependencies for database operations.
     * Each repository handles specific entity CRUD operations.
     */
    @Autowired
    private TournamentRepository tournaments;

    @Autowired
    private TournamentFencerRepository tournamentFencers;

    @Autowired
    private UserRepository users;

    @Autowired
    private RefreshTokenRepository refresh;

    @Autowired
    private FencerRepository fencers;

    @Autowired
    private EventRepository events;

    @Autowired
    private MatchRepository matches;

    @Autowired
    private DirectEliminationMatchRepository directEliminationMatches;

    /**
     * Service layer dependencies for business logic.
     */
    @Autowired
    private FencerService fencerService;

    @Autowired
    private JwtService jwtService;

    /**
     * JDBC template for direct database operations.
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Sets up test environment before each test.
     * 
     * Performs:
     * - Disables foreign key checks temporarily
     * - Cleans all database tables in correct order (child to parent)
     * - Re-enables foreign key checks
     * 
     * Tables cleared:
     * - fencing_match
     * - tournament_fencer
     * - poule
     * - event
     * - user
     * - tournament_fencer_matches
     * - refresh_token
     * - tournament
     * 
     * Error handling:
     * - Catches and logs any database operation exceptions
     */    
    @BeforeEach
    void setUp() {
         try {
            jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0");
            
            // Clear tables in correct order (child to parent)
            jdbcTemplate.execute("DELETE FROM fencing_match");
            jdbcTemplate.execute("DELETE FROM tournament_fencer");
            jdbcTemplate.execute("DELETE FROM poule");
            jdbcTemplate.execute("DELETE FROM event");
            jdbcTemplate.execute("DELETE FROM user");
            jdbcTemplate.execute("DELETE FROM tournament_fencer_matches");
            jdbcTemplate.execute("DELETE FROM refresh_token");
            jdbcTemplate.execute("DELETE FROM tournament");
            // Add other tables as needed
            
            jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Cleans up test environment after each test.
     * 
     * Performs:
     * - Disables foreign key checks temporarily
     * - Removes all test data from tables in correct order
     * - Re-enables foreign key checks
     * 
     * Ensures:
     * - Clean state for next test
     * - No orphaned data
     * - Database integrity
     * 
     * Error handling:
     * - Catches and logs any database operation exceptions
     */
    @AfterEach
    void tearDown() {
         try {
            jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0");
            
            // Clear tables in correct order (child to parent)
            jdbcTemplate.execute("DELETE FROM fencing_match");
            jdbcTemplate.execute("DELETE FROM tournament_fencer");
            jdbcTemplate.execute("DELETE FROM poule");
            jdbcTemplate.execute("DELETE FROM event");
            jdbcTemplate.execute("DELETE FROM user");
            jdbcTemplate.execute("DELETE FROM tournament_fencer_matches");
            jdbcTemplate.execute("DELETE FROM refresh_token");
            jdbcTemplate.execute("DELETE FROM tournament");
            // Add other tables as needed
            
            jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates and authenticates an organiser user for testing purposes.
     * 
     * Process:
     * 1. Creates a new organiser with test credentials
     * 2. Registers the organiser through the API endpoint
     * 3. Retrieves the created user and sets verification status
     * 4. Saves the verified organiser
     *
     * Test data:
     * - Name: "Organizer One"
     * - Email: "organizer.one@example.com"
     * - Phone: "+6591969123"
     * - Password: "Abcd1234!"
     * - Country: "Singapore"
     *
     * @throws Exception if registration or authentication fails
     */
    public void createAndAuthOrganiser() throws Exception {
        RegisterOrganiserDTO registerOrganiserDTO = new RegisterOrganiserDTO(
                "Organizer One",
                "organizer.one@example.com",
                "+6591969123",
                "Abcd1234!",
                "Singapore");

        HttpEntity<RegisterOrganiserDTO> regOrgDTOEntity = new HttpEntity<>(registerOrganiserDTO);
        restTemplate.postForEntity(new URI(baseUrl + port + "/api/v1/auth/register-organiser"), regOrgDTOEntity,
                String.class);

        User u = users.findByEmail("organizer.one@example.com").orElse(null);
        ((Organiser) u).setVerified(true);
        users.save(u);
    }

    /**
 * Creates and authenticates multiple fencer users for testing.
 * 
 * Process for each fencer:
 * 1. Creates fencer with incremental identification
 * 2. Registers through API endpoint
 * 3. Sets points based on index
 * 4. Completes fencer profile with default values
 *
 * @param count number of fencer accounts to create
 * 
 * Profile defaults:
 * - Hand: Right ('R')
 * - Style: Sabre ('S')
 * - Gender: Male ('M')
 * - Club: "Best Club"
 * - Start Year: 2010
 * 
 * @throws Exception if registration or profile completion fails
 */
    public void createAndAuthFencer(int count) throws Exception {
        for (int i = 1; i <= count; i++) {
            RegisterFencerDTO registerFencerDTO = new RegisterFencerDTO(
                    "Fencer" + i,
                    "" + i,
                    "fencer" + i + "@example.com",
                    "Abcd1234!",
                    "+6591569123",
                    "Singapore",
                    LocalDate.of(1990, 1, 1));
                

            HttpEntity<RegisterFencerDTO> regFencerDTOEntity = new HttpEntity<>(registerFencerDTO);
            restTemplate.postForEntity(new URI(baseUrl + port + "/api/v1/auth/register-fencer"), regFencerDTOEntity,
                    String.class);

            Fencer f = (Fencer) users.findByEmail("fencer" + i + "@example.com").orElse(null);
			f.setPoints(i * 100);

            CompleteFencerProfileDTO dto = new CompleteFencerProfileDTO('R', 'S', 'M', "Best Club", 2010);
            fencerService.completeProfile((Fencer) f, dto);
        }
    }

    /**
 * Creates a test tournament with default values.
 * 
 * Process:
 * 1. Creates and authenticates organiser
 * 2. Generates JWT token for authorization
 * 3. Creates tournament through API endpoint
 *
 * Tournament details:
 * - Name: "National Tournament"
 * - Signup End: December 18, 2024
 * - Start Date: December 20, 2024
 * - End Date: December 30, 2024
 * - Advancement Rate: 100%
 * - Difficulty: Beginner ('B')
 *
 * @throws Exception if tournament creation fails
 */
    private void createTournament() throws Exception {
        URI uri = new URI(baseUrl + port + "/api/v1/tournament/create-tournament");

        createAndAuthOrganiser();
        Organiser o = (Organiser) users.findByEmail("organizer.one@example.com").orElse(null);
        String jwtToken = jwtService.generateToken(o);

        CreateTournamentDTO request = new CreateTournamentDTO(
                "National Tournament",
                LocalDate.of(2024, 12, 18),
                100,
                LocalDate.of(2024, 12, 20),
                LocalDate.of(2024, 12, 30),
                "location",
                "description",
                "rules",
                'B');

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CreateTournamentDTO> entity = new HttpEntity<>(request, headers);

        restTemplate.postForEntity(uri, entity, String.class);
    }

    /**
 * Creates multiple test events for a tournament.
 * 
 * Process:
 * 1. Creates tournament if not exists
 * 2. Authenticates organiser
 * 3. Creates three events with different categories
 *
 * Events created:
 * 1. Men's Sabre (M/S)
 * 2. Men's Foil (M/F)
 * 3. Women's Epee (W/E)
 *
 * Common event details:
 * - Maximum participants: 8
 * - Date: December 21, 2024
 * - Time: 10:00 - 18:00
 *
 * @throws Exception if event creation fails
 */
    private void createEvent() throws Exception {
        createTournament();
        Organiser o = (Organiser) users.findByEmail("organizer.one@example.com").orElse(null);
        String jwtToken = jwtService.generateToken(o);

        URI uri = new URI(baseUrl + port + "/api/v1/event/create-event/" + tournaments.findAll().get(0).getId());

        List<CreateEventDTO> request = new ArrayList<>();

        request.add(new CreateEventDTO(
                'M',
                'S',
                8,
                LocalDate.of(2024, 12, 21),
                LocalTime.of(10, 0),
                LocalTime.of(18, 0)));
        request.add(new CreateEventDTO(
                'M',
                'F',
                8,
                LocalDate.of(2024, 12, 21),
                LocalTime.of(10, 0),
                LocalTime.of(18, 0)));
        request.add(new CreateEventDTO(
                'W',
                'E',
                8,
                LocalDate.of(2024, 12, 21),
                LocalTime.of(10, 0),
                LocalTime.of(18, 0)));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<List<CreateEventDTO>> entity = new HttpEntity<>(request, headers);

        restTemplate.postForEntity(uri, entity, String.class);
    }

    /**
 * Registers multiple fencers for an event.
 * 
 * Process:
 * 1. Creates event and fencers if not exists
 * 2. Registers 10 fencers to the first event
 * 3. Authenticates each fencer individually
 * 4. Makes PUT request to register endpoint for each fencer
 *
 * Dependencies:
 * - Requires createEvent() to be successful
 * - Requires createAndAuthFencer() to create 10 fencers
 * - Requires valid JWT token generation
 *
 * @throws Exception if registration process fails
 */
    public void registerEvent() throws Exception {
        createEvent();
        createAndAuthFencer(10);
        for (int i = 1; i <= 10; i++) {
            Fencer f = (Fencer) users.findByEmail("fencer" + i + "@example.com").orElse(null);
            String jwtToken = jwtService.generateToken(f);

            URI uri = new URI(baseUrl + port + "/api/v1/event/register/" + events.findAll().get(0).getId());

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + jwtToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            restTemplate.exchange(uri, HttpMethod.PUT, new HttpEntity<>(headers), String.class);
        }
    }

    /**
 * Creates poules for an event with registered fencers.
 * 
 * Process:
 * 1. Ensures fencers are registered
 * 2. Updates tournament signup end date to past date
 * 3. Authenticates organiser
 * 4. Creates 2 poules through API endpoint
 *
 * Configuration:
 * - Sets signup end date to 2024-11-11
 * - Creates 2 poules for the event
 * - Requires organiser authentication
 *
 * @throws Exception if poule creation fails
 */
	public void createPoule() throws Exception {
		registerEvent();

        // Change signup end date to be in the past for test to work
        Tournament t = tournaments.findAll().get(0);
        t.setSignupEndDate(LocalDate.of(2024, 11, 11));
        tournaments.save(t);

		Organiser o = (Organiser) users.findByEmail("organizer.one@example.com").orElse(null);
		String jwtToken = jwtService.generateToken(o);

		URI uri = new URI(baseUrl + port + "/api/v1/poule/create-poules/" + events.findAll().get(0).getId());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        CreatePoulesDTO request = new CreatePoulesDTO();
        request.setPouleCount(2);

        HttpEntity<CreatePoulesDTO> entity = new HttpEntity<>(request, headers);
		restTemplate.postForEntity(uri, entity, PouleTableDTO.class);
	}

    /**
 * Updates poule tables with match results.
 * 
 * Process:
 * 1. Creates poules if not exists
 * 2. Authenticates organiser
 * 3. Updates two poule tables with match results
 *
 * Poule structure:
 * - Two poules with 5 fencers each
 * - Results matrix for each poule
 * - Scores formatted as "wins,touches"
 * 
 * Score format:
 * - "-1" indicates self-match (diagonal)
 * - "x,y" where x=wins (0/1) and y=touches scored
 * 
 * @throws Exception if poule table update fails
 */
	public void updatePouleTable() throws Exception {
		createPoule();
		Organiser o = (Organiser) users.findByEmail("organizer.one@example.com").orElse(null);
		String jwtToken = jwtService.generateToken(o);

		URI uri = new URI(baseUrl + port + "/api/v1/poule/update-poule-table/" + events.findAll().get(0).getId());

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + jwtToken);
		headers.setContentType(MediaType.APPLICATION_JSON);

		Map<String, String> singleTable1 = new LinkedHashMap<>();
		singleTable1.put("10 Fencer10 (Singapore) -- " + tournamentFencers.findAll().get(9).getId(), "-1,5,5,5,5");
		singleTable1.put("8 Fencer8 (Singapore) -- " + tournamentFencers.findAll().get(7).getId(), "0,-1,5,5,5");
		singleTable1.put("6 Fencer6 (Singapore) -- " + tournamentFencers.findAll().get(5).getId(), "0,2,-1,5,5");
		singleTable1.put("4 Fencer4 (Singapore) -- " + tournamentFencers.findAll().get(3).getId(), "0,3,2,-1,5");
		singleTable1.put("2 Fencer2 (Singapore) -- " + tournamentFencers.findAll().get(1).getId(), "0,3,1,3,-1");
		SinglePouleTableDTO request1 = new SinglePouleTableDTO(1, singleTable1);

		HttpEntity<SinglePouleTableDTO> entity1 = new HttpEntity<>(request1, headers);
		restTemplate.exchange(uri, HttpMethod.PUT, entity1, String.class);

		Map<String, String> singleTable2 = new LinkedHashMap<>();
		singleTable2.put("9 Fencer9 (Singapore) -- " + tournamentFencers.findAll().get(8).getId(), "-1,5,5,5,5");
		singleTable2.put("7 Fencer7 (Singapore) -- " + tournamentFencers.findAll().get(6).getId(), "0,-1,5,5,5");
		singleTable2.put("5 Fencer5 (Singapore) -- " + tournamentFencers.findAll().get(4).getId(), "0,2,-1,5,5");
		singleTable2.put("3 Fencer3 (Singapore) -- " + tournamentFencers.findAll().get(2).getId(), "0,3,2,-1,5");
		singleTable2.put("1 Fencer1 (Singapore) -- " + tournamentFencers.findAll().get(0).getId(), "0,3,1,3,-1");
		SinglePouleTableDTO request2 = new SinglePouleTableDTO(2, singleTable2);

		HttpEntity<SinglePouleTableDTO> entity2 = new HttpEntity<>(request2, headers);
		restTemplate.exchange(uri, HttpMethod.PUT, entity2, String.class);
	}

    /**
 * Creates direct elimination matches after poule completion.
 * 
 * Process:
 * 1. Ensures poule tables are updated
 * 2. Authenticates organiser
 * 3. Creates direct elimination bracket
 *
 * Dependencies:
 * - Requires completed poule phase
 * - Requires valid organiser authentication
 * - Requires updated poule results
 *
 * @throws Exception if direct elimination creation fails
 */
	public void createDirectEliminationMatches() throws Exception {
		updatePouleTable();
		Organiser o = (Organiser) users.findByEmail("organizer.one@example.com").orElse(null);
		String jwtToken = jwtService.generateToken(o);

		URI uri = new URI(baseUrl + port + "/api/v1/direct-elimination/create-direct-elimination-matches/" + events.findAll().get(0).getId());

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + jwtToken);
		headers.setContentType(MediaType.APPLICATION_JSON);

        restTemplate.exchange(uri, HttpMethod.POST, new HttpEntity<>(headers), new ParameterizedTypeReference<List<DirectEliminationBracketDTO>>() {});
	}

    /**
 * Updates direct elimination matches with results.
 * 
 * Process:
 * 1. Creates direct elimination matches if not exists
 * 2. Authenticates organiser
 * 3. Updates multiple matches in sequence:
 *    - Updates match with ID from index 12
 *    - Updates match with ID from index 8
 *    - Updates remaining matches in reverse order
 *
 * Match scores:
 * - Winner score: 15 points
 * - Loser score: 10 points
 * 
 * @throws Exception if match updates fail
 */
    public void updateDirectEliminationMatch() throws Exception {
        createDirectEliminationMatches();
        Organiser o = (Organiser) users.findByEmail("organizer.one@example.com").orElse(null);
		String jwtToken = jwtService.generateToken(o);

        URI uri = new URI(baseUrl + port + "/api/v1/direct-elimination/update-direct-elimination-match/" + events.findAll().get(0).getId());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        int match1Id = directEliminationMatches.findAll().get(12).getId();
        UpdateDirectEliminationMatchDTO request1 = new UpdateDirectEliminationMatchDTO(match1Id, 15, 10);

        HttpEntity<UpdateDirectEliminationMatchDTO> entity = new HttpEntity<>(request1, headers);
        restTemplate.exchange(uri, HttpMethod.PUT, entity, String.class);

        int match2Id = directEliminationMatches.findAll().get(8).getId();
        UpdateDirectEliminationMatchDTO request2 = new UpdateDirectEliminationMatchDTO(match2Id, 15, 10);

        HttpEntity<UpdateDirectEliminationMatchDTO> entity2 = new HttpEntity<>(request2, headers);
        restTemplate.exchange(uri, HttpMethod.PUT, entity2, String.class);

        for (int i = directEliminationMatches.findAll().get(6).getId(); i >= directEliminationMatches.findAll().get(0).getId(); i--) {
            UpdateDirectEliminationMatchDTO request = new UpdateDirectEliminationMatchDTO(i, 15, 10);

            HttpEntity<UpdateDirectEliminationMatchDTO> entity3 = new HttpEntity<>(request, headers);
            restTemplate.exchange(uri, HttpMethod.PUT, entity3, String.class);
        }
    }

    /**
 * Tests successful organiser registration with valid data.
 * 
 * Test data:
 * - Name: "Organizer One"
 * - Email: "organizer.one@example.com"
 * - Phone: "+6591969123"
 * - Password: "Abcd1234!"
 * - Country: "Singapore"
 *
 * Expected outcome:
 * - HTTP Status: 201 (Created)
 * 
 * @throws Exception if registration fails
 */
    @Test
    public void createOrganiser_Valid_Success() throws Exception {

        URI uri = new URI(baseUrl + port + "/api/v1/auth/register-organiser");
        RegisterOrganiserDTO registerOrganiserDTO = new RegisterOrganiserDTO(
                "Organizer One",
                "organizer.one@example.com",
                "+6591969123",
                "Abcd1234!",
                "Singapore");

        HttpEntity<RegisterOrganiserDTO> regOrgDTOEntity = new HttpEntity<>(registerOrganiserDTO);
        ResponseEntity<String> regOrgresult = restTemplate.postForEntity(uri, regOrgDTOEntity, String.class);

        assertEquals(201, regOrgresult.getStatusCode().value());
    }

    /**
 * Tests organiser registration failure with invalid data.
 * 
 * Invalid test data:
 * - Empty name
 * - Invalid email format: "organizer.one@e@xample."
 * - Simple password: "Abcd1234"
 * - Empty country
 *
 * Expected outcome:
 * - HTTP Status: 400 (Bad Request)
 * 
 * @throws Exception if test fails unexpectedly
 */
    @Test
    public void createOrganiser_Invalid_Failure() throws Exception {

        URI uri = new URI(baseUrl + port + "/api/v1/auth/register-organiser");
        RegisterOrganiserDTO registerOrganiserDTO = new RegisterOrganiserDTO(
                "",
                "organizer.one@e@xample.",
                "+6599999999",
                "Abcd1234",
                "");

        HttpEntity<RegisterOrganiserDTO> regOrgDTOEntity = new HttpEntity<>(registerOrganiserDTO);
        ResponseEntity<String> regOrgresult = restTemplate.postForEntity(uri, regOrgDTOEntity, String.class);

        assertEquals(400, regOrgresult.getStatusCode().value());
    }

    /**
 * Tests fencer registration failure with invalid data.
 * 
 * Invalid test data:
 * - Empty first name
 * - Empty last name
 * - Invalid email: "fencer.onexaom"
 * - Simple password: "Abcd1234"
 * - Future birth date: 2099-01-01
 * - Empty country
 *
 * Expected outcome:
 * - HTTP Status: 400 (Bad Request)
 * 
 * @throws Exception if test fails unexpectedly
 */
    @Test
    public void createFencer_Invalid_Failure() throws Exception {

        URI uri = new URI(baseUrl + port + "/api/v1/auth/register-fencer");
        RegisterFencerDTO registerFencerDTO = new RegisterFencerDTO(
                "",
                "",
                "fencer.onexaom",
                "Abcd1234",
                "+6599999999",
                "",
                LocalDate.of(2099, 1, 1));

        HttpEntity<RegisterFencerDTO> regOrgDTOEntity = new HttpEntity<>(registerFencerDTO);
        ResponseEntity<String> regOrgresult = restTemplate.postForEntity(uri, regOrgDTOEntity, String.class);

        assertEquals(400, regOrgresult.getStatusCode().value());
    }

    /**
 * Tests successful organiser authentication with correct credentials.
 * 
 * Process:
 * 1. Registers new organiser
 * 2. Sets organiser as verified
 * 3. Attempts authentication with correct credentials
 *
 * Expected outcomes:
 * - HTTP Status: 200 (OK)
 * - Valid JWT token returned
 * 
 * @throws Exception if authentication process fails
 */
    @Test
    public void authenticateOrganiser_CorrectCredentials_Success() throws Exception {
        RegisterOrganiserDTO registerOrganiserDTO = new RegisterOrganiserDTO(
                "Organizer One",
                "organizer.one@example.com",
                "+6591969123",
                "Abcd1234!",
                "Singapore");

        HttpEntity<RegisterOrganiserDTO> regOrgDTOEntity = new HttpEntity<>(registerOrganiserDTO);
        restTemplate.postForEntity(new URI(baseUrl + port + "/api/v1/auth/register-organiser"), regOrgDTOEntity,
                String.class);

        User u = users.findByEmail("organizer.one@example.com").orElse(null);
        ((Organiser) u).setVerified(true);
        users.save(u);

        URI uri = new URI(baseUrl + port + "/api/v1/auth/login");
        AuthenticationDTO authenticationDTO = new AuthenticationDTO("organizer.one@example.com", "Abcd1234!");
        HttpEntity<AuthenticationDTO> authDTOEntity = new HttpEntity<>(authenticationDTO);
        ResponseEntity<JwtDTO> result = restTemplate.postForEntity(uri, authDTOEntity, JwtDTO.class);
        String token = result.getBody().getToken();

        assertEquals(200, result.getStatusCode().value());
        assertNotEquals(null, token);
    }

    /**
 * Tests organiser authentication failure with incorrect credentials.
 * 
 * Process:
 * 1. Registers new organiser with valid credentials
 * 2. Attempts authentication with empty credentials
 * 
 * Test data:
 * - Valid registration data
 * - Empty authentication credentials
 *
 * Expected outcomes:
 * - HTTP Status: 400 (Bad Request)
 * - Null JWT token
 * 
 * @throws Exception if test execution fails
 */
    @Test
    public void authenticateOrganiser_IncorrectCredentials_Failure() throws Exception {
        RegisterOrganiserDTO registerOrganiserDTO = new RegisterOrganiserDTO(
                "Organizer One",
                "organizer.one@example.com",
                "+6591969123",
                "Abcd1234!",
                "Singapore");

        HttpEntity<RegisterOrganiserDTO> regOrgDTOEntity = new HttpEntity<>(registerOrganiserDTO);
        restTemplate.postForEntity(new URI(baseUrl + port + "/api/v1/auth/register-organiser"), regOrgDTOEntity,
                String.class);

        URI uri = new URI(baseUrl + port + "/api/v1/auth/login");
        AuthenticationDTO authenticationDTO = new AuthenticationDTO("", "");
        HttpEntity<AuthenticationDTO> authDTOEntity = new HttpEntity<>(authenticationDTO);
        ResponseEntity<JwtDTO> result = restTemplate.postForEntity(uri, authDTOEntity, JwtDTO.class);
        String token = result.getBody().getToken();

        assertEquals(400, result.getStatusCode().value());
        assertNull(token);
    }

    /**
 * Tests successful tournament creation with valid data.
 * 
 * Process:
 * 1. Creates and authenticates organiser
 * 2. Generates JWT token
 * 3. Creates tournament with valid data
 *
 * Tournament data:
 * - Name: "National Tournament"
 * - Signup End: 2024-12-18
 * - Start Date: 2024-12-20
 * - End Date: 2024-12-30
 * - Advancement Rate: 80%
 * - Difficulty: Beginner ('B')
 *
 * Expected outcome:
 * - HTTP Status: 201 (Created)
 * 
 * @throws Exception if tournament creation fails
 */
    @Test
    public void createTournament_ValidTournament_Success() throws Exception {
        URI uri = new URI(baseUrl + port + "/api/v1/tournament/create-tournament");

        createAndAuthOrganiser();
        Organiser o = (Organiser) users.findByEmail("organizer.one@example.com").orElse(null);
        String jwtToken = jwtService.generateToken(o);

        CreateTournamentDTO request = new CreateTournamentDTO(
                "National Tournament",
                LocalDate.of(2024, 12, 18),
                80,
                LocalDate.of(2024, 12, 20),
                LocalDate.of(2024, 12, 30),
                "location",
                "description",
                "rules",
                'B');

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CreateTournamentDTO> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> result = restTemplate.postForEntity(uri, entity, String.class);

        assertEquals(201, result.getStatusCode().value());
    }

    /**
 * Tests tournament creation failure with invalid data.
 * 
 * Process:
 * 1. Creates and authenticates organiser
 * 2. Attempts to create tournament with invalid data
 *
 * Invalid data:
 * - Empty name
 * - Invalid advancement rate (59%)
 * - Past dates (2023)
 * - Empty location/description/rules
 * - Invalid difficulty ('S')
 *
 * Expected outcome:
 * - HTTP Status: 400 (Bad Request)
 * 
 * @throws Exception if test execution fails
 */
    @Test
    public void createTournament_Invalid_Failure() throws Exception {
        URI uri = new URI(baseUrl + port + "/api/v1/tournament/create-tournament");

        createAndAuthOrganiser();
        Organiser o = (Organiser) users.findByEmail("organizer.one@example.com").orElse(null);
        String jwtToken = jwtService.generateToken(o);

        CreateTournamentDTO request = new CreateTournamentDTO(
                "",
                LocalDate.of(2024, 12, 21),
                59,
                LocalDate.of(2023, 12, 20),
                LocalDate.of(2023, 11, 30),
                "",
                "",
                "",
                'S');

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CreateTournamentDTO> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> result = restTemplate.postForEntity(uri, entity, String.class);

        assertEquals(400, result.getStatusCode().value());
    }

    /**
 * Tests successful event creation with valid data.
 * 
 * Process:
 * 1. Creates tournament
 * 2. Authenticates organiser
 * 3. Creates multiple events
 *
 * Event data:
 * - Men's Sabre (M/S)
 * - Maximum participants: 64
 * - Date: 2024-12-21
 * - Time: [Incomplete in provided code]
 *
 * Dependencies:
 * - Requires valid tournament
 * - Requires authenticated organiser
 *
 * Expected outcome:
 * - HTTP Status: 201 (Created)
 * 
 * @throws Exception if event creation fails
 */
    @Test
    public void createEvent_ValidEvent_Success() throws Exception {
        createTournament();
        Organiser o = (Organiser) users.findByEmail("organizer.one@example.com").orElse(null);
        String jwtToken = jwtService.generateToken(o);

        URI uri = new URI(baseUrl + port + "/api/v1/event/create-event/" + tournaments.findAll().get(0).getId());

        List<CreateEventDTO> request = new ArrayList<>();

        request.add(new CreateEventDTO(
                'M',
                'S',
                64,
                LocalDate.of(2024, 12, 21),
                LocalTime.of(10, 0),
                LocalTime.of(18, 0)));
        request.add(new CreateEventDTO(
                'M',
                'F',
                64,
                LocalDate.of(2024, 12, 21),
                LocalTime.of(10, 0),
                LocalTime.of(18, 0)));
        request.add(new CreateEventDTO(
                'W',
                'E',
                64,
                LocalDate.of(2024, 12, 21),
                LocalTime.of(10, 0),
                LocalTime.of(18, 0)));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<List<CreateEventDTO>> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> result = restTemplate.postForEntity(uri, entity, String.class);

        assertEquals(201, result.getStatusCode().value());
    }

    /**
 * Tests event creation failure with invalid event data.
 * 
 * Process:
 * 1. Creates tournament
 * 2. Attempts to create events with invalid dates
 *
 * Invalid test cases:
 * 1. Event date after tournament end (2025-12-21)
 * 2. Event date before tournament start (2023-12-21)
 * 3. Invalid gender/weapon combination (F/E)
 *
 * Event details:
 * - Capacity: 64 participants
 * - Time: 10:00 - 18:00
 * 
 * Expected outcome:
 * - HTTP Status: 400 (Bad Request)
 *
 * @throws Exception if test execution fails
 */
    @Test
    public void createEvent_InvalidEvent_Failure() throws Exception {
        createTournament();
        Organiser o = (Organiser) users.findByEmail("organizer.one@example.com").orElse(null);
        String jwtToken = jwtService.generateToken(o);

        URI uri = new URI(baseUrl + port + "/api/v1/event/create-event/" + tournaments.findAll().get(0).getId());

        List<CreateEventDTO> request = new ArrayList<>();

        request.add(new CreateEventDTO(
                'M',
                'S',
                64,
                LocalDate.of(2025, 12, 21),
                LocalTime.of(10, 0),
                LocalTime.of(18, 0)));
        request.add(new CreateEventDTO(
                'M',
                'F',
                64,
                LocalDate.of(2023, 12, 21),
                LocalTime.of(10, 0),
                LocalTime.of(18, 0)));
        request.add(new CreateEventDTO(
                'F',
                'E',
                64,
                LocalDate.of(2023, 12, 21),
                LocalTime.of(10, 0),
                LocalTime.of(18, 0)));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<List<CreateEventDTO>> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> result = restTemplate.postForEntity(uri, entity, String.class);

        assertEquals(400, result.getStatusCode().value());
    }

    /**
 * Tests successful event registration for a fencer.
 * 
 * Process:
 * 1. Creates event
 * 2. Creates and authenticates fencer
 * 3. Registers fencer for event
 *
 * Dependencies:
 * - Requires valid event
 * - Requires authenticated fencer
 * - Requires matching weapon category
 *
 * Expected outcome:
 * - HTTP Status: 200 (OK)
 * 
 * @throws Exception if registration fails
 */
    @Test
    public void registerEvent_ValidEvent_Success() throws Exception {
        createEvent();
        createAndAuthFencer(1);
        Fencer f = (Fencer) users.findByEmail("fencer1@example.com").orElse(null);
        String jwtToken = jwtService.generateToken(f);

        URI uri = new URI(baseUrl + port + "/api/v1/event/register/" + events.findAll().get(0).getId());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.PUT, new HttpEntity<>(headers),
                String.class);
        assertEquals(200, result.getStatusCode().value());
    }

    /**
 * Tests event registration failure due to weapon mismatch.
 * 
 * Process:
 * 1. Creates event (Sabre)
 * 2. Creates fencer with Epee weapon
 * 3. Attempts registration
 *
 * Test data:
 * - Fencer profile: Right-handed, Epee, Male
 * - Event weapon: Sabre
 * - Registration year: 2010
 *
 * Expected outcomes:
 * - HTTP Status: 400 (Bad Request)
 * - Error message: "Fencer's weapon does not match the event's weapon!"
 *
 * Validation checks:
 * - Weapon compatibility
 * - Registration permissions
 * - Authentication validity
 * 
 * @throws Exception if test execution fails
 */
    @Test
    public void registerEvent_WrongWeapon_Failure() throws Exception {
        createEvent();
        RegisterFencerDTO registerFencerDTO = new RegisterFencerDTO(
                    "Fencer" + 11,
                    "" + 11,
                    "fencer" + 11 + "@example.com",
                    "Abcd1234!",
                    "+6591569123",
                    "Singapore",
                    LocalDate.of(1990, 1, 1));

        HttpEntity<RegisterFencerDTO> regFencerDTOEntity = new HttpEntity<>(registerFencerDTO);
        restTemplate.postForEntity(new URI(baseUrl + port + "/api/v1/auth/register-fencer"), regFencerDTOEntity,
                String.class);

                
        Fencer f = (Fencer) users.findByEmail("fencer11@example.com").orElse(null);
        String jwtToken = jwtService.generateToken(f);
        
        CompleteFencerProfileDTO dto = new CompleteFencerProfileDTO('R', 'E', 'M', "Best Club", 2010);
        fencerService.completeProfile((Fencer) f, dto);

        // fencer register for weapon different from his set weapon
        // fencer's weapon -> 'E' but event's weapon -> 'S'
        URI uri = new URI(baseUrl + port + "/api/v1/event/register/" + events.findAll().get(0).getId());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.PUT, new HttpEntity<>(headers),
                String.class);
        assertEquals(400, result.getStatusCode().value());
        assertEquals("Fencer's weapon does not match the event's weapon!", result.getBody());
    }

    /**
 * Tests successful creation of poules with valid configuration.
 * 
 * Process:
 * 1. Registers event and fencers
 * 2. Updates tournament signup end date to past
 * 3. Creates poules with authenticated organiser
 * 4. Verifies poule creation and structure
 *
 * Test configuration:
 * - 2 poules
 * - 5 fencers per poule
 * - Initial scores set to 0
 * - Diagonal entries set to -1 (self-matches)
 *
 * Expected outcomes:
 * - HTTP Status: 201 (Created)
 * - Correct poule structure
 * - Proper fencer distribution
 * - Initial score matrix
 * 
 * @throws Exception if poule creation fails
 */
    @Test
    public void createPoule_ValidPoule_Success() throws Exception {
        registerEvent();

        // Change signup end date to be in the past for test to work
        Tournament t = tournaments.findAll().get(0);
        t.setSignupEndDate(LocalDate.of(2024, 11, 11));
        tournaments.save(t);

        Organiser o = (Organiser) users.findByEmail("organizer.one@example.com").orElse(null);
        String jwtToken = jwtService.generateToken(o);
        
        URI uri = new URI(baseUrl + port + "/api/v1/poule/create-poules/" + events.findAll().get(0).getId());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        CreatePoulesDTO request = new CreatePoulesDTO();
        request.setPouleCount(2);

        HttpEntity<CreatePoulesDTO> entity = new HttpEntity<>(request, headers);

        ResponseEntity<PouleTableDTO> result = restTemplate.postForEntity(uri, entity, PouleTableDTO.class);

        List<Map<String, String>> pouleTableList = new ArrayList<>();

        Map<String, String> poule1 = new LinkedHashMap<>();
        poule1.put("10 Fencer10 (Singapore) -- " + tournamentFencers.findAll().get(9).getId(), "-1,0,0,0,0");
        poule1.put("8 Fencer8 (Singapore) -- " + tournamentFencers.findAll().get(7).getId(), "0,-1,0,0,0");
        poule1.put("6 Fencer6 (Singapore) -- " + tournamentFencers.findAll().get(5).getId(), "0,0,-1,0,0");
        poule1.put("4 Fencer4 (Singapore) -- " + tournamentFencers.findAll().get(3).getId(), "0,0,0,-1,0");
        poule1.put("2 Fencer2 (Singapore) -- " + tournamentFencers.findAll().get(1).getId(), "0,0,0,0,-1");
        pouleTableList.add(poule1);

		Map<String, String> poule2 = new LinkedHashMap<>();
        poule2.put("9 Fencer9 (Singapore) -- " + tournamentFencers.findAll().get(8).getId(), "-1,0,0,0,0");
        poule2.put("7 Fencer7 (Singapore) -- " + tournamentFencers.findAll().get(6).getId(), "0,-1,0,0,0");
        poule2.put("5 Fencer5 (Singapore) -- " + tournamentFencers.findAll().get(4).getId(), "0,0,-1,0,0");
        poule2.put("3 Fencer3 (Singapore) -- " + tournamentFencers.findAll().get(2).getId(), "0,0,0,-1,0");
        poule2.put("1 Fencer1 (Singapore) -- " + tournamentFencers.findAll().get(0).getId(), "0,0,0,0,-1");     
        pouleTableList.add(poule2);

        assertEquals(201, result.getStatusCode().value());
        assertEquals(pouleTableList, result.getBody().getPouleTable());
    }

    /**
 * Tests successful update of poule table scores.
 * 
 * Process:
 * 1. Creates initial poules
 * 2. Authenticates organiser
 * 3. Updates scores for both poules
 * 4. Verifies updates
 *
 * Score format:
 * - "-1": Self-match (diagonal)
 * - "x,y": Where x=victory (0/1) and y=touches scored
 * 
 * Test data for each poule:
 * - 5 fencers
 * - Complete match results
 * - Valid score range (0-5)
 * - Proper victory indicators
 *
 * Expected outcomes:
 * - HTTP Status: 200 (OK)
 * - Correct score updates
 * - Proper data structure
 * - Valid score matrix
 *
 * Validation checks:
 * - Score range
 * - Match completion
 * - Data consistency
 * - Authorization
 * 
 * @throws Exception if update operation fails
 */
	@Test
	public void updatePouleTable_validPouleTable_Success() throws Exception {
		createPoule();
		Organiser o = (Organiser) users.findByEmail("organizer.one@example.com").orElse(null);
        String jwtToken = jwtService.generateToken(o);

		URI uri = new URI(baseUrl + port + "/api/v1/poule/update-poule-table/" + events.findAll().get(0).getId());

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + jwtToken);
		headers.setContentType(MediaType.APPLICATION_JSON);

		Map<String, String> singleTable1 = new LinkedHashMap<>();
		singleTable1.put("10 Fencer10 (Singapore) -- " + tournamentFencers.findAll().get(9).getId(), "-1,5,5,5,5");
		singleTable1.put("8 Fencer8 (Singapore) -- " + tournamentFencers.findAll().get(7).getId(), "0,-1,5,5,5");
		singleTable1.put("6 Fencer6 (Singapore) -- " + tournamentFencers.findAll().get(5).getId(), "0,2,-1,5,5");
		singleTable1.put("4 Fencer4 (Singapore) -- " + tournamentFencers.findAll().get(3).getId(), "0,3,2,-1,5");
		singleTable1.put("2 Fencer2 (Singapore) -- " + tournamentFencers.findAll().get(1).getId(), "0,3,1,3,-1");
		SinglePouleTableDTO request1 = new SinglePouleTableDTO(1, singleTable1);

		HttpEntity<SinglePouleTableDTO> entity1 = new HttpEntity<>(request1, headers);
		ResponseEntity<String> result1 = restTemplate.exchange(uri, HttpMethod.PUT, entity1, String.class);

		assertEquals(200, result1.getStatusCode().value());

		Map<String, String> singleTable2 = new LinkedHashMap<>();
		singleTable2.put("9 Fencer9 (Singapore) -- " + tournamentFencers.findAll().get(8).getId(), "-1,5,5,5,5");
		singleTable2.put("7 Fencer7 (Singapore) -- " + tournamentFencers.findAll().get(6).getId(), "0,-1,5,5,5");
		singleTable2.put("5 Fencer5 (Singapore) -- " + tournamentFencers.findAll().get(4).getId(), "0,2,-1,5,5");
		singleTable2.put("3 Fencer3 (Singapore) -- " + tournamentFencers.findAll().get(2).getId(), "0,3,2,-1,5");
		singleTable2.put("1 Fencer1 (Singapore) -- " + tournamentFencers.findAll().get(0).getId(), "0,3,1,3,-1");
		SinglePouleTableDTO request2 = new SinglePouleTableDTO(2, singleTable2);

		HttpEntity<SinglePouleTableDTO> entity2 = new HttpEntity<>(request2, headers);
		ResponseEntity<String> result2 = restTemplate.exchange(uri, HttpMethod.PUT, entity2, String.class);

		assertEquals(200, result2.getStatusCode().value());

		URI uri2 = new URI(baseUrl + port + "/api/v1/poule/get-poule-table/" + events.findAll().get(0).getId());
		ResponseEntity<PouleTableDTO> result3 = restTemplate.exchange(uri2, HttpMethod.GET, new HttpEntity<>(headers),
				PouleTableDTO.class);
		
		List<Map<String, String>> pouleTableList = new ArrayList<>();

		Map<String, String> poule1 = new LinkedHashMap<>();
		poule1.put("10 Fencer10 (Singapore) -- " + tournamentFencers.findAll().get(9).getId(), "-1,5,5,5,5");
		poule1.put("8 Fencer8 (Singapore) -- " + tournamentFencers.findAll().get(7).getId(), "0,-1,5,5,5");
		poule1.put("6 Fencer6 (Singapore) -- " + tournamentFencers.findAll().get(5).getId(), "0,2,-1,5,5");
		poule1.put("4 Fencer4 (Singapore) -- " + tournamentFencers.findAll().get(3).getId(), "0,3,2,-1,5");
		poule1.put("2 Fencer2 (Singapore) -- " + tournamentFencers.findAll().get(1).getId(), "0,3,1,3,-1");
		pouleTableList.add(poule1);

		Map<String, String> poule2 = new LinkedHashMap<>();
		poule2.put("9 Fencer9 (Singapore) -- " + tournamentFencers.findAll().get(8).getId(), "-1,5,5,5,5");
		poule2.put("7 Fencer7 (Singapore) -- " + tournamentFencers.findAll().get(6).getId(), "0,-1,5,5,5");
		poule2.put("5 Fencer5 (Singapore) -- " + tournamentFencers.findAll().get(4).getId(), "0,2,-1,5,5");
		poule2.put("3 Fencer3 (Singapore) -- " + tournamentFencers.findAll().get(2).getId(), "0,3,2,-1,5");
		poule2.put("1 Fencer1 (Singapore) -- " + tournamentFencers.findAll().get(0).getId(), "0,3,1,3,-1");
		pouleTableList.add(poule2);

		assertEquals(pouleTableList, result3.getBody().getPouleTable());
	}

    /**
 * Tests poule table update failure with invalid score.
 * 
 * Process:
 * 1. Creates initial poules
 * 2. Attempts to update with invalid score (10)
 * 3. Verifies error response
 *
 * Invalid test data:
 * - Score of 10 (exceeds maximum of 5)
 * - Otherwise valid match structure
 * - Proper authentication
 *
 * Expected outcomes:
 * - HTTP Status: 400 (Bad Request)
 * - Error message: "The poule score must be an integer within 0 to 5."
 * 
 * @throws Exception if test execution fails
 */
	@Test
	public void updatePouleTable_InvalidPouleScore_Failure() throws Exception {
        createPoule();
		Organiser o = (Organiser) users.findByEmail("organizer.one@example.com").orElse(null);
        String jwtToken = jwtService.generateToken(o);

		URI uri = new URI(baseUrl + port + "/api/v1/poule/update-poule-table/" + events.findAll().get(0).getId());

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + jwtToken);
		headers.setContentType(MediaType.APPLICATION_JSON);

		Map<String, String> singleTable1 = new LinkedHashMap<>();
        // Invalid score (10 instead of 5 for fencer 10)
        // Max score in poules is 5
		singleTable1.put("10 Fencer10 (Singapore) -- " + tournamentFencers.findAll().get(9).getId(), "-1,10,5,5,5");
		singleTable1.put("8 Fencer8 (Singapore) -- " + tournamentFencers.findAll().get(7).getId(), "0,-1,5,5,5");
		singleTable1.put("6 Fencer6 (Singapore) -- " + tournamentFencers.findAll().get(5).getId(), "0,2,-1,5,5");
		singleTable1.put("4 Fencer4 (Singapore) -- " + tournamentFencers.findAll().get(3).getId(), "0,3,2,-1,5");
		singleTable1.put("2 Fencer2 (Singapore) -- " + tournamentFencers.findAll().get(1).getId(), "0,3,1,3,-1");
		SinglePouleTableDTO request1 = new SinglePouleTableDTO(1, singleTable1);

		HttpEntity<SinglePouleTableDTO> entity1 = new HttpEntity<>(request1, headers);
		ResponseEntity<String> result1 = restTemplate.exchange(uri, HttpMethod.PUT, entity1, String.class);

		assertEquals(400, result1.getStatusCode().value());
        assertEquals("{\"pouleScore\":\"The poule score must be an integer within 0 to 5.\"}", result1.getBody());
    }

    /**
 * Tests successful creation of direct elimination matches after valid poule completion.
 * 
 * Process:
 * 1. Completes poule phase
 * 2. Creates direct elimination bracket
 * 3. Verifies bracket structure
 *
 * Verification points:
 * - 15 total matches created
 * - Proper bracket naming (Finals, Top 16)
 * - Correct seeding of fencers
 * - Proper participant placement
 *
 * Expected outcomes:
 * - HTTP Status: 201 (Created)
 * - Correct bracket size and structure
 * - Proper seeding order
 * 
 * @throws Exception if bracket creation fails
 */
	@Test
	public void createDirectEliminationMatches_ValidPoule_Success() throws Exception {
		updatePouleTable();
		Organiser o = (Organiser) users.findByEmail("organizer.one@example.com").orElse(null);
        String jwtToken = jwtService.generateToken(o);

		URI uri = new URI(baseUrl + port + "/api/v1/direct-elimination/create-direct-elimination-matches/" + events.findAll().get(0).getId());

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + jwtToken);
		headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<List<DirectEliminationBracketDTO>> result = restTemplate.exchange(uri, HttpMethod.POST, new HttpEntity<>(headers), new ParameterizedTypeReference<List<DirectEliminationBracketDTO>>() {});

		assertEquals(201, result.getStatusCode().value());
		assertEquals(15, result.getBody().size());
		assertEquals("Finals", result.getBody().get(0).getName());
		assertEquals("Top 16", result.getBody().get(14).getName());
		assertEquals("5 Fencer5", result.getBody().get(14).getParticipants()[0].getName());
	}

    /**
 * Tests direct elimination creation failure when poules are incomplete.
 * 
 * Process:
 * 1. Creates poules but doesn't complete them
 * 2. Attempts to create direct elimination matches
 * 3. Verifies error response
 *
 * Expected outcomes:
 * - HTTP Status: 400 (Bad Request)
 * - Error message: "Poules not done!"
 * - No bracket creation
 *
 * Validates:
 * - Phase transition requirements
 * - Error handling
 * - Tournament flow integrity
 * 
 * @throws Exception if test execution fails
 */
	@Test
	public void createDirectEliminationMatches_PouleNotDone_Failure() throws Exception {
        createPoule();
        Organiser o = (Organiser) users.findByEmail("organizer.one@example.com").orElse(null);
        String jwtToken = jwtService.generateToken(o);

		URI uri = new URI(baseUrl + port + "/api/v1/direct-elimination/create-direct-elimination-matches/" + events.findAll().get(0).getId());

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + jwtToken);
		headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.POST, new HttpEntity<>(headers), String.class);
        assertEquals(400, result.getStatusCode().value());
        assertEquals("Poules not done!", result.getBody());
    }

    /**
     * Tests successful update of direct elimination match scores.
     * 
     * Process:
     * 1. Creates direct elimination bracket
     * 2. Updates match scores
     * 3. Verifies score updates and progression
     *
     * Test data:
     * - Match scores: 15-10
     * - Valid bracket position
     * - Proper authentication
     *
     * Expected outcomes:
     * - Correct score update
     * - Proper winner determination
     * - Valid progression tracking
     * 
     * @throws Exception if match update fails
     */
	@Test
	public void updateDirectEliminationMatch_ValidMatch_Success() throws Exception {
		createDirectEliminationMatches();
		Organiser o = (Organiser) users.findByEmail("organizer.one@example.com").orElse(null);
        String jwtToken = jwtService.generateToken(o);

		URI uri = new URI(baseUrl + port + "/api/v1/direct-elimination/update-direct-elimination-match/" + events.findAll().get(0).getId());

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + jwtToken);
		headers.setContentType(MediaType.APPLICATION_JSON);

		UpdateDirectEliminationMatchDTO request = new UpdateDirectEliminationMatchDTO(directEliminationMatches.findAll().get(12).getId(), 15, 10);
        
		HttpEntity<UpdateDirectEliminationMatchDTO> entity = new HttpEntity<>(request, headers);
		ResponseEntity<List<DirectEliminationBracketDTO>> result = restTemplate.exchange(uri, HttpMethod.PUT, entity, new ParameterizedTypeReference<List<DirectEliminationBracketDTO>>() {});
        
		assertEquals(201, result.getStatusCode().value());
        // Check if the match is updated properly (Scores and Fencers)
        DirectEliminationMatch match = directEliminationMatches.findAll().get(12);
        assertEquals(15, match.getScore1());
        assertEquals(10, match.getScore2());
        assertEquals("4 Fencer4", tournamentFencers.findById(match.getFencer1()).get().getFencer().getName());
        assertEquals("1 Fencer1", tournamentFencers.findById(match.getFencer2()).get().getFencer().getName());

	}

    /**
 * Tests direct elimination match update failure with invalid score.
 * 
 * Process:
 * 1. Creates direct elimination matches
 * 2. Attempts to update with invalid score (20)
 * 3. Verifies error response
 *
 * Invalid test data:
 * - Score of 20 (exceeds maximum of 15)
 * - Match ID from index 12
 * - Valid opponent score (10)
 *
 * Expected outcomes:
 * - HTTP Status: 400 (Bad Request)
 * - Error message: "Maximum points for a direct elimination match is 15"
 * 
 * @throws Exception if test execution fails
 */
    @Test
    public void updateDirectEliminationMatch_InvalidScore_Failure() throws Exception {
        createDirectEliminationMatches();
		Organiser o = (Organiser) users.findByEmail("organizer.one@example.com").orElse(null);
        String jwtToken = jwtService.generateToken(o);

		URI uri = new URI(baseUrl + port + "/api/v1/direct-elimination/update-direct-elimination-match/" + events.findAll().get(0).getId());

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + jwtToken);
		headers.setContentType(MediaType.APPLICATION_JSON);

        // Score is more than 15 (20)
		UpdateDirectEliminationMatchDTO request = new UpdateDirectEliminationMatchDTO(directEliminationMatches.findAll().get(12).getId(), 20, 10);
        
		HttpEntity<UpdateDirectEliminationMatchDTO> entity = new HttpEntity<>(request, headers);
		ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.PUT, entity, String.class);

		assertEquals(400, result.getStatusCode().value());
        assertEquals("{\"score1\":\"Maximum points for a direct elimination match is 15\"}", result.getBody());
    }

    /**
 * Tests successful event completion and point calculation.
 * 
 * Process:
 * 1. Completes all direct elimination matches
 * 2. Sets event date to past
 * 3. Ends event
 * 4. Verifies winner and point calculations
 *
 * Verification points:
 * - Event completion status
 * - Winner identification
 * - Point calculation accuracy
 * 
 * Point calculation:
 * - Initial points: 800 (Fencer8)
 * - Event points: 436
 * - Final points: 1236
 *
 * Expected outcomes:
 * - HTTP Status: 200 (OK)
 * - Event marked as over
 * - Correct winner ("8 Fencer8")
 * - Accurate point calculation
 * 
 * @throws Exception if event completion fails
 */
    @Test
    public void endEvent_ValidEvent_Success() throws Exception {
        updateDirectEliminationMatch();
        Organiser o = (Organiser) users.findByEmail("organizer.one@example.com").orElse(null);
        String jwtToken = jwtService.generateToken(o);

        // Change event date for testing purpose
        Event e = events.findAll().get(0);
        e.setDate(LocalDate.of(2024, 11, 11));
        events.save(e);

        URI uri = new URI(baseUrl + port + "/api/v1/event/end-event/" + events.findAll().get(0).getId());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.PUT, new HttpEntity<>(headers),
                String.class);
        
        assertEquals(200, result.getStatusCode().value());
        
        // Check event has ended
        assertEquals(true, events.findAll().get(0).isOver());
        
        // Check if winner of finals is correct
        int winnerId = directEliminationMatches.findAll().get(0).getWinner();
        String winnerName = tournamentFencers.findById(winnerId).get().getFencer().getName();
        assertEquals("8 Fencer8", winnerName);
        
        // Check if the amount of points after the event is correct for the winner
        // Initial points = 800 (for fencer 8)
        // New points = 800 + 436 = 1236
        assertEquals(1236, fencers.findByName("8 Fencer8").get().getPoints());
    }

    /**
 * Tests event completion failure when finals are not completed.
 * 
 * Process:
 * 1. Creates direct elimination matches
 * 2. Attempts to end event without completing finals
 * 3. Verifies error response
 *
 * Test conditions:
 * - Direct elimination bracket exists
 * - Finals not completed
 * - Valid authentication
 *
 * Expected outcomes:
 * - HTTP Status: 400 (Bad Request)
 * - Error message: "Event has not started yet!"
 * - Event remains active
 *
 * Validates:
 * - Event completion requirements
 * - Tournament phase validation
 * - Error handling
 * 
 * @throws Exception if test execution fails
 */
    @Test
    public void endEvent_FinalsNotDone_Failure() throws Exception {
        createDirectEliminationMatches();
        Organiser o = (Organiser) users.findByEmail("organizer.one@example.com").orElse(null);
        String jwtToken = jwtService.generateToken(o);

        URI uri = new URI(baseUrl + port + "/api/v1/event/end-event/" + events.findAll().get(0).getId());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.PUT, new HttpEntity<>(headers),
                String.class);
        
        assertEquals(400, result.getStatusCode().value());
        assertEquals("Event has not started yet!", result.getBody());        
    }
}
