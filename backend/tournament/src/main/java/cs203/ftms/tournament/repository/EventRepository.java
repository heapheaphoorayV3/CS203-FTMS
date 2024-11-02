package cs203.ftms.tournament.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import cs203.ftms.tournament.model.Event;
import cs203.ftms.tournament.model.Tournament;


public interface EventRepository extends JpaRepository<Event, Integer> {
    Optional<Event> findByTournamentAndGenderAndWeapon(Tournament t, char g, char w);
}