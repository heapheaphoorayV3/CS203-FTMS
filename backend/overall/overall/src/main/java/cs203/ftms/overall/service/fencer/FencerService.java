package cs203.ftms.overall.service.fencer;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import cs203.ftms.overall.comparator.FencerPointsComparator;
import cs203.ftms.overall.dto.CompleteFencerProfileDTO;
import cs203.ftms.overall.dto.UpdateFencerProfileDTO;
import cs203.ftms.overall.dto.clean.CleanFencerDTO;
import cs203.ftms.overall.model.userrelated.Fencer;
import cs203.ftms.overall.model.userrelated.User;
import cs203.ftms.overall.model.tournamentrelated.TournamentFencer;
import cs203.ftms.overall.model.tournamentrelated.Event;
import cs203.ftms.overall.repository.tournamentrelated.EventRepository;
import cs203.ftms.overall.repository.userrelated.FencerRepository;
import cs203.ftms.overall.repository.userrelated.UserRepository;
import cs203.ftms.overall.validation.OtherValidations;

@Service
public class FencerService {
    private final UserRepository userRepository; 
    private final FencerRepository fencerRepository;
    private final EventRepository eventRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public FencerService(UserRepository userRepository, FencerRepository fencerRepository, EventRepository eventRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository; 
        this.fencerRepository = fencerRepository;
        this.eventRepository = eventRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public CleanFencerDTO getCleanFencerDTO(Fencer f) {
        if (f == null) return null;
        return new CleanFencerDTO(f.getId(), f.getName(), f.getEmail(), f.getContactNo(), f.getCountry(),
        f.getDateOfBirth(), f.getDominantArm(), f.getWeapon(), f.getClub(), f.getPoints(), f.getDebutYear(), f.getGender());
    }

    public Fencer completeProfile(Fencer f, CompleteFencerProfileDTO dto) throws MethodArgumentNotValidException {
        OtherValidations.validDebutYear(f, dto.getDebutYear());
        f.setClub(dto.getClub());
        f.setDebutYear(dto.getDebutYear());
        f.setDominantArm(dto.getDominantArm());
        f.setGender(dto.getGender());
        f.setWeapon(dto.getWeapon());
        return userRepository.save(f);
    }

    public List<Fencer> getInternationalRank(){
        List<Fencer> fencers = fencerRepository.findAll();
        Collections.sort(fencers, new FencerPointsComparator());
        return fencers;
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

    public void updateProfile(Fencer f, UpdateFencerProfileDTO dto) {
        f.setClub(dto.getClub());
        f.setContactNo(dto.getContactNo());
        f.setCountry(dto.getCountry());
        f.setEmail(dto.getEmail());
        f.setName(dto.getName());
        f.setDominantArm(dto.getDominantArm());
        userRepository.save(f);
    }

    public List<Event> getFencerEvents(Fencer f) {
        List<Event> events = new ArrayList<>();
        for(TournamentFencer tf: f.getTournamentFencerProfiles()){
            events.add(eventRepository.findById(tf.getEvent().getId()).orElse(null));
        }
        return events;
    }
}