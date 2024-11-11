package cs203.ftms.overall;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
import cs203.ftms.overall.dto.clean.CleanOrganiserDTO;
import cs203.ftms.overall.dto.clean.CleanTournamentDTO;
import cs203.ftms.overall.model.tournamentrelated.Event;
import cs203.ftms.overall.model.tournamentrelated.Tournament;
import cs203.ftms.overall.model.userrelated.Admin;
import cs203.ftms.overall.model.userrelated.Fencer;
import cs203.ftms.overall.model.userrelated.Organiser;
import cs203.ftms.overall.model.userrelated.User;
import cs203.ftms.overall.repository.tournamentrelated.DirectEliminationMatchRepository;
import cs203.ftms.overall.repository.tournamentrelated.EventRepository;
import cs203.ftms.overall.repository.tournamentrelated.MatchRepository;
import cs203.ftms.overall.repository.tournamentrelated.PouleRepository;
import cs203.ftms.overall.repository.tournamentrelated.TournamentFencerRepository;
import cs203.ftms.overall.repository.tournamentrelated.TournamentRepository;
import cs203.ftms.overall.repository.userrelated.AdminRepository;
import cs203.ftms.overall.repository.userrelated.OrganiserRepository;
import cs203.ftms.overall.repository.userrelated.UserRepository;
import cs203.ftms.overall.security.repository.RefreshTokenRepository;
import cs203.ftms.overall.security.service.JwtService;
import cs203.ftms.overall.service.authentication.AuthenticationService;
import cs203.ftms.overall.service.event.EventService;
import cs203.ftms.overall.service.fencer.FencerService;
import cs203.ftms.overall.service.tournament.TournamentService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;



@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Transactional
class TournamentIntegrationTest {

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
    private EntityManager entityManager;

    @Autowired
    private DirectEliminationMatchRepository directEliminationMatches;

    @Autowired
    private MatchRepository matches;

    @Autowired
    private PouleRepository poules;

    @Autowired
    private EventRepository events;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private TournamentService tournamentService;

    @Autowired
    private EventService eventService;

    @Autowired
    private FencerService fencerService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private OrganiserRepository organiserRepository;

    @Autowired
    private AdminRepository admins;

    @BeforeEach
    @Transactional
    void setUp() {
        entityManager.createNativeQuery("DELETE FROM tournament_fencer_matches").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM tournament_fencer").executeUpdate();
        tournamentFencers.deleteAll();
        directEliminationMatches.deleteAll();
        matches.deleteAll();
        entityManager.createNativeQuery("DELETE FROM poule").executeUpdate();
        poules.deleteAll();
        events.deleteAll();
        tournaments.deleteAll();
        refresh.deleteAll();
        users.deleteAll();
    }

	@AfterEach
    @Transactional
    void tearDown() {
        entityManager.createNativeQuery("DELETE FROM tournament_fencer_matches").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM tournament_fencer").executeUpdate();
        tournamentFencers.deleteAll();
        directEliminationMatches.deleteAll();
        matches.deleteAll();
        entityManager.createNativeQuery("DELETE FROM poule").executeUpdate();
        poules.deleteAll();
        events.deleteAll();
        tournaments.deleteAll();
        refresh.deleteAll();
        users.deleteAll();
    }

    // helper method to create an organiser
    @Transactional
    private String createOrganiser() throws Exception{
        RegisterOrganiserDTO registerOrganiserDTO = new RegisterOrganiserDTO(
            "Organizer One", 
            "organizer.one@example.com", 
            "+6591969123", 
            "Abcd1234!", 
            "Singapore");
        System.out.println(1);
        // authenticationService.createOrganiser(registerOrganiserDTO);
        HttpEntity<RegisterOrganiserDTO> regOrganiserDTOEntity = new HttpEntity<>(registerOrganiserDTO);
        ResponseEntity<String> result = restTemplate.postForEntity(new URI(baseUrl + port + "/api/v1/auth/register-organiser"), regOrganiserDTOEntity, String.class);
        System.out.println(2);
        User u = users.findByEmail("organizer.one@example.com").orElse(null);
        System.out.println(3);
        ((Organiser) u).setVerified(true);
        users.save(u);
        System.out.println(4);

        return u.getEmail();
    }

