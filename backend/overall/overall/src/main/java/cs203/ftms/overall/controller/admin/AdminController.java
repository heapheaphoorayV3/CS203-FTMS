package cs203.ftms.overall.controller.admin;

import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cs203.ftms.overall.dto.VerifyOrgDTO;
import cs203.ftms.overall.dto.clean.CleanOrganiserDTO;
import cs203.ftms.overall.model.userrelated.Organiser;
import cs203.ftms.overall.service.admin.AdminService;
import cs203.ftms.overall.service.organiser.OrganiserService;



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

    @GetMapping("/unverified-organisation")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CleanOrganiserDTO>> getUnverifiedOrgs() {
        List<Organiser> orgs = adminService.getUnverifiedOrgs();
        List<CleanOrganiserDTO> res = new ArrayList<>();
        for (Organiser o : orgs) {
            res.add(organiserService.getCleanOrganiserDTO(o));
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PutMapping("/verify-organisation")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> verifyOrg(@RequestBody VerifyOrgDTO dto) {
        adminService.verifyOrg(dto);
        return new ResponseEntity<>("operation successful", HttpStatus.OK);
    }


    // @GetMapping("/verify/{orgId}")
    // @PreAuthorize("hasRole('ADMIN')")
    // public ResponseEntity<String> verifyOrg(@PathVariable int orgId) {
    //     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    //     User user = (User) authentication.getPrincipal();
    //     if (user instanceof Admin) {
    //         Organiser o = adminService.updateVerified(orgId);
    //         if (o != null) {
    //             return new ResponseEntity<>("operation successful", HttpStatus.OK);
    //         }
    //     }
    //     return new ResponseEntity<>("operation unsuccessful", HttpStatus.BAD_REQUEST);
    // }

}
