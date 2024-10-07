package cs203.ftms.overall.model.userrelated;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;

import cs203.ftms.overall.model.tournamentrelated.Event;
import cs203.ftms.overall.security.model.Role;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

@Entity
@DiscriminatorValue("F")
public class Fencer extends User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "dateOfBirth")
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @Column(name = "dominantArm")
    private char dominantArm;

    @Column(name = "weapon")
    private char weapon;

    @Column(name = "club")
    private String club;

    @Column(name = "points")
    private int points;

    @Column(name = "debutYear")
    private int debutYear;

    @Column(name = "gender")
    private char gender;

    @ManyToMany(fetch = FetchType.EAGER, cascade={CascadeType.PERSIST, CascadeType.DETACH})
    @JoinTable(
            name = "tour_cat_part",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "tour_cat_id") })
    private Set<Event> eventsPart;

    public Fencer() {}

    public Fencer(String name, String email, String password, String contactNo, String country, LocalDate dateOfBirth){ 
        super(name, email, password, contactNo, country, "FENCER");
        this.dateOfBirth = dateOfBirth;
        this.points = 0;
        this.eventsPart = new HashSet<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public char getDominantArm() {
        return dominantArm;
    }

    public void setDominantArm(char dominantArm) {
        this.dominantArm = dominantArm;
    }

    public char getWeapon() {
        return weapon;
    }

    public void setWeapon(char weapon) {
        this.weapon = weapon;
    }

    public String getClub() {
        return club;
    }

    public void setClub(String organisation) {
        this.club = organisation;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getDebutYear() {
        return debutYear;
    }

    public void setDebutYear(int debutYear) {
        this.debutYear = debutYear;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public Set<Event> getEventsPart() {
        return eventsPart;
    }

    public void setEventsPart(Set<Event> tourCatPart) {
        this.eventsPart = tourCatPart;
    }

    
}
