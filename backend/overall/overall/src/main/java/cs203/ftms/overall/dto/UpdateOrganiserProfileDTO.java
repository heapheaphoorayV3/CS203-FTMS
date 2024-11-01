package cs203.ftms.overall.dto;

import java.time.LocalDate;

public class UpdateOrganiserProfileDTO{
    private String name; 
    private String email; 
    private String country;
    private String contactNo;

    public UpdateOrganiserProfileDTO(String name, String email, String contactNo, String country) {
        this.name = name;
        this.email = email;
        this.country = country;
        this.contactNo = contactNo;
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
    public String getContactNo(){
        return contactNo;
    }

    public void setContactNo(String contactNo){
        this.contactNo = contactNo;
    }
}