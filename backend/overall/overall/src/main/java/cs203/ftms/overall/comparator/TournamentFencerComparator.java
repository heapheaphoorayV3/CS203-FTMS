package cs203.ftms.overall.comparator;

import java.util.Comparator;

import cs203.ftms.overall.model.tournamentrelated.TournamentFencer;

public class TournamentFencerComparator implements Comparator<TournamentFencer> {
    public int compare(TournamentFencer t1, TournamentFencer t2) {
        if(t1 == null || t2 == null){
            return 0;
        }
        if (t1.getTournamentRank() != t2.getTournamentRank()) { // at start, all 0, so no diff
            return t1.getTournamentRank() - t2.getTournamentRank();
        }
        
        return -(t1.getFencer().getPoints() - t2.getFencer().getPoints());
    }
}
