package cs203.ftms.overall.dto;

import java.util.Map;

public class PouleMatchResultDTO {
    private int eventId; 
    private int pouleNumber;
    private Map<String,String> pouleScores;
    
    public PouleMatchResultDTO(int eventId, int pouleNumber, Map<String,String> pouleScores) {
        this.eventId = eventId;
        this.pouleNumber = pouleNumber;
        this.pouleScores = pouleScores;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getPouleNumber() {
        return pouleNumber;
    }

    public void setPouleNumber(int pouleNumber) {
        this.pouleNumber = pouleNumber;
    }

    public Map<String,String> getPouleScores() {
        return pouleScores;
    }

    public void setPouleScores(Map<String,String> pouleScores) {
        this.pouleScores = pouleScores;
    }

    
}
