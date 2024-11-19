package cs203.ftms.overall.dto;

/**
 * Data Transfer Object representing a JWT (JSON Web Token) response.
 * Contains details about the authentication message, token, expiration, user type, and refresh token.
 */
public class JwtDTO {
    private String message;
    private String token;
    private long expiresIn;
    private char userType;
    private String refreshToken;

    /**
     * Constructs a JwtDTO with the specified details.
     *
     * @param message The authentication message.
     * @param token The JWT token string.
     * @param expiresIn The expiration time of the token in seconds.
     * @param userType The type of user (e.g., Admin, Fencer, Organiser).
     * @param refreshToken The refresh token associated with the JWT.
     */
    public JwtDTO(String message, String token, long expiresIn, char userType, String refreshToken) {
        this.message = message;
        this.token = token;
        this.expiresIn = expiresIn;
        this.userType = userType;
        this.refreshToken = refreshToken;
    }

    /**
     * Gets the authentication message.
     *
     * @return The message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the authentication message.
     *
     * @param message The message to set.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets the JWT token string.
     *
     * @return The token.
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets the JWT token string.
     *
     * @param token The token to set.
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Gets the expiration time of the token in seconds.
     *
     * @return The expiration time in seconds.
     */
    public long getExpiresIn() {
        return expiresIn;
    }

    /**
     * Sets the expiration time of the token in seconds.
     *
     * @param expiresIn The expiration time to set in seconds.
     */
    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }

    /**
     * Gets the type of user.
     *
     * @return The user type (e.g., Admin, Fencer, Organiser).
     */
    public char getUserType() {
        return userType;
    }

    /**
     * Sets the type of user.
     *
     * @param userType The user type to set.
     */
    public void setUserType(char userType) {
        this.userType = userType;
    }

    /**
     * Gets the refresh token associated with the JWT.
     *
     * @return The refresh token.
     */
    public String getRefreshToken() {
        return refreshToken;
    }

    /**
     * Sets the refresh token associated with the JWT.
     *
     * @param refreshToken The refresh token to set.
     */
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
