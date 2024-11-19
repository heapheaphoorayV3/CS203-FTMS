package cs203.ftms.overall.dto;

import java.util.Map;

/**
 * Data Transfer Object (DTO) for representing the result of a poule match.
 * It contains details about the event, the specific poule number, 
 * and a map of scores for each match in the poule.
 */
public class PouleMatchResultDTO {

    private int eventId; 
    private int pouleNumber;
    private Map<String, String> pouleScores;

    /**
     * Constructs a new PouleMatchResultDTO with the specified event ID, poule number, and scores.
     * 
     * @param eventId the ID of the event
     * @param pouleNumber the number of the poule
     * @param pouleScores a map containing the scores for each match in the poule
     */
    public PouleMatchResultDTO(int eventId, int pouleNumber, Map<String, String> pouleScores) {
        this.eventId = eventId;
        this.pouleNumber = pouleNumber;
        this.pouleScores = pouleScores;
    }

    /**
     * Gets the ID of the event associated with this poule match result.
     * 
     * @return the event ID
     */
    public int getEventId() {
        return eventId;
    }

    /**
     * Sets the ID of the event associated with this poule match result.
     * 
     * @param eventId the event ID to set
     */
    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    /**
     * Gets the number of the poule in the event.
     * 
     * @return the poule number
     */
    public int getPouleNumber() {
        return pouleNumber;
    }

    /**
     * Sets the number of the poule in the event.
     * 
     * @param pouleNumber the poule number to set
     */
    public void setPouleNumber(int pouleNumber) {
        this.pouleNumber = pouleNumber;
    }

    /**
     * Gets the scores for each match in the poule.
     * 
     * @return a map where the keys are match identifiers and the values are the scores
     */
    public Map<String, String> getPouleScores() {
        return pouleScores;
    }

    /**
     * Sets the scores for each match in the poule.
     * 
     * @param pouleScores a map containing the scores for each match in the poule
     */
    public void setPouleScores(Map<String, String> pouleScores) {
        this.pouleScores = pouleScores;
    }
}
