package cs203.ftms.overall.model.tournamentrelated;

import cs203.ftms.overall.model.userrelated.Fencer;
import jakarta.persistence.*;

@Entity
@Table(name = "direct_elimination_match")
public class DirectEliminationMatch extends Match {
    @ManyToOne
    @JoinColumn(name = "event", nullable = false)
    private Event event; 

    public DirectEliminationMatch() {}

    public DirectEliminationMatch(Event event, Fencer fencer1, Fencer fencer2) {
        super(fencer1, fencer2);
        this.event = event;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    
}
