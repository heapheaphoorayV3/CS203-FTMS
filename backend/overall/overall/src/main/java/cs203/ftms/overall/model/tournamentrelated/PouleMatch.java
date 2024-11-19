package cs203.ftms.overall.model.tournamentrelated;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * Represents a match within a poule (group) in a tournament event.
 * Extends the generic Match class and includes an association to a specific poule.
 */
@Entity
@DiscriminatorValue("P")
public class PouleMatch extends Match {

    @ManyToOne
    @JoinColumn(name = "poule_id")
    private Poule poule;

    /**
     * Default constructor for PouleMatch.
     */
    public PouleMatch() {}

    /**
     * Constructs a PouleMatch associated with a specific poule.
     *
     * @param poule The poule to which this match belongs.
     */
    public PouleMatch(Poule poule) {
        super(poule.getEvent());
        this.poule = poule;
    }

    /**
     * Gets the poule associated with this match.
     *
     * @return The poule associated with this match.
     */
    public Poule getPoule() {
        return poule;
    }

    /**
     * Sets the poule associated with this match.
     *
     * @param poule The poule to associate with this match.
     */
    public void setPoule(Poule poule) {
        this.poule = poule;
    }

    /**
     * Checks if this PouleMatch is equal to another object based on match ID.
     *
     * @param obj The object to compare with.
     * @return true if the object is a PouleMatch with the same ID, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PouleMatch pm) {
            return pm.getId() == this.getId();
        }
        return false;
    }
}
