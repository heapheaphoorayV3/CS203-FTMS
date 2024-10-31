package cs203.ftms.overall.model.tournamentrelated;

import java.time.*;
import java.util.*;

import org.antlr.v4.runtime.tree.Trees;

import jakarta.persistence.*;

import cs203.ftms.overall.model.userrelated.Fencer;

@Entity
@Table(name = "event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "tournament_id", nullable = false)
    private Tournament tournament; 

    @OneToMany(mappedBy = "event")
    private Set<TournamentFencer> fencers; 
    
    @Column(name = "event_ended")
    private boolean isOver;

    @Column(name = "gender")
    private char gender;
    
    @Column(name = "weapon") 
    private char weapon; 

    @Column(name = "min_participants")
    private int minParticipants;

    @Column(name = "participant_count")
    private int participantCount;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "start_time")
    private LocalTime startTime; 

    @Column(name = "end_time")
    private LocalTime endTime;
    
    @OneToMany(mappedBy = "event")
    private Set<Poule> poules;

    @OneToMany(mappedBy = "event")
    private Set<DirectEliminationMatch> directEliminationMatches;

    public Event() {}

    public Event(Tournament tournament, char gender, char weapon, int minParticipants, LocalDate date, LocalTime startTime,
            LocalTime endTime) {
        this.tournament = tournament;
        this.gender = gender;
        this.weapon = weapon;
        this.minParticipants = minParticipants;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.poules = new TreeSet<>();
        this.directEliminationMatches = new HashSet<>();
        this.fencers = new HashSet<>();
    }
    
    

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public Set<Poule> getPoules() {
        return poules;
    }

    public void setPoules(Set<Poule> poules) {
        this.poules = poules;
    }

    public Set<DirectEliminationMatch> getDirectEliminationMatches() {
        return directEliminationMatches;
    }

    public void setDirectEliminationMatches(Set<DirectEliminationMatch> directEliminationMatches) {
        this.directEliminationMatches = directEliminationMatches;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public char getWeapon() {
        return weapon;
    }

    public void setWeapon(char weapon) {
        this.weapon = weapon;
    }

    public int getMinParticipants() {
        return minParticipants;
    }

    public void setMinParticipants(int minParticipants) {
        this.minParticipants = minParticipants;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public Set<TournamentFencer> getFencers() {
        return fencers;
    }

    public void setFencers(Set<TournamentFencer> fencers) {
        this.fencers = fencers;
    }

    public int getParticipantCount() {
        return participantCount;
    }

    public void setParticipantCount(int participantCount) {
        this.participantCount = participantCount;
    } 

    public boolean equals(Object obj) {
        if (obj instanceof Event tc) {
            if (tc.getId() == this.getId()) return true;
        }
        return false;
    }

    public boolean isOver(){
        return isOver;
    }

    public void setOver(boolean isOver){
        this.isOver = isOver;
    }
    
}
