package cs203.ftms.overall.dto;

/**
 * Data Transfer Object for creating poules in a tournament.
 * Specifies the number of poules to be created.
 */
public class CreatePoulesDTO {

    private int pouleCount;

    /**
     * Default constructor for CreatePoulesDTO.
     */
    public CreatePoulesDTO() {}

    /**
     * Constructs a CreatePoulesDTO with a specified number of poules.
     *
     * @param pouleCount The number of poules to be created in the tournament.
     */
    public CreatePoulesDTO(int pouleCount) {
        this.pouleCount = pouleCount;
    }

    /**
     * Gets the number of poules.
     *
     * @return The number of poules.
     */
    public int getPouleCount() {
        return pouleCount;
    }

    /**
     * Sets the number of poules.
     *
     * @param pouleCount The number of poules to set.
     */
    public void setPouleCount(int pouleCount) {
        this.pouleCount = pouleCount;
    }
}
