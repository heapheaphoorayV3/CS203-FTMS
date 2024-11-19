package cs203.ftms.overall.service.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cs203.ftms.overall.dto.VerifyOrgDTO;
import cs203.ftms.overall.dto.clean.CleanAdminDTO;
import cs203.ftms.overall.model.userrelated.Admin;
import cs203.ftms.overall.model.userrelated.Organiser;
import cs203.ftms.overall.repository.userrelated.OrganiserRepository;
import jakarta.persistence.EntityNotFoundException;

/**
 * Service class for managing administrative operations.
 * Provides functionality to retrieve admin details, manage organisers, 
 * and handle verification processes.
 */
@Service
public class AdminService {
    private final OrganiserRepository organiserRepository;
    private final MailService mailService;

    @Autowired
    public AdminService(OrganiserRepository organiserRepository, MailService mailService) {
        this.organiserRepository = organiserRepository;
        this.mailService = mailService;
    }

    /**
     * Retrieves a clean data transfer object (DTO) representation of an Admin.
     *
     * @param admin the Admin entity to convert
     * @return a CleanAdminDTO containing non-sensitive Admin information
     * @throws EntityNotFoundException if the provided Admin entity is null
     */
    public CleanAdminDTO getCleanAdmin(Admin admin) {
        if (admin == null) throw new EntityNotFoundException("Admin not found");
        return new CleanAdminDTO(admin.getId(), admin.getName(), admin.getEmail(), admin.getContactNo(), admin.getCountry());
    }

    /**
     * Retrieves a list of unverified Organiser entities.
     *
     * @return a list of organisers whose accounts have not been verified
     */
    public List<Organiser> getUnverifiedOrgs() {
        return organiserRepository.findByVerified(false);
    }

    /**
     * Verifies or denies organisers based on the provided DTO.
     * Approved organisers are marked as verified and sent a confirmation email.
     * Denied organisers are removed from the system.
     *
     * @param dto a VerifyOrgDTO containing lists of organiser IDs to approve or deny
     * @throws EntityNotFoundException if any organiser ID in the DTO does not exist
     */
    public void verifyOrg(VerifyOrgDTO dto) {
        for (int oid : dto.getApprove()) {
            Organiser o = organiserRepository.findById(oid).orElseThrow(() -> new EntityNotFoundException("Organiser with id " + oid + " not found"));

            o.setVerified(true);
            organiserRepository.save(o);
            mailService.sendMail(o.getEmail(), "Account Verified", "Your account has been verified, you may proceed to login!");
        }
        
        for (int oid : dto.getDeny()) {
            Organiser o = organiserRepository.findById(oid).orElseThrow(() -> new EntityNotFoundException("Organiser with id " + oid + " not found"));

            organiserRepository.delete(o);
        }
    }
}
