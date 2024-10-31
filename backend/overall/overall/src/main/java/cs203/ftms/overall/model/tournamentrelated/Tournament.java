package cs203.ftms.overall.model.tournamentrelated;

import java.time.LocalDate;
import java.time.LocalDate;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.*;

import cs203.ftms.overall.model.userrelated.Organiser;

@Entity
@Table(name = "tournament")
public class Tournament {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "organiser_id", nullable = false)
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
    private char difficulty; //B, I, A for Beginner, Intermediate, Advance 

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL)
    private Set<Event> events;

    public Tournament() {}

    public Tournament(String name, Organiser organiser, LocalDate signupEndDate, int advancementRate, LocalDate startDate, LocalDate endDate, String location, String description, String rules, char difficulty) {
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Organiser getOrganiser() {
        return organiser;
    }

    public void setOrganiser(Organiser organiser) {
        this.organiser = organiser;
    }

    public LocalDate getSignupEndDate() {
        return signupEndDate;
    }

    public void setSignupEndDate(LocalDate signupEndDate) {
        this.signupEndDate = signupEndDate;
    }

    public int getAdvancementRate() {
        return advancementRate;
    }

    public void setAdvancementRate(int advancementRate) {
        this.advancementRate = advancementRate;
    }
    
    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    
    public boolean equals(Object obj) {
        if (obj instanceof Tournament t) {
            if (t.getId() == this.getId()) return true;
        }
        return false;
    }

    public char getDifficulty(){
        return difficulty;
    }

    public void setDifficulty(char difficulty){
        this.difficulty = difficulty;
    }

    
}
