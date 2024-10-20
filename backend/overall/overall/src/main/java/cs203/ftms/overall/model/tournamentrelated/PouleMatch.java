package cs203.ftms.overall.model.tournamentrelated;

import java.util.Set;

import cs203.ftms.overall.model.userrelated.Fencer;
import jakarta.persistence.*;


@Entity
@DiscriminatorValue("P")
public class PouleMatch extends Match {

    @ManyToOne
    @JoinColumn(name = "pouleId", nullable = false)
    private Poule poule; 

    public PouleMatch() {}

    public PouleMatch(Poule poule, Set<TournamentFencer> fencers) {
        super(fencers, poule.getEvent());
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