package cs203.ftms.overall.dto.clean;

import java.time.LocalDate;

/**
 * Data Transfer Object for representing a fencer with essential details.
 * This DTO includes information such as name, contact details, nationality,
 * date of birth, and fencing-specific attributes like dominant arm, weapon,
 * club affiliation, and accumulated points.
 */
public class CleanFencerDTO {
    private int id;
    private String name;
    private String email;
    private String country;
    private LocalDate dateOfBirth;
    private char dominantArm;
    private char weapon;
    private String club;
    private int points;
    private int debutYear;
    private char gender;
    private String contactNo;

    /**
     * Constructs a new CleanFencerDTO with the specified details.
     *
     * @param id           The unique identifier of the fencer.
     * @param name         The name of the fencer.
     * @param email        The email of the fencer.
     * @param contactNo    The contact number of the fencer.
     * @param country      The country of the fencer.
     * @param dateOfBirth  The birth date of the fencer.
     * @param dominantArm  The dominant arm of the fencer, either 'L' or 'R'.
     * @param weapon       The weapon category used by the fencer.
     * @param club         The club affiliation of the fencer.
     * @param points       The points accumulated by the fencer.
     * @param debutYear    The debut year of the fencer.
     * @param gender       The gender of the fencer, either 'M' or 'W'.
     */
    public CleanFencerDTO(int id, String name, String email, String contactNo, String country,
                          LocalDate dateOfBirth, char dominantArm, char weapon, String club,
                          int points, int debutYear, char gender) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.country = country;
        this.dateOfBirth = dateOfBirth;
        this.dominantArm = dominantArm;
        this.weapon = weapon;
        this.club = club;
        this.points = points;
        this.debutYear = debutYear;
        this.gender = gender;
        this.contactNo = contactNo;
    }

    /**
     * Gets the name of the fencer.
     *
     * @return The name of the fencer.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the fencer.
     *
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the email of the fencer.
     *
     * @return The email of the fencer.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of the fencer.
     *
     * @param email The email to set.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the country of the fencer.
     *
     * @return The country of the fencer.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the country of the fencer.
     *
     * @param country The country to set.
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Gets the date of birth of the fencer.
     *
     * @return The birth date of the fencer.
     */
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Sets the date of birth of the fencer.
     *
     * @param dateOfBirth The birth date to set.
     */
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Gets the dominant arm of the fencer.
     *
     * @return The dominant arm ('L' or 'R').
     */
    public char getDominantArm() {
        return dominantArm;
    }

    /**
     * Sets the dominant arm of the fencer.
     *
     * @param dominantArm The dominant arm to set ('L' or 'R').
     */
    public void setDominantArm(char dominantArm) {
        this.dominantArm = dominantArm;
    }

    /**
     * Gets the weapon type of the fencer.
     *
     * @return The weapon type.
     */
    public char getWeapon() {
        return weapon;
    }

    /**
     * Sets the weapon type of the fencer.
     *
     * @param weapon The weapon type to set.
     */
    public void setWeapon(char weapon) {
        this.weapon = weapon;
    }

    /**
     * Gets the club affiliation of the fencer.
     *
     * @return The club name.
     */
    public String getClub() {
        return club;
    }

    /**
     * Sets the club affiliation of the fencer.
     *
     * @param organisation The club name to set.
     */
    public void setClub(String organisation) {
        this.club = organisation;
    }

    /**
     * Gets the accumulated points of the fencer.
     *
     * @return The points accumulated.
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
     * @return The gender ('M' or 'W').
     */
    public char getGender() {
        return gender;
    }

    /**
     * Sets the gender of the fencer.
     *
     * @param gender The gender to set ('M' or 'W').
     */
    public void setGender(char gender) {
        this.gender = gender;
    }

    /**
     * Gets the contact number of the fencer.
     *
     * @return The contact number.
     */
    public String getContactNo() {
        return contactNo;
    }

    /**
     * Sets the contact number of the fencer.
     *
     * @param contactNo The contact number to set.
     */
    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    /**
     * Gets the unique identifier of the fencer.
     *
     * @return The fencer ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the fencer.
     *
     * @param id The fencer ID to set.
     */
    public void setId(int id) {
        this.id = id;
    }
}
