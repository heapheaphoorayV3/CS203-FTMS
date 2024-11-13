package cs203.ftms.overall.repository.tournamentrelated;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import cs203.ftms.overall.model.tournamentrelated.Event;
import cs203.ftms.overall.model.tournamentrelated.Tournament;


public interface EventRepository extends JpaRepository<Event, Integer> {
    Optional<Event> findByTournamentAndGenderAndWeapon(Tournament t, char g, char w);
    List<Event> findByGenderAndWeapon(char gender, char weapon); // Add this line

}