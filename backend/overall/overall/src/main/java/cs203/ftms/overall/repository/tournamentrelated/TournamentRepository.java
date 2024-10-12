package cs203.ftms.overall.repository.tournamentrelated;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import cs203.ftms.overall.model.tournamentrelated.Tournament;

public interface TournamentRepository extends JpaRepository<Tournament, Integer> {
    Optional<Tournament> findByName(String name);
}