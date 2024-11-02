package cs203.ftms.overall.service.organiser;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import cs203.ftms.overall.dto.UpdateOrganiserProfileDTO;
import cs203.ftms.overall.dto.clean.CleanOrganiserDTO;
import cs203.ftms.overall.model.tournamentrelated.Tournament;
import cs203.ftms.overall.model.userrelated.Organiser;
import cs203.ftms.overall.model.userrelated.User;
import cs203.ftms.overall.repository.tournamentrelated.TournamentRepository;
import cs203.ftms.overall.repository.userrelated.UserRepository;

@Service
public class OrganiserService {
    private final TournamentRepository tournamentRepository;
    private final PasswordEncoder passwordEncoder; 
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public OrganiserService(TournamentRepository tournamentRepository, PasswordEncoder passwordEncoder, UserRepository userRepository, AuthenticationManager authenticationManager) {
        this.tournamentRepository = tournamentRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
    }

    public CleanOrganiserDTO getCleanOrganiserDTO(Organiser o) {
        if (o == null) return null;
        return new CleanOrganiserDTO(o.getId(), o.isVerified(), o.getName(), o.getEmail(), o.getContactNo(), o.getCountry());
    }

    public List<Tournament> getOrganiserTournaments(Organiser o) {
        return tournamentRepository.findByOrganiserId(o.getId()).orElse(null);
    }

    private User authenticateUser(String email, String password) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(email, password));
        return userRepository.findByEmail(email).orElse(null);
    }

    public String changePassword(User u, String oldPassword, String newPassword) {
        User verifiedUser = authenticateUser(u.getEmail(), oldPassword);
        if (verifiedUser == null) {
            return "old password is incorrect";
        }
        u.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(u);
        return "password changed successfully";
    }

    public void updateProfile(Organiser o, UpdateOrganiserProfileDTO dto) {
        o.setContactNo(dto.getContactNo());
        o.setCountry(dto.getCountry());
        o.setEmail(dto.getEmail());
        o.setName(dto.getName());
        userRepository.save(o);
    }

    public List<Tournament> getOrganiserUpcomingTournaments(Organiser o) {
        List<Tournament> tournaments = tournamentRepository.findByOrganiserId(o.getId()).orElse(null);
        List<Tournament> upcomingTournaments = new ArrayList<>();
        for (Tournament t : tournaments) {
            if (t.getStartDate().isAfter(LocalDate.now())) {
                upcomingTournaments.add(t);
            }
        }
        return upcomingTournaments;
    }

    public List<Tournament> getOrganiserPastTournaments(Organiser o) {
        List<Tournament> tournaments = tournamentRepository.findByOrganiserId(o.getId()).orElse(null);
        List<Tournament> pastTournaments = new ArrayList<>();
        for (Tournament t : tournaments) {
            if (t.getStartDate().isBefore(LocalDate.now())) {
                pastTournaments.add(t);
            }
        }
        return pastTournaments;
    }
}
