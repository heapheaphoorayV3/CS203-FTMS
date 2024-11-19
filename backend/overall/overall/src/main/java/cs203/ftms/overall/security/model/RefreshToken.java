package cs203.ftms.overall.security.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.time.Instant;

import cs203.ftms.overall.model.userrelated.User;

/**
 * Entity class representing a refresh token for user authentication.
 * Stores token details, expiry date, and the associated user.
 */
@Entity
@Table(name = "refresh_token")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "token", nullable = false)
    private String token;

    @Column(name = "expiry_date", nullable = false)
    private Instant expiryDate;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    /**
     * Default constructor for JPA.
     */
    public RefreshToken() {}

    /**
     * Constructs a RefreshToken with the specified token, expiry date, and associated user.
     *
     * @param token      the token value.
     * @param expiryDate the expiry date of the token.
     * @param user       the user associated with the refresh token.
     */
    public RefreshToken(String token, Instant expiryDate, User user) {
        this.token = token;
        this.expiryDate = expiryDate;
        this.user = user;
    }

    /**
     * Gets the unique identifier of the refresh token.
     *
     * @return the ID of the token.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the refresh token.
     *
     * @param id the ID to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the token value.
     *
     * @return the token string.
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets the token value.
     *
     * @param token the token string to set.
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Gets the expiry date of the token.
     *
     * @return the expiry date as an Instant.
     */
    public Instant getExpiryDate() {
        return expiryDate;
    }

    /**
     * Sets the expiry date of the token.
     *
     * @param expiryDate the expiry date to set.
     */
    public void setExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
    }

    /**
     * Gets the user associated with the refresh token.
     *
     * @return the user associated with the token.
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user associated with the refresh token.
     *
     * @param user the user to associate with the token.
     */
    public void setUser(User user) {
        this.user = user;
    }
}
