package cs203.ftms.overall.repository.userrelated;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import cs203.ftms.overall.model.userrelated.Fencer;

/**
 * Repository interface for managing `Fencer` entities.
 * Extends JpaRepository to provide CRUD operations for `Fencer` entities.
 */
public interface FencerRepository extends JpaRepository<Fencer, Integer> {

    /**
     * Finds a fencer by their email address.
     *
     * @param email the email address of the fencer
     * @return an Optional containing the Fencer if found, or empty if not found
     */
    Optional<Fencer> findByEmail(String email);

    /**
     * Finds a fencer by their name.
     *
     * @param name the name of the fencer
     * @return an Optional containing the Fencer if found, or empty if not found
     */
    Optional<Fencer> findByName(String name);
}
