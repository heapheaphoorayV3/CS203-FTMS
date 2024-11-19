package cs203.ftms.overall.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

/**
 * Data Transfer Object (DTO) for updating scores in a direct elimination match.
 * Contains validation constraints for minimum and maximum score values.
 */
public class UpdateDirectEliminationMatchDTO {

    private int matchId;
    
    @Min(value=0, message = "Minimum points for a direct elimination match is 0")
    @Max(value = 15, message = "Maximum points for a direct elimination match is 15")
    private int score1;

    @Min(value=0, message = "Minimum points for a direct elimination match is 0")
    @Max(value = 15, message = "Maximum points for a direct elimination match is 15")
    private int score2;

    /**
     * Constructs an UpdateDirectEliminationMatchDTO with the specified match ID and scores.
     *
     * @param matchId the ID of the match to be updated
     * @param score1  the score for the first fencer
     * @param score2  the score for the second fencer
     */
    public UpdateDirectEliminationMatchDTO(int matchId, int score1, int score2) {
        this.matchId = matchId;
        this.score1 = score1;
        this.score2 = score2;
    }

    /**
     * Gets the match ID of the direct elimination match.
     *
     * @return the match ID
     */
    public int getMatchId() {
        return matchId;
    }

    /**
     * Sets the match ID of the direct elimination match.
     *
     * @param matchId the match ID to set
     */
    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    /**
     * Gets the score of the first fencer in the direct elimination match.
     *
     * @return the score of the first fencer
     */
    public int getScore1() {
        return score1;
    }

    /**
     * Sets the score of the first fencer in the direct elimination match.
     *
     * @param score1 the score of the first fencer to set
     */
    public void setScore1(int score1) {
        this.score1 = score1;
    }

    /**
     * Gets the score of the second fencer in the direct elimination match.
     *
     * @return the score of the second fencer
     */
    public int getScore2() {
        return score2;
    }

    /**
     * Sets the score of the second fencer in the direct elimination match.
     *
     * @param score2 the score of the second fencer to set
     */
    public void setScore2(int score2) {
        this.score2 = score2;
    }
}
