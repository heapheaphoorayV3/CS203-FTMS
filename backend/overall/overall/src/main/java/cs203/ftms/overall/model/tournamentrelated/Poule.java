package cs203.ftms.overall.model.tournamentrelated;

import java.util.LinkedHashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Represents a poule (group) within a tournament event, containing details about the
 * poule number, matches, and participating fencers.
 */
@Entity
@Table(name = "Poule")
public class Poule implements Comparable<Poule> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "poule_number")
    private int pouleNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @OneToMany(mappedBy = "poule", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PouleMatch> pouleMatches;

    @OneToMany(mappedBy = "poule")
    private Set<TournamentFencer> fencers;

    /**
     * Default constructor for Poule.
     */
    public Poule() {}

    /**
     * Constructs a Poule with a specified poule number and associated event.
     *
     * @param pouleNumber The number identifying the poule within the event.
     * @param event The event associated with this poule.
     */
    public Poule(int pouleNumber, Event event) {
        this.pouleNumber = pouleNumber;
        this.event = event;
        this.pouleMatches = new LinkedHashSet<>();
        this.fencers = new LinkedHashSet<>();
    }

    /**
     * Gets the ID of the poule.
     *
     * @return The poule ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID of the poule.
     *
     * @param id The ID to set for the poule.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the poule number.
     *
     * @return The number identifying this poule.
     */
    public int getPouleNumber() {
        return pouleNumber;
    }

    /**
     * Sets the poule number.
     *
     * @param pouleNumber The number identifying this poule.
     */
    public void setPouleNumber(int pouleNumber) {
        this.pouleNumber = pouleNumber;
    }

    /**
     * Gets the event associated with this poule.
     *
     * @return The associated event.
     */
    public Event getEvent() {
        return event;
    }

    /**
     * Sets the event associated with this poule.
     *
     * @param event The event to associate with this poule.
     */
    public void setEvent(Event event) {
        this.event = event;
    }

    /**
     * Gets the matches in this poule.
     *
     * @return The set of matches in this poule.
     */
    public Set<PouleMatch> getPouleMatches() {
        return pouleMatches;
    }

    /**
     * Sets the matches in this poule.
     *
     * @param pouleMatches The set of matches to assign to this poule.
     */
    public void setPouleMatches(Set<PouleMatch> pouleMatches) {
        this.pouleMatches = pouleMatches;
    }

    /**
     * Gets the fencers participating in this poule.
     *
     * @return The set of fencers in this poule.
     */
    public Set<TournamentFencer> getFencers() {
        return fencers;
    }

    /**
     * Sets the fencers participating in this poule.
     *
     * @param fencers The set of fencers to assign to this poule.
     */
    public void setFencers(Set<TournamentFencer> fencers) {
        this.fencers = fencers;
    }

    /**
     * Checks if this poule is equal to another object based on poule ID.
     *
     * @param obj The object to compare with.
     * @return true if the object is a Poule with the same ID, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Poule p) {
            return p.getId() == this.getId();
        }
        return false;
    }

    /**
     * Compares this poule to another based on poule number.
     *
     * @param p The poule to compare with.
     * @return The difference in poule numbers between this poule and the specified poule.
     */
    @Override
    public int compareTo(Poule p) {
        return this.pouleNumber - p.pouleNumber;
    }
}
