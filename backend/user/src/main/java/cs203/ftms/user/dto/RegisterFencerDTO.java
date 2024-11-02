package cs203.ftms.user.dto;

import java.time.LocalDate;

import cs203.ftms.user.validation.ValidContactNumber;
import cs203.ftms.user.validation.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;

public class RegisterFencerDTO {
    @NotBlank
    // custom validator
    private String firstName;

    @NotBlank
    // @custom validator
    private String lastName;

    @Email
    private String email; 

    @ValidPassword
    private String password;

    @ValidContactNumber
    private String contactNo; 

    @NotBlank
    private String country;

    // custom validator -- at least 8 yrs old
    @Past
    private LocalDate dateOfBirth;
    
    public RegisterFencerDTO(String firstName, String lastName, String email, String password, String contactNo, String country, LocalDate dateOfBirth) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.contactNo = contactNo;
        this.country = country;
        this.dateOfBirth = dateOfBirth;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

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

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
  
}
