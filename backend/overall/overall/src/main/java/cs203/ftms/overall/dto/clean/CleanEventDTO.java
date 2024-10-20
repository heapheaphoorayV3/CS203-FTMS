package cs203.ftms.overall.dto.clean;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class CleanEventDTO{
    private int id;
    private char gender;
    private char weapon;
    private String tournamentName;
    private Set<CleanFencerDTO> fencers;
    private int minParticipants;
    private int participantCount;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    
    public CleanEventDTO(int id, char gender, char weapon, String tournamentName, Set<CleanFencerDTO> fencers, int minParticipants,
            int participantCount, LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.id = id;
        this.gender = gender; 
        this.weapon = weapon; 
        this.tournamentName = tournamentName;
        this.fencers = fencers;
        this.minParticipants = minParticipants;
        this.participantCount = participantCount;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getTournamentName() {
        return tournamentName;
    }

    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }

    public Set<CleanFencerDTO> getFencers() {
        return fencers;
    }

    public void setFencers(Set<CleanFencerDTO> fencers) {
        this.fencers = fencers;
    }

    public int getMinParticipants() {
        return minParticipants;
    }

    public void setMinParticipants(int minParticipants) {
        this.minParticipants = minParticipants;
    }

    public int getParticipantCount() {
        return participantCount;
    }

    public void setParticipantCount(int participantCount) {
        this.participantCount = participantCount;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public char getWeapon() {
        return weapon;
    }

    public void setWeapon(char weapon) {
        this.weapon = weapon;
    }
    
    
    
}