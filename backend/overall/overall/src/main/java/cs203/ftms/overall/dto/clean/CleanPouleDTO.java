package cs203.ftms.overall.dto.clean;

import java.util.List;

/**
 * Data Transfer Object for representing a poule within a tournament event, including
 * details such as poule number, associated event, matches, and participating fencers.
 */
public class CleanPouleDTO {
    private int id;
    private int pouleNumber;
    private String eventName;
    private int eventId;
    private List<CleanMatchDTO> pouleMatches;
    private List<CleanTournamentFencerDTO> fencers;

    /**
     * Constructs a CleanPouleDTO with the specified poule details.
     *
     * @param id           The unique identifier of the poule.
     * @param pouleNumber  The number representing the poule within the event.
     * @param eventName    The name of the event associated with this poule.
     * @param eventId      The unique identifier of the event.
     * @param pouleMatches A list of matches within the poule.
     * @param fencers      A list of fencers participating in the poule.
     */
    public CleanPouleDTO(int id, int pouleNumber, String eventName, int eventId, List<CleanMatchDTO> pouleMatches,
                         List<CleanTournamentFencerDTO> fencers) {
        this.id = id;
        this.pouleNumber = pouleNumber;
        this.eventName = eventName;
        this.eventId = eventId;
        this.pouleMatches = pouleMatches;
        this.fencers = fencers;
    }

    /**
     * Gets the unique identifier of the poule.
     *
     * @return The poule ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the poule.
     *
     * @param id The ID to set for the poule.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the number of the poule within the event.
     *
     * @return The poule number.
     */
    public int getPouleNumber() {
        return pouleNumber;
    }

    /**
     * Sets the number of the poule within the event.
     *
     * @param pouleNumber The poule number to set.
     */
    public void setPouleNumber(int pouleNumber) {
        this.pouleNumber = pouleNumber;
    }

    /**
     * Gets the name of the event associated with this poule.
     *
     * @return The event name.
     */
    public String getEventName() {
        return eventName;
    }

    /**
     * Sets the name of the event associated with this poule.
     *
     * @param eventName The event name to set.
     */
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    /**
     * Gets the unique identifier of the event associated with this poule.
     *
     * @return The event ID.
     */
    public int getEventId() {
        return eventId;
    }

    /**
     * Sets the unique identifier of the event associated with this poule.
     *
     * @param eventId The event ID to set.
     */
    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    /**
     * Gets the list of matches within the poule.
     *
     * @return The list of poule matches.
     */
    public List<CleanMatchDTO> getPouleMatches() {
        return pouleMatches;
    }

    /**
     * Sets the list of matches within the poule.
     *
     * @param pouleMatches The list of poule matches to set.
     */
    public void setPouleMatches(List<CleanMatchDTO> pouleMatches) {
        this.pouleMatches = pouleMatches;
    }

    /**
     * Gets the list of fencers participating in the poule.
     *
     * @return The list of fencers.
     */
    public List<CleanTournamentFencerDTO> getFencers() {
        return fencers;
    }

    /**
     * Sets the list of fencers participating in the poule.
     *
     * @param fencers The list of fencers to set.
     */
    public void setFencers(List<CleanTournamentFencerDTO> fencers) {
        this.fencers = fencers;
    }
}
