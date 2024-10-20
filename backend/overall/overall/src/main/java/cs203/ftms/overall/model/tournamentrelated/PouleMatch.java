package cs203.ftms.overall.model.tournamentrelated;

import cs203.ftms.overall.model.userrelated.Fencer;
import jakarta.persistence.*;


@Entity
@Table(name = "poule_match")
public class PouleMatch extends Match {

    @ManyToOne
    private Poule poule; 

    public PouleMatch() {}

    public PouleMatch(Poule poule, Fencer fencer1, Fencer fencer2) {
        super(fencer1, fencer2);
        this.poule = poule;
    }

    public Poule getPoule() {
        return poule;
    }

    public void setPoule(Poule poule) {
        this.poule = poule;
    }
     
    public boolean equals(Object obj) {
        if (obj instanceof PouleMatch pm) {
            if (pm.getId() == this.getId()) return true;
        }
        return false;
    }
}