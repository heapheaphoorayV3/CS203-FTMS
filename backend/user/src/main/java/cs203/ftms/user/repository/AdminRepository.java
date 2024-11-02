package cs203.ftms.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cs203.ftms.user.model.Admin;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
}