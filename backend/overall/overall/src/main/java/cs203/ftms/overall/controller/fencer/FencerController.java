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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cs203.ftms.overall.dto.ChangePasswordDTO;
import cs203.ftms.overall.dto.CompleteFencerProfileDTO;
import cs203.ftms.overall.dto.UpdateFencerProfileDTO;
import cs203.ftms.overall.dto.clean.CleanEventDTO;
import cs203.ftms.overall.dto.clean.CleanFencerDTO;
import cs203.ftms.overall.dto.clean.CleanTournamentFencerDTO;
import cs203.ftms.overall.model.tournamentrelated.Event;
import cs203.ftms.overall.model.tournamentrelated.TournamentFencer;
import cs203.ftms.overall.model.userrelated.Fencer;
import cs203.ftms.overall.model.userrelated.User;
import cs203.ftms.overall.service.event.EventService;
import cs203.ftms.overall.service.fencer.FencerService;
import jakarta.validation.Valid;

/**
 * Controller class responsible for handling HTTP requests related to fencer management.
 * Provides endpoints for viewing, updating, and managing fencer profiles, events, and rankings.
 */
@RestController
@CrossOrigin
@RequestMapping("/api/v1/fencer")
public class FencerController {
    
    private final FencerService fencerService;
    private final EventService eventService;

    /**
     * Constructor for FencerController.
     * 
     * @param fencerService The service layer component for handling fencer-related operations.
     * @param eventService The service layer component for handling event-related operations.
     */
    @Autowired
    public FencerController(FencerService fencerService, EventService eventService) {
        this.fencerService = fencerService;
        this.eventService = eventService;
    }

