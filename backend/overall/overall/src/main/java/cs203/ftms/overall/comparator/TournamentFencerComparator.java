package cs203.ftms.overall.comparator;

import java.util.Comparator;

import cs203.ftms.overall.model.tournamentrelated.TournamentFencer;

/**
 * Comparator for sorting TournamentFencer objects based on tournament rank and fencer points.
 * Used to determine seeding order in tournament events.
 */
public class TournamentFencerComparator implements Comparator<TournamentFencer> {
    
    /**
     * Compares two TournamentFencer objects for ordering.
     * Primary sorting is based on tournament rank (ascending order).
     * If ranks are equal, secondary sorting is based on fencer points (descending order).
     *
     * @param t1 the first TournamentFencer to compare
     * @param t2 the second TournamentFencer to compare
     * @return  a negative integer if t1 should be before t2,
     *          zero if t1 equals t2,
     *          a positive integer if t1 should be after t2.
     *          Returns 0 if either t1 or t2 is null.
     */
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
