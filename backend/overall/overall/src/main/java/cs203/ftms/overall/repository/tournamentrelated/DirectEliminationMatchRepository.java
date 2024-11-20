package cs203.ftms.overall.repository.tournamentrelated;

import java.util.List;

import cs203.ftms.overall.model.tournamentrelated.DirectEliminationMatch;
import cs203.ftms.overall.model.tournamentrelated.Event;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing `DirectEliminationMatch` entities.
 * Provides methods for retrieving direct elimination matches by associated event
 * and by specific rounds within an event.
 */
public interface DirectEliminationMatchRepository extends JpaRepository<DirectEliminationMatch, Integer> {

    /**
     * Finds all direct elimination matches associated with a specific event.
     *
     * @param event The event for which to retrieve direct elimination matches.
     * @return A list of direct elimination matches for the specified event.
     */
    List<DirectEliminationMatch> findByEvent(Event event);

    /**
     * Finds all direct elimination matches associated with a specific event and round.
     *
     * @param event The event for which to retrieve matches.
     * @param roundOf The round number for which to retrieve matches.
     * @return A list of direct elimination matches for the specified event and round.
     */
    List<DirectEliminationMatch> findByEventAndRoundOf(Event event, int roundOf);
}
