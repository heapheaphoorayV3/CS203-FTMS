package cs203.ftms.overall;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
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
import cs203.ftms.overall.dto.JwtDTO;
import cs203.ftms.overall.dto.PouleTableDTO;
import cs203.ftms.overall.dto.RegisterFencerDTO;
import cs203.ftms.overall.dto.RegisterOrganiserDTO;
import cs203.ftms.overall.model.userrelated.Fencer;
import cs203.ftms.overall.model.userrelated.Organiser;
import cs203.ftms.overall.model.userrelated.User;
import cs203.ftms.overall.repository.tournamentrelated.EventRepository;
import cs203.ftms.overall.repository.tournamentrelated.MatchRepository;
import cs203.ftms.overall.repository.tournamentrelated.TournamentFencerRepository;
import cs203.ftms.overall.repository.tournamentrelated.TournamentRepository;
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

    // @Autowired
    // private FencerRepository fencers;

    @Autowired
    private EventRepository events;

    @Autowired
    private MatchRepository matches;

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
                    "Last Name",
                    "fencer" + i + "@example.com",
                    "Abcd1234!",
                    "+6591569123",
                    "Singapore",
                    LocalDate.of(1990, 1, 1));

            HttpEntity<RegisterFencerDTO> regFencerDTOEntity = new HttpEntity<>(registerFencerDTO);
            restTemplate.postForEntity(new URI(baseUrl + port + "/api/v1/auth/register-fencer"), regFencerDTOEntity,
                    String.class);

            Fencer f = (Fencer) users.findByEmail("fencer" + i + "@example.com").orElse(null);

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

//     @Transactional
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
        System.out.println(o);
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

    // @Test
    // public void registerEvent_InvalidEvent_Failure() throws Exception

    @Test
    public void createPoule_ValidPoule_Success() throws Exception {
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

        ResponseEntity<PouleTableDTO> result = restTemplate.postForEntity(uri, entity, PouleTableDTO.class);

        assertEquals(201, result.getStatusCode().value());
    }

}
