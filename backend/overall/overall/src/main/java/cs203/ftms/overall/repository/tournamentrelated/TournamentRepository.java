package cs203.ftms.overall.repository.tournamentrelated;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import cs203.ftms.overall.model.tournamentrelated.Tournament;

public interface TournamentRepository extends JpaRepository<Tournament, Integer> {
    Optional<Tournament> findByName(String name);
    Optional<List<Tournament>> findByOrganiserId(int id);
}