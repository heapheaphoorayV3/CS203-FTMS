package cs203.ftms.overall.dto.clean;

public class CleanMatchDTO {
    private int id; 

    private CleanTournamentFencerDTO fencer1; 
    private int score1; 

    private CleanTournamentFencerDTO fencer2; 
    private int score2; 

    private int winner; 
    private char matchType;
    
    public CleanMatchDTO(int id, CleanTournamentFencerDTO fencer1, int score1, CleanTournamentFencerDTO fencer2,
            int score2, int winner, char matchType) {
        this.id = id;
        this.fencer1 = fencer1;
        this.score1 = score1;
        this.fencer2 = fencer2;
        this.score2 = score2;
        this.winner = winner;
        this.matchType = matchType;
    }

    public CleanMatchDTO(){
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CleanTournamentFencerDTO getFencer1() {
        return fencer1;
    }

    public void setFencer1(CleanTournamentFencerDTO fencer1) {
        this.fencer1 = fencer1;
    }

    public int getScore1() {
        return score1;
    }

    public void setScore1(int score1) {
        this.score1 = score1;
    }

    public CleanTournamentFencerDTO getFencer2() {
        return fencer2;
    }

    public void setFencer2(CleanTournamentFencerDTO fencer2) {
        this.fencer2 = fencer2;
    }

    public int getScore2() {
        return score2;
    }

    public void setScore2(int score2) {
        this.score2 = score2;
    }

    public int getWinner() {
        return winner;
    }

    public void setWinner(int winner) {
        this.winner = winner;
    }

    public char getMatchType() {
        return matchType;
    }

    public void setMatchType(char matchType) {
        this.matchType = matchType;
    } 

    
}
