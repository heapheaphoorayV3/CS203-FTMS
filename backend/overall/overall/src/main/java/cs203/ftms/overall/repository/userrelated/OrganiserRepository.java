package cs203.ftms.overall.repository.userrelated;

import org.springframework.data.jpa.repository.JpaRepository;
import cs203.ftms.overall.model.userrelated.Organiser;
import java.util.List;

public interface OrganiserRepository extends JpaRepository<Organiser, Integer>{
    List<Organiser> findByVerified(boolean verified);
    
}