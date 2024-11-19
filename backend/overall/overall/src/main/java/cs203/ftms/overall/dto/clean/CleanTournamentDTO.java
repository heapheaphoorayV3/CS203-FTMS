package cs203.ftms.overall.dto.clean;

import java.time.LocalDate;
import java.util.List;

/**
 * Data Transfer Object for representing a tournament with relevant details such as
 * tournament name, organizer, schedule, location, description, rules, events, difficulty, and advancement rate.
 */
public class CleanTournamentDTO {
    private int id;
    private String name;
    private String organiserName;
    private LocalDate signupEndDate;
    private LocalDate startDate;
    private LocalDate endDate;
    private String location;
    private String description;
    private String rules;
    private List<CleanEventDTO> events;
    private char difficulty;
    private int advancementRate;

    /**
     * Constructs a CleanTournamentDTO with the specified tournament details.
     *
     * @param id              The unique identifier of the tournament.
     * @param name            The name of the tournament.
     * @param organiserName   The name of the organiser.
     * @param signupEndTime   The date by which participants must sign up.
     * @param startDate       The start date of the tournament.
     * @param endDate         The end date of the tournament.
     * @param location        The location of the tournament.
     * @param description     A description of the tournament.
     * @param rules           The rules governing the tournament.
     * @param events          A list of events associated with the tournament.
     * @param difficulty      The difficulty level of the tournament ('B', 'I', or 'A').
     * @param advancementRate The advancement rate for the tournament.
     */
    public CleanTournamentDTO(int id, String name, String organiserName, LocalDate signupEndTime, LocalDate startDate, LocalDate endDate,
                              String location, String description, String rules, List<CleanEventDTO> events, char difficulty, int advancementRate) {
        this.id = id;
        this.name = name;
        this.organiserName = organiserName;
        this.signupEndDate = signupEndTime;
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
        this.description = description;
        this.rules = rules;
        this.events = events;
        this.difficulty = difficulty;
        this.advancementRate = advancementRate;
    }

    /**
     * Gets the tournament name.
     *
     * @return The name of the tournament.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the tournament name.
     *
     * @param name The name to set for the tournament.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the organizer's name.
     *
     * @return The name of the organizer.
     */
    public String getOrganiserName() {
        return organiserName;
    }

    /**
     * Sets the organizer's name.
     *
     * @param organiserName The name to set for the organizer.
     */
    public void setOrganiserName(String organiserName) {
        this.organiserName = organiserName;
    }

    /**
     * Gets the sign-up end date.
     *
     * @return The date by which participants must sign up.
     */
    public LocalDate getSignupEndDate() {
        return signupEndDate;
    }

    /**
     * Sets the sign-up end date.
     *
     * @param signupEndDate The date to set as the sign-up end date.
     */
    public void setSignupEndDate(LocalDate signupEndDate) {
        this.signupEndDate = signupEndDate;
    }

    /**
     * Gets the start date of the tournament.
     *
     * @return The tournament start date.
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Sets the start date of the tournament.
     *
     * @param startDate The start date to set for the tournament.
     */
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    /**
     * Gets the end date of the tournament.
     *
     * @return The tournament end date.
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Sets the end date of the tournament.
     *
     * @param endDate The end date to set for the tournament.
     */
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    /**
     * Gets the tournament location.
     *
     * @return The location of the tournament.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the tournament location.
     *
     * @param location The location to set for the tournament.
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Gets the tournament description.
     *
     * @return The description of the tournament.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the tournament description.
     *
     * @param description The description to set for the tournament.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the tournament rules.
     *
     * @return The rules governing the tournament.
     */
    public String getRules() {
        return rules;
    }

    /**
     * Sets the tournament rules.
     *
     * @param rules The rules to set for the tournament.
     */
    public void setRules(String rules) {
        this.rules = rules;
    }

    /**
     * Gets the list of events associated with the tournament.
     *
     * @return The list of events.
     */
    public List<CleanEventDTO> getEvents() {
        return events;
    }

    /**
     * Sets the list of events associated with the tournament.
     *
     * @param events The list of events to set.
     */
    public void setEvents(List<CleanEventDTO> events) {
        this.events = events;
    }

    /**
     * Gets the unique identifier of the tournament.
     *
     * @return The tournament ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the tournament.
     *
     * @param id The ID to set for the tournament.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the difficulty level of the tournament.
     *
     * @return The difficulty level ('B', 'I', or 'A').
     */
    public char getDifficulty() {
        return difficulty;
    }

    /**
     * Sets the difficulty level of the tournament.
     *
     * @param difficulty The difficulty level to set ('B', 'I', or 'A').
     */
    public void setDifficulty(char difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * Gets the advancement rate of the tournament.
     *
     * @return The advancement rate.
     */
    public int getAdvancementRate() {
        return advancementRate;
    }

    /**
     * Sets the advancement rate of the tournament.
     *
     * @param advancementRate The advancement rate to set.
     */
    public void setAdvancementRate(int advancementRate) {
        this.advancementRate = advancementRate;
    }
}
