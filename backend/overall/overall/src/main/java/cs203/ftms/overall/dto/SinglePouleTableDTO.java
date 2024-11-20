package cs203.ftms.overall.dto;

import java.util.Map;

/**
 * Data Transfer Object (DTO) representing a single poule table with scores and related details.
 * Contains the poule number and a map representing the scores table.
 */
public class SinglePouleTableDTO {

    private int pouleNumber;
    private Map<String, String> singleTable;

    /**
     * Constructs a SinglePouleTableDTO with the specified poule number and score table.
     *
     * @param pouleNumber the number of the poule
     * @param singleTable a map containing the scores for the poule table
     */
    public SinglePouleTableDTO(int pouleNumber, Map<String, String> singleTable) {
        this.pouleNumber = pouleNumber;
        this.singleTable = singleTable;
    }

    /**
     * Gets the poule number associated with this poule table.
     *
     * @return the poule number
     */
    public int getPouleNumber() {
        return pouleNumber;
    }

    /**
     * Sets the poule number associated with this poule table.
     *
     * @param pouleNumber the poule number to set
     */
    public void setPouleNumber(int pouleNumber) {
        this.pouleNumber = pouleNumber;
    }

    /**
     * Gets the scores table for this single poule.
     *
     * @return a map containing scores for the poule
     */
    public Map<String, String> getSingleTable() {
        return singleTable;
    }

    /**
     * Sets the scores table for this single poule.
     *
     * @param singleTable a map containing scores for the poule
     */
    public void setSingleTable(Map<String, String> singleTable) {
        this.singleTable = singleTable;
    }
}
