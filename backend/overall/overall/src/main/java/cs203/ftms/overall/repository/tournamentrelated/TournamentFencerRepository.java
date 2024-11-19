package cs203.ftms.overall.repository.tournamentrelated;

import org.springframework.data.jpa.repository.JpaRepository;
import cs203.ftms.overall.model.tournamentrelated.TournamentFencer;
import cs203.ftms.overall.model.tournamentrelated.Event;
import cs203.ftms.overall.model.userrelated.Fencer;
import java.util.List;

/**
 * Repository interface for managing `TournamentFencer` entities.
 * Provides methods to retrieve tournament fencers by event and by specific fencer and event combination.
 */
public interface TournamentFencerRepository extends JpaRepository<TournamentFencer, Integer> {

    /**
     * Finds all tournament fencers associated with a specific event.
     *
     * @param event The event for which to retrieve tournament fencers.
     * @return A list of tournament fencers associated with the specified event.
     */
    List<TournamentFencer> findByEvent(Event event);

    /**
     * Finds a specific tournament fencer by fencer and event.
     *
     * @param fencer The fencer to retrieve.
     * @param event The event in which the fencer participated.
     * @return The tournament fencer associated with the specified fencer and event.
     */
    TournamentFencer findByFencerAndEvent(Fencer fencer, Event event);
}
