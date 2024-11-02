package cs203.ftms.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cs203.ftms.user.model.Fencer;

public interface FencerRepository extends JpaRepository<Fencer, Integer> {
}