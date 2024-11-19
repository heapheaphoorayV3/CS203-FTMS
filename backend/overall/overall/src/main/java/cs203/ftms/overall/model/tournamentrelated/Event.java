package cs203.ftms.overall.model.tournamentrelated;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Represents an event within a tournament, detailing participants, scheduling,
 * event type, and the tournament it belongs to.
 */
@Entity
@Table(name = "event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "tournament_id")
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

    /**
     * Default constructor for Event.
     */
    public Event() {}

    /**
     * Constructs an Event with specified tournament details.
     *
     * @param tournament The tournament associated with the event.
     * @param gender The gender category ('M' for male, 'F' for female).
     * @param weapon The weapon category for the event.
     * @param minParticipants Minimum number of participants required.
     * @param date The scheduled date of the event.
     * @param startTime The scheduled start time of the event.
     * @param endTime The scheduled end time of the event.
     */
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

    /**
     * Gets the tournament associated with this event.
     *
     * @return The tournament of the event.
     */
    public Tournament getTournament() {
        return tournament;
    }

    /**
     * Sets the tournament for this event.
     *
     * @param tournament The tournament to associate with the event.
     */
    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    /**
     * Gets the set of poules in this event.
     *
     * @return The set of poules.
     */
    public Set<Poule> getPoules() {
        return poules;
    }

    /**
     * Sets the set of poules for this event.
     *
     * @param poules The set of poules to associate with the event.
     */
    public void setPoules(Set<Poule> poules) {
        this.poules = poules;
    }

    /**
     * Gets the direct elimination matches in this event.
     *
     * @return The set of direct elimination matches.
     */
    public Set<DirectEliminationMatch> getDirectEliminationMatches() {
        return directEliminationMatches;
    }

    /**
     * Sets the direct elimination matches for this event.
     *
     * @param directEliminationMatches The set of direct elimination matches.
     */
    public void setDirectEliminationMatches(Set<DirectEliminationMatch> directEliminationMatches) {
        this.directEliminationMatches = directEliminationMatches;
    }

    /**
     * Gets the ID of the event.
     *
     * @return The event ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID of the event.
     *
     * @param id The ID to assign to the event.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the gender category of the event.
     *
     * @return The gender category.
     */
    public char getGender() {
        return gender;
    }

    /**
     * Sets the gender category of the event.
     *
     * @param gender The gender category.
     */
    public void setGender(char gender) {
        this.gender = gender;
    }

    /**
     * Gets the weapon category of the event.
     *
     * @return The weapon category.
     */
    public char getWeapon() {
        return weapon;
    }

    /**
     * Sets the weapon category of the event.
     *
     * @param weapon The weapon category.
     */
    public void setWeapon(char weapon) {
        this.weapon = weapon;
    }

    /**
     * Gets the minimum number of participants required for the event.
     *
     * @return The minimum participant count.
     */
    public int getMinParticipants() {
        return minParticipants;
    }

    /**
     * Sets the minimum number of participants required for the event.
     *
     * @param minParticipants The minimum participant count.
     */
    public void setMinParticipants(int minParticipants) {
        this.minParticipants = minParticipants;
    }

    /**
     * Gets the date of the event.
     *
     * @return The event date.
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Sets the date of the event.
     *
     * @param date The event date.
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Gets the start time of the event.
     *
     * @return The start time.
     */
    public LocalTime getStartTime() {
        return startTime;
    }

    /**
     * Sets the start time of the event.
     *
     * @param startTime The start time.
     */
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    /**
     * Gets the end time of the event.
     *
     * @return The end time.
     */
    public LocalTime getEndTime() {
        return endTime;
    }

    /**
     * Sets the end time of the event.
     *
     * @param endTime The end time.
     */
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    /**
     * Gets the set of fencers participating in the event.
     *
     * @return The set of fencers.
     */
    public Set<TournamentFencer> getFencers() {
        return fencers;
    }

    /**
     * Sets the set of fencers participating in the event.
     *
     * @param fencers The set of fencers.
     */
    public void setFencers(Set<TournamentFencer> fencers) {
        this.fencers = fencers;
    }

    /**
     * Gets the current participant count for the event.
     *
     * @return The participant count.
     */
    public int getParticipantCount() {
        return participantCount;
    }

    /**
     * Sets the participant count for the event.
     *
     * @param participantCount The participant count.
     */
    public void setParticipantCount(int participantCount) {
        this.participantCount = participantCount;
    }

    /**
     * Checks if the event is over.
     *
     * @return true if the event has ended, false otherwise.
     */
    public boolean isOver() {
        return isOver;
    }

    /**
     * Sets whether the event has ended.
     *
     * @param isOver true to mark the event as ended, false otherwise.
     */
    public void setOver(boolean isOver) {
        this.isOver = isOver;
    }

    /**
     * Compares this event to another object for equality based on their ID.
     *
     * @param obj The object to compare with.
     * @return true if the object is an Event with the same ID, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Event tc) {
            return tc.getId() == this.getId();
        }
        return false;
    }
}
