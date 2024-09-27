package cs203.ftms.overall.service.fencer;

import java.time.Year;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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
        validDebutYear(f, dto.getDebutYear());
        f.setClub(dto.getClub());
        f.setDebutYear(dto.getDebutYear());
        f.setDominantArm(dto.getDominantArm());
        f.setGender(dto.getGender());
        f.setWeapon(dto.getWeapon());
        return userRepository.save(f);
    }

    public void validDebutYear(Fencer f, int year) throws MethodArgumentNotValidException {
        int dobyear = f.getDateOfBirth().getYear();
        int currentyear = Year.now().getValue();
        if ((year < dobyear + 8) || year > currentyear) {
            BindingResult bindingResult = new BeanPropertyBindingResult(year, "debutYear");
            bindingResult.addError(new FieldError("debutYear", "debutYear", "The debut year must be at least 8 years after the birth year."));
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
    }


}