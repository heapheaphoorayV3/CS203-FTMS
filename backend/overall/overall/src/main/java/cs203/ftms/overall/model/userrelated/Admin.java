package cs203.ftms.overall.model.userrelated;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * Represents an admin user with administrative privileges within the system.
 * Extends the base User class and has a predefined role of "ROLE_ADMIN".
 */
@Entity
@DiscriminatorValue("A")
public class Admin extends User {

    /**
     * Default constructor for Admin.
     */
    public Admin() {}

    /**
     * Constructs an Admin with specified details.
     *
     * @param name The name of the admin.
     * @param email The email of the admin.
     * @param password The password for the admin's account.
     * @param contactNo The contact number of the admin.
     * @param country The country of the admin.
     */
    public Admin(String name, String email, String password, String contactNo, String country) {
        super(name, email, password, contactNo, country, "ROLE_ADMIN");
    }
}
