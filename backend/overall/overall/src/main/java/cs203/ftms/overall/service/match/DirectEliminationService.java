package cs203.ftms.overall.service.match;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cs203.ftms.overall.comparator.TournamentFencerComparator;
import cs203.ftms.overall.comparator.TournamentFencerPouleComparator;
import cs203.ftms.overall.datastructure.CustomMatchHeap;
import cs203.ftms.overall.dto.DirectEliminationBracketDTO;
import cs203.ftms.overall.dto.DirectEliminationBracketFencerDTO;
import cs203.ftms.overall.dto.UpdateDirectEliminationMatchDTO;
import cs203.ftms.overall.exception.EntityDoesNotExistException;
import cs203.ftms.overall.exception.PouleMatchesNotDoneException;
import cs203.ftms.overall.model.tournamentrelated.DirectEliminationMatch;
import cs203.ftms.overall.model.tournamentrelated.Event;
import cs203.ftms.overall.model.tournamentrelated.Match;
import cs203.ftms.overall.model.tournamentrelated.Poule;
import cs203.ftms.overall.model.tournamentrelated.PouleMatch;
import cs203.ftms.overall.model.tournamentrelated.TournamentFencer;
import cs203.ftms.overall.repository.tournamentrelated.DirectEliminationMatchRepository;
import cs203.ftms.overall.repository.tournamentrelated.MatchRepository;
import cs203.ftms.overall.repository.tournamentrelated.TournamentFencerRepository;
import cs203.ftms.overall.service.event.EventService;
import jakarta.transaction.Transactional;

@Service
public class DirectEliminationService {
    private final EventService eventService;
    private final PouleService pouleService;
    private final MatchService matchService;
    private final MatchRepository matchRepository;
    private final TournamentFencerRepository tournamentFencerRepository;
    private final DirectEliminationMatchRepository directEliminationMatchRepository;

    @Autowired
    public DirectEliminationService(EventService eventService, PouleService pouleService, MatchService matchService, MatchRepository matchRepository, TournamentFencerRepository tournamentFencerRepository, DirectEliminationMatchRepository directEliminationMatchRepository) {
        this.eventService = eventService;
        this.pouleService = pouleService;
        this.matchService = matchService;
        this.matchRepository = matchRepository;
        this.tournamentFencerRepository = tournamentFencerRepository;
        this.directEliminationMatchRepository = directEliminationMatchRepository;
    }

    public int noOfDEMatches(Event event) {
        int fencersAdvanced = calculateFencersAdvanced(event);
        int nearestPO2 = nearestLowerPowerOf2(fencersAdvanced);
        if(nearestPO2 == fencersAdvanced) {
            return calculateTotalMatchCount(nearestPO2/2);
        }
        return calculateTotalMatchCount(nearestPO2);
    }
    
    // helper for noOfDEMatches
    private int calculateFencersAdvanced(Event event) {
        int advancementRate = event.getTournament().getAdvancementRate();
        return (int) (event.getParticipantCount() * (advancementRate / 100.0));
    }
    
    // helper for noOfDEMatches
    private static int nearestLowerPowerOf2(int a) {
        int b = 1;
        while (b < a) {
            b = b << 1;
        }
        return b > a ? b >> 1 : b;
    }
    
    // helper for noOfDEMatches
    private int calculateTotalMatchCount(int nearestPO2) {
        int totalMatchCount = 0;
        while (nearestPO2 > 0) {
            totalMatchCount += nearestPO2;
            nearestPO2 /= 2;
        }
        return totalMatchCount;
    }

    @Transactional
    public void createAllDEMatches(int eid) {
        Event event = eventService.getEvent(eid);
        // check whether all poule matches are done
        if (event.getPoules().isEmpty()) {
            throw new PouleMatchesNotDoneException("Poules not created yet!");
        }

        for (Poule poule : event.getPoules()) {
            for (PouleMatch pouleMatch : poule.getPouleMatches()) {
                if (pouleMatch.getWinner() == -1) {
                    throw new PouleMatchesNotDoneException("Poule matches can't end in a draw!");
                }
            }
        }
        CustomMatchHeap heap = createAndSaveMatches(event);
        Map<String, List<TournamentFencer>> mappings = pouleService.getFencersAfterPoules(event);
        List<TournamentFencer> fencers = getSortedFencers(mappings);
        int bypassSize = mappings.get("Bypass").size();

        populateInitialDEMatches(fencers, heap, bypassSize);
        // printHeapDetails(heap);
    }

