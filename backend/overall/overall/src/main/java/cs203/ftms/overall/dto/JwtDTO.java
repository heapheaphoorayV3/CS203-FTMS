package cs203.ftms.overall.dto;

public class JwtDTO {
    private String message;
    private String token;
    private long expiresIn;
    private char userType;


    public JwtDTO(String message, String token, long expiresIn, char userType) {
        this.message = message;
        this.token = token;
        this.expiresIn = expiresIn;
        this.userType = userType;
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
}
