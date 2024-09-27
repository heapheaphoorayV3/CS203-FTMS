package cs203.ftms.overall.model.tournamentrelated;

import java.util.*;

import cs203.ftms.overall.model.userrelated.Fencer;
import jakarta.persistence.*;


@Entity
@Table(name = "Poule")
public class Poule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "tournament_category_id", nullable = false)
    private Event event; 
    
    @OneToMany(mappedBy = "poule")
    private Set<PouleMatch> pouleMatches;
    
    public Poule() {}
    
    public Poule(Event event, Set<PouleMatch> pouleMatches) {
        this.pouleMatches = pouleMatches;
    }

    public Set<PouleMatch> getPouleMatches() {
        return pouleMatches;
    }

    public void setPouleMatches(Set<PouleMatch> pouleMatches) {
        this.pouleMatches = pouleMatches;
    } 

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Set<Fencer> getFencers() {
        Set<Fencer> fencers = new HashSet<>();
        for (PouleMatch match : pouleMatches) {
            fencers.add(match.getFencer1());
            fencers.add(match.getFencer2());
        }
        return fencers;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Poule p) {
            if (p.getId() == this.getId()) return true;
        }
        return false;
    }
}
