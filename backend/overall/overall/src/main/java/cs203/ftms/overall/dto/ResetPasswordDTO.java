package cs203.ftms.overall.dto;

import cs203.ftms.overall.validation.ValidPassword;

/**
 * Data Transfer Object (DTO) for resetting a user's password.
 * Contains a field for the new password with validation for password requirements.
 */
public class ResetPasswordDTO {

    @ValidPassword
    private String newPassword;

    /**
     * Constructs a new ResetPasswordDTO with the specified new password.
     *
     * @param newPassword the new password to set
     */
    public ResetPasswordDTO(String newPassword) {
        this.newPassword = newPassword;
    }

    /**
     * Default constructor for ResetPasswordDTO.
     */
    public ResetPasswordDTO() {}

    /**
     * Gets the new password for the reset request.
     *
     * @return the new password as a string
     */
    public String getNewPassword() {
        return newPassword;
    }

    /**
     * Sets the new password for the reset request.
     *
     * @param newPassword the new password to set
     */
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
