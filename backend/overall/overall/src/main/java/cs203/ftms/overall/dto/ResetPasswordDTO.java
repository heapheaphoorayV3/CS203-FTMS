package cs203.ftms.overall.dto;

public class ResetPasswordDTO {
    private String newPassword;

    public ResetPasswordDTO(String newPassword) {
        this.newPassword = newPassword;
    }

    public ResetPasswordDTO() {}

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
