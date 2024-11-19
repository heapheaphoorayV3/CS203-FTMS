package cs203.ftms.overall.dto;

import cs203.ftms.overall.validation.ValidPassword;

/**
 * Data Transfer Object for handling user password change requests.
 * This class contains fields for the old and new passwords, where the new password is validated for complexity requirements.
 */
public class ChangePasswordDTO {

    private String oldPassword;

    @ValidPassword
    private String newPassword;

    /**
     * Constructs a ChangePasswordDTO with the specified old and new passwords.
     *
     * @param oldPassword The current password of the user.
     * @param newPassword The new password to be set for the user.
     */
    public ChangePasswordDTO(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    /**
     * Default constructor for ChangePasswordDTO.
     */
    public ChangePasswordDTO() {}

    /**
     * Gets the current password of the user.
     *
     * @return The current password.
     */
    public String getOldPassword() {
        return oldPassword;
    }

    /**
     * Sets the current password of the user.
     *
     * @param oldPassword The current password to set.
     */
    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    /**
     * Gets the new password of the user.
     *
     * @return The new password.
     */
    public String getNewPassword() {
        return newPassword;
    }

    /**
     * Sets the new password for the user.
     *
     * @param newPassword The new password to set.
     */
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
