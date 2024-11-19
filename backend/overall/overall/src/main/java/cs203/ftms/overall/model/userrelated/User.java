package cs203.ftms.overall.model.userrelated;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import cs203.ftms.overall.security.model.RefreshToken;
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * Represents a base user entity in the system with common attributes like name, email,
 * contact information, role, and account status. Implements Spring Security's UserDetails
 * interface for authentication.
 */
@Entity
@Table(name = "user")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.CHAR)
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

    @Column(name = "role")
    private String role;

    @Column(name = "verification_token")
    private String verificationToken;

    @Column(name = "verification_token_creation")
    private Date verificationTokenCreatedAt;

    @OneToOne(mappedBy = "user")
    private RefreshToken refreshToken;

    /**
     * Default constructor for User.
     */
    public User() {}

    /**
     * Constructs a User with specified details.
     *
     * @param name The name of the user.
     * @param email The email of the user.
     * @param password The password of the user.
     * @param contactNo The contact number of the user.
     * @param country The country of the user.
     * @param role The role assigned to the user.
     */
    public User(String name, String email, String password, String contactNo, String country, String role) {
        this.name = name;
        this.contactNo = contactNo;
        this.country = country;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    /**
     * Gets the user ID.
     *
     * @return The user ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the user ID.
     *
     * @param id The ID to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the name of the user.
     *
     * @return The user's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the user.
     *
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the country of the user.
     *
     * @return The user's country.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the country of the user.
     *
     * @param country The country to set.
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Gets the contact number of the user.
     *
     * @return The contact number.
     */
    public String getContactNo() {
        return contactNo;
    }

    /**
     * Sets the contact number of the user.
     *
     * @param contactNo The contact number to set.
     */
    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    /**
     * Gets the email of the user.
     *
     * @return The user's email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of the user.
     *
     * @param email The email to set.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the password of the user.
     *
     * @return The password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the user.
     *
     * @param password The password to set.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the role of the user.
     *
     * @return The user's role.
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the role of the user.
     *
     * @param role The role to set.
     * @return The updated User object.
     */
    public User setRole(String role) {
        this.role = role;
        return this;
    }

    /**
     * Gets the verification token for email verification.
     *
     * @return The verification token.
     */
    public String getVerificationToken() {
        return verificationToken;
    }

    /**
     * Sets the verification token and updates its creation date.
     *
     * @param verificationToken The verification token to set.
     */
    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
        setVerificationTokenCreatedAt(new Date());
    }

    /**
     * Gets the date the verification token was created.
     *
     * @return The creation date of the verification token.
     */
    public Date getVerificationTokenCreatedAt() {
        return verificationTokenCreatedAt;
    }

    /**
     * Sets the creation date of the verification token.
     *
     * @param verificationTokenCreatedAt The date to set.
     */
    public void setVerificationTokenCreatedAt(Date verificationTokenCreatedAt) {
        this.verificationTokenCreatedAt = verificationTokenCreatedAt;
    }

    /**
     * Checks if the verification token is still valid based on a 15-minute expiration.
     *
     * @return true if valid, false if expired.
     */
    public boolean tokenStillValid() {
        Date now = new Date();
        int minsInMilli = 15 * 60 * 1000; // 15 minutes
        if ((now.getTime() - this.verificationTokenCreatedAt.getTime()) >= minsInMilli) {
            setVerificationToken(null);
            return false;
        }
        return true;
    }

    /**
     * Gets the locked status of the user.
     *
     * @return true if the account is locked, false otherwise.
     */
    public boolean isLocked() {
        return locked;
    }

    /**
     * Sets the locked status of the user.
     *
     * @param locked The locked status to set.
     */
    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    /**
     * Gets the refresh token associated with the user.
     *
     * @return The user's refresh token.
     */
    public RefreshToken getRefreshToken() {
        return refreshToken;
    }

    /**
     * Sets the refresh token associated with the user.
     *
     * @param refreshToken The refresh token to set.
     */
    public void setRefreshToken(RefreshToken refreshToken) {
        this.refreshToken = refreshToken;
    }

    /**
     * Compares this user to another object based on ID equality.
     *
     * @param obj The object to compare.
     * @return true if IDs match, false otherwise.
     */
    public boolean equals(Object obj) {
        if (obj instanceof User u) {
            return u.getId() == this.getId();
        }
        return false;
    }

    // Methods from UserDetails interface

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role));
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
}
