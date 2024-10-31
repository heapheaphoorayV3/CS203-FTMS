package cs203.ftms.overall.model.tournamentrelated;

import java.util.*;

import cs203.ftms.overall.model.userrelated.Fencer;
import jakarta.persistence.*;


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

    public Poule() {}
    
    public Poule(int pouleNumber, Event event) {
        this.pouleNumber = pouleNumber;
        this.event = event; 
        this.pouleMatches = new LinkedHashSet<>(); 
        this.fencers = new LinkedHashSet<>();
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

    public int getPouleNumber() {
        return pouleNumber;
    }

    public void setPouleNumber(int pouleNumber) {
        this.pouleNumber = pouleNumber;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Poule p) {
            if (p.getId() == this.getId()) return true;
        }
        return false;
    }

    public Set<TournamentFencer> getFencers() {
        return fencers;
    }

    public void setFencers(Set<TournamentFencer> fencers) {
        this.fencers = fencers;
    }


    public int compareTo(Poule p) {
        return this.pouleNumber - p.pouleNumber;
    }
    
}
