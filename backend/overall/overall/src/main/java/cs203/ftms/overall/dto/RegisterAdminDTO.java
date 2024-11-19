package cs203.ftms.overall.dto;

import cs203.ftms.overall.validation.ValidContactNumber;
import cs203.ftms.overall.validation.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Data Transfer Object (DTO) for registering a new admin.
 * Contains the necessary information for creating an admin user,
 * including validation constraints for input fields.
 */
public class RegisterAdminDTO {
    
    @NotBlank
    private String name;

    @Email
    private String email;

    @ValidContactNumber
    private String contactNo;

    @ValidPassword
    private String password;

    @NotBlank
    private String country;

    /**
     * Constructs a new RegisterAdminDTO with the specified details.
     *
     * @param name      the name of the admin
     * @param email     the email of the admin
     * @param contactNo the contact number of the admin
     * @param password  the password for the admin account
     * @param country   the country of the admin
     */
    public RegisterAdminDTO(String name, String email, String contactNo, String password, String country) {
        this.name = name;
        this.email = email;
        this.contactNo = contactNo;
        this.password = password;
        this.country = country;
    }

    /**
     * Gets the name of the admin.
     *
     * @return the admin's name as a string
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the admin.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the email of the admin.
     *
     * @return the admin's email as a string
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of the admin.
     *
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the contact number of the admin.
     *
     * @return the admin's contact number as a string
     */
    public String getContactNo() {
        return contactNo;
    }

    /**
     * Sets the contact number of the admin.
     *
     * @param contactNo the contact number to set
     */
    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    /**
     * Gets the password for the admin account.
     *
     * @return the password as a string
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password for the admin account.
     *
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the country of the admin.
     *
     * @return the country as a string
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the country of the admin.
     *
     * @param country the country to set
     */
    public void setCountry(String country) {
        this.country = country;
    }
}
