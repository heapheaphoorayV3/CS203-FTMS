package cs203.ftms.overall.model.tournamentrelated;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;

import cs203.ftms.overall.model.userrelated.Organiser;
import jakarta.persistence.CascadeType;
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
 * Represents a tournament with details about the organizer, location,
 * sign-up period, events, and other attributes.
 */
@Entity
@Table(name = "tournament")
public class Tournament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "organiser_id")
    private Organiser organiser;

    @Column(name = "signup_end_date")
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate signupEndDate;

    @Column(name = "advancement_rate")
    private int advancementRate;

    @Column(name = "start_date")
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate startDate;

    @Column(name = "end_date")
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate endDate;

    @Column(name = "location")
    private String location;

    @Column(name = "description")
    private String description;

    @Column(name = "rules")
    private String rules;

    @Column(name = "difficulty")
    private char difficulty; // B, I, A for Beginner, Intermediate, Advanced

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL)
    private Set<Event> events;

    /**
     * Default constructor for Tournament.
     */
    public Tournament() {}

    /**
     * Constructs a Tournament with specified details.
     *
     * @param name The name of the tournament.
     * @param organiser The organizer of the tournament.
     * @param signupEndDate The end date for sign-ups.
     * @param advancementRate The rate of advancement in the tournament.
     * @param startDate The start date of the tournament.
     * @param endDate The end date of the tournament.
     * @param location The location where the tournament is held.
     * @param description A description of the tournament.
     * @param rules The rules governing the tournament.
     * @param difficulty The difficulty level of the tournament (B, I, A).
     */
    public Tournament(String name, Organiser organiser, LocalDate signupEndDate, int advancementRate, LocalDate startDate,
                     LocalDate endDate, String location, String description, String rules, char difficulty) {
        this.name = name;
        this.organiser = organiser;
        this.signupEndDate = signupEndDate;
        this.advancementRate = advancementRate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
        this.description = description;
        this.rules = rules;
        this.events = new HashSet<>();
        this.difficulty = difficulty;
    }

    /**
     * Gets the ID of the tournament.
     *
     * @return The tournament ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID of the tournament.
     *
     * @param id The ID to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the name of the tournament.
     *
     * @return The tournament name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the tournament.
     *
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the organizer of the tournament.
     *
     * @return The organizer.
     */
    public Organiser getOrganiser() {
        return organiser;
    }

    /**
     * Sets the organizer of the tournament.
     *
     * @param organiser The organizer to set.
     */
    public void setOrganiser(Organiser organiser) {
        this.organiser = organiser;
    }

    /**
     * Gets the sign-up end date.
     *
     * @return The sign-up end date.
     */
    public LocalDate getSignupEndDate() {
        return signupEndDate;
    }

    /**
     * Sets the sign-up end date.
     *
     * @param signupEndDate The sign-up end date.
     */
    public void setSignupEndDate(LocalDate signupEndDate) {
        this.signupEndDate = signupEndDate;
    }

    /**
     * Gets the advancement rate for the tournament.
     *
     * @return The advancement rate.
     */
    public int getAdvancementRate() {
        return advancementRate;
    }

    /**
     * Sets the advancement rate for the tournament.
     *
     * @param advancementRate The advancement rate to set.
     */
    public void setAdvancementRate(int advancementRate) {
        this.advancementRate = advancementRate;
    }

    /**
     * Gets the start date of the tournament.
     *
     * @return The start date.
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Sets the start date of the tournament.
     *
     * @param startDate The start date to set.
     */
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    /**
     * Gets the end date of the tournament.
     *
     * @return The end date.
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Sets the end date of the tournament.
     *
     * @param endDate The end date to set.
     */
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    /**
     * Gets the location of the tournament.
     *
     * @return The tournament location.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the location of the tournament.
     *
     * @param location The location to set.
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Gets the description of the tournament.
     *
     * @return The tournament description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the tournament.
     *
     * @param description The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the rules of the tournament.
     *
     * @return The tournament rules.
     */
    public String getRules() {
        return rules;
    }

    /**
     * Sets the rules of the tournament.
     *
     * @param rules The rules to set.
     */
    public void setRules(String rules) {
        this.rules = rules;
    }

    /**
     * Gets the events associated with this tournament.
     *
     * @return The set of events.
     */
    public Set<Event> getEvents() {
        return events;
    }

    /**
     * Sets the events associated with this tournament.
     *
     * @param events The set of events to set.
     */
    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    /**
     * Checks if this tournament is equal to another object based on tournament ID.
     *
     * @param obj The object to compare with.
     * @return true if the object is a Tournament with the same ID, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Tournament t) {
            return t.getId() == this.getId();
        }
        return false;
    }

    /**
     * Gets the difficulty level of the tournament.
     *
     * @return The difficulty level (B, I, A).
     */
    public char getDifficulty() {
        return difficulty;
    }

    /**
     * Sets the difficulty level of the tournament.
     *
     * @param difficulty The difficulty level to set (B, I, A).
     */
    public void setDifficulty(char difficulty) {
        this.difficulty = difficulty;
    }
}
