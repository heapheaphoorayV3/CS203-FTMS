package cs203.ftms.overall.service.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cs203.ftms.overall.dto.VerifyOrgDTO;
import cs203.ftms.overall.dto.clean.CleanAdminDTO;
import cs203.ftms.overall.model.userrelated.Admin;
import cs203.ftms.overall.model.userrelated.Organiser;
import cs203.ftms.overall.repository.userrelated.AdminRepository;
import cs203.ftms.overall.repository.userrelated.OrganiserRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class AdminService {
    private final AdminRepository adminRepository;
    private final OrganiserRepository organiserRepository;
    private final MailService mailService; 

    @Autowired
    public AdminService(AdminRepository adminRepository, OrganiserRepository organiserRepository, MailService mailService) {
        this.adminRepository = adminRepository;
        this.organiserRepository = organiserRepository;
        this.mailService = mailService;
    }

    public CleanAdminDTO getCleanAdmin(Admin a) {
        if (a == null) return null; 
        return new CleanAdminDTO(a.getId(), a.getName(), a.getEmail(), a.getContactNo(), a.getCountry());
    }

    public List<Organiser> getUnverifiedOrgs() {
        return organiserRepository.findByVerified(false);
    }

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
