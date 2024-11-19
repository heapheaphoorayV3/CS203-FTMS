package cs203.ftms.overall.controller.match;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cs203.ftms.overall.dto.DirectEliminationBracketDTO;
import cs203.ftms.overall.dto.UpdateDirectEliminationMatchDTO;
import cs203.ftms.overall.model.userrelated.Organiser;
import cs203.ftms.overall.model.userrelated.User;
import cs203.ftms.overall.service.match.DirectEliminationService;
import jakarta.validation.Valid;

/**
 * Controller class responsible for managing Direct Elimination matches.
 * Provides endpoints for creating, updating, and retrieving Direct Elimination brackets.
 */
@RestController
@CrossOrigin
@Validated
@RequestMapping("/api/v1/direct-elimination")
public class DirectEliminationController {
    
    private final DirectEliminationService directEliminationService;

    /**
     * Constructor for DirectEliminationController.
     * 
     * @param directEliminationService The service layer component for handling Direct Elimination matches.
     */
    @Autowired
    public DirectEliminationController(DirectEliminationService directEliminationService) {
        this.directEliminationService = directEliminationService;
    }

    /**
     * Creates Direct Elimination matches for a specific event.
     *
     * @param eid The ID of the event.
     * @return ResponseEntity containing a list of DirectEliminationBracketDTOs with HttpStatus.CREATED if successful,
     *         or HttpStatus.BAD_REQUEST if creation fails.
     */
    @PostMapping("/create-direct-elimination-matches/{eid}")
    @PreAuthorize("hasRole('ORGANISER')")
    public ResponseEntity<List<DirectEliminationBracketDTO>> createDirectEliminationMatches(@PathVariable int eid) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Organiser organiser = (Organiser) user;
        directEliminationService.createAllDEMatches(eid, organiser);
        List<DirectEliminationBracketDTO> res = directEliminationService.generateDirectEliminationBracketDTOs(eid);
        if (res.size() != 0) {
            return new ResponseEntity<>(res, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
    }

    /**
     * Updates a specific Direct Elimination match.
     *
     * @param eid The ID of the event.
     * @param updateDirectEliminationMatchDTO The data for updating the Direct Elimination match.
     * @return ResponseEntity containing a list of updated DirectEliminationBracketDTOs with HttpStatus.CREATED if successful,
     *         or HttpStatus.BAD_REQUEST if the update fails.
     */
    @PutMapping("/update-direct-elimination-match/{eid}")
    @PreAuthorize("hasRole('ORGANISER')")
    public ResponseEntity<List<DirectEliminationBracketDTO>> updateDirectEliminationMatch(@PathVariable int eid, @Valid @RequestBody UpdateDirectEliminationMatchDTO updateDirectEliminationMatchDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Organiser organiser = (Organiser) user;
        directEliminationService.updateDEMatch(eid, updateDirectEliminationMatchDTO, organiser);
        List<DirectEliminationBracketDTO> res = directEliminationService.generateDirectEliminationBracketDTOs(eid);
        if (res.size() != 0) {
            return new ResponseEntity<>(res, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
    }

    /**
     * Retrieves the Direct Elimination matches for a specific event.
     *
     * @param eid The ID of the event.
     * @return ResponseEntity containing a list of DirectEliminationBracketDTOs with HttpStatus.OK.
     */
    @GetMapping("/get-direct-elimination-matches/{eid}")
    public ResponseEntity<List<DirectEliminationBracketDTO>> getDirectEliminationMatches(@PathVariable int eid) {
        List<DirectEliminationBracketDTO> res = directEliminationService.generateDirectEliminationBracketDTOs(eid);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
