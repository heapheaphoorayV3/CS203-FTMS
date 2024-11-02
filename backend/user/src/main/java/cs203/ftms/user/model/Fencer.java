package cs203.ftms.user.model;

import java.time.LocalDate;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonFormat;

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
import jakarta.persistence.OneToMany;

@Entity
@DiscriminatorValue("F")
public class Fencer extends User {

    @Column(name = "date_of_birth")
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @Column(name = "dominant_arm")
    private char dominantArm;

    @Column(name = "weapon")
    private char weapon;

    @Column(name = "club")
    private String club;

    @Column(name = "points")
    private int points;

    @Column(name = "debut_year")
    private int debutYear;

    @Column(name = "gender")
    private char gender;

    @OneToMany(mappedBy = "fencer_id", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Integer> tournamentFencerIds; 
    
    public Fencer() {}

    public Fencer(String name, String email, String password, String contactNo, String country, LocalDate dateOfBirth){ 
        super(name, email, password, contactNo, country, "ROLE_FENCER");
        this.dateOfBirth = dateOfBirth;
        this.points = 0;
        this.tournamentFencerIds = new LinkedHashSet<>();
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

    public Set<Integer> getTournamentFencerIds() {
        return tournamentFencerIds;
    }

    public void setTournamentFencerIds(Set<Integer> tournamentFencerIds) {
        this.tournamentFencerIds = tournamentFencerIds;
    }
}
