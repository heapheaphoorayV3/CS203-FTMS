package cs203.ftms.overall.dto;

/**
 * Data Transfer Object representing a fencer in a Direct Elimination Bracket.
 * Contains information about the fencer's ID, name, match result, status, and
 * whether they are the winner.
 */
public class DirectEliminationBracketFencerDTO {

    private int id;
    private String resultText;
    private boolean isWinner;
    private String status;
    private String name;

    /**
     * Constructs a DirectEliminationBracketFencerDTO with the specified details.
     *
     * @param id The unique identifier of the fencer.
     * @param resultText The result text associated with the fencer.
     * @param isWinner Indicates if the fencer is the winner of the match.
     * @param status The status of the fencer in the match.
     * @param name The name of the fencer.
     */
    public DirectEliminationBracketFencerDTO(int id, String resultText, boolean isWinner, String status, String name) {
        this.id = id;
        this.resultText = resultText;
        this.isWinner = isWinner;
        this.status = status;
        this.name = name;
    }

    /**
     * Gets the ID of the fencer.
     *
     * @return The fencer's ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID of the fencer.
     *
     * @param id The fencer's ID to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the result text for the fencer.
     *
     * @return The result text.
     */
    public String getResultText() {
        return resultText;
    }

    /**
     * Sets the result text for the fencer.
     *
     * @param resultText The result text to set.
     */
    public void setResultText(String resultText) {
        this.resultText = resultText;
    }

    /**
     * Checks if the fencer is the winner.
     *
     * @return {@code true} if the fencer is the winner, {@code false} otherwise.
     */
    public boolean getIsWinner() {
        return isWinner;
    }

    /**
     * Sets the winner status of the fencer.
     *
     * @param isWinner {@code true} if the fencer is the winner, {@code false} otherwise.
     */
    public void setIsWinner(boolean isWinner) {
        this.isWinner = isWinner;
    }

    /**
     * Gets the status of the fencer in the match.
     *
     * @return The status.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status of the fencer in the match.
     *
     * @param status The status to set.
     */
    public void setStatus(String status) {
        this.status = status;
    }
    
    /**
     * Gets the name of the fencer.
     *
     * @return The fencer's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the fencer.
     *
     * @param name The fencer's name to set.
     */
    public void setName(String name) {
        this.name = name;
    }
}
