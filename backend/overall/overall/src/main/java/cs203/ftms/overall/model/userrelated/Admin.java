package cs203.ftms.overall.model.userrelated;

import jakarta.persistence.*;

@Entity
// @Table(name = "admin")
@DiscriminatorValue("A")
public class Admin extends User {

    public Admin() {}

    public Admin(String name, String email, String password, String contactNo, String country) {
        super(name, email, password, contactNo, country, "ROLE_ADMIN");
    }

}
