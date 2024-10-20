package cs203.ftms.overall.repository.tournamentrelated;

import org.springframework.data.jpa.repository.JpaRepository;
import cs203.ftms.overall.model.tournamentrelated.Match;
import java.util.Optional;

public interface MatchRepository extends JpaRepository<Match, Integer> {
    // Optional<Match> List<Match> findByFencers(Set<TournamentFencer> fencers);
}