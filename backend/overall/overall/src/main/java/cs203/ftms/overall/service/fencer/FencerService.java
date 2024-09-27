package cs203.ftms.overall.service.fencer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import cs203.ftms.overall.dto.CompleteFencerProfileDTO;
import cs203.ftms.overall.dto.clean.CleanFencerDTO;
import cs203.ftms.overall.model.userrelated.Fencer;
import cs203.ftms.overall.repository.userrelated.UserRepository; 

@Service
public class FencerService {
    private final UserRepository userRepository; 

    @Autowired
    public FencerService(UserRepository userRepository) {
        this.userRepository = userRepository; 
    }

    public CleanFencerDTO getCleanFencerDTO(Fencer f) {
        if (f == null) return null;
        return new CleanFencerDTO(f.getId(), f.getName(), f.getEmail(), f.getContactNo(), f.getCountry(),
        f.getDateOfBirth(), f.getDominantArm(), f.getWeapon(), f.getClub(), f.getPoints(), f.getDebutYear(), f.getGender());
    }

    public Fencer completeProfile(Fencer f, CompleteFencerProfileDTO dto) throws MethodArgumentNotValidException {
        // if (dto.getClub() == null || dto.getDebutYear() != 0 || dto.getDominantArm() != '\u0000' || 
        // dto.getGender() != '\u0000' || dto.getWeapon() != '\u0000') return null; 
        f.setClub(dto.getClub());
        f.setDebutYear(dto.getDebutYear());
        f.setDominantArm(dto.getDominantArm());
        f.setGender(dto.getGender());
        f.setWeapon(dto.getWeapon());
        return userRepository.save(f);
    }

}