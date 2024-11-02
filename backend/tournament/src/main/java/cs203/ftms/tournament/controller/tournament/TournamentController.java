package cs203.ftms.tournament.controller.tournament;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cs203.ftms.tournament.dto.CreateTournamentDTO;
import cs203.ftms.tournament.dto.clean.CleanTournamentDTO;
import cs203.ftms.tournament.model.Tournament;
import cs203.ftms.tournament.model.userrelated.Organiser;
import cs203.ftms.tournament.model.userrelated.User;
import cs203.ftms.tournament.service.tournament.TournamentService;
import jakarta.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/tournament")
public class TournamentController {
    private final TournamentService tournamentService;

    @Autowired
    public TournamentController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    @PostMapping("/create-tournament")
    @PreAuthorize("hasRole('ORGANISER')")
    public ResponseEntity<CleanTournamentDTO> createTournament(@Valid @RequestBody CreateTournamentDTO t) throws MethodArgumentNotValidException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Tournament newTournament = tournamentService.createTournament(t, (Organiser) user);
        if (newTournament != null) {
            CleanTournamentDTO dto = tournamentService.getCleanTournamentDTO(newTournament);
            return new ResponseEntity<>(dto, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/update-tournament/{tid}")
    @PreAuthorize("hasRole('ORGANISER')")
    public ResponseEntity<CleanTournamentDTO> updateTournament(@PathVariable int tid, @Valid @RequestBody CreateTournamentDTO t) throws MethodArgumentNotValidException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Tournament updatedTournament = tournamentService.updateTournament(tid, t, (Organiser) user);
        if (updatedTournament != null) {
            CleanTournamentDTO dto = tournamentService.getCleanTournamentDTO(updatedTournament);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/tournament-details/{tid}")
    public ResponseEntity<CleanTournamentDTO> getTournament(@PathVariable int tid) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Tournament t = tournamentService.getTournament(tid);
        CleanTournamentDTO ct = tournamentService.getCleanTournamentDTO(t);
        if (ct == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(ct, HttpStatus.OK);
    }
    
    @GetMapping("/tournaments")
    public ResponseEntity<List<CleanTournamentDTO>> getAllTournaments() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        List<Tournament> tList = tournamentService.getAllTournaments();

        List<CleanTournamentDTO> ctList = new ArrayList<>();
        for (Tournament t : tList) {
            ctList.add(tournamentService.getCleanTournamentDTO(t));
        }
        return new ResponseEntity<>(ctList, HttpStatus.OK);
    }

    // @DeleteMapping("/delete-tournament/{tid}")
    // @PreAuthorize("hasRole('ORGANISER')")
    // public ResponseEntity<String> deleteTournament(@PathVariable int tid) {
    //     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    //     User user = (User) authentication.getPrincipal();
    //     tournamentService.deleteTournament((Organiser) user, tid);
    //     return new ResponseEntity<>("Deleted successfully", HttpStatus.OK);
    // }

    @GetMapping("/upcoming-tournaments")
    public ResponseEntity<List<CleanTournamentDTO>> getUpcomingTournaments() {

        List<Tournament> tList = tournamentService.getUpcomingTournaments();
        List<CleanTournamentDTO> ctList = new ArrayList<>();
        for (Tournament t : tList) {
            ctList.add(tournamentService.getCleanTournamentDTO(t));
        }
        return new ResponseEntity<>(ctList, HttpStatus.OK);
    }

    @GetMapping("/past-tournaments")
    public ResponseEntity<List<CleanTournamentDTO>> getPastTournaments() {
        List<Tournament> tList = tournamentService.getPastTournaments();
        List<CleanTournamentDTO> ctList = new ArrayList<>();
        for (Tournament t : tList) {
            ctList.add(tournamentService.getCleanTournamentDTO(t));
        }
        return new ResponseEntity<>(ctList, HttpStatus.OK);
    }
}