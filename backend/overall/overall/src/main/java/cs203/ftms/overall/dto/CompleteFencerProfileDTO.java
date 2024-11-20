package cs203.ftms.overall.dto;

import cs203.ftms.overall.validation.ValidDominantArm;
import cs203.ftms.overall.validation.ValidGender;
import cs203.ftms.overall.validation.ValidWeapon;
import jakarta.validation.constraints.NotBlank;

/**
 * Data Transfer Object for creating or updating a fencer's profile.
 * Contains attributes validated according to fencing-related rules and restrictions.
 */
public class CompleteFencerProfileDTO {

    @ValidDominantArm
    private Character dominantArm; // R or L

    @ValidWeapon
    private Character weapon; // S, E, or F

    @ValidGender
    private Character gender; // M or W

    @NotBlank
    private String club;

    // Custom validation to ensure the debut year is appropriate based on age
    private int debutYear;

    /**
     * Constructs a CompleteFencerProfileDTO with the specified parameters.
     *
     * @param dominantArm The dominant arm of the fencer ('R' for right, 'L' for left).
     * @param weapon      The weapon used by the fencer ('S' for sabre, 'E' for epee, 'F' for foil).
     * @param gender      The gender of the fencer ('M' for male, 'W' for female).
     * @param club        The club where the fencer is affiliated.
     * @param debutYear   The year the fencer debuted, validated to ensure the fencer is at least 8 years old.
     */
    public CompleteFencerProfileDTO(Character dominantArm, Character weapon, Character gender, String club, int debutYear) {
        this.dominantArm = dominantArm;
        this.weapon = weapon;
        this.gender = gender;
        this.club = club;
        this.debutYear = debutYear;
    }

    /**
     * Gets the dominant arm of the fencer.
     *
     * @return The dominant arm ('R' or 'L').
     */
    public Character getDominantArm() {
        return dominantArm;
    }

    /**
     * Sets the dominant arm of the fencer.
     *
     * @param dominantArm The dominant arm to set ('R' or 'L').
     */
    public void setDominantArm(Character dominantArm) {
        this.dominantArm = dominantArm;
    }

    /**
     * Gets the weapon used by the fencer.
     *
     * @return The weapon ('S', 'E', or 'F').
     */
    public Character getWeapon() {
        return weapon;
    }

    /**
     * Sets the weapon used by the fencer.
     *
     * @param weapon The weapon to set ('S', 'E', or 'F').
     */
    public void setWeapon(Character weapon) {
        this.weapon = weapon;
    }

    /**
     * Gets the gender of the fencer.
     *
     * @return The gender ('M' or 'W').
     */
    public Character getGender() {
        return gender;
    }

    /**
     * Sets the gender of the fencer.
     *
     * @param gender The gender to set ('M' or 'W').
     */
    public void setGender(Character gender) {
        this.gender = gender;
    }

    /**
     * Gets the club affiliated with the fencer.
     *
     * @return The club name.
     */
    public String getClub() {
        return club;
    }

    /**
     * Sets the club affiliated with the fencer.
     *
     * @param organisation The club name to set.
     */
    public void setClub(String organisation) {
        this.club = organisation;
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
}