    // helper for createAllDEMatches
    private CustomMatchHeap createAndSaveMatches(Event event) {
        int matchCount = noOfDEMatches(event);
        CustomMatchHeap heap = new CustomMatchHeap();
        for (int i = 0; i < matchCount; i++) {
            Match match = heap.insert(new DirectEliminationMatch(event));
            matchRepository.save(match);
        }
        return heap;
    }

    // helper for createAllDEMatches
    private List<TournamentFencer> getSortedFencers(Map<String, List<TournamentFencer>> mappings) {
        List<TournamentFencer> fencers = new ArrayList<>();
        fencers.addAll(mappings.get("Bypass"));
        fencers.addAll(mappings.get("FenceOff"));
        fencers.sort(new TournamentFencerPouleComparator());
        return fencers;
    }

    // helper for createAllDEMatches
    // private void printHeapDetails(CustomMatchHeap heap) {
    //     System.out.println();
    //     heap.printHeap();
    //     int lastLevel = (int) (Math.log(heap.size() + 1) / Math.log(2)) - 1;
    //     System.out.println(lastLevel);
    //     List<Match> matches = heap.getLevel(lastLevel);
    //     System.out.println();
    //     for (Match match : matches) {
    //         System.out.println(match.getId());
    //     }
    // }

    @Transactional
    public void populateInitialDEMatches(List<TournamentFencer> tfencers, CustomMatchHeap heap, int bypassSize) {
        int lastLevel = (int) (Math.log(heap.size() + 1) / Math.log(2)) - 1;
        List<Match> matches = heap.getLevel(lastLevel);
        int[] matchArray = generateMatchArray(lastLevel, tfencers.size());

        int matchCount = 0;
        for (int i = 0; i < matchArray.length; i += 2) {
            DirectEliminationMatch dm = (DirectEliminationMatch) matches.get(matchCount);
            TournamentFencer tf1 = tfencers.get(matchArray[i] - 1);
            dm.setFencer1(tf1.getId());
            tf1.addMatch(dm);
            if (matchArray[i + 1] != 0) {
                TournamentFencer tf2 = tfencers.get(matchArray[i + 1] - 1);
                dm.setFencer2(tf2.getId());
                tf2.addMatch(dm);
                tournamentFencerRepository.save(tf2);
            } else {
                updateNextMatch(tf1, dm);
            }
            tournamentFencerRepository.save(tf1);
            matchRepository.save(dm);
            matchCount++;
        }
    }

    // helper for populateInitialDEMatches
    private int[] previousRound(int[] currentRound, int totalFencers) {
        int size = currentRound.length * 2;
        int[] result = new int[size];
        for (int i = 0; i < size; i += 2) {
            result[i] = currentRound[i / 2];
            result[i + 1] = (size - currentRound[i / 2] < totalFencers) ? size - currentRound[i / 2] + 1 : 0;
        }
        return result;
    }

    // helper for populateInitialDEMatches
    private int[] generateMatchArray(int lastLevel, int totalFencers) {
        int[] matchArray = new int[]{1, 2};
        for (int i = 0; i < lastLevel; i++) {
            matchArray = previousRound(matchArray, totalFencers);
        }
        return matchArray;
    }

    // helper for populateInitialDEMatches
    private void updateNextMatch(TournamentFencer tf1, DirectEliminationMatch dm) {
        int nextMatchId = dm.getNextMatchId();
        DirectEliminationMatch nextMatch = (DirectEliminationMatch) matchRepository.findById(nextMatchId).orElseThrow(() -> new EntityDoesNotExistException("Match does not exist!"));
        tf1.addMatch(nextMatch);
        if (nextMatch.getFencer1() == -1) {
            nextMatch.setFencer1(tf1.getId());
        } else {
            nextMatch.setFencer2(tf1.getId());
        }
        matchRepository.save(nextMatch);
    }

    @Transactional
    public void updateDEMatch(int eid, UpdateDirectEliminationMatchDTO dto) {
        DirectEliminationMatch dm = getDirectEliminationMatch(dto.getMatchId(), eid);

        dm.setScore1(dto.getScore1());
        dm.setScore2(dto.getScore2());

        TournamentFencer fencer1 = matchService.getFencer1(dm);
        TournamentFencer fencer2 = matchService.getFencer2(dm);

        updateMatchWinner(dm, fencer1, fencer2, dto.getScore1(), dto.getScore2());
        matchRepository.save(dm);

        TournamentFencer winner = tournamentFencerRepository.findById(dm.getWinner()).orElseThrow(() -> new EntityDoesNotExistException("Tournament Fencer does not exist!"));
        updateNextMatchWithWinner(dm, winner, fencer1, fencer2);

        tournamentFencerRepository.save(winner);
    }

