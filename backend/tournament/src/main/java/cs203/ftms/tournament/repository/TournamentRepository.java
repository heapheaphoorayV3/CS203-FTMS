package cs203.ftms.tournament.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import cs203.ftms.tournament.model.Tournament;

public interface TournamentRepository extends JpaRepository<Tournament, Integer> {
    Optional<Tournament> findByName(String name);
    Optional<List<Tournament>> findByOrganiserId(int id);
}