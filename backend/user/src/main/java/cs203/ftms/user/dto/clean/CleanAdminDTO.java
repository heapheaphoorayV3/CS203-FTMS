package cs203.ftms.user.dto.clean;

public class CleanAdminDTO {
    private int id;
    private String name; 
    private String email; 
    private String contactNo;
    private String country;

    public CleanAdminDTO(int id, String name, String email, String contactNo, String country) {
        this.id = id; 
        this.name = name;
        this.email = email;
        this.contactNo = contactNo;
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
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    } 

}