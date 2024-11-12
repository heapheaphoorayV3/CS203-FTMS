package cs203.ftms.overall.dto;

import java.time.LocalDate;

import cs203.ftms.overall.validation.ValidContactNumber;
import cs203.ftms.overall.validation.ValidDominantArm;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UpdateFencerProfileDTO{
    @NotBlank
    private String name; 
    @Email
    private String email; 
    @NotBlank
    private String country;
    @ValidContactNumber
    private String contactNo;
    @NotBlank
    private String club;
    @ValidDominantArm
    private char dominantArm; 

    public UpdateFencerProfileDTO(String name, String email, String contactNo, String country, 
    char dominantArm, String club) {
        this.name = name;
        this.email = email;
        this.country = country;
        this.club = club;
        this.contactNo = contactNo;
        this.dominantArm = dominantArm;
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
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    } 

    public char getDominantArm() {
        return dominantArm;
    }

    public void setDominantArm(char dominantArm) {
        this.dominantArm = dominantArm;
    }

    public String getClub() {
        return club;
    }

    public void setClub(String organisation) {
        this.club = organisation;
    }

    public String getContactNo(){
        return contactNo;
    }

    public void setContactNo(String contactNo){
        this.contactNo = contactNo;
    }
}