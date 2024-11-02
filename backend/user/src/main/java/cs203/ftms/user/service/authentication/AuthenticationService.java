package cs203.ftms.user.service.authentication;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import cs203.ftms.user.dto.RegisterAdminDTO;
import cs203.ftms.user.dto.RegisterFencerDTO;
import cs203.ftms.user.dto.RegisterOrganiserDTO;
import cs203.ftms.user.dto.clean.CleanFencerDTO;
import cs203.ftms.user.dto.clean.CleanOrganiserDTO;
import cs203.ftms.user.exception.UserAlreadyExistException;
import cs203.ftms.user.model.Admin;
import cs203.ftms.user.model.Fencer;
import cs203.ftms.user.model.Organiser;
import cs203.ftms.user.model.User;
import cs203.ftms.user.repository.UserRepository;
import cs203.ftms.user.service.mail.MailService;
import jakarta.persistence.EntityNotFoundException;

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

    public CleanFencerDTO getCleanFencer(Fencer f) {
        return new CleanFencerDTO(f.getId(), f.getName(), f.getEmail(), f.getContactNo(), f.getCountry(), f.getDateOfBirth(), f.getDominantArm(), f.getWeapon(), f.getClub(), f.getPoints(), f.getDebutYear(), f.getGender());
    }

    public CleanOrganiserDTO getCleanOrganiser(Organiser o) {
        return new CleanOrganiserDTO(o.getId(), o.isVerified(), o.getName(), o.getEmail(), o.getContactNo(), o.getCountry());
    }

    public User createFencer(RegisterFencerDTO f) {
        if (userRepository.findByEmail(f.getEmail()).orElse(null) != null) {
            throw new UserAlreadyExistException(f.getEmail() + " already exists!");
        }
        String fencerName = String.format("%s %s", f.getLastName().toUpperCase(), f.getFirstName());
        String passwordH = passwordEncoder.encode(f.getPassword());
        User u = new Fencer(fencerName, f.getEmail(), passwordH, f.getContactNo(), f.getCountry(), f.getDateOfBirth());
        return userRepository.save(u);
    }

    public User createOrganiser(RegisterOrganiserDTO o) {
        if (userRepository.findByEmail(o.getEmail()).orElse(null) != null) {
            throw new UserAlreadyExistException(o.getEmail() + " already exists!");
        }
        String passwordH = passwordEncoder.encode(o.getPassword());
        User u = new Organiser(o.getName(), o.getEmail(), passwordH, o.getContactNo(), o.getCountry());
        // ((Organiser) u).setVerified(true);
        userRepository.save(u);
        mailService.sendMail(u.getEmail(), "Account Needs to be Verified", "Your account needs to be verified by the administrator, please wait while we process your registration!");
        return u;
    }

    public User createAdmin(RegisterAdminDTO a) {
        if (userRepository.findByEmail(a.getEmail()).orElse(null) != null) {
            throw new UserAlreadyExistException(a.getEmail() + " already exists!");
        }
        String passwordH = passwordEncoder.encode(a.getPassword());
        User u = new Admin(a.getName(), a.getEmail(), passwordH, a.getContactNo(), a.getCountry());
        return userRepository.save(u);
    }

    public User authenticateUser(String email, String password) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(email, password));
        return userRepository.findByEmail(email).orElse(null);
    }

    public User getUser(int id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User with ID " + id + " not found"));
    }


    public String forgetPassword(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return "User not found";
        }
        String token = UUID.randomUUID().toString();
        user.setVerificationToken(token);
        userRepository.save(user);
        String content = String.format("Please reset your password at http://%s/reset-password/%s \nThe token will expire in 15 minutes.", frontendSource, token);
        mailService.sendMail(email, "Reset Password", content);
        return "Email sent";
    }

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
