package cs203.ftms.overall.dto.clean;

import java.time.LocalDate;

public class CleanFencerDTO{
    private String name; 
    private String email; 
    private String country;
    private LocalDate dateOfBirth;
    private char dominantArm;
    private char weapon;
    private String club;
    private int points;
    private int debutYear;
    private char gender;
    private String contactNo;

    public CleanFencerDTO(String name, String email, String contactNo, String country, 
    LocalDate dateOfBirth, char dominantArm, char weapon, String club, int points, int debutYear, char gender) {
        this.name = name;
        this.email = email;
        this.country = country;
        this.dateOfBirth = dateOfBirth;
        this.dominantArm = dominantArm;
        this.weapon = weapon;
        this.club = club;
        this.points = points;
        this.debutYear = debutYear;
        this.gender = gender;
        this.contactNo = contactNo;

    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    } 

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public char getDominantArm() {
        return dominantArm;
    }

    public void setDominantArm(char dominantArm) {
        this.dominantArm = dominantArm;
    }

    public char getWeapon() {
        return weapon;
    }

    public void setWeapon(char weapon) {
        this.weapon = weapon;
    }

    public String getClub() {
        return club;
    }

    public void setClub(String organisation) {
        this.club = organisation;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getDebutYear() {
        return debutYear;
    }

    public void setDebutYear(int debutYear) {
        this.debutYear = debutYear;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public String getContactNo(){
        return contactNo;
    }

    public void setContactNo(String contactNo){
        this.contactNo = contactNo;
    }

}