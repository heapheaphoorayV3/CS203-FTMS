package cs203.ftms.overall;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import cs203.ftms.overall.dto.RegisterAdminDTO;
import cs203.ftms.overall.dto.RegisterFencerDTO;
import cs203.ftms.overall.dto.RegisterOrganiserDTO;
import cs203.ftms.overall.model.userrelated.Admin;
import cs203.ftms.overall.model.userrelated.Fencer;
import cs203.ftms.overall.model.userrelated.Organiser;
import cs203.ftms.overall.model.userrelated.User;
import cs203.ftms.overall.repository.userrelated.UserRepository;
import cs203.ftms.overall.service.admin.MailService;
import cs203.ftms.overall.service.authentication.AuthenticationService;

class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MailService mailService;


    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createFencer_ValidFields_Success() {
        // Arrange
        RegisterFencerDTO registerFencerDTO = new RegisterFencerDTO("John", "doe", "john.doe@example.com", "Abcd1234!"
        , "+6591238765", "Singapore", LocalDate.of(2000, 1, 1));
        
        when(passwordEncoder.encode(any(String.class))).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        User createdFencer = authenticationService.createFencer(registerFencerDTO);

        // Assert
        assertNotNull(createdFencer);
        assertTrue(createdFencer instanceof Fencer);
        
        verify(passwordEncoder).encode(registerFencerDTO.getPassword());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createOrganiser_ValidFields_ReturnTrue() {
        // Arrange
        RegisterOrganiserDTO registerOrganiserDTO = new RegisterOrganiserDTO("Organizer One", "organizer.one@example.com", "Abcd1234!", "+6591238765", "Singapore");

        when(passwordEncoder.encode(any(String.class))).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);
        doNothing().when(mailService).sendMail(any(String.class), any(String.class), any(String.class));

        // Act
        User createdOrganiser = authenticationService.createOrganiser(registerOrganiserDTO);

        // Assert
        assertNotNull(createdOrganiser);
        assertTrue(createdOrganiser instanceof Organiser);

        verify(passwordEncoder).encode(registerOrganiserDTO.getPassword());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createAdmin_ValidFields_ReturnTrue() {
        // Arrange
        RegisterAdminDTO registerAdminDTO = new RegisterAdminDTO("Admin One", "admin.one@example.com", "Abcd1234!", "+6591238765", "Singapore");

        when(passwordEncoder.encode(any(String.class))).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        User createdAdmin = authenticationService.createAdmin(registerAdminDTO);

        // Assert
        assertNotNull(createdAdmin);
        assertTrue(createdAdmin instanceof Admin);

        verify(passwordEncoder).encode(registerAdminDTO.getPassword());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void authenticateUser_ValidFields_ReturnTrue() {
        // Arrange
        String email = "user@example.com";
        String password = "Abcd1234!";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        User user = new Fencer();
        user.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act
        User authenticatedUser = authenticationService.authenticateUser(email, password);

        // Assert
        assertNotNull(authenticatedUser);
        assertEquals(email, authenticatedUser.getEmail());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository).findByEmail(email);
    }

    @Test
    void refreshToken_ValidToken_ReturnTrue() {
        
    }
}
