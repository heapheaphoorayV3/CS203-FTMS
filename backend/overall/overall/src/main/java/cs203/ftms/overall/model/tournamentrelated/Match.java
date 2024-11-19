package cs203.ftms.overall.model.tournamentrelated;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Represents a generic fencing match in a tournament, containing details about
 * the participants, scores, winner, and associated event.
 */
@Entity
@Table(name = "fencing_match")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "match_type", discriminatorType = DiscriminatorType.CHAR)
@DiscriminatorValue("M")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "fencer1")
    private int fencer1;

    @Column(name = "fencer2")
    private int fencer2;

    @Column(name = "score1")
    private int score1;

    @Column(name = "score2")
    private int score2;

    @Column(name = "winner")
    private int winner; // TournamentFencer winner ID

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "event", nullable = false)
    private Event event;

    /**
     * Default constructor for Match, initializing participant and winner IDs to -1.
     */
    public Match() {
        this.fencer1 = -1;
        this.fencer2 = -1;
        this.winner = -1;
    }

    /**
     * Constructs a Match associated with a specific event.
     *
     * @param event The event associated with this match.
     */
    public Match(Event event) {
        this.fencer1 = -1;
        this.fencer2 = -1;
        this.winner = -1;
        this.event = event;
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
     * Gets the ID of the first fencer.
     *
     * @return The ID of the first fencer.
     */
    public int getFencer1() {
        return fencer1;
    }

    /**
     * Sets the ID of the first fencer.
     *
     * @param fencer1 The ID of the first fencer.
     */
    public void setFencer1(int fencer1) {
        this.fencer1 = fencer1;
    }

    /**
     * Gets the ID of the second fencer.
     *
     * @return The ID of the second fencer.
     */
    public int getFencer2() {
        return fencer2;
    }

    /**
     * Sets the ID of the second fencer.
     *
     * @param fencer2 The ID of the second fencer.
     */
    public void setFencer2(int fencer2) {
        this.fencer2 = fencer2;
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
     * Gets the ID of the winning fencer.
     *
     * @return The ID of the winning fencer.
     */
    public int getWinner() {
        return winner;
    }

    /**
     * Sets the ID of the winning fencer.
     *
     * @param winner The ID of the winning fencer.
     */
    public void setWinner(int winner) {
        this.winner = winner;
    }

    /**
     * Gets the event associated with this match.
     *
     * @return The associated event.
     */
    public Event getEvent() {
        return event;
    }

    /**
     * Sets the event associated with this match.
     *
     * @param event The event to associate with this match.
     */
    public void setEvent(Event event) {
        this.event = event;
    }

    /**
     * Checks if this match is equal to another object based on match ID.
     *
     * @param obj The object to compare with.
     * @return true if the object is a Match with the same ID, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Match m) {
            return m.getId() == this.getId();
        }
        return false;
    }
}
