package cs203.ftms.overall.service.chatbot;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cs203.ftms.overall.comparator.TournamentFencerComparator;
import cs203.ftms.overall.exception.EntityDoesNotExistException;
import cs203.ftms.overall.model.tournamentrelated.Event;
import cs203.ftms.overall.model.tournamentrelated.Tournament;
import cs203.ftms.overall.model.tournamentrelated.TournamentFencer;
import cs203.ftms.overall.model.userrelated.Fencer;
import cs203.ftms.overall.repository.tournamentrelated.EventRepository;
import cs203.ftms.overall.repository.tournamentrelated.TournamentRepository;

/**
 * Service class for chatbot-related functionalities.
 * Provides methods to calculate projected points, expected rank,
 * win rates, and recommend suitable tournaments for fencers.
 */
@Service
public class ChatbotService {
    private final TournamentRepository tournamentRepository;
    private final EventRepository eventRepository;

    @Autowired
    public ChatbotService(TournamentRepository tournamentRepository, EventRepository eventRepository) {
        this.tournamentRepository = tournamentRepository;
        this.eventRepository = eventRepository;
    }

    /**
     * Calculates the projected points a fencer can earn in a specific event.
     *
     * @param eid the ID of the event
     * @param f the fencer participating in the event
     * @return the projected points earned by the fencer
     * @throws EntityDoesNotExistException if the event does not exist
     */
    public int getProjectedPointsEarned(int eid, Fencer f) {
        Event e = eventRepository.findById(eid).orElseThrow(() -> new EntityDoesNotExistException("Event does not exist!"));
        int expectedRank = expectedRank(e, f);
        double numOfFencersThatGetPoints = e.getFencers().size() * 0.8;
        int totalPoints = getPointsForDistribution(e.getFencers());
        int projectedPoints = calculatePoints(expectedRank, totalPoints, (int) numOfFencersThatGetPoints);
        return projectedPoints;
    }

    /**
     * Calculates the expected rank of a fencer in a specific event.
     *
     * @param e the event to evaluate
     * @param f the fencer whose rank is being calculated
     * @return the expected rank of the fencer
     */
    public int expectedRank(Event e, Fencer f) {
        Set<TournamentFencer> t = e.getFencers();
        List<TournamentFencer> fencers = new ArrayList<>(t);
        Collections.sort(fencers, new TournamentFencerComparator());
        for (TournamentFencer tf : fencers) {
            if (tf.getFencer().getPoints() <= f.getPoints()) {
                return fencers.indexOf(tf) + 1;
            }
        }
        return fencers.size() + 1;
    }

    /**
     * Calculates the points for distribution in an event based on participating fencers.
     *
     * @param tfencers the set of fencers participating in the event
     * @return the total points to be distributed
     */
    public int getPointsForDistribution(Set<TournamentFencer> tfencers) {
        int total = 0;
        for (TournamentFencer f : tfencers) {
            total += f.getFencer().getPoints();
        }
        return total / 5;
    }

    /**
     * Calculates the points earned by a fencer based on rank and distribution.
     *
     * @param rank the rank of the fencer
     * @param totalPoints the total points distributed in the event
     * @param totalFencers the total number of fencers earning points
     * @return the points earned by the fencer
     */
    public int calculatePoints(int rank, int totalPoints, int totalFencers) {
        double numerator = totalPoints * Math.pow(totalFencers - rank + 1, 2);
        double denominator = sumOfPowers(totalFencers, 2);
        return (int) (numerator / denominator);
    }

    private static double sumOfPowers(int n, int exponent) {
        double sum = 0;
        for (int i = 1; i <= n; i++) {
            sum += Math.pow(i, exponent);
        }
        return sum;
    }

    /**
     * Recommends tournaments suitable for a fencer based on their experience, gender, and weapon.
     *
     * @param f the fencer for whom tournaments are recommended
     * @return a list of tournaments suitable for the fencer
     */
    public List<Tournament> getRecommendedTournaments(Fencer f) {
        char weapon = f.getWeapon();
        char gender = f.getGender();
        int experience = LocalDate.now().getYear() - f.getDebutYear();
        return tournamentRepository.findAll().stream()
                .filter(t -> isTournamentSuitable(t, experience))
                .filter(t -> t.getEvents().stream()
                .anyMatch(e -> isEventSuitable(e, gender, weapon, f)))
                .collect(Collectors.toList());
    }

    private boolean isTournamentSuitable(Tournament t, int experience) {
        if (experience > 5) {
            return true;
        } else if (experience > 3) {
            return t.getDifficulty() != 'A';
        }
        return t.getDifficulty() == 'B';
    }

    private boolean isEventSuitable(Event e, char gender, char weapon, Fencer f) {
        if (calculateWinrate(e.getId(), f) == 3) {
            return e.getTournament().getDifficulty() == 'B';
        }
        return e.getGender() == gender && e.getWeapon() == weapon && calculateWinrate(e.getId(), f) < 3;
    }

    /**
     * Provides a win rate message for a fencer in a specific event.
     *
     * @param eid the ID of the event
     * @param f the fencer participating in the event
     * @return a string message indicating the win rate
     */
    public String getWinrate(int eid, Fencer f) {
        int winrate = calculateWinrate(eid, f);
        return switch (winrate) {
            case 1 -> "High chance of winning!";
            case 2 -> "Good chance of winning!";
            default -> "It will be a tough fight!";
        };
    }

    /**
     * Calculates the win rate category for a fencer in a specific event.
     *
     * @param eid the ID of the event
     * @param f the fencer participating in the event
     * @return an integer representing the win rate category:
     *         1 (high chance of winning), 2 (good chance), or 3 (tough fight)
     * @throws EntityDoesNotExistException if the event does not exist
     */
    public int calculateWinrate(int eid, Fencer f) {
        int ifFencerInEvent = 1;

        Event e = eventRepository.findById(eid).orElseThrow(() -> new EntityDoesNotExistException("Event does not exist!"));

        for (TournamentFencer tf : e.getFencers()) {
            if (tf.getFencer().getId() == f.getId()) {
                ifFencerInEvent = 0;
            }
        }
        int expectedRank = expectedRank(e, f);
        int totalFencers = e.getFencers().size() + ifFencerInEvent;
        if (totalFencers == 1 || expectedRank <= totalFencers / 10) {
            return 1;
        } else if (expectedRank <= totalFencers / 2) {
            return 2;
        }
        return 3;
    }
}
