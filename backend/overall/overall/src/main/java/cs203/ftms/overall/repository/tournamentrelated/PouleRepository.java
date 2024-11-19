package cs203.ftms.overall.repository.tournamentrelated;

import org.springframework.data.jpa.repository.JpaRepository;
import cs203.ftms.overall.model.tournamentrelated.Poule;
import cs203.ftms.overall.model.tournamentrelated.Event;
import java.util.List;

/**
 * Repository interface for managing `Poule` entities.
 * Provides methods to retrieve poules by associated event and poule number.
 */
public interface PouleRepository extends JpaRepository<Poule, Integer> {

    /**
     * Finds all poules associated with a specific event and poule number.
     *
     * @param event The event associated with the poules.
     * @param pouleNumber The poule number within the event.
     * @return A list of poules for the specified event and poule number.
     */
    List<Poule> findByEventAndPouleNumber(Event event, int pouleNumber);

    /**
     * Finds all poules associated with a specific event.
     *
     * @param event The event associated with the poules.
     * @return A list of poules for the specified event.
     */
    List<Poule> findByEvent(Event event);
}
