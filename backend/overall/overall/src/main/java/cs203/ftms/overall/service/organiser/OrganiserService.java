package cs203.ftms.overall.service.organiser;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

/**
 * Service class for managing organiser-related operations.
 * Provides methods for handling organiser profiles, tournaments, and authentication.
 */
@Service
public class OrganiserService {
    private final TournamentRepository tournamentRepository;
    private final PasswordEncoder passwordEncoder; 
    private final OrganiserRepository organiserRepository;
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;

    @Autowired
    public OrganiserService(TournamentRepository tournamentRepository, PasswordEncoder passwordEncoder, 
                            UserRepository userRepository, OrganiserRepository organiserRepository, 
                            AuthenticationService authenticationService) {
        this.tournamentRepository = tournamentRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.organiserRepository = organiserRepository;
        this.authenticationService = authenticationService;
    }

    /**
     * Converts an Organiser entity into a CleanOrganiserDTO.
     *
     * @param o the Organiser entity to convert
     * @return a CleanOrganiserDTO containing cleaned organiser details
     * @throws EntityNotFoundException if the organiser is null
     */
    public CleanOrganiserDTO getCleanOrganiserDTO(Organiser o) {
        if (o == null) throw new EntityNotFoundException("Organiser cannot be null");
        return new CleanOrganiserDTO(o.getId(), o.isVerified(), o.getName(), o.getEmail(), o.getContactNo(), o.getCountry());
    }

    /**
     * Retrieves all organisers from the repository.
     *
     * @return a list of Organiser entities
     */
    public List<Organiser> getAllOrganisers() {
        return organiserRepository.findAll();
    }

    /**
     * Retrieves all tournaments managed by a specific organiser.
     *
     * @param o the Organiser whose tournaments are to be retrieved
     * @return a sorted list of Tournament entities
     */
    public List<Tournament> getOrganiserTournaments(Organiser o) {
        List<Tournament> tournaments = tournamentRepository.findByOrganiserId(o.getId());
        tournaments.sort((t1, t2) -> t1.getStartDate().compareTo(t2.getStartDate()));
        return tournaments;
    }

    /**
     * Changes the password of an organiser.
     *
     * @param u the User whose password is to be changed
     * @param oldPassword the current password
     * @param newPassword the new password
     * @return a message indicating the result of the password change
     */
    public String changePassword(User u, String oldPassword, String newPassword) {
        User verifiedUser = authenticationService.authenticateUser(u.getEmail(), oldPassword);
        if (verifiedUser == null) {
            return "old password is incorrect";
        }
        u.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(u);
        return "password changed successfully";
    }

    /**
     * Updates the profile of an organiser with new details.
     *
     * @param o the Organiser whose profile is to be updated
     * @param dto the DTO containing updated profile details
     */
    public void updateProfile(Organiser o, UpdateOrganiserProfileDTO dto) {
        o.setContactNo(dto.getContactNo());
        o.setCountry(dto.getCountry());
        o.setEmail(dto.getEmail());
        o.setName(dto.getName());
        userRepository.save(o);
    }

    /**
     * Retrieves upcoming tournaments managed by an organiser.
     *
     * @param o the Organiser whose upcoming tournaments are to be retrieved
     * @return a sorted list of upcoming Tournament entities
     */
    public List<Tournament> getOrganiserUpcomingTournaments(Organiser o) {
        List<Tournament> tournaments = tournamentRepository.findByOrganiserId(o.getId());
        List<Tournament> upcomingTournaments = new ArrayList<>();
        for (Tournament t : tournaments) {
            if (t.getStartDate().isAfter(LocalDate.now())) {
                upcomingTournaments.add(t);
            }
        }
        upcomingTournaments.sort((t1, t2) -> t1.getStartDate().compareTo(t2.getStartDate()));
        return upcomingTournaments;
    }

    /**
     * Retrieves past tournaments managed by an organiser.
     *
     * @param o the Organiser whose past tournaments are to be retrieved
     * @return a sorted list of past Tournament entities
     */
    public List<Tournament> getOrganiserPastTournaments(Organiser o) {
        List<Tournament> tournaments = tournamentRepository.findByOrganiserId(o.getId());
        List<Tournament> pastTournaments = new ArrayList<>();
        for (Tournament t : tournaments) {
            if (t.getStartDate().isBefore(LocalDate.now())) {
                pastTournaments.add(t);
            }
        }
        pastTournaments.sort((t1, t2) -> t1.getStartDate().compareTo(t2.getStartDate()));
        return pastTournaments;
    }
}
