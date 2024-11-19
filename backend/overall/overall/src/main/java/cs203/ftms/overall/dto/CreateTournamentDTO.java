package cs203.ftms.overall.dto;

import java.time.LocalDate;
import cs203.ftms.overall.validation.ValidDifficulty;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Data Transfer Object for creating a new tournament with various attributes such as name, dates, location, 
 * description, rules, advancement rate, and difficulty level.
 */
public class CreateTournamentDTO {

    @NotBlank(message = "Tournament name cannot be empty!")
    private String name;

    @FutureOrPresent(message = "Sign-up end date must be in the future or present")
    @NotNull(message = "Sign-up end date cannot be null")
    private LocalDate signupEndDate;

    @Min(value = 60, message = "Advancement rate cannot be less than 60%")
    @Max(value = 100, message = "Advancement rate cannot be more than 100%")
    private int advancementRate;

    @FutureOrPresent(message = "Start date must be in the future or present")
    private LocalDate startDate;

    @FutureOrPresent(message = "End date must be in the future or present")
    private LocalDate endDate;

    @NotBlank(message = "Location cannot be empty")
    private String location;

    @NotBlank(message = "Description cannot be empty")
    private String description;

    @NotBlank(message = "Rules cannot be empty")
    private String rules;

    @ValidDifficulty
    private Character difficulty;

    /**
     * Constructs a CreateTournamentDTO with specified details for a tournament.
     *
     * @param name           The name of the tournament.
     * @param signupEndDate  The last date for sign-up.
     * @param advancementRate The percentage rate for advancement.
     * @param startDate      The starting date of the tournament.
     * @param endDate        The ending date of the tournament.
     * @param location       The location where the tournament will be held.
     * @param description    The description of the tournament.
     * @param rules          The rules of the tournament.
     * @param difficulty     The difficulty level (B, I, or A).
     */
    public CreateTournamentDTO(String name, LocalDate signupEndDate, int advancementRate, LocalDate startDate, 
                               LocalDate endDate, String location, String description, String rules, Character difficulty) {
        this.name = name;
        this.signupEndDate = signupEndDate;
        this.advancementRate = advancementRate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
        this.description = description;
        this.rules = rules;
        this.difficulty = difficulty;
    }

    public CreateTournamentDTO() {}

    /**
     * Gets the tournament name.
     * 
     * @return The name of the tournament.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the tournament name.
     * 
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the sign-up end date for the tournament.
     * 
     * @return The sign-up end date.
     */
    public LocalDate getSignupEndDate() {
        return signupEndDate;
    }

    /**
     * Sets the sign-up end date for the tournament.
     * 
     * @param signupEndDate The sign-up end date to set.
     */
    public void setSignupEndDate(LocalDate signupEndDate) {
        this.signupEndDate = signupEndDate;
    }

    /**
     * Gets the advancement rate of the tournament.
     * 
     * @return The advancement rate.
     */
    public int getAdvancementRate() {
        return advancementRate;
    }

    /**
     * Sets the advancement rate of the tournament.
     * 
     * @param advancementRate The advancement rate to set.
     */
    public void setAdvancementRate(int advancementRate) {
        this.advancementRate = advancementRate;
    }

    /**
     * Gets the start date of the tournament.
     * 
     * @return The start date.
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Sets the start date of the tournament.
     * 
     * @param startDate The start date to set.
     */
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    /**
     * Gets the end date of the tournament.
     * 
     * @return The end date.
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Sets the end date of the tournament.
     * 
     * @param endDate The end date to set.
     */
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    /**
     * Gets the location of the tournament.
     * 
     * @return The tournament location.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the location of the tournament.
     * 
     * @param location The location to set.
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Gets the description of the tournament.
     * 
     * @return The tournament description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the tournament.
     * 
     * @param description The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the rules of the tournament.
     * 
     * @return The tournament rules.
     */
    public String getRules() {
        return rules;
    }

    /**
     * Sets the rules of the tournament.
     * 
     * @param rules The rules to set.
     */
    public void setRules(String rules) {
        this.rules = rules;
    }

    /**
     * Gets the difficulty level of the tournament.
     * 
     * @return The difficulty level (B, I, or A).
     */
    public Character getDifficulty() {
        return difficulty;
    }

    /**
     * Sets the difficulty level of the tournament.
     * 
     * @param difficulty The difficulty level to set.
     */
    public void setDifficulty(Character difficulty) {
        this.difficulty = difficulty;
    }
}
