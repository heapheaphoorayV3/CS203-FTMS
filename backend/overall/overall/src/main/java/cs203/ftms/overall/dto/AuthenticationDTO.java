package cs203.ftms.overall.dto;

import jakarta.validation.constraints.NotBlank;

public class AuthenticationDTO {
    @NotBlank(message = "Email cannot be empty")
    private String email;
    
    @NotBlank(message = "Password cannot be empty")
    private String password;
    // private char userType;

    public AuthenticationDTO(String email, String password) {
        this.email = email;
        this.password = password;
        // this.userType = userType;
    }

    public AuthenticationDTO() {}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // public char getUserType() {
    //     return userType;
    // }

    // public void setUserType(char userType) {
    //     this.userType = userType;
    // }
}
