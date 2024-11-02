package cs203.ftms.overall.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import cs203.ftms.tournament.controller.ValidGender;
import cs203.ftms.tournament.controller.ValidWeapon;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;


public class UpdateEventDTO{
    @Min(value=8, message = "Event needs to have at least 8 participants")
    private int minParticipants;

    @FutureOrPresent(message = "Date must be in the future")
    private LocalDate date;

    private LocalTime startTime;

    private LocalTime endTime;


    public UpdateEventDTO(int minParticipants, LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.minParticipants = minParticipants;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getMinParticipants() {
        return minParticipants;
    }


    public void setMinParticipants(int minParticipants) {
        this.minParticipants = minParticipants;
    }


    public LocalDate getDate() {
        return date;
    }


    public void setDate(LocalDate date) {
        this.date = date;
    }


    public LocalTime getStartTime() {
        return startTime;
    }


    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }


    public LocalTime getEndTime() {
        return endTime;
    }


    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    
}