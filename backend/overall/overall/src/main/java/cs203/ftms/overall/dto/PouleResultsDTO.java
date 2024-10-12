package cs203.ftms.overall.dto;

import java.util.List;

import cs203.ftms.overall.dto.clean.CleanTournamentFencerDTO;

public class PouleResultsDTO {
    private List<CleanTournamentFencerDTO> bypassFencers;
    private List<CleanTournamentFencerDTO> fenceOffFencers;
    private List<CleanTournamentFencerDTO> eliminatedFencers;

    public PouleResultsDTO() {
    }

    public List<CleanTournamentFencerDTO> getBypassFencers() {
        return bypassFencers;
    }

    public void setBypassFencers(List<CleanTournamentFencerDTO> bypassFencers) {
        this.bypassFencers = bypassFencers;
    }

    public List<CleanTournamentFencerDTO> getFenceOffFencers() {
        return fenceOffFencers;
    }

    public void setFenceOffFencers(List<CleanTournamentFencerDTO> fenceOffFencers) {
        this.fenceOffFencers = fenceOffFencers;
    }

    public List<CleanTournamentFencerDTO> getEliminatedFencers() {
        return eliminatedFencers;
    }

    public void setEliminatedFencers(List<CleanTournamentFencerDTO> eliminatedFencers) {
        this.eliminatedFencers = eliminatedFencers;
    }

    

    
}
