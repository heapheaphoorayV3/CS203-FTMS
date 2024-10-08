package cs203.ftms.overall.service.authentication;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import cs203.ftms.overall.dto.RegisterAdminDTO;
import cs203.ftms.overall.dto.RegisterFencerDTO;
import cs203.ftms.overall.dto.RegisterOrganiserDTO;
import cs203.ftms.overall.dto.clean.CleanFencerDTO;
import cs203.ftms.overall.dto.clean.CleanOrganiserDTO;
import cs203.ftms.overall.model.userrelated.Admin;
import cs203.ftms.overall.model.userrelated.Fencer;
import cs203.ftms.overall.model.userrelated.Organiser;
import cs203.ftms.overall.model.userrelated.User;
import cs203.ftms.overall.repository.userrelated.UserRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class AuthenticationService {
    
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationService(UserRepository userRepository, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    public CleanFencerDTO getCleanFencer(Fencer f) {
        return new CleanFencerDTO(f.getId(), f.getName(), f.getEmail(), f.getContactNo(), f.getCountry(), f.getDateOfBirth(), f.getDominantArm(), f.getWeapon(), f.getClub(), f.getPoints(), f.getDebutYear(), f.getGender());
    }

    public CleanOrganiserDTO getCleanOrganiser(Organiser o) {
        return new CleanOrganiserDTO(o.getId(), o.isVerified(), o.getName(), o.getEmail(), o.getContactNo(), o.getCountry());
    }

    public User createFencer(RegisterFencerDTO f) {
        String fencerName = String.format("%s %s", f.getLastName().toUpperCase(), f.getFirstName());
        String passwordH = passwordEncoder.encode(f.getPassword());
        User u = new Fencer(fencerName, f.getEmail(), passwordH, f.getContactNo(), f.getCountry(), f.getDateOfBirth());
        return userRepository.save(u);
    }

    public User createOrganiser(RegisterOrganiserDTO o) {
        String passwordH = passwordEncoder.encode(o.getPassword());
        User u = new Organiser(o.getName(), o.getEmail(), passwordH, o.getContactNo(), o.getCountry());
        ((Organiser) u).setVerified(true);
        return userRepository.save(u);
    }

    public User createAdmin(RegisterAdminDTO a) {
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
}
