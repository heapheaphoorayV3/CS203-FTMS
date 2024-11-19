package cs203.ftms.overall.controller.chatbot;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cs203.ftms.overall.dto.clean.CleanTournamentDTO;
import cs203.ftms.overall.model.tournamentrelated.Tournament;
import cs203.ftms.overall.model.userrelated.Fencer;
import cs203.ftms.overall.model.userrelated.User;
import cs203.ftms.overall.service.chatbot.ChatbotService;
import cs203.ftms.overall.service.tournament.TournamentService;


/**
 * Controller class handling chatbot-related endpoints for the fencing tournament management system.
 * Provides functionality for tournament recommendations, projected points calculations, and winrate statistics.
 * All endpoints require FENCER role authorization.
 */
@RestController
@CrossOrigin
@Validated
@RequestMapping("/api/v1/chatbot")
public class ChatbotController {
    private final ChatbotService chatbotService;
    private final TournamentService tournamentService;


    /**
     * Constructs a ChatbotController with the specified chatbot and tournament services.
     *
     * @param chatbotService The service handling chatbot operations
     * @param tournamentService The service handling tournament-related operations
     */
    @Autowired
    public ChatbotController(ChatbotService chatbotService, TournamentService tournamentService){
        this.chatbotService = chatbotService;
        this.tournamentService = tournamentService;
    }

    /**
     * Retrieves the projected points for a specific event for the authenticated fencer.
     * 
     * @param eid The ID of the event to calculate projected points for
     * @return ResponseEntity containing the projected points if calculation is successful,
     *         or null with BAD_REQUEST status if no points can be calculated
     * @throws ClassCastException if authenticated user is not a Fencer
     */
    @GetMapping("/projected-points/{eid}")
    @PreAuthorize("hasRole('FENCER')")
    public ResponseEntity<Integer> getProjectedPoints(@PathVariable int eid) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Integer projectedPoints = chatbotService.getProjectedPointsEarned(eid, (Fencer) user);
        if (projectedPoints == 0) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(projectedPoints, HttpStatus.OK);
    }
    
    
    /**
     * Retrieves recommended tournaments for the authenticated fencer.
     * This endpoint requires the user to have a FENCER role.
     * 
     * @return ResponseEntity containing a list of CleanTournamentDTO objects representing recommended tournaments
     *         with HTTP status 200 (OK)
     * @throws ClassCastException if the authenticated user is not a Fencer
     * @see CleanTournamentDTO
     * @see Tournament
     */
    @GetMapping("/recommend-tournaments")
    @PreAuthorize("hasRole('FENCER')")
    public ResponseEntity<List<CleanTournamentDTO>> recommendTournaments() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        List<Tournament> tournaments = chatbotService.getRecommendedTournaments((Fencer) user);

        List<CleanTournamentDTO> ctList = new ArrayList<>();
        for (Tournament t : tournaments) {
            ctList.add(tournamentService.getCleanTournamentDTO(t));
        }
        return new ResponseEntity<>(ctList, HttpStatus.OK);

    }

    /**
     * Retrieves expected win rate for an authenticated fencer.
     * 
     * @param eid The ID of the event to calculate win rate for
     * @return ResponseEntity containing win rate statistics as a String
     * @throws AccessDeniedException if user is not authenticated or lacks FENCER role
     */
    @GetMapping("/winrate/{eid}")
    @PreAuthorize("hasRole('FENCER')")
    public ResponseEntity<String> winrate(@PathVariable int eid) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Fencer f = (Fencer) user;
        String s = chatbotService.getWinrate(eid, f);
        return new ResponseEntity<>(s, HttpStatus.OK);
    }
    
}
