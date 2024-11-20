package cs203.ftms.overall.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;

/**
 * Data Transfer Object (DTO) for updating event details.
 * Contains validation constraints for the minimum number of participants and future date requirements.
 */
public class UpdateEventDTO {

    @Min(value=8, message = "Event needs to have at least 8 participants")
    private int minParticipants;

    @FutureOrPresent(message = "Date must be in the future")
    private LocalDate date;

    private LocalTime startTime;

    private LocalTime endTime;

    /**
     * Constructs an UpdateEventDTO with the specified minimum participants, date, start time, and end time.
     *
     * @param minParticipants the minimum number of participants required for the event
     * @param date            the date of the event
     * @param startTime       the start time of the event
     * @param endTime         the end time of the event
     */
    public UpdateEventDTO(int minParticipants, LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.minParticipants = minParticipants;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Gets the minimum number of participants for the event.
     *
     * @return the minimum number of participants
     */
    public int getMinParticipants() {
        return minParticipants;
    }

    /**
     * Sets the minimum number of participants for the event.
     *
     * @param minParticipants the minimum number of participants to set
     */
    public void setMinParticipants(int minParticipants) {
        this.minParticipants = minParticipants;
    }

    /**
     * Gets the date of the event.
     *
     * @return the event date
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Sets the date of the event.
     *
     * @param date the event date to set
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Gets the start time of the event.
     *
     * @return the start time of the event
     */
    public LocalTime getStartTime() {
        return startTime;
    }

    /**
     * Sets the start time of the event.
     *
     * @param startTime the start time to set
     */
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    /**
     * Gets the end time of the event.
     *
     * @return the end time of the event
     */
    public LocalTime getEndTime() {
        return endTime;
    }

    /**
     * Sets the end time of the event.
     *
     * @param endTime the end time to set
     */
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
}
