package cs203.ftms.overall.model.tournamentrelated;

import cs203.ftms.overall.model.userrelated.Fencer;

import jakarta.persistence.*;


@MappedSuperclass
public class Match {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "fencer1")
    private Fencer fencer1;

    @Column(name = "fencer2")
    private Fencer fencer2; 
    
    @Column(name = "score1")
    private int score1; 

    @Column(name = "score2")
    private int score2; 

    public Match() {}

    public Match(Fencer fencer1, Fencer fencer2) {
        this.fencer1 = fencer1; 
        this.fencer2 = fencer2;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Fencer getFencer1() {
        return fencer1;
    }

    public void setFencer1(Fencer fencer1) {
        this.fencer1 = fencer1;
    }

    public Fencer getFencer2() {
        return fencer2;
    }

    public void setFencer2(Fencer fencer2) {
        this.fencer2 = fencer2;
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
        // return String.format("%d (%s) - %d (%s)", score1, fencers[0].getName(), score2, fencers[1].getName());
        return String.format("%d (%s) - %d (%s)", score1, getFencer1().getName(), score2, getFencer2().getName());

    }

    public boolean equals(Object obj) {
        if (obj instanceof Match m) {
            if (m.getId() == this.getId()) return true;
        }
        return false;
    }
}
