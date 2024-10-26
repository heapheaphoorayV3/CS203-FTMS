package cs203.ftms.overall.dto;

public class DirectEliminationBracketDTO {
    
    private int id;
    private String name;
    private int nextMatchId;
    private String tournamentRoundText;
    private String startTime;
    private String state;
    private DirectEliminationBracketFencerDTO[] participants;

    public DirectEliminationBracketDTO(int id, String name, int nextMatchId, String tournamentRoundText, String startTime, String state, DirectEliminationBracketFencerDTO[] participants) {
        this.id = id;
        this.name = name;
        this.nextMatchId = nextMatchId;
        this.tournamentRoundText = tournamentRoundText;
        this.startTime = startTime;
        this.state = state;
        this.participants = participants;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNextMatchId() {
        return nextMatchId;
    }

    public void setNextMatchId(int nextMatchId) {
        this.nextMatchId = nextMatchId;
    }

    public String getTournamentRoundText() {
        return tournamentRoundText;
    }
    
    public void setTournamentRoundText(String tournamentRoundText) {
        this.tournamentRoundText = tournamentRoundText;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getState() {
        return state;
    }
    
    public void setState(String state) {
        this.state = state;
    }

    public DirectEliminationBracketFencerDTO[] getParticipants() {
        return participants;
    }

    public void setParticipants(DirectEliminationBracketFencerDTO[] participants) {
        this.participants = participants;
    }
}
