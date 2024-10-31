package cs203.ftms.overall.dto.clean;

import java.util.List;

public class CleanPouleDTO{
    private int id;
    private int pouleNumber;
    private String eventName; 
    private int eventId; 
    private List<CleanMatchDTO> pouleMatches;
    private List<CleanTournamentFencerDTO> fencers;
    
    public CleanPouleDTO(int id, int pouleNumber, String eventName, int eventId, List<CleanMatchDTO> pouleMatches,
         List<CleanTournamentFencerDTO> fencers) {
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

    public List<CleanMatchDTO> getPouleMatches() {
        return pouleMatches;
    }

    public void setPouleMatches(List<CleanMatchDTO> pouleMatches) {
        this.pouleMatches = pouleMatches;
    }

    public List<CleanTournamentFencerDTO> getFencers() {
        return fencers;
    }

    public void setFencers(List<CleanTournamentFencerDTO> fencers) {
        this.fencers = fencers;
    } 

    
}