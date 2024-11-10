package cs203.ftms.overall.service.match;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cs203.ftms.overall.dto.clean.CleanMatchDTO;
import cs203.ftms.overall.dto.clean.CleanTournamentFencerDTO;
import cs203.ftms.overall.exception.EntityDoesNotExistException;
import cs203.ftms.overall.model.tournamentrelated.Match;
import cs203.ftms.overall.model.tournamentrelated.TournamentFencer;
import cs203.ftms.overall.repository.tournamentrelated.TournamentFencerRepository;
import cs203.ftms.overall.service.event.EventService;

@Service
public class MatchService {
    private final TournamentFencerRepository tournamentFencerRepository;
    private final EventService eventService;

    @Autowired
    public MatchService(TournamentFencerRepository tournamentFencerRepository, EventService eventService) {
        this.tournamentFencerRepository = tournamentFencerRepository;
        this.eventService = eventService;
    }

    public List<TournamentFencer> getFencersInMatch(Match m) {
        List<TournamentFencer> fencerList = new ArrayList<>(2);
        
        addFencerToList(fencerList, m.getFencer1());
        addFencerToList(fencerList, m.getFencer2());
        
        return fencerList;
    }

    public TournamentFencer getFencer1(Match match) {
        TournamentFencer fencer = tournamentFencerRepository.findById(match.getFencer1())
        .orElseThrow(() -> new EntityDoesNotExistException("Tournament Fencer does not exist!"));
        return fencer;
    } 

    public TournamentFencer getFencer2(Match match) {
        TournamentFencer fencer = tournamentFencerRepository.findById(match.getFencer2())
        .orElseThrow(() -> new EntityDoesNotExistException("Tournament Fencer does not exist!"));
        return fencer;
    } 
    
    // helper for getFencersInMatch
    private void addFencerToList(List<TournamentFencer> fencerList, int fencerId) {
        if (fencerId != -1) {
            TournamentFencer fencer = tournamentFencerRepository.findById(fencerId)
            .orElseThrow(() -> new EntityDoesNotExistException("Tournament Fencer does not exist!"));
            fencerList.add(fencer);
        }
    }

    public CleanMatchDTO getCleanMatchDTO(Match m, char matchType) {
        List<TournamentFencer> fencerList = getFencersInMatch(m);
        
        CleanTournamentFencerDTO ctf1 = fencerList.size() > 0 ? eventService.getCleanTournamentFencerDTO(fencerList.get(0)) : null;
        CleanTournamentFencerDTO ctf2 = fencerList.size() > 1 ? eventService.getCleanTournamentFencerDTO(fencerList.get(1)) : null;

        return new CleanMatchDTO(
            m.getId(),
            ctf1,
            m.getScore1(),
            ctf2,
            m.getScore2(),
            m.getWinner(),
            matchType
        );
    }  

    
}
