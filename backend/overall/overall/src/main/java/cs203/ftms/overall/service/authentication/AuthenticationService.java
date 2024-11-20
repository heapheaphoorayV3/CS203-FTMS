package cs203.ftms.overall.service.authentication;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
import cs203.ftms.overall.service.admin.MailService;
import jakarta.persistence.EntityNotFoundException;

/**
 * Service class for managing authentication and user-related operations.
 * Handles user registration, authentication, password management, and retrieval of user information.
 */
@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService; 

    @Value("${frontend.source}")
    private String frontendSource;

    @Autowired
    public AuthenticationService(UserRepository userRepository, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, MailService mailService) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
    }

    /**
     * Converts a Fencer entity into a clean data transfer object (DTO).
     *
     * @param f the Fencer entity to convert
     * @return a CleanFencerDTO containing non-sensitive Fencer information
     */
    public CleanFencerDTO getCleanFencer(Fencer f) {
        return new CleanFencerDTO(f.getId(), f.getName(), f.getEmail(), f.getContactNo(), f.getCountry(), f.getDateOfBirth(), f.getDominantArm(), f.getWeapon(), f.getClub(), f.getPoints(), f.getDebutYear(), f.getGender());
    }

    /**
     * Converts an Organiser entity into a clean data transfer object (DTO).
     *
     * @param o the Organiser entity to convert
     * @return a CleanOrganiserDTO containing non-sensitive Organiser information
     */
    public CleanOrganiserDTO getCleanOrganiser(Organiser o) {
        return new CleanOrganiserDTO(o.getId(), o.isVerified(), o.getName(), o.getEmail(), o.getContactNo(), o.getCountry());
    }

    /**
     * Creates and registers a new Fencer user.
     *
     * @param f the registration details of the Fencer
     * @return the created User entity
     * @throws UserAlreadyExistException if a user with the given email already exists
     */
    public User createFencer(RegisterFencerDTO f) {
        if (userRepository.findByEmail(f.getEmail()).orElse(null) != null) {
            throw new UserAlreadyExistException(f.getEmail() + " already exists!");
        }
        String fencerName = String.format("%s %s", f.getLastName().toUpperCase(), f.getFirstName());
        String passwordH = passwordEncoder.encode(f.getPassword());
        User u = new Fencer(fencerName, f.getEmail(), passwordH, f.getContactNo(), f.getCountry(), f.getDateOfBirth());
        return userRepository.save(u);
    }

    /**
     * Creates and registers a new Organiser user.
     *
     * @param o the registration details of the Organiser
     * @return the created User entity
     * @throws UserAlreadyExistException if a user with the given email already exists
     */
    public User createOrganiser(RegisterOrganiserDTO o) {
        if (userRepository.findByEmail(o.getEmail()).orElse(null) != null) {
            throw new UserAlreadyExistException(o.getEmail() + " already exists!");
        }
        String passwordH = passwordEncoder.encode(o.getPassword());
        User u = new Organiser(o.getName(), o.getEmail(), passwordH, o.getContactNo(), o.getCountry());
        userRepository.save(u);
        mailService.sendMail(u.getEmail(), "Account Needs to be Verified", "Your account needs to be verified by the administrator, please wait while we process your registration!");
        return u;
    }

    /**
     * Creates and registers a new Admin user.
     *
     * @param a the registration details of the Admin
     * @return the created User entity
     * @throws UserAlreadyExistException if a user with the given email already exists
     */
    public User createAdmin(RegisterAdminDTO a) {
        if (userRepository.findByEmail(a.getEmail()).orElse(null) != null) {
            throw new UserAlreadyExistException(a.getEmail() + " already exists!");
        }
        String passwordH = passwordEncoder.encode(a.getPassword());
        User u = new Admin(a.getName(), a.getEmail(), passwordH, a.getContactNo(), a.getCountry());
        return userRepository.save(u);
    }

    /**
     * Authenticates a user based on their email and password.
     *
     * @param email the email address of the user
     * @param password the password of the user
     * @return the authenticated User entity
     */
    public User authenticateUser(String email, String password) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(email, password));
        return userRepository.findByEmail(email).orElse(null);
    }

    /**
     * Retrieves a User entity by its ID.
     *
     * @param id the ID of the user
     * @return the User entity
     * @throws EntityNotFoundException if no user with the given ID exists
     */
    public User getUser(int id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User with ID " + id + " not found"));
    }

    /**
     * Handles the "forgot password" process by generating a reset token and sending an email.
     *
     * @param email the email address of the user
     * @return a message indicating the status of the operation
     */
    public String forgetPassword(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return "User not found";
        }
        String token = UUID.randomUUID().toString();
        user.setVerificationToken(token);
        userRepository.save(user);
        String content = String.format("Please reset your password at %s/reset-password/%s \nThe token will expire in 15 minutes.", frontendSource, token);
        mailService.sendMail(email, "Reset Password", content);
        return "Email sent";
    }

    /**
     * Resets a user's password using a verification token.
     *
     * @param token the verification token
     * @param newPassword the new password to set
     * @return a message indicating the status of the operation
     */
    public String resetPassword(String token, String newPassword) {
        User user = userRepository.findByVerificationToken(token).orElse(null);
        if (user == null) {
            return "User not found";
        } else if (!user.tokenStillValid()) {
            return "Expired token";
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setVerificationToken(null);
        userRepository.save(user);
        return "Password changed";
    }
}
