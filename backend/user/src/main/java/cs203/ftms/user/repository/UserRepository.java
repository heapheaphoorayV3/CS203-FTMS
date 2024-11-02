package cs203.ftms.user.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import cs203.ftms.user.model.User;

public interface UserRepository extends JpaRepository<User, Integer>{

    Optional<User> findByEmail(String email);
    Optional<User> findByVerificationToken(String token);
}
