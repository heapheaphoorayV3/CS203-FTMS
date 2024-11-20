package cs203.ftms.overall.model.userrelated;

import java.util.HashSet;
import java.util.Set;

import cs203.ftms.overall.model.tournamentrelated.Tournament;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;

/**
 * Represents an organizer user in the system, with specific attributes for managing
 * tournaments and verification status. Extends the base User class with a predefined
 * role of "ROLE_ORGANISER".
 */
@Entity
@DiscriminatorValue("O")
public class Organiser extends User {

    /**
     * Indicates whether the organiser's account has been verified by administrators.
     * Verified organisers have full privileges to create and manage tournaments.
     * Default value is false until verification is completed.
     * Verification status affects what actions the organiser can perform:
     * - Unverified organisers cannot login to perform any organiser functions like create or modify tournaments
     * - Verified organisers can create, modify, and manage tournaments
     */
    @Column(name = "verified")
    private boolean verified;

    /**
     * Collection of all tournaments created and managed by this organiser.
     * Configured with:
     * - Eager fetching to ensure tournament data is immediately available
     * - Cascade ALL to automatically handle related tournament operations
     * - orphanRemoval to ensure tournaments are deleted if removed from this collection
     * 
     * This relationship allows the organiser to:
     * - Create new tournaments
     * - Modify tournament details
     * - Monitor tournament progress
     * - Access participant information
     * - Manage tournament schedules
     */
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "organiser", orphanRemoval = true)
    private Set<Tournament> tourHost;


    /**
     * Default constructor for Organiser.
     */
    public Organiser() {
        this.tourHost = new HashSet<>();
    }

    /**
     * Constructs an Organiser with specified details.
     *
     * @param name The name of the organiser.
     * @param email The email of the organiser.
     * @param password The password for the organiser's account.
     * @param contactNo The contact number of the organiser.
     * @param country The country of the organiser.
     */
    public Organiser(String name, String email, String password, String contactNo, String country) {
        super(name, email, password, contactNo, country, "ROLE_ORGANISER");
        this.tourHost = new HashSet<>();
    }

    /**
     * Checks if the organizer is verified.
     *
     * @return true if verified, false otherwise.
     */
    public boolean isVerified() {
        return verified;
    }

    /**
     * Sets the verification status of the organizer.
     *
     * @param verified The verification status to set.
     */
    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    /**
     * Checks if the organiser's account is enabled. An account is enabled if the organizer
     * is verified.
     *
     * @return true if verified, false otherwise.
     */
    @Override
    public boolean isEnabled() {
        return verified;
    }

    /**
     * Gets the set of tournaments hosted by this organizer.
     *
     * @return The set of hosted tournaments.
     */
    public Set<Tournament> getTourHost() {
        return tourHost;
    }

    /**
     * Sets the tournaments hosted by this organizer.
     *
     * @param tourHost The set of tournaments to host.
     */
    public void setTourHost(Set<Tournament> tourHost) {
        this.tourHost = tourHost;
    }
}
