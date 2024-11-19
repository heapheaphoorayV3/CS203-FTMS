package cs203.ftms.overall.repository.userrelated;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import cs203.ftms.overall.model.userrelated.User;

/**
 * Repository interface for managing `User` entities.
 * Extends JpaRepository to provide CRUD operations for `User` entities.
 * Provides methods to find users by email and by verification token.
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Finds a user by their email address.
     *
     * @param email The email address of the user.
     * @return An optional containing the user if found, or empty if no user matches the email.
     */
    Optional<User> findByEmail(String email);

    /**
     * Finds a user by their verification token.
     *
     * @param token The verification token associated with the user.
     * @return An optional containing the user if found, or empty if no user matches the token.
     */
    Optional<User> findByVerificationToken(String token);
}
