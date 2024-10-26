package cs203.ftms.overall.model.tournamentrelated;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

import cs203.ftms.overall.model.userrelated.Fencer;
import jakarta.persistence.*;


@Entity
@Table(name = "tournament_fencer")
public class TournamentFencer {
    // fencer will store a list of tf 
    // all the tournament related details will be linked to tf
    // if fencer unregister or nvr attend, then delete this profile
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fencer_id", nullable = false)
    private Fencer fencer; 

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event; 

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "tournament_fencer_matches")
    private Set<Match> matches; 

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH})
    @JoinColumn(name = "poule_id")
    private Poule poule; 

    @Column(name="tournament_rank")
    private int tournamentRank; // specific to particular event

    @Column(name = "poule_wins")
    private int pouleWins;

    @Column(name = "poule_points")
    private int poulePoints;

    public TournamentFencer(Fencer fencer, Event event) {
        this.fencer = fencer;
        this.event = event;
        this.matches = new LinkedHashSet<>();
    }

    public TournamentFencer() {
        this.matches = new LinkedHashSet<>();
    }

    public Fencer getFencer() {
        return fencer;
    }

    public void setFencer(Fencer fencer) {
        this.fencer = fencer;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Set<Match> getMatches() {
        return matches;
    }

    public void setMatches(Set<Match> matches) {
        this.matches = matches;
    }

    public Poule getPoule() {
        return poule;
    }

    public void setPoule(Poule poule) {
        this.poule = poule;
    }

    public int getTournamentRank() {
        return tournamentRank;
    }

    public void setTournamentRank(int tournamentPoints) {
        this.tournamentRank = tournamentPoints;
    } 

    // public int compareTo(TournamentFencer tf) {
    //     if(tf.getFencer() == null || this.fencer == null){
    //         return 0;
    //     }
    //     System.out.println("tf's fencer = " + tf.getFencer());
    //     System.out.println("this.fencer = "+ this.fencer);
    //     if ((this.points - tf.points) != 0) { // at start, all 0, so no diff
    //         return -(this.points - tf.points);
    //     }
        
    //     return -(this.fencer.getPoints() - tf.getFencer().getPoints());
    // }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPouleWins() {
        return pouleWins;
    }

    public void setPouleWins(int pouleWins) {
        this.pouleWins = pouleWins;
    }

    public int getPoulePoints(){
        return poulePoints;
    }

    public void setPoulePoints(int poulePoints){
        this.poulePoints = poulePoints;
    }

    public void addMatch(Match match){
        matches.add(match);
    }

    public void removeMatch(Match match){
        matches.remove(match);
    }
}
