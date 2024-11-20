package cs203.ftms.overall.dto;

import java.time.LocalDate;
import cs203.ftms.overall.validation.ValidContactNumber;
import cs203.ftms.overall.validation.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;

/**
 * Data Transfer Object (DTO) for registering a new fencer.
 * Contains essential information such as name, email, contact number,
 * country, and date of birth, with validation annotations to ensure data integrity.
 */
public class RegisterFencerDTO {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Email
    private String email;

    @ValidPassword
    private String password;

    @ValidContactNumber
    private String contactNo;

    @NotBlank
    private String country;

    @Past
    private LocalDate dateOfBirth;

    /**
     * Constructs a new RegisterFencerDTO with specified details.
     *
     * @param firstName   the first name of the fencer
     * @param lastName    the last name of the fencer
     * @param email       the email of the fencer
     * @param password    the password for the fencer account
     * @param contactNo   the contact number of the fencer
     * @param country     the country of the fencer
     * @param dateOfBirth the date of birth of the fencer, must be in the past
     */
    public RegisterFencerDTO(String firstName, String lastName, String email, String password, String contactNo, String country, LocalDate dateOfBirth) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.contactNo = contactNo;
        this.country = country;
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Gets the first name of the fencer.
     *
     * @return the first name as a string
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the fencer.
     *
     * @param firstName the first name to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the last name of the fencer.
     *
     * @return the last name as a string
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the fencer.
     *
     * @param lastName the last name to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the email of the fencer.
     *
     * @return the email as a string
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of the fencer.
     *
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the password for the fencer account.
     *
     * @return the password as a string
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password for the fencer account.
     *
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the contact number of the fencer.
     *
     * @return the contact number as a string
     */
    public String getContactNo() {
        return contactNo;
    }

    /**
     * Sets the contact number of the fencer.
     *
     * @param contactNo the contact number to set
     */
    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    /**
     * Gets the country of the fencer.
     *
     * @return the country as a string
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the country of the fencer.
     *
     * @param country the country to set
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Gets the date of birth of the fencer.
     *
     * @return the date of birth as a LocalDate
     */
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Sets the date of birth of the fencer.
     *
     * @param dateOfBirth the date of birth to set, must be in the past
     */
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