    // helper for updateDEMatch
    private DirectEliminationMatch getDirectEliminationMatch(int matchId, int eid) {
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new EntityDoesNotExistException("Match does not exist!"));
        if (match.getEvent().getId() != eid) {
            throw new EntityDoesNotExistException("Match does not exist in this event!");
        }
        return (DirectEliminationMatch) match;
    }

    // helper for updateDEMatch
    private void updateMatchWinner(DirectEliminationMatch dm, TournamentFencer fencer1, TournamentFencer fencer2, int score1, int score2) {
        if (score1 > score2) {
            dm.setWinner(fencer1.getId());
            swapRanksIfNecessary(fencer1, fencer2);
        } else {
            dm.setWinner(fencer2.getId());
            swapRanksIfNecessary(fencer2, fencer1);
        }
    }

    // helper for updateDEMatch
    private void swapRanksIfNecessary(TournamentFencer higherRanked, TournamentFencer lowerRanked) {
        if (higherRanked.getTournamentRank() > lowerRanked.getTournamentRank()) {
            int temp = higherRanked.getTournamentRank();
            higherRanked.setTournamentRank(lowerRanked.getTournamentRank());
            lowerRanked.setTournamentRank(temp);
        }
    }

    // helper for updateDEMatch
    private void updateNextMatchWithWinner(DirectEliminationMatch dm, TournamentFencer winner, TournamentFencer fencer1, TournamentFencer fencer2) {
        if (dm.getRoundOf() != 2) {
            DirectEliminationMatch nextMatch = (DirectEliminationMatch) matchRepository.findById(dm.getNextMatchId()).orElseThrow(() -> new EntityDoesNotExistException("Match does not exist!"));
            if (nextMatch.getFencer1() == -1 || nextMatch.getFencer1() == fencer1.getId() || nextMatch.getFencer1() == fencer2.getId()) {
                nextMatch.setFencer1(winner.getId());
            } else if (nextMatch.getFencer2() == -1 || nextMatch.getFencer2() == fencer1.getId() || nextMatch.getFencer2() == fencer2.getId()) {
                nextMatch.setFencer2(winner.getId());
            }
            matchRepository.save(nextMatch);
            winner.addMatch(nextMatch);
        }
    }

    public DirectEliminationBracketDTO getDirectEliminationBracketDTO(DirectEliminationMatch m) {
        String roundText = getRoundText(m.getRoundOf());
        DirectEliminationBracketFencerDTO[] fencersDTO = getFencersDTO(m);
        return new DirectEliminationBracketDTO(m.getId(), roundText, m.getNextMatchId(), roundText, null, null, fencersDTO);
    }

    // helper for getDirectEliminationBracketDTO
    private String getRoundText(int roundOf) {
        return switch (roundOf) {
            case 2 -> "Finals";
            case 4 -> "Semi-Finals";
            case 8 -> "Quarter-Finals";
            default -> String.format("Top %d", roundOf);
        };
    }

    // helper for getDirectEliminationBracketDTO
    private DirectEliminationBracketFencerDTO[] getFencersDTO(DirectEliminationMatch m) {
        DirectEliminationBracketFencerDTO[] fencersDTO = new DirectEliminationBracketFencerDTO[matchService.getFencersInMatch(m).size()];
        int fencerCount = 0;
        for (TournamentFencer tf : matchService.getFencersInMatch(m)) {
            int score = (fencerCount == 0) ? m.getScore1() : m.getScore2();
            fencersDTO[fencerCount] = getDirectEliminationBracketFencerDTO(tf, m.getWinner(), score);
            fencerCount++;
        }
        return fencersDTO;
    }

    public DirectEliminationBracketFencerDTO getDirectEliminationBracketFencerDTO(TournamentFencer tf, int winner, int score) {
        boolean isWinner = (winner == tf.getId());
        return new DirectEliminationBracketFencerDTO(tf.getId(), String.format("%d", score), isWinner, null, tf.getFencer().getName());
    }

    public List<DirectEliminationBracketDTO> generateDirectEliminationBracketDTOs(int eid) {
        Event event = eventService.getEvent(eid);
        List<DirectEliminationMatch> matches = directEliminationMatchRepository.findByEvent(event);
        return matches.stream()
                .map(this::getDirectEliminationBracketDTO)
                .collect(Collectors.toList());
    }

    public List<TournamentFencer> getTournamentRanks(int eid) {
        Event event = eventService.getEvent(eid);
        List<TournamentFencer> tfs = new ArrayList<>(event.getFencers());
        tfs.sort(new TournamentFencerComparator());
        return tfs;
    }
}
