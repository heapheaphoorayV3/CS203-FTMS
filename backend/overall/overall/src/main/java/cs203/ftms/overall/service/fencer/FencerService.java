package cs203.ftms.overall.service.fencer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import cs203.ftms.overall.comparator.FencerPointsComparator;
import cs203.ftms.overall.dto.CompleteFencerProfileDTO;
import cs203.ftms.overall.dto.UpdateFencerProfileDTO;
import cs203.ftms.overall.dto.clean.CleanFencerDTO;
import cs203.ftms.overall.dto.clean.CleanTournamentFencerDTO;
import cs203.ftms.overall.model.tournamentrelated.Event;
import cs203.ftms.overall.model.tournamentrelated.TournamentFencer;
import cs203.ftms.overall.model.userrelated.Fencer;
import cs203.ftms.overall.model.userrelated.User;
import cs203.ftms.overall.repository.tournamentrelated.EventRepository;
import cs203.ftms.overall.repository.userrelated.FencerRepository;
import cs203.ftms.overall.repository.userrelated.UserRepository;
import cs203.ftms.overall.service.authentication.AuthenticationService;
import cs203.ftms.overall.validation.OtherValidations;

/**
 * Service class for managing fencer-related operations.
 * Handles profile updates, event retrieval, ranking, and password management for fencers.
 */
@Service
public class FencerService {
    private final UserRepository userRepository; 
    private final FencerRepository fencerRepository;
    private final EventRepository eventRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationService authenticationService;

    @Autowired
    public FencerService(UserRepository userRepository, FencerRepository fencerRepository, EventRepository eventRepository, 
                         PasswordEncoder passwordEncoder, AuthenticationService authenticationService) {
        this.userRepository = userRepository; 
        this.fencerRepository = fencerRepository;
        this.eventRepository = eventRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationService = authenticationService;
    }

    /**
     * Converts a TournamentFencer entity into a CleanTournamentFencerDTO.
     *
     * @param tf the TournamentFencer entity to convert
     * @return a CleanTournamentFencerDTO containing fencer details for the tournament
     */
    public CleanTournamentFencerDTO getCleanTournamentFencerDTO(TournamentFencer tf) {
        if (tf == null) return null;
        return new CleanTournamentFencerDTO(tf.getId(), tf.getFencer().getId(), tf.getFencer().getName(), tf.getFencer().getClub(), tf.getFencer().getCountry(),
        tf.getFencer().getDominantArm(), tf.getTournamentRank(), tf.getEvent().getId(), tf.getPouleWins(), tf.getPoulePoints(), tf.getPointsAfterEvent());
    }

    /**
     * Converts a Fencer entity into a CleanFencerDTO.
     *
     * @param f the Fencer entity to convert
     * @return a CleanFencerDTO containing fencer details
     */
    public CleanFencerDTO getCleanFencerDTO(Fencer f) {
        if (f == null) return null;
        return new CleanFencerDTO(f.getId(), f.getName(), f.getEmail(), f.getContactNo(), f.getCountry(), f.getDateOfBirth(), f.getDominantArm(),
                                   f.getWeapon(), f.getClub(), f.getPoints(), f.getDebutYear(), f.getGender());
    }

    /**
     * Retrieves all fencers from the repository.
     *
     * @return a list of all Fencer entities
     */
    public List<Fencer> getAllFencers() {
        return fencerRepository.findAll();
    }

    /**
     * Completes a fencer's profile with additional details.
     *
     * @param f the Fencer whose profile is to be completed
     * @param dto the DTO containing additional profile details
     * @return the updated Fencer entity
     * @throws MethodArgumentNotValidException if the debut year is invalid
     */
    public Fencer completeProfile(Fencer f, CompleteFencerProfileDTO dto) throws MethodArgumentNotValidException {
        OtherValidations.validDebutYear(f, dto.getDebutYear());
        f.setClub(dto.getClub());
        f.setDebutYear(dto.getDebutYear());
        f.setDominantArm(dto.getDominantArm());
        f.setGender(dto.getGender());
        f.setWeapon(dto.getWeapon());
        return userRepository.save(f);
    }

