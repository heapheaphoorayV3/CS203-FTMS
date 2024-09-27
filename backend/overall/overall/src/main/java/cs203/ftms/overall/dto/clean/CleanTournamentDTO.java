package cs203.ftms.overall.dto.clean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public class CleanTournamentDTO {
    private int id;
    private String name;
    private String organiserName;
    private LocalDateTime signupEndDate;
    private LocalDate startDate;
    private LocalDate endDate;
    private String location;
    private String description;
    private String rules;
    private Set<CleanEventDTO> events;
    
    public CleanTournamentDTO(int id, String name, String organiserName, LocalDateTime signupEndTime, LocalDate startDate, LocalDate endDate,
            String location, String description, String rules, Set<CleanEventDTO> events) {
        this.id = id;
        this.name = name;
        this.organiserName = organiserName;
        this.signupEndDate = signupEndTime;
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
        this.description = description;
        this.rules = rules;
        this.events = events;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrganiserName() {
        return organiserName;
    }

    public void setOrganiserName(String organiserName) {
        this.organiserName = organiserName;
    }

    public LocalDateTime getSignupEndDate() {
        return signupEndDate;
    }

    public void setSignupEndDate(LocalDateTime signupEndDate) {
        this.signupEndDate = signupEndDate;
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

    public Set<CleanEventDTO> getEvents() {
        return events;
    }

    public void setEvents(Set<CleanEventDTO> events) {
        this.events = events;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    

    
}
