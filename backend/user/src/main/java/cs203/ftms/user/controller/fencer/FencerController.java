package cs203.ftms.user.controller.fencer;

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

import cs203.ftms.tournament.model.tournamentrelated.Event;
import cs203.ftms.user.dto.ChangePasswordDTO;
import cs203.ftms.user.dto.CompleteFencerProfileDTO;
import cs203.ftms.user.dto.UpdateFencerProfileDTO;
import cs203.ftms.user.dto.clean.CleanEventDTO;
import cs203.ftms.user.dto.clean.CleanFencerDTO;
import cs203.ftms.user.model.Fencer;
import cs203.ftms.user.model.User;
import cs203.ftms.user.service.event.EventService;
import cs203.ftms.user.service.fencer.FencerService;
import jakarta.validation.Valid;



@RestController
@CrossOrigin
@RequestMapping("/api/v1/fencer")
public class FencerController {
    private final FencerService fencerService;
    private final EventService eventService;

    @Autowired
    public FencerController(FencerService fencerService, EventService eventService) {
        this.fencerService = fencerService;
        this.eventService = eventService;
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

    @GetMapping("/events")
    @PreAuthorize("hasRole('FENCER')")
    public ResponseEntity<List<CleanEventDTO>> getAllFencerEvents() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        List<Event> eList = fencerService.getFencerEvents((Fencer) user);
        List<CleanEventDTO> ctList = new ArrayList<>();
        for (Event e : eList) {
            ctList.add(eventService.getCleanEventDTO(e));
        }
        return new ResponseEntity<>(ctList, HttpStatus.OK);
    }

    @GetMapping("/upcoming-events")
    @PreAuthorize("hasRole('FENCER')")
    public ResponseEntity<List<CleanEventDTO>> getFencerUpcomingEvents() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        List<Event> eList = fencerService.getFencerUpcomingEvents((Fencer) user);    
        List<CleanEventDTO> ctList = new ArrayList<>();
        for (Event e : eList) {
            ctList.add(eventService.getCleanEventDTO(e));
        }
        return new ResponseEntity<>(ctList, HttpStatus.OK);
    }

    @GetMapping("/past-events")
    @PreAuthorize("hasRole('FENCER')")
    public ResponseEntity<List<CleanEventDTO>> getFencerPastEvents() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        List<Event> eList = fencerService.getFencerPastEvents((Fencer) user);
        List<CleanEventDTO> ctList = new ArrayList<>();
        for (Event e : eList) {
            ctList.add(eventService.getCleanEventDTO(e));
        }
        return new ResponseEntity<>(ctList, HttpStatus.OK);
    }

    @GetMapping("/men-sabre-ranking")
    @PreAuthorize("hasAnyRole('FENCER', 'ORGANISER', 'ADMIN')")
    public ResponseEntity<List<CleanFencerDTO>> getMenSabreRanking() {
        List<Fencer> fencers = fencerService.getFilterdInternationalRank('S', 'M'); 
        List<CleanFencerDTO> res = new ArrayList<>();
        for (Fencer fencer : fencers) {
            res.add(fencerService.getCleanFencerDTO(fencer));
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/women-sabre-ranking")
    @PreAuthorize("hasAnyRole('FENCER', 'ORGANISER', 'ADMIN')")
    public ResponseEntity<List<CleanFencerDTO>> getWomenSabreRanking() {
        List<Fencer> fencers = fencerService.getFilterdInternationalRank('S', 'W'); 
        List<CleanFencerDTO> res = new ArrayList<>();
        for (Fencer fencer : fencers) {
            res.add(fencerService.getCleanFencerDTO(fencer));
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/men-epee-ranking")
    @PreAuthorize("hasAnyRole('FENCER', 'ORGANISER', 'ADMIN')")
    public ResponseEntity<List<CleanFencerDTO>> getMenEpeeRanking() {
        List<Fencer> fencers = fencerService.getFilterdInternationalRank('E', 'M'); 
        List<CleanFencerDTO> res = new ArrayList<>();
        for (Fencer fencer : fencers) {
            res.add(fencerService.getCleanFencerDTO(fencer));
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/women-epee-ranking")
    @PreAuthorize("hasAnyRole('FENCER', 'ORGANISER', 'ADMIN')")
    public ResponseEntity<List<CleanFencerDTO>> getWomenEpeeRanking() {
        List<Fencer> fencers = fencerService.getFilterdInternationalRank('E', 'W'); 
        List<CleanFencerDTO> res = new ArrayList<>();
        for (Fencer fencer : fencers) {
            res.add(fencerService.getCleanFencerDTO(fencer));
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/men-foil-ranking")
    @PreAuthorize("hasAnyRole('FENCER', 'ORGANISER', 'ADMIN')")
    public ResponseEntity<List<CleanFencerDTO>> getMenFoilRanking() {
        List<Fencer> fencers = fencerService.getFilterdInternationalRank('F', 'M'); 
        List<CleanFencerDTO> res = new ArrayList<>();
        for (Fencer fencer : fencers) {
            res.add(fencerService.getCleanFencerDTO(fencer));
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/women-foil-ranking")
    @PreAuthorize("hasAnyRole('FENCER', 'ORGANISER', 'ADMIN')")
    public ResponseEntity<List<CleanFencerDTO>> getWomenFoilRanking() {
        List<Fencer> fencers = fencerService.getFilterdInternationalRank('F', 'W'); 
        List<CleanFencerDTO> res = new ArrayList<>();
        for (Fencer fencer : fencers) {
            res.add(fencerService.getCleanFencerDTO(fencer));
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}