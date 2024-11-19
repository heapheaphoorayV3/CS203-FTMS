package cs203.ftms.overall.dto;

import cs203.ftms.overall.validation.ValidContactNumber;
import cs203.ftms.overall.validation.ValidDominantArm;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Data Transfer Object (DTO) for updating a fencer's profile information.
 * Contains validation constraints for name, email, country, contact number, and dominant arm.
 */
public class UpdateFencerProfileDTO {

    @NotBlank
    private String name; 

    @Email
    private String email; 

    @NotBlank
    private String country;

    @ValidContactNumber
    private String contactNo;

    @NotBlank
    private String club;

    @ValidDominantArm
    private char dominantArm; 

    /**
     * Constructs an UpdateFencerProfileDTO with the specified name, email, contact number, country, dominant arm, and club.
     *
     * @param name        the fencer's name
     * @param email       the fencer's email address
     * @param contactNo   the fencer's contact number
     * @param country     the fencer's country of residence
     * @param dominantArm the fencer's dominant arm ('L' or 'R')
     * @param club        the fencer's club affiliation
     */
    public UpdateFencerProfileDTO(String name, String email, String contactNo, String country, 
    char dominantArm, String club) {
        this.name = name;
        this.email = email;
        this.country = country;
        this.club = club;
        this.contactNo = contactNo;
        this.dominantArm = dominantArm;
    }

    /**
     * Gets the fencer's name.
     *
     * @return the name of the fencer
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the fencer's name.
     *
     * @param name the name to set for the fencer
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the fencer's email address.
     *
     * @return the email of the fencer
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the fencer's email address.
     *
     * @param email the email to set for the fencer
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the fencer's country of residence.
     *
     * @return the country of the fencer
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the fencer's country of residence.
     *
     * @param country the country to set for the fencer
     */
    public void setCountry(String country) {
        this.country = country;
    } 

    /**
     * Gets the fencer's dominant arm.
     *
     * @return the dominant arm of the fencer ('L' or 'R')
     */
    public char getDominantArm() {
        return dominantArm;
    }

    /**
     * Sets the fencer's dominant arm.
     *
     * @param dominantArm the dominant arm to set ('L' or 'R')
     */
    public void setDominantArm(char dominantArm) {
        this.dominantArm = dominantArm;
    }

    /**
     * Gets the fencer's club affiliation.
     *
     * @return the club affiliation of the fencer
     */
    public String getClub() {
        return club;
    }

    /**
     * Sets the fencer's club affiliation.
     *
     * @param club the club affiliation to set
     */
    public void setClub(String club) {
        this.club = club;
    }

    /**
     * Gets the fencer's contact number.
     *
     * @return the contact number of the fencer
     */
    public String getContactNo() {
        return contactNo;
    }

    /**
     * Sets the fencer's contact number.
     *
     * @param contactNo the contact number to set for the fencer
     */
    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }
}
