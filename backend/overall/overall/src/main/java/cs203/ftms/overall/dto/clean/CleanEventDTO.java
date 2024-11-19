package cs203.ftms.overall.dto.clean;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Data Transfer Object for representing an event with only essential details.
 * This DTO includes information about the event, such as its tournament,
 * participant details, date, and timing.
 */
public class CleanEventDTO {
    private int id;
    private char gender;
    private char weapon;
    private String tournamentName;
    private List<CleanFencerDTO> fencers;
    private int minParticipants;
    private int participantCount;
    private LocalDate eventDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate signupEndDate;
    private boolean isOver;
    
    /**
     * Constructs a new CleanEventDTO with the specified details.
     *
     * @param id The unique identifier of the event.
     * @param gender The gender category of the event.
     * @param weapon The weapon type used in the event.
     * @param tournamentName The name of the tournament.
     * @param fencers The list of fencers participating in the event.
     * @param minParticipants The minimum number of participants required for the event.
     * @param participantCount The current number of participants in the event.
     * @param eventDate The date of the event.
     * @param startTime The start time of the event.
     * @param endTime The end time of the event.
     * @param signupEndDate The sign-up end date for the event.
     * @param isOver The status of the event.
     */
    public CleanEventDTO(int id, char gender, char weapon, String tournamentName, List<CleanFencerDTO> fencers, int minParticipants,
            int participantCount, LocalDate eventDate, LocalTime startTime, LocalTime endTime, LocalDate signupEndDate, boolean isOver) {
        this.id = id;
        this.gender = gender; 
        this.weapon = weapon; 
        this.tournamentName = tournamentName;
        this.fencers = fencers;
        this.minParticipants = minParticipants;
        this.participantCount = participantCount;
        this.eventDate = eventDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.signupEndDate = signupEndDate;
        this.isOver = isOver;
    }

    /**
     * Gets the name of the tournament associated with this event.
     *
     * @return The tournament name.
     */
    public String getTournamentName() {
        return tournamentName;
    }

    /**
     * Sets the name of the tournament associated with this event.
     *
     * @param tournamentName The tournament name to set.
     */
    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }

    /**
     * Gets the list of fencers participating in this event.
     *
     * @return The list of fencers.
     */
    public List<CleanFencerDTO> getFencers() {
        return fencers;
    }

    /**
     * Sets the list of fencers participating in this event.
     *
     * @param fencers The list of fencers to set.
     */
    public void setFencers(List<CleanFencerDTO> fencers) {
        this.fencers = fencers;
    }

    /**
     * Gets the minimum number of participants required for this event.
     *
     * @return The minimum number of participants.
     */
    public int getMinParticipants() {
        return minParticipants;
    }

    /**
     * Sets the minimum number of participants required for this event.
     *
     * @param minParticipants The minimum number of participants to set.
     */
    public void setMinParticipants(int minParticipants) {
        this.minParticipants = minParticipants;
    }

    /**
     * Gets the current number of participants in the event.
     *
     * @return The participant count.
     */
    public int getParticipantCount() {
        return participantCount;
    }

    /**
     * Sets the current number of participants in the event.
     *
     * @param participantCount The participant count to set.
     */
    public void setParticipantCount(int participantCount) {
        this.participantCount = participantCount;
    }

    /**
     * Gets the date of the event.
     *
     * @return The event date.
     */
    public LocalDate getEventDate() {
        return eventDate;
    }

    /**
     * Sets the date of the event.
     *
     * @param date The event date to set.
     */
    public void setEventDate(LocalDate date) {
        this.eventDate = date;
    }

    /**
     * Gets the start time of the event.
     *
     * @return The start time.
     */
    public LocalTime getStartTime() {
        return startTime;
    }

    /**
     * Sets the start time of the event.
     *
     * @param startTime The start time to set.
     */
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    /**
     * Gets the end time of the event.
     *
     * @return The end time.
     */
    public LocalTime getEndTime() {
        return endTime;
    }

    /**
     * Sets the end time of the event.
     *
     * @param endTime The end time to set.
     */
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    /**
     * Gets the unique identifier of the event.
     *
     * @return The event ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the event.
     *
     * @param id The event ID to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the gender category of the event.
     *
     * @return The gender category.
     */
    public char getGender() {
        return gender;
    }

    /**
     * Sets the gender category of the event.
     *
     * @param gender The gender category to set.
     */
    public void setGender(char gender) {
        this.gender = gender;
    }

    /**
     * Gets the weapon type used in the event.
     *
     * @return The weapon type.
     */
    public char getWeapon() {
        return weapon;
    }

    /**
     * Sets the weapon type used in the event.
     *
     * @param weapon The weapon type to set.
     */
    public void setWeapon(char weapon) {
        this.weapon = weapon;
    }

    /**
     * Gets the sign-up end date for the event.
     *
     * @return The sign-up end date.
     */
    public LocalDate getSignupEndDate() {
        return signupEndDate;
    }

    /**
     * Sets the sign-up end date for the event.
     *
     * @param signupEndDate The sign-up end date to set.
     */
    public void setSignupEndDate(LocalDate signupEndDate) {
        this.signupEndDate = signupEndDate;
    }

    /**
     * Gets the status of the event.
     *
     * @return The status of the event.
     */
    public boolean getIsOver() {
        return isOver;
    }

    /**
     * Sets the status of the event.
     *
     * @param isOver The status of the event to set.
     */
    public void setIsOver(boolean isOver) {
        this.isOver = isOver;
    }
}
