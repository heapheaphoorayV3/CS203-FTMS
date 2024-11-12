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
     * @param admin Admin object
     * @return Admin object without sensitive information
     */
    public CleanAdminDTO getCleanAdmin(Admin admin) {
        if (admin == null) throw new EntityNotFoundException("Admin not found"); 
        return new CleanAdminDTO(admin.getId(), admin.getName(), admin.getEmail(), admin.getContactNo(), admin.getCountry());
    }

    
    /** 
     * @return List of unverified organisers
     */
    public List<Organiser> getUnverifiedOrgs() {
        return organiserRepository.findByVerified(false);
    }

    
    
    /** 
     * @param dto List of organiser ids to approve and deny
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
