package cs203.ftms.overall.dto;

/**
 * Data Transfer Object (DTO) for handling refresh token requests.
 * This class encapsulates the refresh token needed to request a new access token.
 */
public class RefreshTokenRequestDTO {
    
    private String token;

    /**
     * Default constructor for RefreshTokenRequestDTO.
     */
    public RefreshTokenRequestDTO() {}

    /**
     * Constructs a new RefreshTokenRequestDTO with the specified token.
     *
     * @param token the refresh token used to request a new access token
     */
    public RefreshTokenRequestDTO(String token) {
        this.token = token;
    }

    /**
     * Gets the refresh token.
     *
     * @return the refresh token as a string
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets the refresh token.
     *
     * @param token the refresh token to set
     */
    public void setToken(String token) {
        this.token = token;
    }
}
