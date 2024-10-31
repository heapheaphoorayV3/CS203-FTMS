package cs203.ftms.overall.controller.match;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import cs203.ftms.overall.service.match.PouleService;

@RestController
@CrossOrigin
@Validated
@RequestMapping("/api/v1/poule")
public class PouleController {
    private final PouleService pouleService;

    @Autowired
    public PouleController(PouleService pouleService) {
        this.pouleService = pouleService;
    }

    @GetMapping("/{eid}/get-recommended-poules")
    @PreAuthorize("hasRole('ORGANISER')")
    public ResponseEntity<Set<String>> getRecommendedPoules(@PathVariable int eid) {
        Set<String> poules = pouleService.recommendPoules(eid);
        if (poules == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(poules, HttpStatus.OK);
    }
    

    @PostMapping("/{eid}/create-poules")
    @PreAuthorize("hasRole('ORGANISER')")
    public ResponseEntity<PouleTableDTO> createPoules(@PathVariable int eid, @RequestBody CreatePoulesDTO dto) {
        Set<CleanPouleDTO> create = pouleService.createPoules(eid, dto);
        PouleTableDTO pouleTable = pouleService.getPouleTable(eid, true);
        if (create != null && pouleTable != null) {
            return new ResponseEntity<>(pouleTable, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{eid}/get-poule-table")
    @PreAuthorize("hasAnyRole('FENCER', 'ORGANISER', 'ADMIN')")
    public ResponseEntity<PouleTableDTO> getPouleTable(@PathVariable int eid) {
        PouleTableDTO pouleTable = pouleService.getPouleTable(eid, false);
        if (pouleTable != null) {
            return new ResponseEntity<>(pouleTable, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{eid}/update-poule-table")
    @PreAuthorize("hasRole('ORGANISER')")
    public ResponseEntity<String> updatePouleScore(@PathVariable int eid, @RequestBody SinglePouleTableDTO dto) throws MethodArgumentNotValidException {
        boolean update = pouleService.updatePouleTable(eid, dto);
        if (update) {
            return new ResponseEntity<>("poule update successful", HttpStatus.OK);
        }
        return new ResponseEntity<>("poule update unsuccessful", HttpStatus.BAD_REQUEST);
    }
    
    @GetMapping("/{eid}/get-poules-result")
    @PreAuthorize("hasAnyRole('FENCER', 'ORGANISER', 'ADMIN')")
    public ResponseEntity<PouleResultsDTO> getPouleResults(@PathVariable int eid) {
        PouleResultsDTO poulesResult = pouleService.poulesResult(eid);
        if(poulesResult.getFenceOffFencers() != null){
            return new ResponseEntity<>(poulesResult, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }
}
