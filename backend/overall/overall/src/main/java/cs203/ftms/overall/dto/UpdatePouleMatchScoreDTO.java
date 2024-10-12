package cs203.ftms.overall.dto;

public class UpdatePouleMatchScoreDTO {
    private int matchId;
    private int fencer1Score;
    private int fencer2Score;
    private int winner;

    public UpdatePouleMatchScoreDTO(int matchId, int fencer1Score, int fencer2Score) {
        this.matchId = matchId;
        this.fencer1Score = fencer1Score;
        this.fencer2Score = fencer2Score;
        if(fencer1Score > fencer2Score){
            this.winner = 1;
        }else{
            this.winner = 2;
        }
    }

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public int getFencer1Score() {
        return fencer1Score;
    }

    public void setFencer1Score(int fencer1Score) {
        this.fencer1Score = fencer1Score;
    }

    public int getFencer2Score() {
        return fencer2Score;
    }

    public void setFencer2Score(int fencer2Score) {
        this.fencer2Score = fencer2Score;
    }

    public int getWinner(){
        return winner;
    }

    public void setWinner(int winner) {
        this.winner = winner;
    }
}