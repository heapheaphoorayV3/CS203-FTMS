package cs203.ftms.overall.service.organiser;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cs203.ftms.overall.dto.clean.CleanOrganiserDTO;
import cs203.ftms.overall.model.tournamentrelated.Tournament;
import cs203.ftms.overall.model.userrelated.Organiser;
import cs203.ftms.overall.repository.tournamentrelated.TournamentRepository;
import cs203.ftms.overall.repository.userrelated.OrganiserRepository;

@Service
public class OrganiserService {
    private final OrganiserRepository organiserRepository;
    private final TournamentRepository tournamentRepository;

    @Autowired
    public OrganiserService(OrganiserRepository organiserRepository, TournamentRepository tournamentRepository) {
        this.organiserRepository = organiserRepository;
        this.tournamentRepository = tournamentRepository;
    }

    public CleanOrganiserDTO getCleanOrganiserDTO(Organiser o) {
        if (o == null) return null;
        return new CleanOrganiserDTO(o.getId(), o.isVerified(), o.getName(), o.getEmail(), o.getContactNo(), o.getCountry());
    }

        public List<Tournament> getOrganiserTournaments(Organiser o) {
        return tournamentRepository.findByOrganiserId(o.getId()).orElse(null);
    }
}
