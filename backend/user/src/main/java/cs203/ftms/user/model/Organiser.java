package cs203.ftms.user.model;

import java.util.*;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("O")
public class Organiser extends User {

    @Column(name = "verified")
    private boolean verified;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "organiser")
    private Set<Integer> tourHostId;

    public Organiser() {}

    public Organiser(String name, String email, String password, String contactNo, String country) {
        super(name, email, password, contactNo, country, "ROLE_ORGANISER");
        this.tourHostId = new HashSet<>();
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    @Override
    public boolean isEnabled() { // verified
        return verified;
    }

    public Set<Integer> getTourHost() {
        return tourHostId;
    }

    public void setTourHost(Set<Integer> tourHost) {
        this.tourHostId = tourHost;
    }

    
}
