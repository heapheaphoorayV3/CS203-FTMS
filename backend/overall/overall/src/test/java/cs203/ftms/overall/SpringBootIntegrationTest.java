package cs203.ftms.overall;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
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

import com.fasterxml.jackson.databind.ObjectMapper;

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

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
// @Transactional
class SpringBootIntegrationTest {

    @LocalServerPort
    private int port;

    private final String baseUrl = "http://localhost:";

    @Autowired
    private TestRestTemplate restTemplate;

    // repositories
    @Autowired
    private TournamentRepository tournaments;

    @Autowired
    private TournamentFencerRepository tournamentFencers;

    @Autowired
    private UserRepository users;

    @Autowired
    private RefreshTokenRepository refresh;

    // @Autowired
    // private OrganiserRepository organisers;

    @Autowired
    private FencerRepository fencers;

    @Autowired
    private EventRepository events;

    @Autowired
    private MatchRepository matches;

	@Autowired
	private DirectEliminationMatchRepository directEliminationMatches;

    @Autowired
    private FencerService fencerService;

    @Autowired
    private JwtService jwtService;

    // @PersistenceContext
    // private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        // jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0");
        // events.deleteAll();
        // matches.deleteAll();
        // tournamentFencers.deleteAll();
        // tournaments.deleteAll();
        // refresh.deleteAll();
        // users.deleteAll();
        // jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");

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

