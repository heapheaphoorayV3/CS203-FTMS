package cs203.ftms.tournament.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cs203.ftms.tournament.model.*;

public interface MatchRepository extends JpaRepository<Match, Integer> {
}