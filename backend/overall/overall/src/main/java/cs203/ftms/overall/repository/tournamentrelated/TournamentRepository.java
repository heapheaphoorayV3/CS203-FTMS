package cs203.ftms.overall.repository.tournamentrelated;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import jakarta.transaction.Transactional;

import cs203.ftms.overall.model.tournamentrelated.Tournament;

/**
 * Repository interface for managing `Tournament` entities.
 * Provides methods to find tournaments by name, organiser ID, and signup end date,
 * as well as a custom query to delete tournaments by ID.
 */
public interface TournamentRepository extends JpaRepository<Tournament, Integer> {

    /**
     * Finds a tournament by its name.
     *
     * @param name The name of the tournament.
     * @return An optional containing the tournament if found, or empty if no tournament matches the name.
     */
    Optional<Tournament> findByName(String name);

    /**
     * Finds all tournaments organized by a specific organiser.
     *
     * @param id The ID of the organiser.
     * @return A list of tournaments organized by the specified organiser.
     */
    List<Tournament> findByOrganiserId(int id);

    /**
     * Finds all tournaments with a specific signup end date.
     *
     * @param signupEndDate The signup end date of the tournaments.
     * @return A list of tournaments with the specified signup end date.
     */
    List<Tournament> findBySignupEndDate(LocalDate signupEndDate);

    /**
     * Deletes a tournament by its ID.
     *
     * @param id The ID of the tournament to delete.
     */
    @Modifying
    @Transactional
    @Query("delete from Tournament t where t.id = ?1")
    void deleteTournamentById(int id);
}
