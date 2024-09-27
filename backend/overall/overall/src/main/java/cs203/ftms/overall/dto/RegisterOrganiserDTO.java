package cs203.ftms.overall.dto;

import cs203.ftms.overall.validation.ValidContactNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class RegisterOrganiserDTO {
    @NotBlank
    private String name; 

    @Email
    private String email; 

    @ValidContactNumber
    private String contactNo; 

    // custom validation
    private String password;

    @NotBlank
    private String country;
    
    public RegisterOrganiserDTO(String name, String email, String contactNo, String password, String country) {
        this.name = name;
        this.email = email;
        this.contactNo = contactNo;
        this.password = password;
        this.country = country;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getContactNo() {
        return contactNo;
    }
    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    } 

    
}
