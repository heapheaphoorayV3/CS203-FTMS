package cs203.ftms.overall.dto.clean;

/**
 * Data Transfer Object for representing a cleaned version of Admin details.
 * This DTO contains only the essential information for an Admin.
 */
public class CleanAdminDTO {
    private int id;
    private String name; 
    private String email; 
    private String contactNo;
    private String country;

    /**
     * Constructs a new CleanAdminDTO with the specified details.
     *
     * @param id The unique identifier of the admin.
     * @param name The name of the admin.
     * @param email The email address of the admin.
     * @param contactNo The contact number of the admin.
     * @param country The country associated with the admin.
     */
    public CleanAdminDTO(int id, String name, String email, String contactNo, String country) {
        this.id = id; 
        this.name = name;
        this.email = email;
        this.contactNo = contactNo;
        this.country = country;
    }

    /**
     * Gets the name of the admin.
     *
     * @return The name of the admin.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the admin.
     *
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the email address of the admin.
     *
     * @return The email address of the admin.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the admin.
     *
     * @param email The email address to set.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the contact number of the admin.
     *
     * @return The contact number of the admin.
     */
    public String getContactNo() {
        return contactNo;
    }

    /**
     * Sets the contact number of the admin.
     *
     * @param contactNo The contact number to set.
     */
    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    /**
     * Gets the country associated with the admin.
     *
     * @return The country of the admin.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the country associated with the admin.
     *
     * @param country The country to set.
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Gets the unique identifier of the admin.
     *
     * @return The admin's unique identifier.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the admin.
     *
     * @param id The ID to set.
     */
    public void setId(int id) {
        this.id = id;
    }
}