    /**
     * Retrieves the international rank of a fencer.
     *
     * @param f the Fencer whose rank is to be retrieved
     * @return the international rank, or -1 if the fencer is not ranked
     */
    public int getInternationalRank(Fencer f) {
        List<Fencer> fencers = getFilterdInternationalRank(f.getWeapon(), f.getGender());
        for (int i = 1; i <= fencers.size(); i++) {
            if (fencers.get(i - 1).equals(f)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Changes the password of a user.
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
     * Updates a fencer's profile with new details.
     *
     * @param f the Fencer whose profile is to be updated
     * @param dto the DTO containing the updated profile details
     */
    public void updateProfile(Fencer f, UpdateFencerProfileDTO dto) {
        f.setClub(dto.getClub());
        f.setContactNo(dto.getContactNo());
        f.setCountry(dto.getCountry());
        f.setEmail(dto.getEmail());
        f.setName(dto.getName());
        f.setDominantArm(dto.getDominantArm());
        userRepository.save(f);
    }
    
    /**
     * Retrieves past event profiles of a fencer.
     *
     * @param f the Fencer whose past event profiles are to be retrieved
     * @return a list of TournamentFencer profiles for past events
     */
    public List<TournamentFencer> getFencerPastEventsProfiles(Fencer f) {
        List<TournamentFencer> profiles = new ArrayList<>();
        for (TournamentFencer tf : f.getTournamentFencerProfiles()) {
            if (tf.getEvent().getDate().isBefore(LocalDate.now())) {
                profiles.add(tf);
            }
        }
        return profiles;
    }

    /**
     * Retrieves all events a fencer has participated in.
     *
     * @param f the Fencer whose events are to be retrieved
     * @return a list of Event entities
     */
    public List<Event> getFencerEvents(Fencer f) {
        List<Event> events = new ArrayList<>();
        for (TournamentFencer tf : f.getTournamentFencerProfiles()) {
            events.add(eventRepository.findById(tf.getEvent().getId()).orElse(null));
        }
        events.sort(Comparator.comparing(Event::getDate));
        return events;
    }

    /**
     * Retrieves upcoming events a fencer is registered for.
     *
     * @param f the Fencer whose upcoming events are to be retrieved
     * @return a list of Event entities for upcoming events
     */
    public List<Event> getFencerUpcomingEvents(Fencer f) {
        List<Event> events = new ArrayList<>();
        for (TournamentFencer tf : f.getTournamentFencerProfiles()) {
            Event e = eventRepository.findById(tf.getEvent().getId()).orElse(null);
            if (e.getDate().isAfter(LocalDate.now())) {
                events.add(e);
            }
        }
        events.sort(Comparator.comparing(Event::getDate));
        return events;
    }

    /**
     * Retrieves past events a fencer participated in.
     *
     * @param f the Fencer whose past events are to be retrieved
     * @return a list of Event entities for past events
     */
    public List<Event> getFencerPastEvents(Fencer f) {
        List<Event> events = new ArrayList<>();
        for (TournamentFencer tf : f.getTournamentFencerProfiles()) {
            Event e = eventRepository.findById(tf.getEvent().getId()).orElse(null);
            if (e.getDate().isBefore(LocalDate.now()) || e.isOver()) {
                events.add(e);
            }
        }
        events.sort(Comparator.comparing(Event::getDate));
        return events;
    }

    /**
     * Retrieves a filtered list of fencers for international ranking.
     *
     * @param weapon the weapon type to filter by
     * @param gender the gender to filter by
     * @return a sorted list of Fencers based on points
     */
    public List<Fencer> getFilterdInternationalRank(char weapon, char gender) {
        List<Fencer> fencers = fencerRepository.findAll();
        Collections.sort(fencers, new FencerPointsComparator());
        List<Fencer> filteredFencers = new ArrayList<>();
        for (Fencer f : fencers) {
            if (f.getWeapon() == weapon && f.getGender() == gender) {
                filteredFencers.add(f);
            }
        }
        return filteredFencers;
    }

    /**
     * Retrieves a fencer's past event points as DTOs.
     *
     * @param f the Fencer whose past event points are to be retrieved
     * @return a list of CleanTournamentFencerDTO for past events
     */
    public List<CleanTournamentFencerDTO> getFencerPastEventsPoints(Fencer f) {
        Set<TournamentFencer> tfs = f.getTournamentFencerProfiles();
        List<TournamentFencer> tfList = new ArrayList<>(tfs);
        Collections.sort(tfList, (a, b) -> a.getEvent().getDate().compareTo(b.getEvent().getDate()));
        List<CleanTournamentFencerDTO> res = new ArrayList<>();
        for (TournamentFencer tf : tfList) {
            if (tf.getEvent().getDate().isBefore(LocalDate.now()) || tf.getEvent().isOver()) {
                res.add(getCleanTournamentFencerDTO(tf));
            }
        }
        return res;
    }
}
