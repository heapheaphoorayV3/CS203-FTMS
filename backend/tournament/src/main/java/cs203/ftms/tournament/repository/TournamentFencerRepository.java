package cs203.ftms.tournament.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cs203.ftms.tournament.model.*;
import cs203.ftms.user.model.Fencer;

import java.util.List;


public interface TournamentFencerRepository extends JpaRepository<TournamentFencer, Integer> {
    List<TournamentFencer> findByEvent(Event event);
    TournamentFencer findByFencerAndEvent(Fencer fencer, Event event);
}