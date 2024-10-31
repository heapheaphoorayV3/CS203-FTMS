package cs203.ftms.overall.comparator;

import java.util.Comparator;

import cs203.ftms.overall.model.userrelated.Fencer;

public class FencerPointsComparator implements Comparator<Fencer> {
    @Override
    public int compare(Fencer f1, Fencer f2) {
        return -(f1.getPoints() - f2.getPoints());
    }
}
