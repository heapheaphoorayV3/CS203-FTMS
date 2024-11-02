package cs203.ftms.user.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import cs203.ftms.user.validation.ValidDominantArm;
import cs203.ftms.user.validation.ValidGender;
import cs203.ftms.user.validation.ValidWeapon;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;

public class CompleteFencerProfileDTO {
    @ValidDominantArm
    private Character dominantArm; // R L 

    @ValidWeapon
    private Character weapon; // SEF 

    @ValidGender
    private Character gender; // WM

    @NotBlank
    private String club;
    
    // custom validator 
    private int debutYear; // check at least 8 year old from birth year and this year
    
    public CompleteFencerProfileDTO(Character dominantArm, Character weapon, Character gender, String club, int debutYear) {
        this.dominantArm = dominantArm;
        this.weapon = weapon;
        this.gender = gender;
        this.club = club;
        this.debutYear = debutYear;
    }

    public Character getDominantArm() {
        return dominantArm;
    }

    public void setDominantArm(Character dominantArm) {
        this.dominantArm = dominantArm;
    }

    public Character getWeapon() {
        return weapon;
    }

    public void setWeapon(Character weapon) {
        this.weapon = weapon;
    }

    public Character getGender() {
        return gender;
    }

    public void setGender(Character gender) {
        this.gender = gender;
    }

    public String getClub() {
        return club;
    }

    public void setClub(String organisation) {
        this.club = organisation;
    }

    public int getDebutYear() {
        return debutYear;
    }

    public void setDebutYear(int debutYear) {
        this.debutYear = debutYear;
    }

    
}
