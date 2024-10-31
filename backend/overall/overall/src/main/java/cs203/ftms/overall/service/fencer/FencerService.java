package cs203.ftms.overall.service.fencer;

import java.util.Collections;
import java.util.List;

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
import cs203.ftms.overall.repository.userrelated.FencerRepository;
import cs203.ftms.overall.repository.userrelated.UserRepository;
import cs203.ftms.overall.validation.OtherValidations;

@Service
public class FencerService {
    private final UserRepository userRepository; 
    private final FencerRepository fencerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public FencerService(UserRepository userRepository, FencerRepository fencerRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository; 
        this.fencerRepository = fencerRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    

    public CleanFencerDTO getCleanFencerDTO(Fencer f) {
        if (f == null) return null;
        return new CleanFencerDTO(f.getId(), f.getName(), f.getEmail(), f.getContactNo(), f.getCountry(),
        f.getDateOfBirth(), f.getDominantArm(), f.getWeapon(), f.getClub(), f.getPoints(), f.getDebutYear(), f.getGender());
    }

    public Fencer completeProfile(Fencer f, CompleteFencerProfileDTO dto) throws MethodArgumentNotValidException {
        // if (dto.getClub() == null || dto.getDebutYear() != 0 || dto.getDominantArm() != '\u0000' || 
        // dto.getGender() != '\u0000' || dto.getWeapon() != '\u0000') return null; 
        OtherValidations.validDebutYear(f, dto.getDebutYear());
        f.setClub(dto.getClub());
        f.setDebutYear(dto.getDebutYear());
        f.setDominantArm(dto.getDominantArm());
        f.setGender(dto.getGender());
        f.setWeapon(dto.getWeapon());
        return userRepository.save(f);
    }

    // public List<Event> getLastEvents(Fencer f, int count) {
    //     List<TournamentFencer> tfProfiles = new ArrayList<>(f.getTournamentFencerProfiles()); 
    //     List<Event> events = new ArrayList<>(); 
    //     int startIndex = (tfProfiles.size()-count-1 < 0) ? 0 : tfProfiles.size()-count-1;
    //     for (int i = startIndex; i < tfProfiles.size(); i++) {
    //         events.add(tfProfiles.get(i).getEvent());
    //     }
    //     return events;
    // }

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
}