package cs203.ftms.overall.dto;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Data Transfer Object (DTO) for representing a poule table in a fencing tournament.
 * The table contains match details as a list of maps, where each map holds 
 * key-value pairs representing match attributes.
 */
public class PouleTableDTO {

    private List<Map<String, String>> pouleTable;

    /**
     * Default constructor for PouleTableDTO, initializing the poule table as an empty list.
     */
    public PouleTableDTO() {
        this.pouleTable = new LinkedList<>();
    }

    /**
     * Adds a new map of match details to the poule table.
     *
     * @param pouleTable a map containing key-value pairs representing match details
     */
    public void addPouleTable(Map<String, String> pouleTable) {
        this.pouleTable.add(pouleTable);
    }

    /**
     * Gets the list representing the poule table.
     *
     * @return a list of maps, each containing match details
     */
    public List<Map<String, String>> getPouleTable() {
        return pouleTable;
    }

    /**
     * Sets the list representing the poule table.
     *
     * @param pouleTable a list of maps to set as the poule table
     */
    public void setPouleTable(List<Map<String, String>> pouleTable) {
        this.pouleTable = pouleTable;
    }
}
