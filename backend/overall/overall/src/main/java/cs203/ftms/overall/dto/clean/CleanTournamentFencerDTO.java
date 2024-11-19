package cs203.ftms.overall.dto.clean;

/**
 * Data Transfer Object for representing a fencer's participation in a tournament, including details about
 * their club, country, performance in poule, and overall ranking in the tournament event.
 */
public class CleanTournamentFencerDTO {
    private int tournamentFencerId;
    private int fencerId; 
    private String fencerName; 
    private String fencerClub; 
    private String country; 
    private char dominantArm; 
    private int tournamentRank;
    private int eventId;
    private int pouleWins;
    private int poulePoints;
    private int pointsAfterEvent;

    /**
     * Constructs a CleanTournamentFencerDTO with details about the fencer and their performance in the tournament event.
     *
     * @param tournamentFencerId The unique identifier for the fencer in the tournament.
     * @param fencerId           The unique identifier for the fencer.
     * @param fencerName         The name of the fencer.
     * @param fencerClub         The club the fencer represents.
     * @param country            The country of the fencer.
     * @param dominantArm        The fencer's dominant arm ('L' or 'R').
     * @param tournamentRank     The fencer's rank in the tournament.
     * @param eventId            The unique identifier for the event in which the fencer is participating.
     * @param pouleWins          The number of wins the fencer achieved in poule matches.
     * @param poulePoints        The points the fencer earned in poule matches.
     * @param pointsAfterEvent   The fencer's total points after the event.
     */
    public CleanTournamentFencerDTO(int tournamentFencerId, int fencerId, String fencerName, String fencerClub, String country,
                                    char dominantArm, int tournamentRank, int eventId, int pouleWins, int poulePoints, int pointsAfterEvent) {
        this.tournamentFencerId = tournamentFencerId;
        this.fencerId = fencerId;
        this.fencerName = fencerName;
        this.fencerClub = fencerClub;
        this.country = country;
        this.dominantArm = dominantArm;
        this.tournamentRank = tournamentRank;
        this.eventId = eventId;
        this.pouleWins = pouleWins;
        this.poulePoints = poulePoints;
        this.pointsAfterEvent = pointsAfterEvent;
    }

    /**
     * Gets the tournament fencer ID.
     *
     * @return The unique identifier for the tournament fencer.
     */
    public int getTournamentFencerId() {
        return tournamentFencerId;
    }

    /**
     * Sets the tournament fencer ID.
     *
     * @param tournamentFencerId The unique identifier to set for the tournament fencer.
     */
    public void setTournamentFencerId(int tournamentFencerId) {
        this.tournamentFencerId = tournamentFencerId;
    }

    /**
     * Gets the fencer ID.
     *
     * @return The unique identifier of the fencer.
     */
    public int getFencerId() {
        return fencerId;
    }

    /**
     * Sets the fencer ID.
     *
     * @param fencerId The unique identifier to set for the fencer.
     */
    public void setFencerId(int fencerId) {
        this.fencerId = fencerId;
    }

    /**
     * Gets the fencer's name.
     *
     * @return The name of the fencer.
     */
    public String getFencerName() {
        return fencerName;
    }

    /**
     * Sets the fencer's name.
     *
     * @param fencerName The name to set for the fencer.
     */
    public void setFencerName(String fencerName) {
        this.fencerName = fencerName;
    }

    /**
     * Gets the club of the fencer.
     *
     * @return The fencer's club.
     */
    public String getFencerClub() {
        return fencerClub;
    }

    /**
     * Sets the club of the fencer.
     *
     * @param fencerClub The club to set for the fencer.
     */
    public void setFencerClub(String fencerClub) {
        this.fencerClub = fencerClub;
    }

    /**
     * Gets the fencer's country.
     *
     * @return The country of the fencer.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the fencer's country.
     *
     * @param country The country to set for the fencer.
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Gets the fencer's dominant arm.
     *
     * @return The dominant arm ('L' or 'R') of the fencer.
     */
    public char getDominantArm() {
        return dominantArm;
    }

    /**
     * Sets the fencer's dominant arm.
     *
     * @param dominantArm The dominant arm ('L' or 'R') to set for the fencer.
     */
    public void setDominantArm(char dominantArm) {
        this.dominantArm = dominantArm;
    }

    /**
     * Gets the tournament rank of the fencer.
     *
     * @return The rank of the fencer in the tournament.
     */
    public int getTournamentRank() {
        return tournamentRank;
    }

    /**
     * Sets the tournament rank of the fencer.
     *
     * @param tournamentRank The rank to set for the fencer in the tournament.
     */
    public void setTournamentRank(int tournamentRank) {
        this.tournamentRank = tournamentRank;
    }

    /**
     * Gets the event ID.
     *
     * @return The unique identifier of the event in which the fencer participated.
     */
    public int getEventId() {
        return eventId;
    }

    /**
     * Sets the event ID.
     *
     * @param eventId The unique identifier of the event to set for the fencer.
     */
    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    /**
     * Gets the number of poule wins for the fencer.
     *
     * @return The number of wins in poule matches.
     */
    public int getPouleWins() {
        return pouleWins;
    }

    /**
     * Sets the number of poule wins for the fencer.
     *
     * @param pouleWins The number of wins in poule matches to set.
     */
    public void setPouleWins(int pouleWins) {
        this.pouleWins = pouleWins;
    }

    /**
     * Gets the poule points for the fencer.
     *
     * @return The points earned by the fencer in poule matches.
     */
    public int getPoulePoints() {
        return poulePoints;
    }

    /**
     * Sets the poule points for the fencer.
     *
     * @param poulePoints The points to set for the fencer in poule matches.
     */
    public void setPoulePoints(int poulePoints) {
        this.poulePoints = poulePoints;
    }

    /**
     * Gets the fencer's points after the event.
     *
     * @return The fencer's points after participating in the event.
     */
    public int getPointsAfterEvent() {
        return pointsAfterEvent;
    }

    /**
     * Sets the fencer's points after the event.
     *
     * @param pointsAfterEvent The points to set for the fencer after the event.
     */
    public void setPointsAfterEvent(int pointsAfterEvent) {
        this.pointsAfterEvent = pointsAfterEvent;
    }
}
