package cs203.ftms.overall.dto;

public class CreatePoulesDTO {
    private int pouleCount;

    public CreatePoulesDTO() {}
    
    public CreatePoulesDTO(int pouleCount) {
        this.pouleCount = pouleCount;
    }

    public int getPouleCount() {
        return pouleCount;
    }

    public void setPouleCount(int pouleCount) {
        this.pouleCount = pouleCount;
    }
}