package cs203.ftms.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cs203.ftms.user.model.Organiser;

import java.util.List;

public interface OrganiserRepository extends JpaRepository<Organiser, Integer>{
    List<Organiser> findByVerified(boolean verified);
    
}