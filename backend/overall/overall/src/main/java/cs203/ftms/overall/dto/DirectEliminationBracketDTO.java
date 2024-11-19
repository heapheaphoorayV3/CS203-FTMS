package cs203.ftms.overall.dto;

/**
 * Data Transfer Object representing a Direct Elimination Bracket in a tournament. This class
 * contains information about a specific match in the bracket, including participants, state,
 * round details, and timing.
 */
public class DirectEliminationBracketDTO {
    
    private int id;
    private String name;
    private int nextMatchId;
    private String tournamentRoundText;
    private String startTime;
    private String state;
    private DirectEliminationBracketFencerDTO[] participants;

    /**
     * Constructs a DirectEliminationBracketDTO with the specified details.
     *
     * @param id The unique identifier of the bracket match.
     * @param name The name of the bracket.
     * @param nextMatchId The ID of the next match in the bracket.
     * @param tournamentRoundText The text representing the tournament round (e.g., "Final").
     * @param startTime The start time of the match.
     * @param state The state of the match (e.g., "Scheduled", "Completed").
     * @param participants The array of participants in the match.
     */
    public DirectEliminationBracketDTO(int id, String name, int nextMatchId, String tournamentRoundText, String startTime, String state, DirectEliminationBracketFencerDTO[] participants) {
        this.id = id;
        this.name = name;
        this.nextMatchId = nextMatchId;
        this.tournamentRoundText = tournamentRoundText;
        this.startTime = startTime;
        this.state = state;
        this.participants = participants;
    }

    /**
     * Gets the ID of the bracket match.
     *
     * @return The bracket match ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID of the bracket match.
     *
     * @param id The bracket match ID to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the name of the bracket.
     *
     * @return The bracket name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the bracket.
     *
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the ID of the next match in the bracket.
     *
     * @return The next match ID.
     */
    public int getNextMatchId() {
        return nextMatchId;
    }

    /**
     * Sets the ID of the next match in the bracket.
     *
     * @param nextMatchId The next match ID to set.
     */
    public void setNextMatchId(int nextMatchId) {
        this.nextMatchId = nextMatchId;
    }

    /**
     * Gets the text representing the tournament round.
     *
     * @return The tournament round text.
     */
    public String getTournamentRoundText() {
        return tournamentRoundText;
    }
    
    /**
     * Sets the text representing the tournament round.
     *
     * @param tournamentRoundText The tournament round text to set.
     */
    public void setTournamentRoundText(String tournamentRoundText) {
        this.tournamentRoundText = tournamentRoundText;
    }

    /**
     * Gets the start time of the match.
     *
     * @return The start time.
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * Sets the start time of the match.
     *
     * @param startTime The start time to set.
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * Gets the state of the match.
     *
     * @return The match state.
     */
    public String getState() {
        return state;
    }
    
    /**
     * Sets the state of the match.
     *
     * @param state The match state to set.
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * Gets the participants in the match.
     *
     * @return An array of participants.
     */
    public DirectEliminationBracketFencerDTO[] getParticipants() {
        return participants;
    }

    /**
     * Sets the participants in the match.
     *
     * @param participants An array of participants to set.
     */
    public void setParticipants(DirectEliminationBracketFencerDTO[] participants) {
        this.participants = participants;
    }
}
