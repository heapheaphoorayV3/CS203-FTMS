package cs203.ftms.overall.model.userrelated;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;

import cs203.ftms.overall.model.tournamentrelated.TournamentFencer;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;

/**
 * Represents a fencer user in the system, with attributes specific to fencing, such as
 * weapon preference, dominant arm, club affiliation, and tournament profiles.
 * Extends the base User class with a predefined role of "ROLE_FENCER".
 */
@Entity
@DiscriminatorValue("F")
public class Fencer extends User {

    /**
     * The fencer's date of birth.
     * Date format: yyyy-MM-dd
     * Used for age verification and category assignment in tournaments.
     */
    @Column(name = "date_of_birth")
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate dateOfBirth;

    /**
     * The fencer's dominant arm.
     * Valid values:
     * - 'R': Right-handed
     * - 'L': Left-handed
     * This information is important for bout organization and strategy.
     */
    @Column(name = "dominant_arm")
    private char dominantArm;

    /**
     * The primary weapon category of the fencer.
     * Valid values:
     * - 'F': Foil
     * - 'E': Epee
     * - 'S': Sabre
     * Determines which weapon events the fencer typically participates in.
     */
    @Column(name = "weapon")
    private char weapon;

    /**
     * The fencing club or organization that the fencer represents.
     * This could be a school, university, or private fencing club.
     */
    @Column(name = "club")
    private String club;

    /**
     * The fencer's current ranking points.
     * Accumulated from tournament performances.
     * Used to determine seeding in tournaments and overall rankings.
     */
    @Column(name = "points")
    private int points;

    /**
     * The year when the fencer started competitive fencing.
     * Used to track experience level and career duration.
     */
    @Column(name = "debut_year")
    private int debutYear;

    /**
     * The fencer's gender.
     * Valid values:
     * - 'M': Male
     * - 'F': Female
     * Used for appropriate event categorization and registration.
     */
    @Column(name = "gender")
    private char gender;

    /**
     * Collection of all tournament participations for this fencer.
     * Each entry represents the fencer's profile in a specific tournament event.
     * Fetched eagerly to ensure tournament history is immediately available.
     * Cascade ALL ensures that operations on the fencer cascade to their tournament profiles.
     * For example, if a fencer is deleted, all their tournament profiles are also deleted.
     */
    @OneToMany(mappedBy = "fencer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<TournamentFencer> tournamentFencerProfiles;


    /**
     * Default constructor for Fencer.
     */
    public Fencer() {
        this.tournamentFencerProfiles = new LinkedHashSet<>();
    }

    /**
     * Constructs a Fencer with specified details.
     *
     * @param name The name of the fencer.
     * @param email The email of the fencer.
     * @param password The password for the fencer's account.
     * @param contactNo The contact number of the fencer.
     * @param country The country of the fencer.
     * @param dateOfBirth The date of birth of the fencer.
     */
    public Fencer(String name, String email, String password, String contactNo, String country, LocalDate dateOfBirth) {
        super(name, email, password, contactNo, country, "ROLE_FENCER");
        this.dateOfBirth = dateOfBirth;
        this.points = 0;
        this.tournamentFencerProfiles = new LinkedHashSet<>();
    }

    /**
     * Gets the date of birth of the fencer.
     *
     * @return The date of birth.
     */
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Sets the date of birth of the fencer.
     *
     * @param dateOfBirth The date of birth to set.
     */
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Gets the dominant arm of the fencer.
     *
     * @return The dominant arm ('L' for left, 'R' for right).
     */
    public char getDominantArm() {
        return dominantArm;
    }

    /**
     * Sets the dominant arm of the fencer.
     *
     * @param dominantArm The dominant arm ('L' or 'R').
     */
    public void setDominantArm(char dominantArm) {
        this.dominantArm = dominantArm;
    }

    /**
     * Gets the weapon type preferred by the fencer.
     *
     * @return The weapon type.
     */
    public char getWeapon() {
        return weapon;
    }

    /**
     * Sets the weapon type preferred by the fencer.
     *
     * @param weapon The weapon type.
     */
    public void setWeapon(char weapon) {
        this.weapon = weapon;
    }

    /**
     * Gets the club the fencer is affiliated with.
     *
     * @return The club name.
     */
    public String getClub() {
        return club;
    }

    /**
     * Sets the club affiliation of the fencer.
     *
     * @param club The club name.
     */
    public void setClub(String club) {
        this.club = club;
    }

    /**
     * Gets the accumulated points of the fencer.
     *
     * @return The points.
     */
    public int getPoints() {
        return points;
    }

    /**
     * Sets the accumulated points of the fencer.
     *
     * @param points The points to set.
     */
    public void setPoints(int points) {
        this.points = points;
    }

    /**
     * Gets the debut year of the fencer.
     *
     * @return The debut year.
     */
    public int getDebutYear() {
        return debutYear;
    }

    /**
     * Sets the debut year of the fencer.
     *
     * @param debutYear The debut year to set.
     */
    public void setDebutYear(int debutYear) {
        this.debutYear = debutYear;
    }

    /**
     * Gets the gender of the fencer.
     *
     * @return The gender ('M' for male, 'F' for female).
     */
    public char getGender() {
        return gender;
    }

    /**
     * Sets the gender of the fencer.
     *
     * @param gender The gender ('M' or 'F').
     */
    public void setGender(char gender) {
        this.gender = gender;
    }

    /**
     * Gets the tournament profiles of the fencer.
     *
     * @return The set of TournamentFencer profiles.
     */
    public Set<TournamentFencer> getTournamentFencerProfiles() {
        return tournamentFencerProfiles;
    }

    /**
     * Sets the tournament profiles of the fencer.
     *
     * @param tournamentFencerProfiles The set of TournamentFencer profiles.
     */
    public void setTournamentFencerProfiles(Set<TournamentFencer> tournamentFencerProfiles) {
        this.tournamentFencerProfiles = tournamentFencerProfiles;
    }
}
