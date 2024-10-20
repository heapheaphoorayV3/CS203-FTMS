package cs203.ftms.overall.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import cs203.ftms.overall.validation.ValidGender;
import cs203.ftms.overall.validation.ValidWeapon;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;


public class CreateEventDTO{
    @ValidGender
    private Character gender;

    @ValidWeapon
    private Character weapon;

    @Min(value=8, message = "Event needs to have at least 8 participants")
    private int minParticipants;

    @FutureOrPresent(message = "Date must be in the future")
    private LocalDate date;

    // @FutureOrPresent(message = "Start time must be in the future")
    private LocalTime startTime;

    // @FutureOrPresent(message = "End time must be in the future")
    private LocalTime endTime;


    public CreateEventDTO(Character gender, Character weapon, int minParticipants, LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.gender = gender;
        this.weapon = weapon;
        this.minParticipants = minParticipants;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }


    public Character getGender() {
        return gender;
    }


    public void setGender(Character gender) {
        this.gender = gender;
    }


    public Character getWeapon() {
        return weapon;
    }


    public void setWeapon(Character weapon) {
        this.weapon = weapon;
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