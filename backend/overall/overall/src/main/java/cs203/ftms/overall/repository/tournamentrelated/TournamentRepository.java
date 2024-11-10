package cs203.ftms.overall.repository.tournamentrelated;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cs203.ftms.overall.model.tournamentrelated.Tournament;
import jakarta.transaction.Transactional;

import java.time.LocalDate;


public interface TournamentRepository extends JpaRepository<Tournament, Integer> {
    Optional<Tournament> findByName(String name);
    List<Tournament> findByOrganiserId(int id);
    List<Tournament> findBySignupEndDate(LocalDate signupEndDate);
    
    @Modifying
    @Transactional
    @Query("delete from Tournament t where t.id = ?1")
    void deleteTournamentById(int id);
}