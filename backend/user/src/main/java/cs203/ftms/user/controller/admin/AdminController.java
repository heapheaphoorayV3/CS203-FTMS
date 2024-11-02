package cs203.ftms.user.controller.admin;

import java.util.List;
import java.util.ArrayList;

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

import cs203.ftms.user.dto.VerifyOrgDTO;
import cs203.ftms.user.dto.clean.CleanAdminDTO;
import cs203.ftms.user.dto.clean.CleanOrganiserDTO;
import cs203.ftms.user.model.Admin;
import cs203.ftms.user.model.Organiser;
import cs203.ftms.user.model.User;
import cs203.ftms.user.service.admin.AdminService;
import cs203.ftms.user.service.organiser.OrganiserService;



@RestController
@CrossOrigin
@RequestMapping("/api/v1/admin")
public class AdminController {
    private final AdminService adminService;
    private final OrganiserService organiserService;

    @Autowired
    public AdminController(AdminService adminService, OrganiserService organiserService) {
        this.adminService = adminService;
        this.organiserService = organiserService;
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CleanAdminDTO> getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        CleanAdminDTO co = adminService.getCleanAdmin((Admin) user);
        if (co == null) return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(co, HttpStatus.OK);
    }

    @GetMapping("/unverified-organiser")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CleanOrganiserDTO>> getUnverifiedOrgs() {
        List<Organiser> orgs = adminService.getUnverifiedOrgs();
        List<CleanOrganiserDTO> res = new ArrayList<>();
        for (Organiser o : orgs) {
            res.add(organiserService.getCleanOrganiserDTO(o));
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PutMapping("/verify-organiser")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> verifyOrg(@RequestBody VerifyOrgDTO dto) {
        adminService.verifyOrg(dto);
        return new ResponseEntity<>("operation successful", HttpStatus.OK);
    }

}
