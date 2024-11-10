package cs203.ftms.overall.service.fencer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

@Service
public class FencerService {
    private final UserRepository userRepository; 
    private final FencerRepository fencerRepository;
    private final EventRepository eventRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationService authenticationService;

    @Autowired
    public FencerService(UserRepository userRepository, FencerRepository fencerRepository, EventRepository eventRepository, PasswordEncoder passwordEncoder, AuthenticationService authenticationService) {
        this.userRepository = userRepository; 
        this.fencerRepository = fencerRepository;
        this.eventRepository = eventRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationService = authenticationService;
    }

    public CleanTournamentFencerDTO getCleanTournamentFencerDTO(TournamentFencer tf) {
        if (tf == null) return null;
        return new CleanTournamentFencerDTO(tf.getId(), tf.getFencer().getId(), tf.getFencer().getName(), tf.getFencer().getClub(), tf.getFencer().getCountry(),
        tf.getFencer().getDominantArm(), tf.getTournamentRank(), tf.getEvent().getId(), tf.getPouleWins(), tf.getPoulePoints());
    }

    public CleanFencerDTO getCleanFencerDTO(Fencer f) {
        if (f == null) return null;
        return new CleanFencerDTO(f.getId(), f.getName(), f.getEmail(), f.getContactNo(), f.getCountry(), f.getDateOfBirth(), f.getDominantArm(),f.getWeapon(), f.getClub(),f.getPoints(), f.getDebutYear(), f.getGender());
    }

    public List<Fencer> getAllFencers() {
        return fencerRepository.findAll();
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

    public String changePassword(User u, String oldPassword, String newPassword) {
        User verifiedUser = authenticationService.authenticateUser(u.getEmail(), oldPassword);
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
    
    public List<TournamentFencer> getFencerPastEventsProfiles(Fencer f){
        List<TournamentFencer> profiles = new ArrayList<>();
        for(TournamentFencer tf: f.getTournamentFencerProfiles()){
            if(tf.getEvent().getDate().isBefore(LocalDate.now())){
                profiles.add(tf);
            }
        }
        return profiles;
    }

    public List<Event> getFencerEvents(Fencer f) {
        List<Event> events = new ArrayList<>();
        for(TournamentFencer tf: f.getTournamentFencerProfiles()){
            events.add(eventRepository.findById(tf.getEvent().getId()).orElse(null));
        }
        return events;
    }

    public List<Event> getFencerUpcomingEvents(Fencer f) {
        List<Event> events = new ArrayList<>();
        for(TournamentFencer tf: f.getTournamentFencerProfiles()){
            Event e = eventRepository.findById(tf.getEvent().getId()).orElse(null);
            if(e.getDate().isAfter(LocalDate.now())){
                events.add(e);
            }
        }
        return events;
    }

    public List<Event> getFencerPastEvents(Fencer f) {
        List<Event> events = new ArrayList<>();
        for(TournamentFencer tf: f.getTournamentFencerProfiles()){
            Event e = eventRepository.findById(tf.getEvent().getId()).orElse(null);
            if(e.getDate().isBefore(LocalDate.now())){
                events.add(e);
            }
        }
        return events;
    }

    public List<Fencer> getFilterdInternationalRank(char weapon, char gender){
        List<Fencer> fencers = fencerRepository.findAll();
        Collections.sort(fencers, new FencerPointsComparator());
        List<Fencer> filteredFencers = new ArrayList<>();
        for(Fencer f: fencers){
            if(f.getWeapon() == weapon && f.getGender() == gender){
                filteredFencers.add(f);
            }
        }
        return filteredFencers;
    }

    
}