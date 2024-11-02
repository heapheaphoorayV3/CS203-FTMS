package cs203.ftms.tournament.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cs203.ftms.tournament.model.Event;
import cs203.ftms.tournament.model.Poule;

import java.util.List;


public interface PouleRepository extends JpaRepository<Poule, Integer> {
    List<Poule> findByEventAndPouleNumber(Event event, int pouleNumber);
    List<Poule> findByEvent(Event event);
}