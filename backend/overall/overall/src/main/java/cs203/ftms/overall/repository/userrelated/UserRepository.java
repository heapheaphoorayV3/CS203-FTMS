package cs203.ftms.overall.repository.userrelated;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import cs203.ftms.overall.model.userrelated.User;

public interface UserRepository extends JpaRepository<User, Integer>{

    Optional<User> findByEmail(String email);
}
