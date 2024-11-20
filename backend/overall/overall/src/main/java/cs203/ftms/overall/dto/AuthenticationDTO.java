package cs203.ftms.overall.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Data Transfer Object for handling user authentication requests.
 * This class contains the user's email and password and includes validation annotations to ensure fields are not blank.
 */
public class AuthenticationDTO {

    @NotBlank(message = "Email cannot be empty")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    private String password;

    /**
     * Constructs an AuthenticationDTO with the specified email and password.
     *
     * @param email    The email address of the user.
     * @param password The password of the user.
     */
    public AuthenticationDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    /**
     * Default constructor for AuthenticationDTO.
     */
    public AuthenticationDTO() {}

    /**
     * Gets the email address of the user.
     *
     * @return The email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the user.
     *
     * @param email The email address to set.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the password of the user.
     *
     * @return The password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the user.
     *
     * @param password The password to set.
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
