package cs203.ftms.overall;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import cs203.ftms.overall.dto.RegisterAdminDTO;
import cs203.ftms.overall.dto.RegisterFencerDTO;
import cs203.ftms.overall.dto.RegisterOrganiserDTO;
import cs203.ftms.overall.dto.clean.CleanFencerDTO;
import cs203.ftms.overall.dto.clean.CleanOrganiserDTO;
import cs203.ftms.overall.exception.UserAlreadyExistException;
import cs203.ftms.overall.model.userrelated.Admin;
import cs203.ftms.overall.model.userrelated.Fencer;
import cs203.ftms.overall.model.userrelated.Organiser;
import cs203.ftms.overall.model.userrelated.User;
import cs203.ftms.overall.repository.userrelated.UserRepository;
import cs203.ftms.overall.security.service.JwtService;
import cs203.ftms.overall.service.admin.MailService;
import cs203.ftms.overall.service.authentication.AuthenticationService;
import jakarta.persistence.EntityNotFoundException;

class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MailService mailService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests the behavior of getCleanFencer method when a valid Fencer object is provided.
     * Verifies that the Fencer object is correctly mapped to the CleanFencerDTO.
     */
    @Test
    void getCleanFencer_ValidFencer_ReturnsCleanFencerDTO() {
        // Arrange
        Fencer fencer = new Fencer();
        fencer.setId(1);
        fencer.setName("John Doe");
        fencer.setEmail("john.doe@example.com");
        fencer.setContactNo("+6591234567");
        fencer.setCountry("Singapore");
        fencer.setDateOfBirth(LocalDate.of(2000, 1, 1));
        fencer.setDominantArm('R');
        fencer.setWeapon('F');
        fencer.setClub("Singapore Fencing Club");
        fencer.setPoints(100);
        fencer.setDebutYear(2018);
        fencer.setGender('M');

        // Act
        CleanFencerDTO cleanFencerDTO = authenticationService.getCleanFencer(fencer);

        // Assert
        assertNotNull(cleanFencerDTO);
        assertEquals(fencer.getId(), cleanFencerDTO.getId());
        assertEquals(fencer.getName(), cleanFencerDTO.getName());
        assertEquals(fencer.getEmail(), cleanFencerDTO.getEmail());
        assertEquals(fencer.getContactNo(), cleanFencerDTO.getContactNo());
        assertEquals(fencer.getCountry(), cleanFencerDTO.getCountry());
        assertEquals(fencer.getDateOfBirth(), cleanFencerDTO.getDateOfBirth());
        assertEquals(fencer.getDominantArm(), cleanFencerDTO.getDominantArm());
        assertEquals(fencer.getWeapon(), cleanFencerDTO.getWeapon());
        assertEquals(fencer.getClub(), cleanFencerDTO.getClub());
        assertEquals(fencer.getPoints(), cleanFencerDTO.getPoints());
        assertEquals(fencer.getDebutYear(), cleanFencerDTO.getDebutYear());
        assertEquals(fencer.getGender(), cleanFencerDTO.getGender());
    }

    /**
     * Tests the behavior of getCleanOrganiser method when a valid Organiser object is provided.
     * Verifies that the Organiser object is correctly mapped to the CleanOrganiserDTO.
     */
    @Test
    void getCleanOrganiser_ValidOrganiser_ReturnsCleanOrganiserDTO() {
        // Arrange
        Organiser organiser = new Organiser();
        organiser.setId(1);
        organiser.setVerified(true);
        organiser.setName("Organizer One");
        organiser.setEmail("organizer.one@example.com");
        organiser.setContactNo("+6599876543");
        organiser.setCountry("Singapore");

        // Act
        CleanOrganiserDTO cleanOrganiserDTO = authenticationService.getCleanOrganiser(organiser);

        // Assert
        assertNotNull(cleanOrganiserDTO);
        assertEquals(organiser.getId(), cleanOrganiserDTO.getId());
        assertEquals(organiser.isVerified(), cleanOrganiserDTO.isVerified());
        assertEquals(organiser.getName(), cleanOrganiserDTO.getName());
        assertEquals(organiser.getEmail(), cleanOrganiserDTO.getEmail());
        assertEquals(organiser.getContactNo(), cleanOrganiserDTO.getContactNo());
        assertEquals(organiser.getCountry(), cleanOrganiserDTO.getCountry());
    }

    /**
     * Tests the createFencer method when the fencer is created with valid fields.
     * Verifies that the fencer is created successfully and the password is encoded.
     */
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

    /**
     * Tests the createFencer method when a fencer with the same email already exists.
     * Verifies that a UserAlreadyExistException is thrown.
     */
    @Test
    void createFencer_AlreadyExist_Failure() {
        // Arrange
        RegisterFencerDTO registerFencerDTO = new RegisterFencerDTO("John", "Doe", "john.doe@example.com", "Abcd1234!",
                "+6591238765", "Singapore", LocalDate.of(2000, 1, 1));

        when(userRepository.findByEmail(registerFencerDTO.getEmail())).thenReturn(Optional.of(new Fencer()));

        // Act & Assert
        assertThrows(UserAlreadyExistException.class, () -> authenticationService.createFencer(registerFencerDTO));
    }

    /**
     * Tests the createOrganiser method when the organiser is created with valid fields.
     * Verifies that the organiser is created successfully and the password is encoded.
     */
    @Test
    void createOrganiser_ValidFields_ReturnTrue() {
        // Arrange
        RegisterOrganiserDTO registerOrganiserDTO = new RegisterOrganiserDTO("Organizer One", "organizer.one@example.com", "Abcd1234!", "+6591238765", "Singapore");

        when(passwordEncoder.encode(any(String.class))).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        User createdOrganiser = authenticationService.createOrganiser(registerOrganiserDTO);

        // Assert
        assertNotNull(createdOrganiser);
        assertTrue(createdOrganiser instanceof Organiser);

        verify(passwordEncoder).encode(registerOrganiserDTO.getPassword());
        verify(userRepository).save(any(User.class));
    }

    /**
     * Tests the createOrganiser method when an organiser with the same email already exists.
     * Verifies that a UserAlreadyExistException is thrown.
     */
    @Test
    void createOrganiser_AlreadyExist_Failure() {
        // Arrange
        RegisterOrganiserDTO registerOrganiserDTO = new RegisterOrganiserDTO("Organizer One", "organizer.one@example.com", "Abcd1234!", "+6591238765", "Singapore");

        when(userRepository.findByEmail(registerOrganiserDTO.getEmail())).thenReturn(Optional.of(new Organiser()));

        // Act & Assert
        assertThrows(UserAlreadyExistException.class, () -> authenticationService.createOrganiser(registerOrganiserDTO));
    }

    /**
     * Tests the createAdmin method when the admin is created with valid fields.
     * Verifies that the admin is created successfully and the password is encoded.
     */
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

    /**
     * Tests the createAdmin method when an admin with the same email already exists.
     * Verifies that a UserAlreadyExistException is thrown.
     */
    @Test
    void createAdmin_AlreadyExist_Failure() {
        // Arrange
        RegisterAdminDTO registerAdminDTO = new RegisterAdminDTO("Admin One", "admin.one@example.com", "Abcd1234!", "+6591238765", "Singapore");

        when(userRepository.findByEmail(registerAdminDTO.getEmail())).thenReturn(Optional.of(new Admin()));

        // Act & Assert
        assertThrows(UserAlreadyExistException.class, () -> authenticationService.createAdmin(registerAdminDTO));
    }

    /**
     * Tests the authenticateUser method when valid authentication details are provided.
     * Verifies that the authentication is successful and returns the authenticated user.
     */
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

    /**
     * Tests the getUser method when a valid user ID is provided.
     * Verifies that the method correctly returns the user associated with the ID.
     */
    @Test
    void getUser_ValidId_ReturnUser() {
        // Arrange
        int userId = 1;
        User user = new Fencer();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        User foundUser = authenticationService.getUser(userId);

        // Assert
        assertNotNull(foundUser);
        assertEquals(userId, foundUser.getId());
        verify(userRepository).findById(userId);
    }

    /**
     * Tests the getUser method when an invalid user ID is provided.
     * Verifies that an EntityNotFoundException is thrown when the user is not found.
     */
    @Test
    void getUser_InvalidId_ThrowsException() {
        // Arrange
        int userId = 1;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> authenticationService.getUser(userId));
    }

    /**
     * Tests the forgetPassword method when the user is found.
     * Verifies that the method generates a token and sends an email.
     */
    @Test
    void forgetPassword_UserFound_ReturnsToken() {
        // Arrange
        String email = "user@example.com";
        User user = new Fencer();
        user.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act
        String result = authenticationService.forgetPassword(email);

        // Assert
        assertNotNull(user.getVerificationToken());
        assertTrue(result.contains("Email sent"));
        verify(userRepository).findByEmail(email);
        verify(userRepository).save(user);
        verify(mailService).sendMail(any(String.class), any(String.class), any(String.class));
    }

    /**
     * Tests the forgetPassword method when the user is not found.
     * Verifies that the method returns a "User not found" message.
     */
    @Test
    void forgetPassword_UserNotFound_ReturnsUserNotFound() {
        // Arrange
        String email = "user@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act
        String result = authenticationService.forgetPassword(email);

        // Assert
        assertEquals("User not found", result);
        verify(userRepository).findByEmail(email);
    }

    /**
     * Tests the resetPassword method when a valid token is provided.
     * Verifies that the password is successfully updated.
     */
    @Test
    void resetPassword_ValidToken_Success() {
        // Arrange
        String token = UUID.randomUUID().toString();
        String newPassword = "NewPassword123!";
        User user = new Fencer();
        user.setVerificationToken(token);
        when(userRepository.findByVerificationToken(token)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(newPassword)).thenReturn("hashedPassword");

        // Act
        String result = authenticationService.resetPassword(token, newPassword);

        // Assert
        assertEquals("Password changed", result);
        assertEquals("hashedPassword", user.getPassword());
        assertNull(user.getVerificationToken());
        verify(userRepository).findByVerificationToken(token);
        verify(userRepository).save(user);
    }

    /**
     * Tests the resetPassword method when an invalid token is provided.
     * Verifies that the method returns "User not found".
     */
    @Test
    void resetPassword_InvalidToken_ReturnsInvalidToken() {
        // Arrange
        String token = UUID.randomUUID().toString();
        String newPassword = "NewPassword123!";
        when(userRepository.findByVerificationToken(token)).thenReturn(Optional.empty());

        // Act
        String result = authenticationService.resetPassword(token, newPassword);

        // Assert
        assertEquals("User not found", result);
        verify(userRepository).findByVerificationToken(token);
    }

    /**
     * Tests the resetPassword method when an expired token is provided.
     * Verifies that the method returns "Expired token".
     */
    @Test
    void resetPassword_ExpiredToken_ReturnsExpiredToken() {
        // Arrange
        String token = UUID.randomUUID().toString();
        String newPassword = "NewPassword123!";
        User user = new User();
        user.setVerificationToken(token);
        when(userRepository.findByVerificationToken(token)).thenReturn(Optional.of(user));
        
        // Mock the tokenStillValid method to return false
        User spyUser = Mockito.spy(user);
        Mockito.doReturn(false).when(spyUser).tokenStillValid();
        when(userRepository.findByVerificationToken(token)).thenReturn(Optional.of(spyUser));

        // Act
        String result = authenticationService.resetPassword(token, newPassword);

        // Assert
        assertEquals("Expired token", result);
        verify(userRepository).findByVerificationToken(token);
        Mockito.verify(spyUser).tokenStillValid();
    }
    
}
