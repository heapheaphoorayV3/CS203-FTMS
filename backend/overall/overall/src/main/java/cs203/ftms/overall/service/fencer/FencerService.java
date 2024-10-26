package cs203.ftms.overall.service.fencer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import cs203.ftms.overall.dto.CompleteFencerProfileDTO;
import cs203.ftms.overall.dto.clean.CleanFencerDTO;
import cs203.ftms.overall.model.tournamentrelated.Event;
import cs203.ftms.overall.model.tournamentrelated.TournamentFencer;
import cs203.ftms.overall.model.userrelated.Fencer;
import cs203.ftms.overall.repository.userrelated.FencerRepository;
import cs203.ftms.overall.repository.userrelated.UserRepository;
import cs203.ftms.overall.validation.OtherValidations; 
import cs203.ftms.overall.comparator.FencerPointsComparator;

@Service
public class FencerService {
    private final UserRepository userRepository; 
    private final FencerRepository fencerRepository;

    @Autowired
    public FencerService(UserRepository userRepository, FencerRepository fencerRepository) {
        this.userRepository = userRepository; 
        this.fencerRepository = fencerRepository;
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

    public List<Event> getLastEvents(Fencer f, int count) {
        List<TournamentFencer> tfProfiles = new ArrayList<>(f.getTournamentFencerProfiles()); 
        List<Event> events = new ArrayList<>(); 
        int startIndex = (tfProfiles.size()-count-1 < 0) ? 0 : tfProfiles.size()-count-1;
        for (int i = startIndex; i < tfProfiles.size(); i++) {
            events.add(tfProfiles.get(i).getEvent());
        }
        return events;
    }

    public List<Fencer> getInternationalRank(){
        List<Fencer> fencers = fencerRepository.findAll();
        Collections.sort(fencers, new FencerPointsComparator());
        return fencers;
    }
}