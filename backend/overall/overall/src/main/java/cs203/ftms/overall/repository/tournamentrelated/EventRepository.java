package cs203.ftms.overall.repository.tournamentrelated;

import org.springframework.data.jpa.repository.JpaRepository;
import cs203.ftms.overall.model.tournamentrelated.Event;

public interface EventRepository extends JpaRepository<Event, Integer> {
    
}