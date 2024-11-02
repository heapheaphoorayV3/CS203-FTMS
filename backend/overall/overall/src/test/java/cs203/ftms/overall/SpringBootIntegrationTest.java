package cs203.ftms.overall;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
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

import cs203.ftms.overall.dto.AuthenticationDTO;
import cs203.ftms.overall.dto.CompleteFencerProfileDTO;
import cs203.ftms.overall.dto.CreateEventDTO;
import cs203.ftms.overall.dto.CreateTournamentDTO;
import cs203.ftms.overall.dto.JwtDTO;
import cs203.ftms.overall.dto.RegisterFencerDTO;
import cs203.ftms.overall.dto.RegisterOrganiserDTO;
import cs203.ftms.overall.model.tournamentrelated.Event;
import cs203.ftms.overall.model.tournamentrelated.Tournament;
import cs203.ftms.overall.model.userrelated.Fencer;
import cs203.ftms.overall.model.userrelated.Organiser;
import cs203.ftms.overall.model.userrelated.User;
import cs203.ftms.overall.repository.tournamentrelated.TournamentFencerRepository;
import cs203.ftms.overall.repository.tournamentrelated.TournamentRepository;
import cs203.ftms.overall.repository.userrelated.UserRepository;
import cs203.ftms.overall.service.authentication.AuthenticationService;
import cs203.ftms.overall.service.event.EventService;
import cs203.ftms.overall.service.fencer.FencerService;
import cs203.ftms.overall.service.tournament.TournamentService;
import cs203.ftms.tournament.service.security.repository.RefreshTokenRepository;
import cs203.ftms.tournament.service.security.service.JwtService;
import jakarta.transaction.Transactional;



