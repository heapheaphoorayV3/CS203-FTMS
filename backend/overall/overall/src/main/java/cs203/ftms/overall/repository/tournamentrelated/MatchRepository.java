package cs203.ftms.overall.repository.tournamentrelated;

import org.springframework.data.jpa.repository.JpaRepository;
import cs203.ftms.overall.model.tournamentrelated.Match;

/**
 * Repository interface for managing `Match` entities.
 * Extends JpaRepository to provide CRUD operations for `Match` entities.
 */
public interface MatchRepository extends JpaRepository<Match, Integer> {
}
