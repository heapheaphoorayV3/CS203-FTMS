package cs203.ftms.overall.repository.userrelated;

import org.springframework.data.jpa.repository.JpaRepository;
import cs203.ftms.overall.model.userrelated.Organiser;

import java.util.List;
import java.util.Optional;

public interface OrganiserRepository extends JpaRepository<Organiser, Integer>{
    List<Organiser> findByVerified(boolean verified);
    Optional<Organiser> findByEmail(String email);
}