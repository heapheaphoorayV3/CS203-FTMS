package cs203.ftms.overall.service.organiser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cs203.ftms.overall.dto.clean.CleanOrganiserDTO;
import cs203.ftms.overall.model.userrelated.Organiser;
import cs203.ftms.overall.repository.userrelated.OrganiserRepository;

@Service
public class OrganiserService {
    private final OrganiserRepository organiserRepository;

    @Autowired
    public OrganiserService(OrganiserRepository organiserRepository) {
        this.organiserRepository = organiserRepository;
    }

    public CleanOrganiserDTO getCleanOrganiserDTO(Organiser o) {
        if (o == null) return null;
        return new CleanOrganiserDTO(o.getId(), o.isVerified(), o.getName(), o.getEmail(), o.getContactNo(), o.getCountry());
    }
}
