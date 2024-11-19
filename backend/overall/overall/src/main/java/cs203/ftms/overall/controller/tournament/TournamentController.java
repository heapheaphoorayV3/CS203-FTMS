package cs203.ftms.overall.controller.tournament;

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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cs203.ftms.overall.dto.CreateTournamentDTO;
import cs203.ftms.overall.dto.clean.CleanTournamentDTO;
import cs203.ftms.overall.model.tournamentrelated.Tournament;
import cs203.ftms.overall.model.userrelated.Organiser;
import cs203.ftms.overall.model.userrelated.User;
import cs203.ftms.overall.service.tournament.TournamentService;
import jakarta.validation.Valid;

/**
 * Controller class responsible for managing tournament-related operations.
 * Provides endpoints for creating, updating, deleting, and retrieving tournaments.
 */
@RestController
@CrossOrigin
@RequestMapping("/api/v1/tournament")
public class TournamentController {
    
    private final TournamentService tournamentService;

    /**
     * Constructor for TournamentController.
     * 
     * @param tournamentService The service layer component for handling tournament-related operations.
     */
    @Autowired
    public TournamentController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    /**
     * Creates a new tournament.
     *
     * @param createTournamentDTO Data for creating the tournament, provided as CreateTournamentDTO.
     * @return ResponseEntity with CleanTournamentDTO and HttpStatus.CREATED if creation is successful,
     *         or HttpStatus.BAD_REQUEST if creation fails.
     * @throws MethodArgumentNotValidException if the input data is invalid.
     */
    @PostMapping("/create-tournament")
    @PreAuthorize("hasRole('ORGANISER')")
    public ResponseEntity<CleanTournamentDTO> createTournament(@Valid @RequestBody CreateTournamentDTO createTournamentDTO) throws MethodArgumentNotValidException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Tournament newTournament = tournamentService.createTournament(createTournamentDTO, (Organiser) user);
        if (newTournament != null) {
            CleanTournamentDTO res = tournamentService.getCleanTournamentDTO(newTournament);
            return new ResponseEntity<>(res, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    /**
     * Updates an existing tournament.
     *
     * @param tid The ID of the tournament to update.
     * @param createTournamentDTO Updated tournament data as CreateTournamentDTO.
     * @return ResponseEntity with CleanTournamentDTO and HttpStatus.OK if update is successful,
     *         or HttpStatus.BAD_REQUEST if update fails.
     * @throws MethodArgumentNotValidException if the input data is invalid.
     */
    @PutMapping("/update-tournament/{tid}")
    @PreAuthorize("hasRole('ORGANISER')")
    public ResponseEntity<CleanTournamentDTO> updateTournament(@PathVariable int tid, @Valid @RequestBody CreateTournamentDTO createTournamentDTO) throws MethodArgumentNotValidException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Tournament updatedTournament = tournamentService.updateTournament(tid, createTournamentDTO, (Organiser) user);
        CleanTournamentDTO res = tournamentService.getCleanTournamentDTO(updatedTournament);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    /**
     * Retrieves details of a specific tournament.
     *
     * @param tid The ID of the tournament to retrieve.
     * @return ResponseEntity with CleanTournamentDTO and HttpStatus.OK if retrieval is successful,
     *         or HttpStatus.BAD_REQUEST if retrieval fails.
     */
    @GetMapping("/tournament-details/{tid}")
    public ResponseEntity<CleanTournamentDTO> getTournament(@PathVariable int tid) {
        Tournament t = tournamentService.getTournament(tid);
        CleanTournamentDTO res = tournamentService.getCleanTournamentDTO(t);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    /**
     * Retrieves all tournaments.
     *
     * @return ResponseEntity containing a list of CleanTournamentDTO with HttpStatus.OK.
     */
    @GetMapping("/tournaments")
    public ResponseEntity<List<CleanTournamentDTO>> getAllTournaments() {
        List<Tournament> tList = tournamentService.getAllTournaments();

        List<CleanTournamentDTO> res = new ArrayList<>();
        for (Tournament t : tList) {
            res.add(tournamentService.getCleanTournamentDTO(t));
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    /**
     * Deletes a specific tournament.
     *
     * @param tid The ID of the tournament to delete.
     * @return ResponseEntity with a success message and HttpStatus.OK.
     */
    @DeleteMapping("/delete-tournament/{tid}")
    @PreAuthorize("hasRole('ORGANISER')")
    public ResponseEntity<String> deleteTournament(@PathVariable int tid) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        tournamentService.deleteTournament((Organiser) user, tid);
        return new ResponseEntity<>("Deleted tournament successfully", HttpStatus.OK);
    }

    /**
     * Retrieves upcoming tournaments.
     *
     * @return ResponseEntity containing a list of CleanTournamentDTO with HttpStatus.OK.
     */
    @GetMapping("/upcoming-tournaments")
    public ResponseEntity<List<CleanTournamentDTO>> getUpcomingTournaments() {
        List<Tournament> tList = tournamentService.getUpcomingTournaments();
        List<CleanTournamentDTO> res = new ArrayList<>();
        for (Tournament t : tList) {
            res.add(tournamentService.getCleanTournamentDTO(t));
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    /**
     * Retrieves past tournaments.
     *
     * @return ResponseEntity containing a list of CleanTournamentDTO with HttpStatus.OK.
     */
    @GetMapping("/past-tournaments")
    public ResponseEntity<List<CleanTournamentDTO>> getPastTournaments() {
        List<Tournament> tList = tournamentService.getPastTournaments();
        List<CleanTournamentDTO> res = new ArrayList<>();
        for (Tournament t : tList) {
            res.add(tournamentService.getCleanTournamentDTO(t));
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
