package cs203.ftms.overall.dto.clean;

import java.util.*;

public class CleanPouleDTO{
    private int id;
    private int pouleNumber;
    private String eventName; 
    private int eventId; 
    private Set<CleanMatchDTO> pouleMatches;
    private Set<CleanTournamentFencerDTO> fencers;
    
    public CleanPouleDTO(int id, int pouleNumber, String eventName, int eventId, Set<CleanMatchDTO> pouleMatches,
            Set<CleanTournamentFencerDTO> fencers) {
        this.id = id;
        this.pouleNumber = pouleNumber;
        this.eventName = eventName;
        this.eventId = eventId;
        this.pouleMatches = pouleMatches;
        this.fencers = fencers;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPouleNumber() {
        return pouleNumber;
    }

    public void setPouleNumber(int pouleNumber) {
        this.pouleNumber = pouleNumber;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public Set<CleanMatchDTO> getPouleMatches() {
        return pouleMatches;
    }

    public void setPouleMatches(Set<CleanMatchDTO> pouleMatches) {
        this.pouleMatches = pouleMatches;
    }

    public Set<CleanTournamentFencerDTO> getFencers() {
        return fencers;
    }

    public void setFencers(Set<CleanTournamentFencerDTO> fencers) {
        this.fencers = fencers;
    } 

    
}