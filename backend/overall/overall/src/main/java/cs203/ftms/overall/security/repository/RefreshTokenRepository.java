package cs203.ftms.overall.security.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cs203.ftms.overall.security.model.RefreshToken;

/**
 * Repository interface for performing CRUD operations on RefreshToken entities.
 */
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {

    /**
     * Finds a RefreshToken by its token value.
     *
     * @param token the token string to search for.
     * @return an Optional containing the matching RefreshToken if found, or empty otherwise.
     */
    Optional<RefreshToken> findByToken(String token);
}
