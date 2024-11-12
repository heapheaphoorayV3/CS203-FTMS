package cs203.ftms.overall.controller.chatbot;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


@RestController
@CrossOrigin
@Validated
@RequestMapping("/api/v1/chatbot")
public class ChatbotController {
    private final ChatbotService chatbotService;
    private final TournamentService tournamentService;


    @Autowired
    public ChatbotController(ChatbotService chatbotService, TournamentService tournamentService){
        this.chatbotService = chatbotService;
        this.tournamentService = tournamentService;
    }

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
