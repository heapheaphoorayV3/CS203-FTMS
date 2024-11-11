package cs203.ftms.overall.model.userrelated;

import java.util.*;

import cs203.ftms.overall.model.tournamentrelated.Tournament;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("O")
public class Organiser extends User {

    @Column(name = "verified")
    private boolean verified;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "organiser", orphanRemoval = true)
    private Set<Tournament> tourHost;

    public Organiser() {
        this.tourHost = new HashSet<>();
    }

    public Organiser(String name, String email, String password, String contactNo, String country) {
        super(name, email, password, contactNo, country, "ROLE_ORGANISER");
        this.tourHost = new HashSet<>();
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

    public Set<Tournament> getTourHost() {
        return tourHost;
    }

    public void setTourHost(Set<Tournament> tourHost) {
        this.tourHost = tourHost;
    }

    
}
