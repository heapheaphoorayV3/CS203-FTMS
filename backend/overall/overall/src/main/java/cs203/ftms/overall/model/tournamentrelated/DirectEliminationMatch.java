package cs203.ftms.overall.model.tournamentrelated;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * Entity representing a Direct Elimination Match in a tournament.
 * This type of match progresses through rounds, with each match potentially
 * leading to a "next match" in the elimination bracket.
 */
@Entity
@DiscriminatorValue("D")
public class DirectEliminationMatch extends Match {

    /**
     * Indicates the tournament round this match belongs to (e.g., 32 for Round of 32, 16 for Round of 16, 8 for Quarter-finals, 4 for Semi-finals, 2 for Finals).
     * For example:
     * - roundOf = 32 means this match is part of the Round of 32
     * - roundOf = 16 means this match is part of the Round of 16
     * - roundOf = 8 means this match is part of the Quarter-finals
     * - roundOf = 4 means this match is part of the Semi-finals
     * - roundOf = 2 means this match is the Finals
     */
    @Column(name = "round_of")
    private int roundOf;

    /**
     * The ID of the next match in the elimination bracket that this match's winner will advance to.
     * For example, winners of two Round of 16 matches will advance to face each other in a Round of 8 match.
     * Will be 0 for the final match as there is no next match to advance to.
     */
    @Column(name = "next_match_id")
    private int nextMatchId;


    /**
     * Default constructor for DirectEliminationMatch.
     */
    public DirectEliminationMatch() {}

    /**
     * Constructs a DirectEliminationMatch associated with a specific event.
     *
     * @param event The event associated with this match.
     */
    public DirectEliminationMatch(Event event) {
        super(event);
    }

    /**
     * Gets the round of the match (e.g., Round of 16).
     *
     * @return The round of the match.
     */
    public int getRoundOf() {
        return roundOf;
    }

    /**
     * Sets the round of the match.
     *
     * @param roundOf The round of the match.
     */
    public void setRoundOf(int roundOf) {
        this.roundOf = roundOf;
    }

    /**
     * Gets the ID of the next match in the elimination bracket.
     *
     * @return The ID of the next match.
     */
    public int getNextMatchId() {
        return nextMatchId;
    }

    /**
     * Sets the ID of the next match in the elimination bracket.
     *
     * @param nextMatchId The ID of the next match.
     */
    public void setNextMatchId(int nextMatchId) {
        this.nextMatchId = nextMatchId;
    }
}
