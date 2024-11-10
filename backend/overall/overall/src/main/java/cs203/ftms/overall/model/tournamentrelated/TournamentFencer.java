package cs203.ftms.overall.model.tournamentrelated;

import java.util.LinkedHashSet;
import java.util.Set;

import cs203.ftms.overall.model.userrelated.Fencer;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


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
    @JoinColumn(name = "event_id")
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

    @Column(name = "points_after_event")
    private int pointsAfterEvent;

    public TournamentFencer(Fencer fencer, Event event) {
        this.fencer = fencer;
        this.event = event;
        this.matches = new LinkedHashSet<>();
        this.pointsAfterEvent = -1;
    }

    public TournamentFencer() {
        this.matches = new LinkedHashSet<>();
        this.pointsAfterEvent = -1;
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

    public int getPointsAfterEvent() {
        return pointsAfterEvent;
    }

    public void setPointsAfterEvent(int pointsAfterEvent) {
        this.pointsAfterEvent = pointsAfterEvent;
    }
}
