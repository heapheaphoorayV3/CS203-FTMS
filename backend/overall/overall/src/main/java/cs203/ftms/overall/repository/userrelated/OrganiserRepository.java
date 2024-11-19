package cs203.ftms.overall.repository.userrelated;

import org.springframework.data.jpa.repository.JpaRepository;
import cs203.ftms.overall.model.userrelated.Organiser;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing `Organiser` entities.
 * Extends JpaRepository to provide CRUD operations for `Organiser` entities.
 * Provides methods to retrieve organisers by their verification status or email.
 */
public interface OrganiserRepository extends JpaRepository<Organiser, Integer> {

    /**
     * Finds all organisers based on their verification status.
     *
     * @param verified the verification status to filter organisers
     * @return a list of organisers with the specified verification status
     */
    List<Organiser> findByVerified(boolean verified);

    /**
     * Finds an organiser by their email address.
     *
     * @param email the email address of the organiser
     * @return an Optional containing the Organiser if found, or empty if not found
     */
    Optional<Organiser> findByEmail(String email);
}
