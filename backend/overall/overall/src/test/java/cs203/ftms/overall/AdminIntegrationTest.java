package cs203.ftms.overall;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import cs203.ftms.overall.dto.VerifyOrgDTO;
import cs203.ftms.overall.dto.clean.CleanOrganiserDTO;
import cs203.ftms.overall.model.userrelated.Admin;
import cs203.ftms.overall.model.userrelated.Organiser;
import cs203.ftms.overall.repository.userrelated.AdminRepository;
import cs203.ftms.overall.security.repository.RefreshTokenRepository;
import cs203.ftms.overall.security.service.JwtService;
import cs203.ftms.overall.service.admin.AdminService;
import cs203.ftms.overall.service.organiser.OrganiserService;
import jakarta.transaction.Transactional;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AdminIntegrationTest {

    @LocalServerPort
    private int port;

    private final String baseUrl = "http://localhost:";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AdminRepository admins;

    @Autowired
    private RefreshTokenRepository refresh;

    @Autowired
    private JwtService jwtService;

    @MockBean
    private AdminService adminService;

    @MockBean
    private OrganiserService organiserService;

    @BeforeEach
    @Transactional
    void setUp() {
        refresh.deleteAll();
        admins.deleteAll();
    }

    @AfterEach
    @Transactional
    void tearDown() {
        refresh.deleteAll();
        admins.deleteAll();
    }


    // @Test
    // public void getProfile_Valid_Success() throws Exception {
    //     // Create and authenticate an admin user
    //     String adminEmail = "admin@example.com";
    //     Admin admin = new Admin();
    //     admin.setEmail(adminEmail);
    //     admin.setPassword("password");
    //     admin.setRole("ROLE_ADMIN"); // Ensure the role is correctly set
    //     admins.save(admin);

    //     String jwtToken = jwtService.generateToken(admin);

    //     URI uri = new URI(baseUrl + port + "/api/v1/admin/profile");

    //     HttpHeaders headers = new HttpHeaders();
    //     headers.set("Authorization", "Bearer " + jwtToken);
    //     headers.setContentType(MediaType.APPLICATION_JSON);

    //     HttpEntity<String> entity = new HttpEntity<>(headers);

    //     ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);

    //     assertEquals(200, result.getStatusCode().value());
    // }

    @Test
    public void getProfile_Invalid_Failure() throws Exception {
        // Create and authenticate an admin user
        String adminEmail = "admin@example.com";
        Admin admin = new Admin();
        admin.setEmail(adminEmail);
        admin.setPassword("password");
        admin.setRole("ROLE_ADMIN"); // Ensure the role is correctly set
        admins.save(admin);

        // Generate an invalid JWT token (e.g., by using a different user or an invalid secret)
        String invalidJwtToken = "invalid.jwt.token";

        URI uri = new URI("http://localhost:" + port + "/api/v1/admin/profile");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + invalidJwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);

        assertEquals(401, result.getStatusCode().value());
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

        Mockito.when(adminService.getUnverifiedOrgs()).thenReturn(organisers);
        Mockito.when(organiserService.getCleanOrganiserDTO(organiser1)).thenReturn(cleanOrganisers.get(0));
        Mockito.when(organiserService.getCleanOrganiserDTO(organiser2)).thenReturn(cleanOrganisers.get(1));

        URI uri = new URI(baseUrl + port + "/api/v1/admin/unverified-organiser");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);

        assertEquals(200, result.getStatusCode().value());
        // Add assertions to verify the response content
    }

    @Test
        public void verifyOrg_ShouldVerifyOrganiser() throws Exception {
        // Arrange
        VerifyOrgDTO verifyOrgDTO = new VerifyOrgDTO();

        // Create and authenticate an admin user
        String adminEmail = "admin@example.com";
        Admin admin = new Admin();
        admin.setEmail(adminEmail);
        admin.setPassword("password");
        admin.setRole("ROLE_ADMIN"); // Ensure the role is correctly set
        admins.save(admin);

        String jwtToken = jwtService.generateToken(admin);

        URI uri = new URI(baseUrl + port + "/api/v1/admin/verify-organiser");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<VerifyOrgDTO> entity = new HttpEntity<>(verifyOrgDTO, headers);

        ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.PUT, entity, String.class);

        assertEquals(200, result.getStatusCode().value());
        assertEquals("operation successful", result.getBody());
    }
}
