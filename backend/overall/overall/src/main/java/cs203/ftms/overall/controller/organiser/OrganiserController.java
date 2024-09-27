package cs203.ftms.overall.controller.organiser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cs203.ftms.overall.dto.clean.CleanOrganiserDTO;
import cs203.ftms.overall.model.userrelated.Organiser;
import cs203.ftms.overall.model.userrelated.User;
import cs203.ftms.overall.service.organiser.OrganiserService;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/organiser")
public class OrganiserController {
    private final OrganiserService organiserService;

    @Autowired
    public OrganiserController(OrganiserService organiserService) {
        this.organiserService = organiserService;
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
}
