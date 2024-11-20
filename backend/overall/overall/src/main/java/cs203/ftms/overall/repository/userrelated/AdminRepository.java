package cs203.ftms.overall.repository.userrelated;

import org.springframework.data.jpa.repository.JpaRepository;
import cs203.ftms.overall.model.userrelated.Admin;

/**
 * Repository interface for managing `Admin` entities.
 * Extends JpaRepository to provide CRUD operations for `Admin` entities.
 */
public interface AdminRepository extends JpaRepository<Admin, Integer> {
}
