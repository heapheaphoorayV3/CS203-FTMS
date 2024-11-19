package cs203.ftms.overall.security.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cs203.ftms.overall.model.userrelated.User;
import cs203.ftms.overall.security.model.RefreshToken;
import cs203.ftms.overall.security.repository.RefreshTokenRepository;

/**
 * Service class responsible for creating, managing, and verifying refresh tokens.
 */
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${security.refreshtoken.expiration-time}")
    private long expiry;

    /**
     * Constructs the RefreshTokenService with the specified repository.
     *
     * @param refreshTokenRepository the repository to manage RefreshToken entities.
     */
    @Autowired
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    /**
     * Creates a new refresh token for a given user, replacing any existing token.
     *
     * @param user the user for whom the refresh token is being created.
     * @return the newly created RefreshToken entity.
     */
    public RefreshToken createRefreshToken(User user) {
        RefreshToken oldRT = user.getRefreshToken();
        if (oldRT != null) {
            refreshTokenRepository.delete(oldRT);
        }
        RefreshToken refreshToken = new RefreshToken(UUID.randomUUID().toString(), Instant.now().plusMillis(expiry), user);
        return refreshTokenRepository.save(refreshToken);
    }

    /**
     * Finds a refresh token by its token string.
     *
     * @param token the token string to search for.
     * @return an Optional containing the RefreshToken if found, otherwise empty.
     */
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    /**
     * Verifies whether the refresh token is still valid based on its expiration date.
     * Deletes the token if expired and throws a runtime exception.
     *
     * @param token the RefreshToken to verify.
     * @return the same RefreshToken if it has not expired.
     * @throws RuntimeException if the token is expired.
     */
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + " Refresh token is expired. Please make a new login..!");
        }
        return token;
    }
}