    @AfterEach
    void tearDown() {
        // jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0");
        // events.deleteAll();
        // matches.deleteAll();
        // tournamentFencers.deleteAll();
        // tournaments.deleteAll();
        // refresh.deleteAll();
        // users.deleteAll();
        // jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");

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

	public void createPoule() throws Exception {
		registerEvent();
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

    // @Test
    // public void createPoule_ValidPoule_Success() throws Exception {
    //     registerEvent();
    //     Organiser o = (Organiser) users.findByEmail("organizer.one@example.com").orElse(null);
    //     String jwtToken = jwtService.generateToken(o);
        
    //     URI uri = new URI(baseUrl + port + "/api/v1/poule/create-poules/" + events.findAll().get(0).getId());

    //     HttpHeaders headers = new HttpHeaders();
    //     headers.set("Authorization", "Bearer " + jwtToken);
    //     headers.setContentType(MediaType.APPLICATION_JSON);

    //     CreatePoulesDTO request = new CreatePoulesDTO();
    //     request.setPouleCount(2);

    //     HttpEntity<CreatePoulesDTO> entity = new HttpEntity<>(request, headers);

    //     ResponseEntity<PouleTableDTO> result = restTemplate.postForEntity(uri, entity, PouleTableDTO.class);

    //     List<Map<String, String>> pouleTableList = new ArrayList<>();

    //     Map<String, String> poule1 = new LinkedHashMap<>();
    //     poule1.put("10 Fencer10 (Singapore) -- " + tournamentFencers.findAll().get(9).getId(), "-1,0,0,0,0");
    //     poule1.put("8 Fencer8 (Singapore) -- " + tournamentFencers.findAll().get(7).getId(), "0,-1,0,0,0");
    //     poule1.put("6 Fencer6 (Singapore) -- " + tournamentFencers.findAll().get(5).getId(), "0,0,-1,0,0");
    //     poule1.put("4 Fencer4 (Singapore) -- " + tournamentFencers.findAll().get(3).getId(), "0,0,0,-1,0");
    //     poule1.put("2 Fencer2 (Singapore) -- " + tournamentFencers.findAll().get(1).getId(), "0,0,0,0,-1");
    //     pouleTableList.add(poule1);

	// 	Map<String, String> poule2 = new LinkedHashMap<>();
    //     poule2.put("9 Fencer9 (Singapore) -- " + tournamentFencers.findAll().get(8).getId(), "-1,0,0,0,0");
    //     poule2.put("7 Fencer7 (Singapore) -- " + tournamentFencers.findAll().get(6).getId(), "0,-1,0,0,0");
    //     poule2.put("5 Fencer5 (Singapore) -- " + tournamentFencers.findAll().get(4).getId(), "0,0,-1,0,0");
    //     poule2.put("3 Fencer3 (Singapore) -- " + tournamentFencers.findAll().get(2).getId(), "0,0,0,-1,0");
    //     poule2.put("1 Fencer1 (Singapore) -- " + tournamentFencers.findAll().get(0).getId(), "0,0,0,0,-1");     
    //     pouleTableList.add(poule2);

    //     assertEquals(201, result.getStatusCode().value());
    //     assertEquals(pouleTableList, result.getBody().getPouleTable());
    // }

	// @Test
	// public void updatePouleTable_validPouleTable_Success() throws Exception {
	// 	createPoule();
	// 	Organiser o = (Organiser) users.findByEmail("organizer.one@example.com").orElse(null);
    //     String jwtToken = jwtService.generateToken(o);

	// 	URI uri = new URI(baseUrl + port + "/api/v1/poule/update-poule-table/" + events.findAll().get(0).getId());

	// 	HttpHeaders headers = new HttpHeaders();
	// 	headers.set("Authorization", "Bearer " + jwtToken);
	// 	headers.setContentType(MediaType.APPLICATION_JSON);

	// 	Map<String, String> singleTable1 = new LinkedHashMap<>();
	// 	singleTable1.put("10 Fencer10 (Singapore) -- " + tournamentFencers.findAll().get(9).getId(), "-1,5,5,5,5");
	// 	singleTable1.put("8 Fencer8 (Singapore) -- " + tournamentFencers.findAll().get(7).getId(), "0,-1,5,5,5");
	// 	singleTable1.put("6 Fencer6 (Singapore) -- " + tournamentFencers.findAll().get(5).getId(), "0,2,-1,5,5");
	// 	singleTable1.put("4 Fencer4 (Singapore) -- " + tournamentFencers.findAll().get(3).getId(), "0,3,2,-1,5");
	// 	singleTable1.put("2 Fencer2 (Singapore) -- " + tournamentFencers.findAll().get(1).getId(), "0,3,1,3,-1");
	// 	SinglePouleTableDTO request1 = new SinglePouleTableDTO(1, singleTable1);

	// 	HttpEntity<SinglePouleTableDTO> entity1 = new HttpEntity<>(request1, headers);
	// 	ResponseEntity<String> result1 = restTemplate.exchange(uri, HttpMethod.PUT, entity1, String.class);

	// 	assertEquals(200, result1.getStatusCode().value());

	// 	Map<String, String> singleTable2 = new LinkedHashMap<>();
	// 	singleTable2.put("9 Fencer9 (Singapore) -- " + tournamentFencers.findAll().get(8).getId(), "-1,5,5,5,5");
	// 	singleTable2.put("7 Fencer7 (Singapore) -- " + tournamentFencers.findAll().get(6).getId(), "0,-1,5,5,5");
	// 	singleTable2.put("5 Fencer5 (Singapore) -- " + tournamentFencers.findAll().get(4).getId(), "0,2,-1,5,5");
	// 	singleTable2.put("3 Fencer3 (Singapore) -- " + tournamentFencers.findAll().get(2).getId(), "0,3,2,-1,5");
	// 	singleTable2.put("1 Fencer1 (Singapore) -- " + tournamentFencers.findAll().get(0).getId(), "0,3,1,3,-1");
	// 	SinglePouleTableDTO request2 = new SinglePouleTableDTO(2, singleTable2);

	// 	HttpEntity<SinglePouleTableDTO> entity2 = new HttpEntity<>(request2, headers);
	// 	ResponseEntity<String> result2 = restTemplate.exchange(uri, HttpMethod.PUT, entity2, String.class);

	// 	assertEquals(200, result2.getStatusCode().value());

	// 	URI uri2 = new URI(baseUrl + port + "/api/v1/poule/get-poule-table/" + events.findAll().get(0).getId());
	// 	ResponseEntity<PouleTableDTO> result3 = restTemplate.exchange(uri2, HttpMethod.GET, new HttpEntity<>(headers),
	// 			PouleTableDTO.class);
		
	// 	List<Map<String, String>> pouleTableList = new ArrayList<>();

	// 	Map<String, String> poule1 = new LinkedHashMap<>();
	// 	poule1.put("10 Fencer10 (Singapore) -- " + tournamentFencers.findAll().get(9).getId(), "-1,5,5,5,5");
	// 	poule1.put("8 Fencer8 (Singapore) -- " + tournamentFencers.findAll().get(7).getId(), "0,-1,5,5,5");
	// 	poule1.put("6 Fencer6 (Singapore) -- " + tournamentFencers.findAll().get(5).getId(), "0,2,-1,5,5");
	// 	poule1.put("4 Fencer4 (Singapore) -- " + tournamentFencers.findAll().get(3).getId(), "0,3,2,-1,5");
	// 	poule1.put("2 Fencer2 (Singapore) -- " + tournamentFencers.findAll().get(1).getId(), "0,3,1,3,-1");
	// 	pouleTableList.add(poule1);

	// 	Map<String, String> poule2 = new LinkedHashMap<>();
	// 	poule2.put("9 Fencer9 (Singapore) -- " + tournamentFencers.findAll().get(8).getId(), "-1,5,5,5,5");
	// 	poule2.put("7 Fencer7 (Singapore) -- " + tournamentFencers.findAll().get(6).getId(), "0,-1,5,5,5");
	// 	poule2.put("5 Fencer5 (Singapore) -- " + tournamentFencers.findAll().get(4).getId(), "0,2,-1,5,5");
	// 	poule2.put("3 Fencer3 (Singapore) -- " + tournamentFencers.findAll().get(2).getId(), "0,3,2,-1,5");
	// 	poule2.put("1 Fencer1 (Singapore) -- " + tournamentFencers.findAll().get(0).getId(), "0,3,1,3,-1");
	// 	pouleTableList.add(poule2);

	// 	assertEquals(pouleTableList, result3.getBody().getPouleTable());
	// }

	// @Test
	// public void updatePouleTable_InvalidPouleScore_Failure() throws Exception {
    //     createPoule();
	// 	Organiser o = (Organiser) users.findByEmail("organizer.one@example.com").orElse(null);
    //     String jwtToken = jwtService.generateToken(o);

	// 	URI uri = new URI(baseUrl + port + "/api/v1/poule/update-poule-table/" + events.findAll().get(0).getId());

	// 	HttpHeaders headers = new HttpHeaders();
	// 	headers.set("Authorization", "Bearer " + jwtToken);
	// 	headers.setContentType(MediaType.APPLICATION_JSON);

	// 	Map<String, String> singleTable1 = new LinkedHashMap<>();
    //     // Invalid score (10 instead of 5 for fencer 10)
    //     // Max score in poules is 5
	// 	singleTable1.put("10 Fencer10 (Singapore) -- " + tournamentFencers.findAll().get(9).getId(), "-1,10,5,5,5");
	// 	singleTable1.put("8 Fencer8 (Singapore) -- " + tournamentFencers.findAll().get(7).getId(), "0,-1,5,5,5");
	// 	singleTable1.put("6 Fencer6 (Singapore) -- " + tournamentFencers.findAll().get(5).getId(), "0,2,-1,5,5");
	// 	singleTable1.put("4 Fencer4 (Singapore) -- " + tournamentFencers.findAll().get(3).getId(), "0,3,2,-1,5");
	// 	singleTable1.put("2 Fencer2 (Singapore) -- " + tournamentFencers.findAll().get(1).getId(), "0,3,1,3,-1");
	// 	SinglePouleTableDTO request1 = new SinglePouleTableDTO(1, singleTable1);

	// 	HttpEntity<SinglePouleTableDTO> entity1 = new HttpEntity<>(request1, headers);
	// 	ResponseEntity<String> result1 = restTemplate.exchange(uri, HttpMethod.PUT, entity1, String.class);

	// 	assertEquals(400, result1.getStatusCode().value());
    //     assertEquals("{\"pouleScore\":\"The poule score must be an integer within 0 to 5.\"}", result1.getBody());
    // }

	// @Test
	// public void createDirectEliminationMatches_ValidPoule_Success() throws Exception {
	// 	updatePouleTable();
	// 	Organiser o = (Organiser) users.findByEmail("organizer.one@example.com").orElse(null);
    //     String jwtToken = jwtService.generateToken(o);

	// 	URI uri = new URI(baseUrl + port + "/api/v1/direct-elimination/create-direct-elimination-matches/" + events.findAll().get(0).getId());

	// 	HttpHeaders headers = new HttpHeaders();
	// 	headers.set("Authorization", "Bearer " + jwtToken);
	// 	headers.setContentType(MediaType.APPLICATION_JSON);

    //     ResponseEntity<List<DirectEliminationBracketDTO>> result = restTemplate.exchange(uri, HttpMethod.POST, new HttpEntity<>(headers), new ParameterizedTypeReference<List<DirectEliminationBracketDTO>>() {});

	// 	assertEquals(201, result.getStatusCode().value());
	// 	assertEquals(15, result.getBody().size());
	// 	assertEquals("Finals", result.getBody().get(0).getName());
	// 	assertEquals("Top 16", result.getBody().get(14).getName());
	// 	assertEquals("5 Fencer5", result.getBody().get(14).getParticipants()[0].getName());
	// }

	// @Test
	// public void createDirectEliminationMatches_PouleNotDone_Failure() throws Exception {
    //     createPoule();
    //     Organiser o = (Organiser) users.findByEmail("organizer.one@example.com").orElse(null);
    //     String jwtToken = jwtService.generateToken(o);

	// 	URI uri = new URI(baseUrl + port + "/api/v1/direct-elimination/create-direct-elimination-matches/" + events.findAll().get(0).getId());

	// 	HttpHeaders headers = new HttpHeaders();
	// 	headers.set("Authorization", "Bearer " + jwtToken);
	// 	headers.setContentType(MediaType.APPLICATION_JSON);

    //     ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.POST, new HttpEntity<>(headers), String.class);
    //     assertEquals(400, result.getStatusCode().value());
    //     assertEquals("Poules not done!", result.getBody());
    // }

	// @Test
	// public void updateDirectEliminationMatch_ValidMatch_Success() throws Exception {
	// 	createDirectEliminationMatches();
	// 	Organiser o = (Organiser) users.findByEmail("organizer.one@example.com").orElse(null);
    //     String jwtToken = jwtService.generateToken(o);

	// 	URI uri = new URI(baseUrl + port + "/api/v1/direct-elimination/update-direct-elimination-match/" + events.findAll().get(0).getId());

	// 	HttpHeaders headers = new HttpHeaders();
	// 	headers.set("Authorization", "Bearer " + jwtToken);
	// 	headers.setContentType(MediaType.APPLICATION_JSON);

	// 	UpdateDirectEliminationMatchDTO request = new UpdateDirectEliminationMatchDTO(directEliminationMatches.findAll().get(12).getId(), 15, 10);
        
	// 	HttpEntity<UpdateDirectEliminationMatchDTO> entity = new HttpEntity<>(request, headers);
	// 	ResponseEntity<List<DirectEliminationBracketDTO>> result = restTemplate.exchange(uri, HttpMethod.PUT, entity, new ParameterizedTypeReference<List<DirectEliminationBracketDTO>>() {});
        
	// 	assertEquals(201, result.getStatusCode().value());
    //     // Check if the match is updated properly (Scores and Fencers)
    //     DirectEliminationMatch match = directEliminationMatches.findAll().get(12);
    //     assertEquals(15, match.getScore1());
    //     assertEquals(10, match.getScore2());
    //     assertEquals("4 Fencer4", tournamentFencers.findById(match.getFencer1()).get().getFencer().getName());
    //     assertEquals("1 Fencer1", tournamentFencers.findById(match.getFencer2()).get().getFencer().getName());

	// }

    // @Test
    // public void updateDirectEliminationMatch_InvalidScore_Failure() throws Exception {
    //     createDirectEliminationMatches();
	// 	Organiser o = (Organiser) users.findByEmail("organizer.one@example.com").orElse(null);
    //     String jwtToken = jwtService.generateToken(o);

	// 	URI uri = new URI(baseUrl + port + "/api/v1/direct-elimination/update-direct-elimination-match/" + events.findAll().get(0).getId());

	// 	HttpHeaders headers = new HttpHeaders();
	// 	headers.set("Authorization", "Bearer " + jwtToken);
	// 	headers.setContentType(MediaType.APPLICATION_JSON);

    //     // Score is more than 15 (20)
	// 	UpdateDirectEliminationMatchDTO request = new UpdateDirectEliminationMatchDTO(directEliminationMatches.findAll().get(12).getId(), 20, 10);
        
	// 	HttpEntity<UpdateDirectEliminationMatchDTO> entity = new HttpEntity<>(request, headers);
	// 	ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.PUT, entity, String.class);

	// 	assertEquals(400, result.getStatusCode().value());
    //     assertEquals("{\"score1\":\"Maximum points for a direct elimination match is 15\"}", result.getBody());
    // }

    // @Test
    // public void endEvent_ValidEvent_Success() throws Exception {
    //     updateDirectEliminationMatch();
    //     Organiser o = (Organiser) users.findByEmail("organizer.one@example.com").orElse(null);
    //     String jwtToken = jwtService.generateToken(o);

    //     // Change event date for testing purpose
    //     Event e = events.findAll().get(0);
    //     e.setDate(LocalDate.of(2024, 11, 11));
    //     events.save(e);

    //     URI uri = new URI(baseUrl + port + "/api/v1/event/end-event/" + events.findAll().get(0).getId());

    //     HttpHeaders headers = new HttpHeaders();
    //     headers.set("Authorization", "Bearer " + jwtToken);
    //     headers.setContentType(MediaType.APPLICATION_JSON);

    //     ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.PUT, new HttpEntity<>(headers),
    //             String.class);
        
    //     assertEquals(200, result.getStatusCode().value());
        
    //     // Check event has ended
    //     assertEquals(true, events.findAll().get(0).isOver());
        
    //     // Check if winner of finals is correct
    //     int winnerId = directEliminationMatches.findAll().get(0).getWinner();
    //     String winnerName = tournamentFencers.findById(winnerId).get().getFencer().getName();
    //     assertEquals("8 Fencer8", winnerName);
        
    //     // Check if the amount of points after the event is correct for the winner
    //     // Initial points = 800 (for fencer 8)
    //     // New points = 800 + 436 = 1236
    //     assertEquals(1236, fencers.findByName("8 Fencer8").get().getPoints());

    // }

    // @Test
    // public void endEvent_FinalsNotDone_Failure() throws Exception {
    //     createDirectEliminationMatches();
    //     Organiser o = (Organiser) users.findByEmail("organizer.one@example.com").orElse(null);
    //     String jwtToken = jwtService.generateToken(o);

    //     URI uri = new URI(baseUrl + port + "/api/v1/event/end-event/" + events.findAll().get(0).getId());

    //     HttpHeaders headers = new HttpHeaders();
    //     headers.set("Authorization", "Bearer " + jwtToken);
    //     headers.setContentType(MediaType.APPLICATION_JSON);

    //     ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.PUT, new HttpEntity<>(headers),
    //             String.class);
        
    //     assertEquals(400, result.getStatusCode().value());
    //     assertEquals("Event has not started yet!", result.getBody());        
    // }
}
