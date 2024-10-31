package cs203.ftms.overall.repository.tournamentrelated;

import java.util.List;

import cs203.ftms.overall.model.tournamentrelated.DirectEliminationMatch;
import cs203.ftms.overall.model.tournamentrelated.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DirectEliminationMatchRepository extends JpaRepository<DirectEliminationMatch, Integer> {
    List<DirectEliminationMatch> findByEvent(Event event);
    List<DirectEliminationMatch> findByEventAndRoundOf(Event event, int roundOf);
}
