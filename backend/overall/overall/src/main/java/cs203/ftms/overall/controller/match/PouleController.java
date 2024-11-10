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

    @GetMapping("/get-recommended-poules/{eid}")
    @PreAuthorize("hasRole('ORGANISER')")
    public ResponseEntity<Set<String>> getRecommendedPoules(@PathVariable int eid) {
        Set<String> res = pouleService.recommendPoules(eid);
        if (res == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
    

    @PostMapping("/create-poules/{eid}")
    @PreAuthorize("hasRole('ORGANISER')")
    public ResponseEntity<PouleTableDTO> createPoules(@PathVariable int eid, @RequestBody CreatePoulesDTO dto) {
        Set<CleanPouleDTO> create = pouleService.createPoules(eid, dto);
        PouleTableDTO res = pouleService.getPouleTable(eid, true);
        if (create != null && res != null) {
            return new ResponseEntity<>(res, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/get-poule-table/{eid}")
    @PreAuthorize("hasAnyRole('FENCER', 'ORGANISER', 'ADMIN')")
    public ResponseEntity<PouleTableDTO> getPouleTable(@PathVariable int eid) {
        PouleTableDTO res = pouleService.getPouleTable(eid, false);
        if (res != null) {
            return new ResponseEntity<>(res, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/update-poule-table/{eid}")
    @PreAuthorize("hasRole('ORGANISER')")
    public ResponseEntity<String> updatePouleScore(@PathVariable int eid, @RequestBody SinglePouleTableDTO dto) throws MethodArgumentNotValidException {
        boolean update = pouleService.updatePouleTable(eid, dto);
        if (update) {
            return new ResponseEntity<>("poule update successful", HttpStatus.OK);
        }
        return new ResponseEntity<>("poule update unsuccessful", HttpStatus.BAD_REQUEST);
    }
    
    @GetMapping("/get-poules-result/{eid}")
    @PreAuthorize("hasAnyRole('FENCER', 'ORGANISER', 'ADMIN')")
    public ResponseEntity<PouleResultsDTO> getPouleResults(@PathVariable int eid) {
        PouleResultsDTO res = pouleService.poulesResult(eid);
        if(res.getFenceOffFencers() != null){
            return new ResponseEntity<>(res, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }
}
