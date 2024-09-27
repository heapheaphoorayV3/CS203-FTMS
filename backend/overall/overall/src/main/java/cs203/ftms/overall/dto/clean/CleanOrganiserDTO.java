package cs203.ftms.overall.dto.clean;

public class CleanOrganiserDTO{
    private boolean verified;
    private String name; 
    private String email; 
    private String contactNo;
    private String country;

    public CleanOrganiserDTO(boolean verified, String name, String email, String contactNo, String country){
        this.verified = verified;
        this.name = name;
        this.email = email;
        this.contactNo = contactNo;
        this.country = country;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    
}