@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class SpringBootIntegrationTest {

	@LocalServerPort
	private int port;


    private final String baseUrl = "http://localhost:";

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private TournamentRepository tournaments;

    @Autowired
    private TournamentFencerRepository tournamentFencers;

	@Autowired
	private UserRepository users;

    @Autowired 
    private RefreshTokenRepository refresh;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private FencerService fencerService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private TournamentService tournamentService;

    @Autowired
    private EventService eventService;

    @BeforeEach
    void setUp() {
        tournamentFencers.deleteAll();
        tournaments.deleteAll();
        refresh.deleteAll();
		users.deleteAll();
    }

	@AfterEach
	void tearDown(){
        tournamentFencers.deleteAll();
		tournaments.deleteAll();
        refresh.deleteAll();
		users.deleteAll();
	}

    @Transactional
    public String createAndAuthOrganiser() throws Exception{
        RegisterOrganiserDTO registerOrganiserDTO = new RegisterOrganiserDTO(
            "Organizer One", 
            "organizer.one@example.com", 
            "+6591969123", 
            "Abcd1234!", 
            "Singapore");

        authenticationService.createOrganiser(registerOrganiserDTO);

        User u = users.findByEmail("organizer.one@example.com").orElse(null);
        ((Organiser) u).setVerified(true);
        users.save(u);

        return u.getEmail();
    }

    @Transactional
    public String createAndAuthFencer() throws Exception{
        RegisterFencerDTO registerFencerDTO= new RegisterFencerDTO(
            "Fencer One", 
            "Last Name",
            "fencer.one@example.com", 
            "Abcd1234!", 
            "+6591569123", 
            "Singapore",
            LocalDate.of(1990, 1, 1)
        );

        HttpEntity<RegisterFencerDTO> regFencerDTOEntity = new HttpEntity<>(registerFencerDTO);
        ResponseEntity<String> result = restTemplate.postForEntity(new URI(baseUrl + port + "/api/v1/auth/register-fencer"), regFencerDTOEntity, String.class);

        Fencer f = (Fencer) users.findByEmail("fencer.one@example.com").orElse(null);

        CompleteFencerProfileDTO dto = new CompleteFencerProfileDTO('R', 'S', 'M', "Best Club", 2010);
        User u = fencerService.completeProfile((Fencer) f, dto);
       
        return u.getEmail();        
    }

    @Transactional
    private Tournament createTournament(Organiser o) throws Exception{       
        CreateTournamentDTO createTournamentDTO = new CreateTournamentDTO(
                "National Tournament", 
                LocalDate.of(2024, 12, 18), 
                80, 
                LocalDate.of(2024, 12, 20), 
                LocalDate.of(2024, 12, 30), 
                "location", 
                "description", 
                "rules",
                'B'
        );

        return tournamentService.createTournament(createTournamentDTO, o);
    }
    
    @Transactional
    private List<Event> createEvent(Tournament t) throws Exception {
        List<CreateEventDTO> request = new ArrayList<>();

        request.add(new CreateEventDTO(
            'M',
            'S',
            64,
            LocalDate.of(2024, 12, 21),
            LocalTime.of(10, 0),
            LocalTime.of(18, 0)
        ));
        request.add(new CreateEventDTO(
            'M',
            'F',
            64,
            LocalDate.of(2024, 12, 21),
            LocalTime.of(10, 0),
            LocalTime.of(18, 0)
        ));
        request.add(new CreateEventDTO(
            'F',
            'E',
            64,
            LocalDate.of(2024, 12, 21),
            LocalTime.of(10, 0),
            LocalTime.of(18, 0)
        ));

        return eventService.createEvent(t.getId(), t.getOrganiser(), request);
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
    public void createFencer_Invalid_Failure() throws Exception{

        URI uri = new URI(baseUrl + port + "/api/v1/auth/register-fencer");
        RegisterFencerDTO registerFencerDTO= new RegisterFencerDTO(
            "", 
            "", 
            "fencer.onexaom", 
            "Abcd1234", 
            "+6599999999", 
            "", 
            LocalDate.of(2099, 1, 1) 
        );

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
        restTemplate.postForEntity(new URI(baseUrl + port + "/api/v1/auth/register-organiser"), regOrgDTOEntity, String.class);

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
        restTemplate.postForEntity(new URI(baseUrl + port + "/api/v1/auth/register-organiser"), regOrgDTOEntity, String.class);

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

        String oemail = createAndAuthOrganiser();
        Organiser o = (Organiser) users.findByEmail(oemail).orElse(null);
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
                'B'
                );
                
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<CreateTournamentDTO> entity = new HttpEntity<>(request, headers);
        
        ResponseEntity<String> result = restTemplate.postForEntity(uri, entity, String.class);
        
        assertEquals(201, result.getStatusCode().value());
    }

    @Test
    public void createEvent_ValidEvent_Success() throws Exception {
        String oemail = createAndAuthOrganiser();
        Organiser o = (Organiser) users.findByEmail(oemail).orElse(null);
        String jwtToken = jwtService.generateToken(o);
        Tournament t = createTournament(o);
        
        URI uri = new URI(baseUrl + port + "/api/v1/event/" + t.getId() + "/create-event");

        List<CreateEventDTO> request = new ArrayList<>();

        request.add(new CreateEventDTO(
            'M',
            'S',
            64,
            LocalDate.of(2024, 12, 21),
            LocalTime.of(10, 0),
            LocalTime.of(18, 0)
        ));
        request.add(new CreateEventDTO(
            'M',
            'F',
            64,
            LocalDate.of(2024, 12, 21),
            LocalTime.of(10, 0),
            LocalTime.of(18, 0)
        ));
        request.add(new CreateEventDTO(
            'W',
            'E',
            64,
            LocalDate.of(2024, 12, 21),
            LocalTime.of(10, 0),
            LocalTime.of(18, 0)
        ));
                
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<List<CreateEventDTO>> entity = new HttpEntity<>(request, headers);
        
        ResponseEntity<String> result = restTemplate.postForEntity(uri, entity, String.class);
        
        assertEquals(201, result.getStatusCode().value());
    }
    
    @Test
    public void registerEvent_ValidEvent_Success() throws Exception{
        String oemail = createAndAuthOrganiser();
        Organiser o = (Organiser) users.findByEmail(oemail).orElse(null);

        Tournament t = createTournament(o);
        List<Event> events = createEvent(t);

        String femail = createAndAuthFencer();
        Fencer f = (Fencer) users.findByEmail(femail).orElse(null);
        String jwtToken = jwtService.generateToken(f);

        URI uri = new URI(baseUrl + port + "/api/v1/event/register/" + events.get(0).getId());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.PUT, new HttpEntity<>(headers), String.class);
        System.out.println(result.getBody());
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void createEvent_InvalidEvent_Failure() throws Exception {
        String oemail = createAndAuthOrganiser();
        Organiser o = (Organiser) users.findByEmail(oemail).orElse(null);
        String jwtToken = jwtService.generateToken(o);
        Tournament t = createTournament(o);
        
        URI uri = new URI(baseUrl + port + "/api/v1/event/" + t.getId() + "/create-event");

        List<CreateEventDTO> request = new ArrayList<>();

        request.add(new CreateEventDTO(
            'M',
            'S',
            64,
            LocalDate.of(2025, 12, 21),
            LocalTime.of(10, 0),
            LocalTime.of(18, 0)
        ));
        request.add(new CreateEventDTO(
            'M',
            'F',
            64,
            LocalDate.of(2023, 12, 21),
            LocalTime.of(10, 0),
            LocalTime.of(18, 0)
        ));
        request.add(new CreateEventDTO(
            'F',
            'E',
            64,
            LocalDate.of(2023, 12, 21),
            LocalTime.of(10, 0),
            LocalTime.of(18, 0)
        ));
                
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<List<CreateEventDTO>> entity = new HttpEntity<>(request, headers);
        
        ResponseEntity<String> result = restTemplate.postForEntity(uri, entity, String.class);
        
        assertEquals(400, result.getStatusCode().value());
    }

    @Test
    public void createTournament_Invalid_Failure() throws Exception {
        URI uri = new URI(baseUrl + port + "/api/v1/tournament/create-tournament");

        String oemail = createAndAuthOrganiser();
        Organiser o = (Organiser) users.findByEmail(oemail).orElse(null);
        String jwtToken = jwtService.generateToken(o);
        
        CreateTournamentDTO request = new CreateTournamentDTO(
                "", 
                LocalDate.of(2024, 12, 21), 
                59, 
                LocalDate.of(2023, 12, 20), 
                LocalDate.of(2023, 11, 30), 
                "", 
                "", 
                "" ,
                'S'
                );
                
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<CreateTournamentDTO> entity = new HttpEntity<>(request, headers);
        
        ResponseEntity<String> result = restTemplate.postForEntity(uri, entity, String.class);
        
        assertEquals(400, result.getStatusCode().value());
    }
}

