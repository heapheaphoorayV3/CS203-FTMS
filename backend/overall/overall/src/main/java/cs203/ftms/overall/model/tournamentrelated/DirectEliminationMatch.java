package cs203.ftms.overall.model.tournamentrelated;

import java.util.Set;

import cs203.ftms.overall.model.userrelated.Fencer;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("D")
public class DirectEliminationMatch extends Match {

    @Column(name = "round_of")
    private int roundOf;

    @Column(name = "next_match_id")
    private int nextMatchId;

    public DirectEliminationMatch() {}

    public DirectEliminationMatch(Event event) {
        super(event);
    }

    public int getRoundOf() {
        return roundOf;
    }

    public void setRoundOf(int roundOf) {
        this.roundOf = roundOf;
    }

    public int getNextMatchId() {
        return nextMatchId;
    }

    public void setNextMatchId(int nextMatchId) {
        this.nextMatchId = nextMatchId;
    }

    
    
}
