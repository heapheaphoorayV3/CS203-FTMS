package cs203.ftms.overall.dto;

import cs203.ftms.overall.validation.ValidContactNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Data Transfer Object (DTO) for updating an organiser's profile information.
 * Contains validation constraints for name, email, country, and contact number.
 */
public class UpdateOrganiserProfileDTO {

    @NotBlank
    private String name; 

    @Email
    private String email; 

    @NotBlank
    private String country;

    @ValidContactNumber
    private String contactNo;

    /**
     * Constructs an UpdateOrganiserProfileDTO with the specified name, email, contact number, and country.
     *
     * @param name      the organiser's name
     * @param email     the organiser's email address
     * @param contactNo the organiser's contact number
     * @param country   the organiser's country of residence
     */
    public UpdateOrganiserProfileDTO(String name, String email, String contactNo, String country) {
        this.name = name;
        this.email = email;
        this.country = country;
        this.contactNo = contactNo;
    }

    /**
     * Gets the organiser's name.
     *
     * @return the name of the organiser
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the organiser's name.
     *
     * @param name the name to set for the organiser
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the organiser's email address.
     *
     * @return the email of the organiser
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the organiser's email address.
     *
     * @param email the email to set for the organiser
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the organiser's country of residence.
     *
     * @return the country of the organiser
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the organiser's country of residence.
     *
     * @param country the country to set for the organiser
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Gets the organiser's contact number.
     *
     * @return the contact number of the organiser
     */
    public String getContactNo() {
        return contactNo;
    }

    /**
     * Sets the organiser's contact number.
     *
     * @param contactNo the contact number to set for the organiser
     */
    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }
}
