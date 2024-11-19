package cs203.ftms.overall.controller.match;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cs203.ftms.overall.dto.CreatePoulesDTO;
import cs203.ftms.overall.dto.PouleResultsDTO;
import cs203.ftms.overall.dto.PouleTableDTO;
import cs203.ftms.overall.dto.SinglePouleTableDTO;
import cs203.ftms.overall.dto.clean.CleanPouleDTO;
import cs203.ftms.overall.model.userrelated.Organiser;
import cs203.ftms.overall.model.userrelated.User;
import cs203.ftms.overall.service.match.PouleService;

/**
 * Controller class for managing Poule (round-robin) matches.
 * Provides endpoints for creating, updating, and retrieving poule tables and results.
 */
@RestController
@CrossOrigin
@Validated
@RequestMapping("/api/v1/poule")
public class PouleController {
    
    private final PouleService pouleService;

    /**
     * Constructor for PouleController.
     * 
     * @param pouleService The service layer component for handling poule-related operations.
     */
    @Autowired
    public PouleController(PouleService pouleService) {
        this.pouleService = pouleService;
    }

    /**
     * Retrieves recommended poule configurations for a specific event.
     *
     * @param eid The ID of the event.
     * @return ResponseEntity containing a set of recommended poule configurations with HttpStatus.OK if successful,
     *         or HttpStatus.BAD_REQUEST if the recommendation fails.
     */
    @GetMapping("/get-recommended-poules/{eid}")
    @PreAuthorize("hasRole('ORGANISER')")
    public ResponseEntity<Set<String>> getRecommendedPoules(@PathVariable int eid) {
        Set<String> res = pouleService.recommendPoules(eid);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    /**
     * Creates poules (round-robin groups) for a specific event.
     *
     * @param eid The ID of the event.
     * @param createPoulesDTO Data for creating the poules, provided as CreatePoulesDTO.
     * @return ResponseEntity containing a PouleTableDTO with HttpStatus.CREATED.
     */
    @PostMapping("/create-poules/{eid}")
    @PreAuthorize("hasRole('ORGANISER')")
    public ResponseEntity<PouleTableDTO> createPoules(@PathVariable int eid, @RequestBody CreatePoulesDTO createPoulesDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Organiser organiser = (Organiser) user;
        Set<CleanPouleDTO> create = pouleService.createPoules(eid, createPoulesDTO, organiser);
        PouleTableDTO res = pouleService.getPouleTable(eid, true);
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    /**
     * Retrieves the poule table for a specific event.
     *
     * @param eid The ID of the event.
     * @return ResponseEntity containing a PouleTableDTO with HttpStatus.OK.
     */
    @GetMapping("/get-poule-table/{eid}")
    public ResponseEntity<PouleTableDTO> getPouleTable(@PathVariable int eid) {
        PouleTableDTO res = pouleService.getPouleTable(eid, false);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    /**
     * Updates the scores for a specific poule match.
     *
     * @param eid The ID of the event.
     * @param singlePouleTableDTO The updated score data for the poule match, provided as SinglePouleTableDTO.
     * @return ResponseEntity with a success message and HttpStatus.OK if the update is successful,
     *         or HttpStatus.BAD_REQUEST if the update fails.
     * @throws MethodArgumentNotValidException if the input data is invalid.
     */
    @PutMapping("/update-poule-table/{eid}")
    @PreAuthorize("hasRole('ORGANISER')")
    public ResponseEntity<String> updatePouleScore(@PathVariable int eid, @RequestBody SinglePouleTableDTO singlePouleTableDTO) throws MethodArgumentNotValidException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Organiser organiser = (Organiser) user;
        boolean update = pouleService.updatePouleTable(eid, singlePouleTableDTO, organiser);
        if (update) {
            return new ResponseEntity<>("poule update successful", HttpStatus.OK);
        }
        return new ResponseEntity<>("poule update unsuccessful", HttpStatus.BAD_REQUEST);
    }

    /**
     * Retrieves the results of all poules for a specific event.
     *
     * @param eid The ID of the event.
     * @return ResponseEntity containing a PouleResultsDTO with HttpStatus.OK.
     */
    @GetMapping("/get-poules-result/{eid}")
    public ResponseEntity<PouleResultsDTO> getPouleResults(@PathVariable int eid) {
        PouleResultsDTO res = pouleService.poulesResult(eid);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
