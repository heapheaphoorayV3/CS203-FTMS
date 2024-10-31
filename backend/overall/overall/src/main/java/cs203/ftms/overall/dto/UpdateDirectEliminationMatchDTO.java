package cs203.ftms.overall.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class UpdateDirectEliminationMatchDTO {
    private int matchId;
    
    @Min(value=0, message = "Minimum points for a direct elimination match is 0")
    @Max(value = 15, message = "Maximum points for a direct elimination match is 15")
    private int score1;

    @Min(value=0, message = "Minimum points for a direct elimination match is 0")
    @Max(value = 15, message = "Maximum points for a direct elimination match is 15")
    private int score2;

    public UpdateDirectEliminationMatchDTO(int matchId, int score1, int score2) {
        this.matchId = matchId;
        this.score1 = score1;
        this.score2 = score2;
    }

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public int getScore1() {
        return score1;
    }

    public void setScore1(int score1) {
        this.score1 = score1;
    }

    public int getScore2() {
        return score2;
    }

    public void setScore2(int score2) {
        this.score2 = score2;
    }

}