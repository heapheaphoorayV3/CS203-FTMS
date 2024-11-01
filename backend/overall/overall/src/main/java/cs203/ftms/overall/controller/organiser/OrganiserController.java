package cs203.ftms.overall.controller.organiser;

import java.time.LocalDate;
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

@RestController
@CrossOrigin
@RequestMapping("/api/v1/organiser")
public class OrganiserController {
    private final OrganiserService organiserService;
    private final TournamentService tournamentService;

    @Autowired
    public OrganiserController(OrganiserService organiserService, TournamentService tournamentService) {
        this.organiserService = organiserService;
        this.tournamentService = tournamentService;
    }


    @GetMapping("/profile")
    @PreAuthorize("hasRole('ORGANISER')")
    public ResponseEntity<CleanOrganiserDTO> getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        CleanOrganiserDTO co = organiserService.getCleanOrganiserDTO((Organiser) user);
        if (co == null) return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(co, HttpStatus.OK);
    }
  
    @GetMapping("/tournaments")
    @PreAuthorize("hasRole('ORGANISER')")
    public ResponseEntity<List<CleanTournamentDTO>> getOrganiserTournaments() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        List<Tournament> tList = organiserService.getOrganiserTournaments((Organiser) user);
        
        List<CleanTournamentDTO> ctList = new ArrayList<>();
        for (Tournament t : tList) {
            ctList.add(tournamentService.getCleanTournamentDTO(t));
        }
        return new ResponseEntity<>(ctList, HttpStatus.OK);
    }

    @PutMapping("/change-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        String res = organiserService.changePassword(user, changePasswordDTO.getOldPassword(), changePasswordDTO.getNewPassword());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PutMapping("/update-profile")
    @PreAuthorize("hasRole('ORGANISER')")
    public ResponseEntity<String> updateProfile(@RequestBody UpdateOrganiserProfileDTO dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        organiserService.updateProfile((Organiser) user, dto);
        return new ResponseEntity<>("Profile updated sucessfully!", HttpStatus.OK);

    @GetMapping("/upcoming-tournaments")
    @PreAuthorize("hasRole('ORGANISER')")
    public ResponseEntity<List<CleanTournamentDTO>> getOrganiserUpcomingTournaments() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        List<Tournament> tList = organiserService.getOrganiserTournaments((Organiser) user);
        
        List<CleanTournamentDTO> ctList = new ArrayList<>();
        for (Tournament t : tList) {
            if(t.getStartDate().isAfter(LocalDate.now())){
            ctList.add(tournamentService.getCleanTournamentDTO(t));
            }
        }
        return new ResponseEntity<>(ctList, HttpStatus.OK);
    }

    @GetMapping("/past-tournaments")
    @PreAuthorize("hasRole('ORGANISER')")
    public ResponseEntity<List<CleanTournamentDTO>> getOrganiserPastTournaments() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        List<Tournament> tList = organiserService.getOrganiserTournaments((Organiser) user);
        
        List<CleanTournamentDTO> ctList = new ArrayList<>();
        for (Tournament t : tList) {
            if(t.getStartDate().isBefore(LocalDate.now())){
            ctList.add(tournamentService.getCleanTournamentDTO(t));
            }
        }
        return new ResponseEntity<>(ctList, HttpStatus.OK);
    }
}
