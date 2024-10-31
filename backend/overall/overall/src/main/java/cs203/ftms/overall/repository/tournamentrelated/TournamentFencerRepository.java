package cs203.ftms.overall.repository.tournamentrelated;

import org.springframework.data.jpa.repository.JpaRepository;
import cs203.ftms.overall.model.tournamentrelated.*;
import java.util.List;


public interface TournamentFencerRepository extends JpaRepository<TournamentFencer, Integer> {
    List<TournamentFencer> findByEvent(Event event);
}