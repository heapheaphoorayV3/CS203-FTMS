package cs203.ftms.overall.repository.tournamentrelated;

import org.springframework.data.jpa.repository.JpaRepository;
import cs203.ftms.overall.model.tournamentrelated.Poule;
import java.util.List;
import cs203.ftms.overall.model.tournamentrelated.Event;


public interface PouleRepository extends JpaRepository<Poule, Integer> {
    List<Poule> findByEventAndPouleNumber(Event event, int pouleNumber);
    List<Poule> findByEvent(Event event);
}