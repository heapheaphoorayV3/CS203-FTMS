package cs203.ftms.overall.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.lang.NonNull;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public class CreateTournamentDTO {
    @NotBlank(message = "Tournament name cannot be empty!")
    private String name;

    @FutureOrPresent
    @NotNull
    private LocalDate signupEndDate;
    
    @Min(value = 60, message = "Advancement rate cannot be less than 60%")
    @Max(value=100, message="Advancement rate cannot be more than 100%")
    private int advancementRate;

    @FutureOrPresent
    private LocalDate startDate;

    @FutureOrPresent
    private LocalDate endDate;

    @NotBlank
    private String location;

    @NotBlank
    private String description;
    
    @NotBlank
    private String rules;

    
    public CreateTournamentDTO(String name, LocalDate signupEndDate, int advancementRate, LocalDate startDate, LocalDate endDate, String location, String description, String rules){
        this.name = name;
        this.signupEndDate = signupEndDate;
        this.advancementRate = advancementRate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
        this.description = description;
        this.rules = rules;
 
    }

    public CreateTournamentDTO() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getSignupEndDate() {
        return signupEndDate;
    }

    public void setSignupEndDate(LocalDate signupEndDate) {
        this.signupEndDate = signupEndDate;
    }

    public int getAdvancementRate() {
        return advancementRate;
    }

    public void setAdvancementRate(int advancementRate) {
        this.advancementRate = advancementRate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }
    
}