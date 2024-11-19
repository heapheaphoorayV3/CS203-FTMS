package cs203.ftms.overall.comparator;

import java.util.Comparator;

import cs203.ftms.overall.model.tournamentrelated.TournamentFencer;

/**
 * Comparator for sorting TournamentFencer objects based on poule performance metrics.
 * Used to determine rankings after poule matches are completed.
 * Sorts fencers first by number of poule wins (descending), then by poule points (descending).
 */
public class TournamentFencerPouleComparator implements Comparator<TournamentFencer> {
    public int compare(TournamentFencer t1, TournamentFencer t2) {
        int windiff = t1.getPouleWins() - t2.getPouleWins(); 
        if (windiff != 0) return -windiff; 
        int poulespointdiff = t1.getPoulePoints() - t2.getPoulePoints(); 
        if (poulespointdiff != 0) return -poulespointdiff;
        int internationalpointdiff = t1.getFencer().getPoints() - t2.getFencer().getPoints();
        return -internationalpointdiff; 
    }
}
