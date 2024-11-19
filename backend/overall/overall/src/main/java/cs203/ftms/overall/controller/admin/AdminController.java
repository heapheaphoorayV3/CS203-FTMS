package cs203.ftms.overall.controller.admin;

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

import cs203.ftms.overall.dto.VerifyOrgDTO;
import cs203.ftms.overall.dto.clean.CleanAdminDTO;
import cs203.ftms.overall.dto.clean.CleanOrganiserDTO;
import cs203.ftms.overall.exception.EntityDoesNotExistException;
import cs203.ftms.overall.model.userrelated.Admin;
import cs203.ftms.overall.model.userrelated.Organiser;
import cs203.ftms.overall.model.userrelated.User;
import cs203.ftms.overall.service.admin.AdminService;
import cs203.ftms.overall.service.organiser.OrganiserService;


/**
 * REST controller handling admin operations including organizer verification.
 * All endpoints require ADMIN role authorization.
 */
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

    /**
     * Retrieves the profile information of the currently authenticated admin.
     * 
     * @return ResponseEntity containing CleanAdminDTO with sanitized admin data
     *         Returns HTTP 200 (OK) if successful
     *         Returns HTTP 400 (BAD REQUEST) if profile data cannot be retrieved
     * @throws ClassCastException if authenticated user is not an Admin
     */
    @GetMapping("/profile")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CleanAdminDTO> getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        CleanAdminDTO res = adminService.getCleanAdmin((Admin) user);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    /**
     * Retrieves a list of all unverified organizers in the system.
     * 
     * @return ResponseEntity containing {@code List<CleanOrganiserDTO>} with sanitized organizer data
     *         Returns HTTP 200 (OK) with the list of unverified organizers
     * @throws EntityDoesNotExistException if no unverified organizers exist
     */
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

    /**
     * Verifies an organizer's account status.
     * 
     * @param verifyOrgDTO DTO containing organizer verification details
     * @return ResponseEntity with success message and HTTP 200 (OK)
     * @throws EntityDoesNotExistException if organizer not found
     * @throws IllegalStateException if organizer is already verified
     */
    @PutMapping("/verify-organiser")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> verifyOrg(@RequestBody VerifyOrgDTO verifyOrgDTO) {
        adminService.verifyOrg(verifyOrgDTO);
        return new ResponseEntity<>("operation successful", HttpStatus.OK);
    }

}