    /**
     * Retrieves the authenticated fencer's profile information.
     *
     * @return ResponseEntity with CleanFencerDTO and HttpStatus.OK if retrieval is successful,
     *         or HttpStatus.BAD_REQUEST if retrieval fails.
     */
    @GetMapping("/profile")
    @PreAuthorize("hasRole('FENCER')")
    public ResponseEntity<CleanFencerDTO> getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        CleanFencerDTO res = fencerService.getCleanFencerDTO((Fencer) user);
        if (res == null) return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    /**
     * Completes the fencer's profile with additional information.
     *
     * @param completeFencerProfileDTO The complete profile data, validated as CompleteFencerProfileDTO.
     * @return ResponseEntity with a success message and HttpStatus.OK on success,
     *         or HttpStatus.BAD_REQUEST if completion fails.
     * @throws MethodArgumentNotValidException if the input is invalid.
     */
    @PutMapping("/complete-profile")
    @PreAuthorize("hasRole('FENCER')")
    public ResponseEntity<String> completeProfile(@Valid @RequestBody CompleteFencerProfileDTO completeFencerProfileDTO) throws MethodArgumentNotValidException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Fencer f = fencerService.completeProfile((Fencer) user, completeFencerProfileDTO);
        if (f != null) {
            return new ResponseEntity<>("fencer profile completed successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("fencer profile completion unsuccessful", HttpStatus.BAD_REQUEST);
    }

    /**
     * Retrieves all fencers' profiles (admin only).
     *
     * @return ResponseEntity with a list of CleanFencerDTO and HttpStatus.OK.
     */
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CleanFencerDTO>> getAllFencers() {
        List<Fencer> fencers = fencerService.getAllFencers();
        List<CleanFencerDTO> res = new ArrayList<>();
        for (Fencer fencer : fencers) {
            res.add(fencerService.getCleanFencerDTO(fencer));
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    /**
     * Retrieves the international ranking of fencers.
     *
     * @return ResponseEntity with a list of CleanFencerDTO and HttpStatus.OK.
     */
    @GetMapping("/international-ranking")
    @PreAuthorize("hasRole('FENCER')")
    public ResponseEntity<Integer> getInternationalRanking() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        int rank = fencerService.getInternationalRank((Fencer) user); 
        return new ResponseEntity<>(rank, HttpStatus.OK);
    }

    /**
     * Changes the authenticated user's password.
     *
     * @param changePasswordDTO Contains the old and new passwords.
     * @return ResponseEntity with a success message and HttpStatus.OK.
     */
    @PutMapping("/change-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordDTO changePasswordDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        String res = fencerService.changePassword(user, changePasswordDTO.getOldPassword(), changePasswordDTO.getNewPassword());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    /**
     * Updates the fencer's profile information.
     *
     * @param updateFencerProfileDTO Profile update data as UpdateFencerProfileDTO.
     * @return ResponseEntity with a success message and HttpStatus.OK.
     */
    @PutMapping("/update-profile")
    @PreAuthorize("hasRole('FENCER')")
    public ResponseEntity<String> updateProfile(@Valid @RequestBody UpdateFencerProfileDTO updateFencerProfileDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        fencerService.updateProfile((Fencer) user, updateFencerProfileDTO);
        return new ResponseEntity<>("Profile updated successfully!", HttpStatus.OK);
    }

    /**
     * Retrieves all events the fencer is registered for.
     *
     * @return ResponseEntity with a list of CleanEventDTO and HttpStatus.OK.
     */
    @GetMapping("/events")
    @PreAuthorize("hasRole('FENCER')")
    public ResponseEntity<List<CleanEventDTO>> getAllFencerEvents() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        List<Event> eList = fencerService.getFencerEvents((Fencer) user);
        List<CleanEventDTO> res = new ArrayList<>();
        for (Event e : eList) {
            res.add(eventService.getCleanEventDTO(e));
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    /**
     * Retrieves upcoming events for the fencer.
     *
     * @return ResponseEntity with a list of CleanEventDTO and HttpStatus.OK.
     */
    @GetMapping("/upcoming-events")
    @PreAuthorize("hasRole('FENCER')")
    public ResponseEntity<List<CleanEventDTO>> getFencerUpcomingEvents() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        List<Event> eList = fencerService.getFencerUpcomingEvents((Fencer) user);    
        List<CleanEventDTO> res = new ArrayList<>();
        for (Event e : eList) {
            res.add(eventService.getCleanEventDTO(e));
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    /**
     * Retrieves past events for the fencer.
     *
     * @return ResponseEntity with a list of CleanEventDTO and HttpStatus.OK.
     */
    @GetMapping("/past-events")
    @PreAuthorize("hasRole('FENCER')")
    public ResponseEntity<List<CleanEventDTO>> getFencerPastEvents() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        List<Event> eList = fencerService.getFencerPastEvents((Fencer) user);
        List<CleanEventDTO> res = new ArrayList<>();
        for (Event e : eList) {
            res.add(eventService.getCleanEventDTO(e));
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    /**
    * Retrieves a list of past events and associated points for the authenticated fencer.
    *
    * @return a ResponseEntity containing a list of CleanTournamentFencerDTO objects representing
    *         past events and points scored by the authenticated fencer, with an HTTP status of OK.
    */
    @GetMapping("/past-events-points")
    @PreAuthorize("hasRole('FENCER')")
    public ResponseEntity<List<CleanTournamentFencerDTO>> getFencerPastEventsPoints() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        List<CleanTournamentFencerDTO> tfs = fencerService.getFencerPastEventsPoints((Fencer) user);
        return new ResponseEntity<>(tfs, HttpStatus.OK);
    }

    /**
     * Retrieves a list of past event profiles for the authenticated fencer.
     *
     * @return a ResponseEntity containing a list of CleanTournamentFencerDTO objects representing
     *         past event profiles of the authenticated fencer, with an HTTP status of OK.
     */
    @GetMapping("/past-events-profiles")
    @PreAuthorize("hasRole('FENCER')")
    public ResponseEntity<List<CleanTournamentFencerDTO>> getFencerPastEventsProfiles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        List<TournamentFencer> profiles = fencerService.getFencerPastEventsProfiles((Fencer) user);
        List<CleanTournamentFencerDTO> tfList = new ArrayList<>();
        for (TournamentFencer tf : profiles) {
            tfList.add(fencerService.getCleanTournamentFencerDTO(tf));
        }
        return new ResponseEntity<>(tfList, HttpStatus.OK);
    }


    /**
     * Retrieves men's sabre ranking of fencers.
     *
     * @return ResponseEntity with a list of CleanFencerDTO and HttpStatus.OK.
     */
    @GetMapping("/men-sabre-ranking")
    public ResponseEntity<List<CleanFencerDTO>> getMenSabreRanking() {
        List<Fencer> fencers = fencerService.getFilterdInternationalRank('S', 'M'); 
        List<CleanFencerDTO> res = new ArrayList<>();
        for (Fencer fencer : fencers) {
            res.add(fencerService.getCleanFencerDTO(fencer));
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    /**
     * Retrieves women's sabre ranking of fencers.
     *
     * @return ResponseEntity with a list of CleanFencerDTO and HttpStatus.OK.
     */
    @GetMapping("/women-sabre-ranking")
    public ResponseEntity<List<CleanFencerDTO>> getWomenSabreRanking() {
        List<Fencer> fencers = fencerService.getFilterdInternationalRank('S', 'W'); 
        List<CleanFencerDTO> res = new ArrayList<>();
        for (Fencer fencer : fencers) {
            res.add(fencerService.getCleanFencerDTO(fencer));
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    /**
     * Retrieves men's epee ranking of fencers.
     *
     * @return ResponseEntity with a list of CleanFencerDTO and HttpStatus.OK.
     */
    @GetMapping("/men-epee-ranking")
    public ResponseEntity<List<CleanFencerDTO>> getMenEpeeRanking() {
        List<Fencer> fencers = fencerService.getFilterdInternationalRank('E', 'M'); 
        List<CleanFencerDTO> res = new ArrayList<>();
        for (Fencer fencer : fencers) {
            res.add(fencerService.getCleanFencerDTO(fencer));
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    /**
     * Retrieves women's epee ranking of fencers.
     *
     * @return ResponseEntity with a list of CleanFencerDTO and HttpStatus.OK.
     */
    @GetMapping("/women-epee-ranking")
    public ResponseEntity<List<CleanFencerDTO>> getWomenEpeeRanking() {
        List<Fencer> fencers = fencerService.getFilterdInternationalRank('E', 'W'); 
        List<CleanFencerDTO> res = new ArrayList<>();
        for (Fencer fencer : fencers) {
            res.add(fencerService.getCleanFencerDTO(fencer));
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    /**
     * Retrieves men's foil ranking of fencers.
     *
     * @return ResponseEntity with a list of CleanFencerDTO and HttpStatus.OK.
     */
    @GetMapping("/men-foil-ranking")
    public ResponseEntity<List<CleanFencerDTO>> getMenFoilRanking() {
        List<Fencer> fencers = fencerService.getFilterdInternationalRank('F', 'M'); 
        List<CleanFencerDTO> res = new ArrayList<>();
        for (Fencer fencer : fencers) {
            res.add(fencerService.getCleanFencerDTO(fencer));
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    /**
     * Retrieves women's foil ranking of fencers.
     *
     * @return ResponseEntity with a list of CleanFencerDTO and HttpStatus.OK.
     */
    @GetMapping("/women-foil-ranking")
    public ResponseEntity<List<CleanFencerDTO>> getWomenFoilRanking() {
        List<Fencer> fencers = fencerService.getFilterdInternationalRank('F', 'W'); 
        List<CleanFencerDTO> res = new ArrayList<>();
        for (Fencer fencer : fencers) {
            res.add(fencerService.getCleanFencerDTO(fencer));
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
