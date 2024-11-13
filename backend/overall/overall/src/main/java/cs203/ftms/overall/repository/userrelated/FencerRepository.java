package cs203.ftms.overall.repository.userrelated;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import cs203.ftms.overall.model.userrelated.Fencer;

public interface FencerRepository extends JpaRepository<Fencer, Integer> {
    Optional<Fencer> findByEmail(String email);
    Optional<Fencer> findByName(String name);
}