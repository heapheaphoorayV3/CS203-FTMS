package cs203.ftms.overall.model.tournamentrelated;

import cs203.ftms.overall.model.userrelated.Fencer;

import jakarta.persistence.*;

import java.util.Iterator;
import java.util.Set;

@Entity
@Table(name = "fencing_match")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="match_type",
        discriminatorType = DiscriminatorType.CHAR)
@DiscriminatorValue("M")
// @MappedSuperclass
public class Match {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // @ManyToOne
    // @JoinColumn(name = "fencer1_id")
    // private TournamentFencer fencer1;

    // @ManyToOne
    // @JoinColumn(name = "fencer2_id")
    // private TournamentFencer fencer2; 

    @ManyToMany(mappedBy = "matches", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.DETACH})
    private Set<TournamentFencer> fencers; 

    // @ManyToOne
    // private Fencer fencer1;

    // @ManyToOne
    // private Fencer fencer2; 

    // @Column(name = "fencer1")
    // private Fencer fencer1;

    // @Column(name = "fencer2")
    // private Fencer fencer2; 
    
    @Column(name = "score1")
    private int score1; 

    @Column(name = "score2")
    private int score2; 

    @Column(name = "winner")
    private int winner; // tf winner id 

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "event", nullable = false)
    private Event event; 

    public Match() {}

    public Match(Set<TournamentFencer> fencers, Event event) {
        this.fencers = fencers; 
        this.event = event;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public Set<TournamentFencer> getFencers() {
        return fencers;
    }

    public void setFencers(Set<TournamentFencer> fencers) {
        this.fencers = fencers;
    }

    public int getScore1() {
        return score1;
    }

    public void setScore1(int score1) {
        this.score1 = score1;
    }

    public int getScore2() {
        return score2;
    }

    public void setScore2(int score2) {
        this.score2 = score2;
    }

    public String toString() {
        Iterator<TournamentFencer> iter = fencers.iterator();
        TournamentFencer tf1 = iter.next();
        TournamentFencer tf2 = iter.next();
        // return String.format("%d (%s) - %d (%s)", score1, fencers[0].getName(), score2, fencers[1].getName());
        return String.format("%d (%s) - %d (%s)", score1, tf1.getFencer().getName(), score2, tf2.getFencer().getName());
    }

    // public Fencer[] getFencers() {
    //     return fencers;
    // }

    // public void setFencers(Fencer[] fencers) {
    //     this.fencers = fencers;
    // }

    public boolean equals(Object obj) {
        if (obj instanceof Match m) {
            if (m.getId() == this.getId()) return true;
        }
        return false;
    }

    public int getWinner() {
        return winner;
    }

    public void setWinner(int winner) {
        this.winner = winner;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    
}
