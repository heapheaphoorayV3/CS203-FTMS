package cs203.ftms.overall.controller.organiser;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cs203.ftms.overall.dto.ChangePasswordDTO;
import cs203.ftms.overall.dto.UpdateOrganiserProfileDTO;
import cs203.ftms.overall.dto.clean.CleanOrganiserDTO;
import cs203.ftms.overall.dto.clean.CleanTournamentDTO;
import cs203.ftms.overall.model.tournamentrelated.Tournament;
import cs203.ftms.overall.model.userrelated.Organiser;
import cs203.ftms.overall.model.userrelated.User;
import cs203.ftms.overall.service.organiser.OrganiserService;
import cs203.ftms.overall.service.tournament.TournamentService;
import jakarta.validation.Valid;

/**
 * Controller class for managing Organiser-related operations.
 * Provides endpoints for viewing, updating, and managing organiser profiles and tournaments.
 */
@RestController
@CrossOrigin
@RequestMapping("/api/v1/organiser")
public class OrganiserController {
    
    private final OrganiserService organiserService;
    private final TournamentService tournamentService;

    /**
     * Constructor for OrganiserController.
     * 
     * @param organiserService The service layer component for handling organiser-related operations.
     * @param tournamentService The service layer component for handling tournament-related operations.
     */
    @Autowired
    public OrganiserController(OrganiserService organiserService, TournamentService tournamentService) {
        this.organiserService = organiserService;
        this.tournamentService = tournamentService;
    }

    /**
     * Retrieves the authenticated organiser's profile information.
     *
     * @return ResponseEntity with CleanOrganiserDTO and HttpStatus.OK if retrieval is successful,
     *         or HttpStatus.BAD_REQUEST if retrieval fails.
     */
    @GetMapping("/profile")
    @PreAuthorize("hasRole('ORGANISER')")
    public ResponseEntity<CleanOrganiserDTO> getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        CleanOrganiserDTO res = organiserService.getCleanOrganiserDTO((Organiser) user);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    /**
     * Retrieves all organisers' profiles (admin only).
     *
     * @return ResponseEntity containing a list of CleanOrganiserDTO with HttpStatus.OK.
     */
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CleanOrganiserDTO>> getAllOrganisers() {
        List<Organiser> oList = organiserService.getAllOrganisers();
        List<CleanOrganiserDTO> res = new ArrayList<>();
        for (Organiser o : oList) {
            res.add(organiserService.getCleanOrganiserDTO(o));
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    /**
     * Retrieves all tournaments managed by the authenticated organiser.
     *
     * @return ResponseEntity containing a list of CleanTournamentDTO with HttpStatus.OK.
     */
    @GetMapping("/tournaments")
    @PreAuthorize("hasRole('ORGANISER')")
    public ResponseEntity<List<CleanTournamentDTO>> getOrganiserTournaments() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        List<Tournament> tList = organiserService.getOrganiserTournaments((Organiser) user);
        
        List<CleanTournamentDTO> res = new ArrayList<>();
        for (Tournament t : tList) {
            res.add(tournamentService.getCleanTournamentDTO(t));
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    /**
     * Changes the password for the authenticated organiser.
     *
     * @param changePasswordDTO Contains the old and new passwords.
     * @return ResponseEntity with a success message and HttpStatus.OK.
     */
    @PutMapping("/change-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordDTO changePasswordDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        String res = organiserService.changePassword(user, changePasswordDTO.getOldPassword(), changePasswordDTO.getNewPassword());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    /**
     * Updates the organiser's profile information.
     *
     * @param updateOrganiserProfileDTO Profile update data as UpdateOrganiserProfileDTO.
     * @return ResponseEntity with a success message and HttpStatus.OK.
     */
    @PutMapping("/update-profile")
    @PreAuthorize("hasRole('ORGANISER')")
    public ResponseEntity<String> updateProfile(@Valid @RequestBody UpdateOrganiserProfileDTO updateOrganiserProfileDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        organiserService.updateProfile((Organiser) user, updateOrganiserProfileDTO);
        return new ResponseEntity<>("Profile updated successfully!", HttpStatus.OK);
    }

    /**
     * Retrieves upcoming tournaments managed by the authenticated organiser.
     *
     * @return ResponseEntity containing a list of CleanTournamentDTO with HttpStatus.OK.
     */
    @GetMapping("/upcoming-tournaments")
    @PreAuthorize("hasRole('ORGANISER')")
    public ResponseEntity<List<CleanTournamentDTO>> getOrganiserUpcomingTournaments() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        List<Tournament> tList = organiserService.getOrganiserUpcomingTournaments((Organiser) user);
        List<CleanTournamentDTO> res = new ArrayList<>();
        for (Tournament t : tList) {
            res.add(tournamentService.getCleanTournamentDTO(t)); 
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    /**
     * Retrieves past tournaments managed by the authenticated organiser.
     *
     * @return ResponseEntity containing a list of CleanTournamentDTO with HttpStatus.OK.
     */
    @GetMapping("/past-tournaments")
    @PreAuthorize("hasRole('ORGANISER')")
    public ResponseEntity<List<CleanTournamentDTO>> getOrganiserPastTournaments() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        List<Tournament> tList = organiserService.getOrganiserPastTournaments((Organiser) user);
        List<CleanTournamentDTO> res = new ArrayList<>();
        for (Tournament t : tList) {
            res.add(tournamentService.getCleanTournamentDTO(t));
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
