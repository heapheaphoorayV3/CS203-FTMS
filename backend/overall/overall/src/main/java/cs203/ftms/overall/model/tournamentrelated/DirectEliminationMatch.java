package cs203.ftms.overall.model.tournamentrelated;

import java.util.Set;

import cs203.ftms.overall.model.userrelated.Fencer;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("D")
public class DirectEliminationMatch extends Match {

    public DirectEliminationMatch() {}

    public DirectEliminationMatch(Event event, Set<TournamentFencer> fencers) {
        super(fencers, event);
    }
    
}
