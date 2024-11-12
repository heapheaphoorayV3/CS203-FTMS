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
import cs203.ftms.overall.repository.userrelated.OrganiserRepository;
import cs203.ftms.overall.repository.userrelated.UserRepository;
import cs203.ftms.overall.service.authentication.AuthenticationService;
import jakarta.persistence.EntityNotFoundException;

@Service
public class OrganiserService {
    private final TournamentRepository tournamentRepository;
    private final PasswordEncoder passwordEncoder; 
    private final OrganiserRepository organiserRepository;
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;

    @Autowired
    public OrganiserService(TournamentRepository tournamentRepository, PasswordEncoder passwordEncoder, UserRepository userRepository, OrganiserRepository organiserRepository, AuthenticationService authenticationService) {
        this.tournamentRepository = tournamentRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.organiserRepository = organiserRepository;
        this.authenticationService = authenticationService;
    }

    public CleanOrganiserDTO getCleanOrganiserDTO(Organiser o) {
        if (o == null) throw new EntityNotFoundException("Organiser cannot be null");
        return new CleanOrganiserDTO(o.getId(), o.isVerified(), o.getName(), o.getEmail(), o.getContactNo(), o.getCountry());
    }

    public List<Organiser> getAllOrganisers() {
        return organiserRepository.findAll();
    }

    public List<Tournament> getOrganiserTournaments(Organiser o) {
        return tournamentRepository.findByOrganiserId(o.getId());
    }

    public String changePassword(User u, String oldPassword, String newPassword) {
        User verifiedUser = authenticationService.authenticateUser(u.getEmail(), oldPassword);
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
        List<Tournament> tournaments = tournamentRepository.findByOrganiserId(o.getId());
        List<Tournament> upcomingTournaments = new ArrayList<>();
        for (Tournament t : tournaments) {
            if (t.getStartDate().isAfter(LocalDate.now())) {
                upcomingTournaments.add(t);
            }
        }
        return upcomingTournaments;
    }

    public List<Tournament> getOrganiserPastTournaments(Organiser o) {
        List<Tournament> tournaments = tournamentRepository.findByOrganiserId(o.getId());
        List<Tournament> pastTournaments = new ArrayList<>();
        for (Tournament t : tournaments) {
            if (t.getStartDate().isBefore(LocalDate.now())) {
                pastTournaments.add(t);
            }
        }
        return pastTournaments;
    }
}
