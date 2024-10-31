package cs203.ftms.overall.controller.match;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import cs203.ftms.overall.service.match.DirectEliminationService;

@RestController
@CrossOrigin
@Validated
@RequestMapping("/api/v1/direct-elimination")
public class DirectEliminationController {
    private final DirectEliminationService directEliminationService;

    @Autowired
    public DirectEliminationController(DirectEliminationService directEliminationService) {
        this.directEliminationService = directEliminationService;
    }

    @PostMapping("/{eid}/create-direct-elimination-matches")
    @PreAuthorize("hasRole('ORGANISER')")
    public ResponseEntity<List<DirectEliminationBracketDTO>> createDirectEliminationMatches(@PathVariable int eid) {
        directEliminationService.createAllDEMatches(eid);
        List<DirectEliminationBracketDTO> dtos = directEliminationService.generateDirectEliminationBracketDTOs(eid);
        if (dtos != null && dtos.size() != 0) {
            return new ResponseEntity<>(dtos, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{eid}/update-direct-elimination-match")
    @PreAuthorize("hasRole('ORGANISER')")
    public ResponseEntity<List<DirectEliminationBracketDTO>> updateDirectEliminationMatch(@PathVariable int eid, @RequestBody UpdateDirectEliminationMatchDTO dto) {
        directEliminationService.updateDEMatch(eid, dto);
        List<DirectEliminationBracketDTO> dtos = directEliminationService.generateDirectEliminationBracketDTOs(eid);
        if (dtos != null && dtos.size() != 0) {
            return new ResponseEntity<>(dtos, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{eid}/get-direct-elimination-matches")
    @PreAuthorize("hasAnyRole('FENCER', 'ORGANISER', 'ADMIN')")
    public ResponseEntity<List<DirectEliminationBracketDTO>> getDirectEliminationMatches(@PathVariable int eid) {
        List<DirectEliminationBracketDTO> dtos = directEliminationService.generateDirectEliminationBracketDTOs(eid);
        if (dtos != null) {
            return new ResponseEntity<>(dtos, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }
}
