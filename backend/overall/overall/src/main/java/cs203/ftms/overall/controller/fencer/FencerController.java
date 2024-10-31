package cs203.ftms.overall.controller.fencer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cs203.ftms.overall.dto.ChangePasswordDTO;
import cs203.ftms.overall.dto.CompleteFencerProfileDTO;
import cs203.ftms.overall.dto.UpdateFencerProfileDTO;
import cs203.ftms.overall.dto.clean.CleanFencerDTO;
import cs203.ftms.overall.model.userrelated.Fencer;
import cs203.ftms.overall.model.userrelated.User;
import cs203.ftms.overall.service.fencer.FencerService;
import jakarta.validation.Valid;



@RestController
@CrossOrigin
@RequestMapping("/api/v1/fencer")
public class FencerController {
    private final FencerService fencerService;

    @Autowired
    public FencerController(FencerService fencerService) {
        this.fencerService = fencerService;
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('FENCER')")
    public ResponseEntity<CleanFencerDTO> getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        CleanFencerDTO cf = fencerService.getCleanFencerDTO((Fencer) user);
        if (cf == null) return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(cf, HttpStatus.OK);
    }
    
    @PutMapping("/complete-profile")
    @PreAuthorize("hasRole('FENCER')")
    public ResponseEntity<String> completeProfile(@Valid @RequestBody CompleteFencerProfileDTO dto) throws MethodArgumentNotValidException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Fencer f = fencerService.completeProfile((Fencer) user, dto);
        if (f != null) {
            return new ResponseEntity<>("fencer profile completed successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("fencer profile completion unsuccessful", HttpStatus.BAD_REQUEST);
    }
    
    @GetMapping("/international-ranking")
    @PreAuthorize("hasAnyRole('FENCER', 'ORGANISER', 'ADMIN')")
    public ResponseEntity<List<CleanFencerDTO>> getInternationalRanking() {
        List<Fencer> fencers = fencerService.getInternationalRank(); 
        List<CleanFencerDTO> res = new ArrayList<>();
        for (Fencer fencer : fencers) {
            res.add(fencerService.getCleanFencerDTO(fencer));
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PutMapping("/change-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        String res = fencerService.changePassword(user, changePasswordDTO.getOldPassword(), changePasswordDTO.getNewPassword());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PutMapping("/update-profile")
    @PreAuthorize("hasRole('ORGANISER')")
    public ResponseEntity<String> updateProfile(@RequestBody UpdateFencerProfileDTO dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        fencerService.updateProfile((Fencer) user, dto);
        return new ResponseEntity<>("Profile updated sucessfully!", HttpStatus.OK);
    }
}