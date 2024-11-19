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

    @Column(name = "round_of")
    private int roundOf;

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
