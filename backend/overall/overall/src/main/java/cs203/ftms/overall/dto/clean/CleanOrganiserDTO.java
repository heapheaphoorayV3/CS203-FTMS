package cs203.ftms.overall.dto.clean;

/**
 * Data Transfer Object for representing an organiser with essential details
 * such as ID, verification status, name, email, contact number, and country.
 */
public class CleanOrganiserDTO {
    private int id;
    private boolean verified;
    private String name;
    private String email;
    private String contactNo;
    private String country;

    /**
     * Constructs a CleanOrganiserDTO with the specified organiser details.
     *
     * @param id         The unique identifier of the organiser.
     * @param verified   The verification status of the organiser.
     * @param name       The name of the organiser.
     * @param email      The email address of the organiser.
     * @param contactNo  The contact number of the organiser.
     * @param country    The country of the organiser.
     */
    public CleanOrganiserDTO(int id, boolean verified, String name, String email, String contactNo, String country) {
        this.id = id;
        this.verified = verified;
        this.name = name;
        this.email = email;
        this.contactNo = contactNo;
        this.country = country;
    }

    /**
     * Gets the verification status of the organiser.
     *
     * @return {@code true} if the organiser is verified, otherwise {@code false}.
     */
    public boolean isVerified() {
        return verified;
    }

    /**
     * Sets the verification status of the organiser.
     *
     * @param verified {@code true} to mark the organiser as verified, otherwise {@code false}.
     */
    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    /**
     * Gets the name of the organiser.
     *
     * @return The organiser's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the organiser.
     *
     * @param name The name to set for the organiser.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the email of the organiser.
     *
     * @return The organiser's email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of the organiser.
     *
     * @param email The email to set for the organiser.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the contact number of the organiser.
     *
     * @return The organiser's contact number.
     */
    public String getContactNo() {
        return contactNo;
    }

    /**
     * Sets the contact number of the organiser.
     *
     * @param contactNo The contact number to set for the organiser.
     */
    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    /**
     * Gets the country of the organiser.
     *
     * @return The organiser's country.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the country of the organiser.
     *
     * @param country The country to set for the organiser.
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Gets the unique identifier of the organiser.
     *
     * @return The organiser's ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the organiser.
     *
     * @param id The ID to set for the organiser.
     */
    public void setId(int id) {
        this.id = id;
    }
}
