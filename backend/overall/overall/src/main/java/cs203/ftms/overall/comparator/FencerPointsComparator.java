package cs203.ftms.overall.comparator;

import java.util.Comparator;

import cs203.ftms.overall.model.userrelated.Fencer;

/**
 * Comparator implementation for comparing Fencer objects based on their points.
 * Sorts fencers in descending order of points (highest points first).
 */
public class FencerPointsComparator implements Comparator<Fencer> {
    @Override
    public int compare(Fencer f1, Fencer f2) {
        return -(f1.getPoints() - f2.getPoints());
    }
}
