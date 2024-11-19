package cs203.ftms.overall.dto;

import java.util.List;

import cs203.ftms.overall.dto.clean.CleanTournamentFencerDTO;

/**
 * Data Transfer Object (DTO) for representing the results of a poule round.
 * It includes lists of fencers who bypassed to the next round, 
 * those who are in fence-off matches, and those who have been eliminated.
 */
public class PouleResultsDTO {

    private List<CleanTournamentFencerDTO> bypassFencers;
    private List<CleanTournamentFencerDTO> fenceOffFencers;
    private List<CleanTournamentFencerDTO> eliminatedFencers;

    /**
     * Default constructor for PouleResultsDTO.
     */
    public PouleResultsDTO() {
    }

    /**
     * Gets the list of fencers who bypassed to the next round.
     *
     * @return a list of fencers who bypassed to the next round
     */
    public List<CleanTournamentFencerDTO> getBypassFencers() {
        return bypassFencers;
    }

    /**
     * Sets the list of fencers who bypassed to the next round.
     *
     * @param bypassFencers the list of bypass fencers to set
     */
    public void setBypassFencers(List<CleanTournamentFencerDTO> bypassFencers) {
        this.bypassFencers = bypassFencers;
    }

    /**
     * Gets the list of fencers who are in fence-off matches.
     *
     * @return a list of fencers in fence-off matches
     */
    public List<CleanTournamentFencerDTO> getFenceOffFencers() {
        return fenceOffFencers;
    }

    /**
     * Sets the list of fencers who are in fence-off matches.
     *
     * @param fenceOffFencers the list of fence-off fencers to set
     */
    public void setFenceOffFencers(List<CleanTournamentFencerDTO> fenceOffFencers) {
        this.fenceOffFencers = fenceOffFencers;
    }

    /**
     * Gets the list of fencers who have been eliminated from the tournament.
     *
     * @return a list of eliminated fencers
     */
    public List<CleanTournamentFencerDTO> getEliminatedFencers() {
        return eliminatedFencers;
    }

    /**
     * Sets the list of fencers who have been eliminated from the tournament.
     *
     * @param eliminatedFencers the list of eliminated fencers to set
     */
    public void setEliminatedFencers(List<CleanTournamentFencerDTO> eliminatedFencers) {
        this.eliminatedFencers = eliminatedFencers;
    }
}
