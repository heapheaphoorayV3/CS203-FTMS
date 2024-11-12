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

    @PutMapping("/update-direct-elimination-match/{eid}")
    @PreAuthorize("hasRole('ORGANISER')")
    public ResponseEntity<List<DirectEliminationBracketDTO>> updateDirectEliminationMatch(@PathVariable int eid, @Valid @RequestBody UpdateDirectEliminationMatchDTO dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Organiser organiser = (Organiser) user;
        directEliminationService.updateDEMatch(eid, dto, organiser);
        List<DirectEliminationBracketDTO> res = directEliminationService.generateDirectEliminationBracketDTOs(eid);
        if (res.size() != 0) {
            return new ResponseEntity<>(res, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/get-direct-elimination-matches/{eid}")
    public ResponseEntity<List<DirectEliminationBracketDTO>> getDirectEliminationMatches(@PathVariable int eid) {
        List<DirectEliminationBracketDTO> res = directEliminationService.generateDirectEliminationBracketDTOs(eid);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
