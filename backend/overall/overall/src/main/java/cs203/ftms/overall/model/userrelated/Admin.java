package cs203.ftms.overall.model.userrelated;

import cs203.ftms.overall.security.model.Role;
import jakarta.persistence.*;

@Entity
// @Table(name = "admin")
@DiscriminatorValue("A")
public class Admin extends User {

    public Admin() {}

    public Admin(String name, String email, String password, String contactNo, String country, Role role) {
        super(name, email, password, contactNo, country,  role);
    }

}
