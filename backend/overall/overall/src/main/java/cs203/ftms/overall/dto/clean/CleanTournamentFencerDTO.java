package cs203.ftms.overall.dto.clean;

public class CleanTournamentFencerDTO {
    private int tournamentFencerId;
    private int fencerId; 
    private String fencerName; 
    private String fencerClub; 
    private String country; 
    private char dominantArm; 
    private int tournamentPoints;
    private int eventId;

    
    public CleanTournamentFencerDTO(int tournamentFencerId, int fencerId, String fencerName, String fencerClub, String country,
            char dominantArm, int tournamentPoints, int eventId) {
        this.tournamentFencerId = tournamentFencerId;
        this.fencerId = fencerId;
        this.fencerName = fencerName;
        this.fencerClub = fencerClub;
        this.country = country;
        this.dominantArm = dominantArm;
        this.tournamentPoints = tournamentPoints;
        this.eventId = eventId;
    }

    public int getTournamentFencerId() {
        return tournamentFencerId;
    }

    public void setTournamentFencerId(int tournamentFencerId) {
        this.tournamentFencerId = tournamentFencerId;
    }

    public int getFencerId() {
        return fencerId;
    }


    public void setFencerId(int fencerId) {
        this.fencerId = fencerId;
    }


    public String getFencerName() {
        return fencerName;
    }


    public void setFencerName(String fencerName) {
        this.fencerName = fencerName;
    }


    public String getFencerClub() {
        return fencerClub;
    }


    public void setFencerClub(String fencerClub) {
        this.fencerClub = fencerClub;
    }


    public String getCountry() {
        return country;
    }


    public void setCountry(String country) {
        this.country = country;
    }


    public char getDominantArm() {
        return dominantArm;
    }


    public void setDominantArm(char dominantArm) {
        this.dominantArm = dominantArm;
    }


    public int getTournamentPoints() {
        return tournamentPoints;
    }


    public void setTournamentPoints(int tournamentPoints) {
        this.tournamentPoints = tournamentPoints;
    }


    public int getEventId() {
        return eventId;
    }


    public void setEventId(int eventId) {
        this.eventId = eventId;
    } 

}
