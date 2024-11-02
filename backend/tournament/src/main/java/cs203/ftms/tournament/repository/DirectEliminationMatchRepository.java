package cs203.ftms.tournament.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cs203.ftms.tournament.model.DirectEliminationMatch;
import cs203.ftms.tournament.model.Event;

public interface DirectEliminationMatchRepository extends JpaRepository<DirectEliminationMatch, Integer> {
    List<DirectEliminationMatch> findByEvent(Event event);
    List<DirectEliminationMatch> findByEventAndRoundOf(Event event, int roundOf);
}
