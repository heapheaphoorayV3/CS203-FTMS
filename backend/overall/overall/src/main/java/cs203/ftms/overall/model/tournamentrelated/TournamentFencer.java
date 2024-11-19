package cs203.ftms.overall.model.tournamentrelated;

import java.util.LinkedHashSet;
import java.util.Set;

import cs203.ftms.overall.model.userrelated.Fencer;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


/**
 * Represents a fencer's participation in a specific tournament event,
 * containing details about their matches, ranking, and performance in poules.
 */
@Entity
@Table(name = "tournament_fencer")
public class TournamentFencer {

    /**
     * Unique identifier for the tournament fencer entry.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * The fencer participating in the tournament.
     * Represents the actual fencer entity with their personal details.
     * Fetched eagerly to ensure fencer details are always available.
     * Cannot be null as every tournament fencer must be associated with a real fencer.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fencer_id", nullable = false)
    private Fencer fencer;

    /**
     * The specific event within the tournament that this fencer is participating in.
     * Fetched eagerly to ensure event details are always available.
     * For example, Men's Foil, Women's Epee, etc.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "event_id")
    private Event event;

    /**
     * Collection of all matches this fencer has participated in during the tournament.
     * Includes both poule matches and direct elimination matches.
     * Fetched eagerly to ensure match details are immediately available.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "tournament_fencer_matches")
    private Set<Match> matches;

    /**
     * The poule group this fencer is assigned to during the poule round.
     * Cascade operations are limited to PERSIST and DETACH to maintain data integrity.
     * May be null if the fencer hasn't been assigned to a poule yet or if the event
     * doesn't use poules.
     */
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH})
    @JoinColumn(name = "poule_id")
    private Poule poule;

    /**
     * The fencer's final ranking in the tournament.
     * Updated after each round (poules and direct elimination).
     * Lower numbers indicate better performance (1 is first place).
     */
    @Column(name = "tournament_rank")
    private int tournamentRank;

    /**
     * The number of victories achieved by the fencer during the poule round.
     * Used in calculating the fencer's seeding for direct elimination.
     */
    @Column(name = "poule_wins")
    private int pouleWins;

    /**
     * The total number of points scored by the fencer during the poule round.
     * Used along with poule wins to determine seeding for direct elimination.
     * Points are the sum of touches scored minus touches received in poule matches.
     */
    @Column(name = "poule_points")
    private int poulePoints;

    /**
     * The total ranking points earned by the fencer after the event is complete.
     * These points contribute to the fencer's overall ranking in the competition circuit.
     * Points are awarded based on final placement and event importance.
     */
    @Column(name = "points_after_event")
    private int pointsAfterEvent;


    /**
     * Constructs a TournamentFencer associated with a specific fencer and event.
     *
     * @param fencer The fencer participating in the tournament.
     * @param event The event in which the fencer is participating.
     */
    public TournamentFencer(Fencer fencer, Event event) {
        this.fencer = fencer;
        this.event = event;
        this.matches = new LinkedHashSet<>();
        this.pointsAfterEvent = -1;
    }

    public TournamentFencer() {
        this.matches = new LinkedHashSet<>();
        this.pointsAfterEvent = -1;
    }

    /**
     * Gets the ID of the tournament fencer.
     *
     * @return The tournament fencer ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID of the tournament fencer.
     *
     * @param id The ID to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the fencer associated with this tournament fencer.
     *
     * @return The fencer.
     */
    public Fencer getFencer() {
        return fencer;
    }

    /**
     * Sets the fencer associated with this tournament fencer.
     *
     * @param fencer The fencer to associate.
     */
    public void setFencer(Fencer fencer) {
        this.fencer = fencer;
    }

    /**
     * Gets the event associated with this tournament fencer.
     *
     * @return The event.
     */
    public Event getEvent() {
        return event;
    }

    /**
     * Sets the event associated with this tournament fencer.
     *
     * @param event The event to associate.
     */
    public void setEvent(Event event) {
        this.event = event;
    }

    /**
     * Gets the matches played by this tournament fencer.
     *
     * @return The set of matches.
     */
    public Set<Match> getMatches() {
        return matches;
    }

    /**
     * Sets the matches played by this tournament fencer.
     *
     * @param matches The set of matches.
     */
    public void setMatches(Set<Match> matches) {
        this.matches = matches;
    }

    /**
     * Adds a match to the tournament fencer's match history.
     *
     * @param match The match to add.
     */
    public void addMatch(Match match) {
        matches.add(match);
    }

    /**
     * Removes a match from the tournament fencer's match history.
     *
     * @param match The match to remove.
     */
    public void removeMatch(Match match) {
        matches.remove(match);
    }

    /**
     * Gets the poule associated with this tournament fencer.
     *
     * @return The poule.
     */
    public Poule getPoule() {
        return poule;
    }

    /**
     * Sets the poule associated with this tournament fencer.
     *
     * @param poule The poule to associate.
     */
    public void setPoule(Poule poule) {
        this.poule = poule;
    }

    /**
     * Gets the rank of the fencer in the tournament.
     *
     * @return The tournament rank.
     */
    public int getTournamentRank() {
        return tournamentRank;
    }

    /**
     * Sets the rank of the fencer in the tournament.
     *
     * @param tournamentRank The tournament rank to set.
     */
    public void setTournamentRank(int tournamentRank) {
        this.tournamentRank = tournamentRank;
    }

    /**
     * Gets the number of wins in poule matches.
     *
     * @return The number of poule wins.
     */
    public int getPouleWins() {
        return pouleWins;
    }

    /**
     * Sets the number of wins in poule matches.
     *
     * @param pouleWins The number of poule wins to set.
     */
    public void setPouleWins(int pouleWins) {
        this.pouleWins = pouleWins;
    }

    /**
     * Gets the points accumulated in poule matches.
     *
     * @return The poule points.
     */
    public int getPoulePoints() {
        return poulePoints;
    }

    /**
     * Sets the points accumulated in poule matches.
     *
     * @param poulePoints The poule points to set.
     */
    public void setPoulePoints(int poulePoints) {
        this.poulePoints = poulePoints;
    }

    /**
     * Gets the points of the fencer after the event.
     *
     * @return The points of the fencer after the event.
     */
    public int getPointsAfterEvent() {
        return pointsAfterEvent;
    }

    /**
     * Sets the points of the fencer after the event.
     *
     * @param pointsAfterEvent The points to set after the event.
     */
    public void setPointsAfterEvent(int pointsAfterEvent) {
        this.pointsAfterEvent = pointsAfterEvent;
    }
}
