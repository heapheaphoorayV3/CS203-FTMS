package cs203.ftms.overall.dto.clean;

public class CleanTournamentFencerDTO {
    private int tournamentFencerId;
    private int fencerId; 
    private String fencerName; 
    private String fencerClub; 
    private String country; 
    private char dominantArm; 
    private int tournamentRank;
    private int eventId;
    private int pouleWins;
    private int poulePoints;

    
    public CleanTournamentFencerDTO(int tournamentFencerId, int fencerId, String fencerName, String fencerClub, String country,
            char dominantArm, int tournamentRank, int eventId, int pouleWins, int poulePoints) {
        this.tournamentFencerId = tournamentFencerId;
        this.fencerId = fencerId;
        this.fencerName = fencerName;
        this.fencerClub = fencerClub;
        this.country = country;
        this.dominantArm = dominantArm;
        this.tournamentRank = tournamentRank;
        this.eventId = eventId;
        this.pouleWins = pouleWins;
        this.poulePoints = poulePoints;
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


    public int getTournamentRank() {
        return tournamentRank;
    }


    public void setTournamentRank(int tournamentPoints) {
        this.tournamentRank = tournamentPoints;
    }


    public int getEventId() {
        return eventId;
    }


    public void setEventId(int eventId) {
        this.eventId = eventId;
    } 

    public int getPouleWins() {
        return pouleWins;
    }

    public void setPouleWins(int pouleWins) {
        this.pouleWins = pouleWins;
    }

    public int getPoulePoints() {
        return poulePoints;
    }

    public void setPoulePoints(int poulePoints) {
        this.poulePoints = poulePoints;
    }

}
