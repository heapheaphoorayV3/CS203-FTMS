package cs203.ftms.tournament.model;

import jakarta.persistence.*;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import cs203.ftms.user.model.Fencer;

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

    @Column(name = "fencer1")
    private int fencer1;

    @Column(name = "fencer2")
    private int fencer2;

    @Column(name = "score1")
    private int score1; 

    @Column(name = "score2")
    private int score2; 

    @Column(name = "winner")
    private int winner; // tf winner id

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "event", nullable = false)
    private Event event; 

    public Match() {
        this.fencer1 = -1;
        this.fencer2 = -1;
        this.winner = -1;
    }

    public Match(Event event) {
        this.fencer1 = -1;
        this.fencer2 = -1;
        this.winner = -1;
        this.event = event;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
    
    public int getFencer1(){
        return fencer1;
    }

    public int getFencer2(){
        return fencer2;
    }

    public void setFencer1(int fencer1){
        this.fencer1 = fencer1;
    }

    public void setFencer2(int fencer2){
        this.fencer2 = fencer2;
    }
}
