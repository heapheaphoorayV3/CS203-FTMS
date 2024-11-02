package cs203.ftms.user.dto;

public class JwtDTO {
    private String message;
    private String token;
    private long expiresIn;
    private char userType;
    private String refreshToken;


    public JwtDTO(String message, String token, long expiresIn, char userType, String refreshToken) {
        this.message = message;
        this.token = token;
        this.expiresIn = expiresIn;
        this.userType = userType;
        this.refreshToken = refreshToken; 
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public char getUserType() {
        return userType;
    }

    public void setUserType(char userType) {
        this.userType = userType;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