    // helper method to create a fencer
    @Transactional
    private String createFencer() throws Exception{
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

    private String authenticateUser(String email, String password) throws Exception {
        AuthenticationDTO authDTO = new AuthenticationDTO(email, password);
        HttpEntity<AuthenticationDTO> authDTOEntity = new HttpEntity<>(authDTO);
        ResponseEntity<JwtDTO> result = restTemplate.postForEntity(new URI(baseUrl + port + "/api/v1/auth/login"), authDTOEntity, JwtDTO.class);
        return result.getBody().getToken();
    }

    @Transactional
    private Tournament createTournament(Organiser o) throws Exception{       
        Tournament t = new Tournament(
                "National Tournament", 
                o,
                LocalDate.of(2024, 12, 18), 
                80, 
                LocalDate.of(2024, 12, 20), 
                LocalDate.of(2024, 12, 30), 
                "location", 
                "description", 
                "rules",
                'B'
        );
        return tournaments.save(t);

        // String jwtToken = authenticateUser(o.getEmail(), o.getPassword());
        // HttpHeaders headers = new HttpHeaders();
        // headers.set("Authorization", "Bearer " + jwtToken);
        // headers.setContentType(MediaType.APPLICATION_JSON);
        
        // HttpEntity<CreateTournamentDTO> entity = new HttpEntity<>(createTournamentDTO, headers);

        // ResponseEntity<String> result = restTemplate.postForEntity(new URI(baseUrl + port + "/api/v1/tournament/create-tournament"), entity, String.class);
        // return tournaments.findByName("National Tournament").orElse(null);
    }
    
    // helper method to create 3 events for the given tournament
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
    public void getUnverifiedOrgs_ShouldReturnUnverifiedOrganisers() throws Exception {
        // Arrange
        // Create and authenticate an admin user
        String adminEmail = "admin@example.com";
        Admin admin = new Admin();
        admin.setEmail(adminEmail);
        admin.setPassword("password");
        admin.setRole("ROLE_ADMIN"); // Ensure the role is correctly set
        admins.save(admin);
        String jwtToken = jwtService.generateToken(admin);
        List<Organiser> organisers = new ArrayList<>();
        Organiser organiser1 = new Organiser();
        organiser1.setId(1);
        organiser1.setName("Organiser 1");
        organiser1.setVerified(false);
        organisers.add(organiser1);
        Organiser organiser2 = new Organiser();
        organiser2.setId(2);
        organiser2.setName("Organiser 2");
        organiser2.setVerified(false);
        organisers.add(organiser2);
        List<CleanOrganiserDTO> cleanOrganisers = new ArrayList<>();
        cleanOrganisers.add(new CleanOrganiserDTO(organiser1.getId(), organiser1.isVerified(), organiser1.getName(), organiser1.getEmail(), organiser1.getContactNo(), organiser1.getCountry()));
        cleanOrganisers.add(new CleanOrganiserDTO(organiser2.getId(), organiser2.isVerified(), organiser2.getName(), organiser2.getEmail(), organiser2.getContactNo(), organiser2.getCountry()));
        // Mockito.when(adminService.getUnverifiedOrgs()).thenReturn(organisers);
        // Mockito.when(organiserService.getCleanOrganiserDTO(organiser1)).thenReturn(cleanOrganisers.get(0));
        // Mockito.when(organiserService.getCleanOrganiserDTO(organiser2)).thenReturn(cleanOrganisers.get(1));
        URI uri = new URI(baseUrl + port + "/api/v1/admin/unverified-organiser");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
        System.out.println("end of test 7");
        assertEquals(200, result.getStatusCode().value());
        // Add assertions to verify the response content
    }

    @Test
    public void createTournament_ValidTournament_Success() throws Exception {
        // Create and save the organiser
        Organiser o = new Organiser();
        o.setEmail("organiser@example.com");
        o.setPassword("Abcd1234!");
        o.setRole("ROLE_ORGANISER");
        o.setVerified(true);
        Organiser newO = organiserRepository.save(o);

        // Generate JWT token for the saved organiser
        String jwtToken = jwtService.generateToken(newO);

        // Create the URI for the endpoint
        URI uri = new URI(baseUrl + port + "/api/v1/tournament/create-tournament");

        // Create the request payload
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

        // Set the JWT token in the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create the HTTP entity with the request payload and headers
        HttpEntity<CreateTournamentDTO> entity = new HttpEntity<>(request, headers);

        // Make the request and get the response
        ResponseEntity<CleanTournamentDTO> result = restTemplate.postForEntity(uri, entity, CleanTournamentDTO.class);

        // Assert the response status and body
        System.out.println("end of test 1");
        assertEquals(201, result.getStatusCode().value());
        assertNotNull(result.getBody());
    }

    // @Test
    // public void createTournament_ValidTournament_Success() throws Exception {
    //     Organiser o = new Organiser();
    //     o.setEmail("organiser.one@example.com");
    //     o.setPassword("Abcd1234!");
    //     o.setRole("ROLE_ORGANISER");
    //     Organiser newO = organiserRepository.save(o);
    //     String jwtToken = jwtService.generateToken(newO);

    //     URI uri = new URI(baseUrl + port + "/api/v1/tournament/create-tournament");

    //     // String oemail = createOrganiser();
    //     // Organiser o = (Organiser) users.findByEmail(oemail).orElse(null);
    //     // String jwtToken = authenticateUser(oemail, o.getPassword());
        
    //     CreateTournamentDTO request = new CreateTournamentDTO(
    //             "National Tournament", 
    //             LocalDate.of(2024, 12, 18), 
    //             80, 
    //             LocalDate.of(2024, 12, 20), 
    //             LocalDate.of(2024, 12, 30), 
    //             "location", 
    //             "description", 
    //             "rules",
    //             'B');
                
    //     HttpHeaders headers = new HttpHeaders();
    //     headers.set("Authorization", "Bearer " + jwtToken);
    //     headers.setContentType(MediaType.APPLICATION_JSON);
        
    //     HttpEntity<CreateTournamentDTO> entity = new HttpEntity<>(request, headers);
        
    //     ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
    //     System.out.println("end of test 1");
    //     assertEquals(201, result.getStatusCode().value());

    // }

    @Test
    public void createTournament_Invalid_Failure() throws Exception {
        URI uri = new URI(baseUrl + port + "/api/v1/tournament/create-tournament");

        // String oemail = createOrganiser();
        // Organiser o = (Organiser) users.findByEmail(oemail).orElse(null);
        // System.out.println("jwt start");
        // String jwtToken = authenticateUser(oemail, o.getPassword());
        // System.out.println("jwt end");

        Organiser o = new Organiser();
        o.setEmail("organiser@example.com");
        o.setPassword("Abcd1234!");
        o.setRole("ROLE_ORGANISER");
        o.setVerified(true);
        Organiser newO = organiserRepository.save(o);

        // Generate JWT token for the saved organiser
        String jwtToken = jwtService.generateToken(newO);
        
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
        System.out.println(jwtToken);
        headers.set("Authorization", "Bearer " + jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<CreateTournamentDTO> entity = new HttpEntity<>(request, headers);
        System.out.println("start of test 2");
        // ResponseEntity<String> result = restTemplate.postForEntity(uri, entity, String.class);
        ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
        System.out.println("end of test 2");
        assertEquals(400, result.getStatusCode().value());

    }

    @Test
    public void updateTournament_ValidFields_Success() throws Exception {
        // Arrange
        // String oemail = createOrganiser();
        // Organiser o = (Organiser) users.findByEmail(oemail).orElse(null);
        // String jwtToken = authenticateUser(oemail, o.getPassword());

        Organiser o = new Organiser();
        o.setEmail("organiser@example.com");
        o.setPassword("Abcd1234!");
        o.setRole("ROLE_ORGANISER");
        o.setVerified(true);
        Organiser newO = organiserRepository.save(o);

        // Generate JWT token for the saved organiser
        String jwtToken = jwtService.generateToken(newO);

        Tournament tournament = createTournament(o);
        URI uri = new URI(baseUrl + port + "/api/v1/tournament/update-tournament/" + tournament.getId());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        CreateTournamentDTO request = new CreateTournamentDTO(
            "Updated Tournament", 
            LocalDate.of(2025, 12, 18), 
            100, 
            LocalDate.of(2025, 12, 20), 
            LocalDate.of(2025, 12, 30), 
            "new location", 
            "new description", 
            "new rules",
            'A'
        );

        HttpEntity<CreateTournamentDTO> entity = new HttpEntity<>(request, headers);

        // Act
        ResponseEntity<CleanTournamentDTO> result = restTemplate.exchange(uri, HttpMethod.PUT, entity, CleanTournamentDTO.class);
        System.out.println("end of test 3");
        // Assert
        assertEquals(200, result.getStatusCode().value());
        assertNotNull(result.getBody());
        assertEquals(request.getName(), result.getBody().getName());
        assertEquals(request.getStartDate(), result.getBody().getStartDate());
        assertEquals(request.getEndDate(), result.getBody().getEndDate());
        assertEquals(request.getAdvancementRate(), result.getBody().getAdvancementRate());
        assertEquals(request.getSignupEndDate(), result.getBody().getSignupEndDate());
        assertEquals(request.getLocation(), result.getBody().getLocation());
        assertEquals(request.getDescription(), result.getBody().getDescription());
        assertEquals(request.getRules(), result.getBody().getRules());
        assertEquals(request.getDifficulty(), result.getBody().getDifficulty());

    }

    @Test
    public void updateTournament_InvalidTournamentId_Failure() throws Exception {
        // Arrange
        // String oemail = createOrganiser();
        // Organiser o = (Organiser) users.findByEmail(oemail).orElse(null);
        // System.out.println("organiser id " + o.getId());
        // System.out.println("organiser email " + o.getEmail());
        // String jwtToken = authenticateUser(oemail, o.getPassword());
        Organiser o = new Organiser();
        o.setEmail("organiser@example.com");
        o.setPassword("Abcd1234!");
        o.setRole("ROLE_ORGANISER");
        o.setVerified(true);
        Organiser newO = organiserRepository.save(o);

        // Generate JWT token for the saved organiser
        String jwtToken = jwtService.generateToken(newO);

        URI uri = new URI(baseUrl + port + "/api/v1/tournament/update-tournament/10000"); // invalid tournament ID

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        CreateTournamentDTO request = new CreateTournamentDTO(
            "Updated Tournament", 
            LocalDate.of(2025, 12, 18), 
            100, 
            LocalDate.of(2025, 12, 20), 
            LocalDate.of(2025, 12, 30), 
            "new location", 
            "new description", 
            "new rules",
            'A'
        );

        HttpEntity<CreateTournamentDTO> entity = new HttpEntity<>(request, headers);

        // Act
        ResponseEntity<CleanTournamentDTO> result = restTemplate.exchange(uri, HttpMethod.PUT, entity, CleanTournamentDTO.class);
        System.out.println("end of test 4");

        // Assert
        assertEquals(400, result.getStatusCode().value());

    }

    @Test
    public void updateTournament_InvalidFields_Failure() throws Exception {
        // Arrange
        // String oemail = createOrganiser();
        // Organiser o = (Organiser) users.findByEmail(oemail).orElse(null);
        // String jwtToken = authenticateUser(oemail, o.getPassword());
        Organiser o = new Organiser();
        o.setEmail("organiser@example.com");
        o.setPassword("Abcd1234!");
        o.setRole("ROLE_ORGANISER");
        o.setVerified(true);
        Organiser newO = organiserRepository.save(o);

        // Generate JWT token for the saved organiser
        String jwtToken = jwtService.generateToken(newO);
        Tournament tournament = createTournament(o);

        URI uri = new URI(baseUrl + port + "/api/v1/tournament/update-tournament/" + tournament.getId()); // invalid tournament ID

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        CreateTournamentDTO request = new CreateTournamentDTO(
            "", // blank name 
            LocalDate.of(2023, 12, 21), // past sign up date n after start date
            101, // invalid advancement rate
            LocalDate.of(2023, 12, 20), // past start date
            LocalDate.of(2023, 12, 30), // past end date
            "", // blank location
            "", // blank description
            "", // blank rules
            'C' // invalid difficulty
        );

        HttpEntity<CreateTournamentDTO> entity = new HttpEntity<>(request, headers);

        // Act
        ResponseEntity<CleanTournamentDTO> result = restTemplate.exchange(uri, HttpMethod.PUT, entity, CleanTournamentDTO.class);
        System.out.println("end of test 5");

        // Assert
        assertEquals(400, result.getStatusCode().value());

    }

    @Test
    public void testUpdateTournament_Unauthorized() throws Exception {
        // Arrange
        Organiser o = new Organiser();
        o.setEmail("organiser@example.com");
        o.setPassword("Abcd1234!");
        o.setRole("ROLE_ORGANISER");
        o.setVerified(true);
        organiserRepository.save(o);

        Tournament tournament = createTournament(o);
        System.out.println("tournament id " + tournament.getId());

        // create another organiser 
        Organiser o2 = new Organiser();
        o2.setEmail("organiser2@example.com");
        o2.setPassword("Abcd1234!");
        o2.setRole("ROLE_ORGANISER");
        o2.setVerified(true);
        Organiser savedO = organiserRepository.save(o);

        // Generate JWT token for the saved organiser
        String jwtToken = jwtService.generateToken(savedO);
        System.out.println(jwtToken);

        URI uri = new URI(baseUrl + port + "/api/v1/tournament/update-tournament/" + tournament.getId());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        CreateTournamentDTO request = new CreateTournamentDTO(
            "Updated Tournament", 
            LocalDate.of(2025, 12, 18), 
            100, 
            LocalDate.of(2025, 12, 20), 
            LocalDate.of(2025, 12, 30), 
            "new location", 
            "new description", 
            "new rules",
            'A'
        );

        HttpEntity<CreateTournamentDTO> entity = new HttpEntity<>(request, headers);

        // Act
        ResponseEntity<CleanTournamentDTO> result = restTemplate.exchange(uri, HttpMethod.PUT, entity, CleanTournamentDTO.class);
        System.out.println("end of test 6");
        // Assert
        assertEquals(401, result.getStatusCode().value());
        assertEquals("Organiser does not match the tournament organiser.", result.getBody());


    }

}
