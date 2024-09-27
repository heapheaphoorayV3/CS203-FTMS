package cs203.ftms.overall.model.userrelated;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "user")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="user_type",
        discriminatorType = DiscriminatorType.CHAR)
@DiscriminatorValue("U")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name; 

    @Column(name = "country")
    private String country; 

    @Column(name = "email", unique = true, nullable = false)
    private String email; 

    @Column(name = "contactNo")
    private String contactNo; 

    @Column(name = "password")
    private String password;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "locked")
    private boolean locked;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH})
    @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false)
    private Role role;

    @Column(name = "verification_token")
    private String verificationToken;

    @Column(name = "verificiation_token_creation")
    private Date verificationTokenCreatedAt;

    public User() {}

    public User(String name, String email, String password, String contactNo, String country, Role role) {
        this.name = name;
        this.contactNo = contactNo;
        this.country = country; 
        this.email = email;
        this.password = password;
        this.role = role;
    } 

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    } 

    
    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role.getName().toString());

        return List.of(authority);
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Role getRole() {
        return role;
    }

    public User setRole(Role role) {
        this.role = role;

        return this;
    }

      public String getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
        setVerificationTokenCreatedAt(new Date());
    }

    public Date getVerificationTokenCreatedAt() {
        return verificationTokenCreatedAt;
    }

    public void setVerificationTokenCreatedAt(Date verificationTokenCreatedAt) {
        this.verificationTokenCreatedAt = verificationTokenCreatedAt;
    }

    public boolean tokenStillValid() {
        Date now = new Date();
        if ((now.getTime() - this.verificationTokenCreatedAt.getTime()) >= 15*60*1000) {
            setVerificationToken(null);
            return false;
        }
        return true;
    }

    public boolean equals(Object obj) {
        if (obj instanceof User u) {
            if (u.getId() == this.getId()) return true;
        }
        return false;
    }
}
