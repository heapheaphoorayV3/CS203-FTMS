package cs203.ftms.overall.repository.tournamentrelated;

import org.springframework.data.jpa.repository.JpaRepository;
import cs203.ftms.overall.model.tournamentrelated.Poule;

public interface PouleRepository extends JpaRepository<Poule, Integer> {
    
}