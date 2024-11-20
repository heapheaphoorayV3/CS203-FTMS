package cs203.ftms.overall.dto.clean;

/**
 * Data Transfer Object for representing a match, containing information
 * about the two fencers, their scores, the winner, and the type of match.
 */
public class CleanMatchDTO {
    private int id;
    private CleanTournamentFencerDTO fencer1;
    private int score1;
    private CleanTournamentFencerDTO fencer2;
    private int score2;
    private int winner;
    private char matchType;

    /**
     * Constructs a CleanMatchDTO with specified details about the match.
     *
     * @param id         The unique identifier of the match.
     * @param fencer1    The first fencer participating in the match.
     * @param score1     The score of the first fencer.
     * @param fencer2    The second fencer participating in the match.
     * @param score2     The score of the second fencer.
     * @param winner     The ID of the winning fencer.
     * @param matchType  The type of the match.
     */
    public CleanMatchDTO(int id, CleanTournamentFencerDTO fencer1, int score1, CleanTournamentFencerDTO fencer2,
                         int score2, int winner, char matchType) {
        this.id = id;
        this.fencer1 = fencer1;
        this.score1 = score1;
        this.fencer2 = fencer2;
        this.score2 = score2;
        this.winner = winner;
        this.matchType = matchType;
    }

    /**
     * Default constructor for CleanMatchDTO.
     */
    public CleanMatchDTO() {
    }

    /**
     * Gets the ID of the match.
     *
     * @return The match ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID of the match.
     *
     * @param id The match ID to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the first fencer in the match.
     *
     * @return The first fencer.
     */
    public CleanTournamentFencerDTO getFencer1() {
        return fencer1;
    }

    /**
     * Sets the first fencer in the match.
     *
     * @param fencer1 The first fencer to set.
     */
    public void setFencer1(CleanTournamentFencerDTO fencer1) {
        this.fencer1 = fencer1;
    }

    /**
     * Gets the score of the first fencer.
     *
     * @return The score of the first fencer.
     */
    public int getScore1() {
        return score1;
    }

    /**
     * Sets the score of the first fencer.
     *
     * @param score1 The score to set for the first fencer.
     */
    public void setScore1(int score1) {
        this.score1 = score1;
    }

    /**
     * Gets the second fencer in the match.
     *
     * @return The second fencer.
     */
    public CleanTournamentFencerDTO getFencer2() {
        return fencer2;
    }

    /**
     * Sets the second fencer in the match.
     *
     * @param fencer2 The second fencer to set.
     */
    public void setFencer2(CleanTournamentFencerDTO fencer2) {
        this.fencer2 = fencer2;
    }

    /**
     * Gets the score of the second fencer.
     *
     * @return The score of the second fencer.
     */
    public int getScore2() {
        return score2;
    }

    /**
     * Sets the score of the second fencer.
     *
     * @param score2 The score to set for the second fencer.
     */
    public void setScore2(int score2) {
        this.score2 = score2;
    }

    /**
     * Gets the winner of the match by ID.
     *
     * @return The ID of the winning fencer.
     */
    public int getWinner() {
        return winner;
    }

    /**
     * Sets the winner of the match by ID.
     *
     * @param winner The ID of the winning fencer to set.
     */
    public void setWinner(int winner) {
        this.winner = winner;
    }

    /**
     * Gets the type of the match.
     *
     * @return The match type.
     */
    public char getMatchType() {
        return matchType;
    }

    /**
     * Sets the type of the match.
     *
     * @param matchType The match type to set.
     */
    public void setMatchType(char matchType) {
        this.matchType = matchType;
    }
}
