package cs203.ftms.overall.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import cs203.ftms.overall.validation.ValidGender;
import cs203.ftms.overall.validation.ValidWeapon;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;

/**
 * Data Transfer Object for creating a new event.
 * Contains details about event-specific configurations such as gender, weapon, participant count,
 * date, and timing, with necessary validations.
 */
public class CreateEventDTO {

    @ValidGender
    private Character gender;

    @ValidWeapon
    private Character weapon;

    @Min(value = 8, message = "Event needs to have at least 8 participants")
    private int minParticipants;

    @FutureOrPresent(message = "Date must be in the future")
    private LocalDate date;

    private LocalTime startTime;

    private LocalTime endTime;

    /**
     * Constructs a CreateEventDTO with the specified parameters.
     *
     * @param gender          The gender category for the event ('M' for male, 'W' for female).
     * @param weapon          The weapon category for the event ('S' for sabre, 'E' for epee, 'F' for foil).
     * @param minParticipants Minimum required participants for the event, at least 8.
     * @param date            The date on which the event is scheduled, must be today or a future date.
     * @param startTime       The starting time for the event.
     * @param endTime         The ending time for the event.
     */
    public CreateEventDTO(Character gender, Character weapon, int minParticipants, LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.gender = gender;
        this.weapon = weapon;
        this.minParticipants = minParticipants;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Gets the gender category for the event.
     *
     * @return The gender category.
     */
    public Character getGender() {
        return gender;
    }

    /**
     * Sets the gender category for the event.
     *
     * @param gender The gender category to set.
     */
    public void setGender(Character gender) {
        this.gender = gender;
    }

    /**
     * Gets the weapon category for the event.
     *
     * @return The weapon category.
     */
    public Character getWeapon() {
        return weapon;
    }

    /**
     * Sets the weapon category for the event.
     *
     * @param weapon The weapon category to set.
     */
    public void setWeapon(Character weapon) {
        this.weapon = weapon;
    }

    /**
     * Gets the minimum required participants for the event.
     *
     * @return The minimum participants.
     */
    public int getMinParticipants() {
        return minParticipants;
    }

    /**
     * Sets the minimum required participants for the event.
     *
     * @param minParticipants The minimum participants to set.
     */
    public void setMinParticipants(int minParticipants) {
        this.minParticipants = minParticipants;
    }

    /**
     * Gets the date for the event.
     *
     * @return The event date.
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Sets the date for the event.
     *
     * @param date The event date to set.
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Gets the starting time for the event.
     *
     * @return The event start time.
     */
    public LocalTime getStartTime() {
        return startTime;
    }

    /**
     * Sets the starting time for the event.
     *
     * @param startTime The event start time to set.
     */
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    /**
     * Gets the ending time for the event.
     *
     * @return The event end time.
     */
    public LocalTime getEndTime() {
        return endTime;
    }

    /**
     * Sets the ending time for the event.
     *
     * @param endTime The event end time to set.
     */
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
}
