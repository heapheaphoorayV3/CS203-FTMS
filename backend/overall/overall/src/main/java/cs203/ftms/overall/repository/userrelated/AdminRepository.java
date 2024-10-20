package cs203.ftms.overall.repository.userrelated;

import org.springframework.data.jpa.repository.JpaRepository;
import cs203.ftms.overall.model.userrelated.Admin;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
}