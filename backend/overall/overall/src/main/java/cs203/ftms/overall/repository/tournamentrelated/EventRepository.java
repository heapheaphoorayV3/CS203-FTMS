package cs203.ftms.overall.repository.tournamentrelated;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import cs203.ftms.overall.model.tournamentrelated.Event;
import cs203.ftms.overall.model.tournamentrelated.Tournament;

/**
 * Repository interface for managing `Event` entities.
 * Extends JpaRepository to provide CRUD operations for `Event` entities.
 * Provides methods for retrieving events based on tournament, gender, and weapon type.
 */
public interface EventRepository extends JpaRepository<Event, Integer> {

    /**
     * Finds an event associated with a specific tournament, gender, and weapon type.
     *
     * @param tournament the tournament associated with the event
     * @param gender the gender category of the event
     * @param weapon the weapon type of the event
     * @return an Optional containing the event if found, or empty if no event matches the criteria
     */
    Optional<Event> findByTournamentAndGenderAndWeapon(Tournament tournament, char gender, char weapon);

    /**
     * Finds all events matching a specific gender and weapon type.
     *
     * @param gender the gender category of the events
     * @param weapon the weapon type of the events
     * @return a list of events matching the specified gender and weapon type
     */
    List<Event> findByGenderAndWeapon(char gender, char weapon);

    /**
     * Finds all events associated with a specific tournament.
     *
     * @param tournament the tournament associated with the events
     * @return a list of events associated with the specified tournament
     */
    List<Event> findByTournament(Tournament tournament);
}
