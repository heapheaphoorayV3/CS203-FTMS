package cs203.ftms.overall.dto;

public class DirectEliminationBracketFencerDTO {

    private int id;
    private String resultText;
    private boolean isWinner;
    private String status;
    private String name;

    public DirectEliminationBracketFencerDTO(int id, String resultText, boolean isWinner, String status, String name) {
        this.id = id;
        this.resultText = resultText;
        this.isWinner = isWinner;
        this.status = status;
        this.name = name;
    }

    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public String getResultText() {
        return resultText;
    }

    public void setResultText(String resultText) {
        this.resultText = resultText;
    }

    public boolean getIsWinner() {
        return isWinner;
    }

    public void setIsWinner(boolean isWinner) {
        this.isWinner = isWinner;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
