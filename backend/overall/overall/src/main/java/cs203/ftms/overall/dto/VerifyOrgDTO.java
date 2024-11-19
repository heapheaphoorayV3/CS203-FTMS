package cs203.ftms.overall.dto;

import java.util.List;

/**
 * Data Transfer Object (DTO) for verifying organisations.
 * Contains a list of organisation IDs to approve and deny.
 */
public class VerifyOrgDTO {
   
    private List<Integer> approve;
    private List<Integer> deny;

    public VerifyOrgDTO() {
    }

    /**
     * Constructs a VerifyOrgDTO with the specified list of organisation IDs to approve and deny.
     *
     * @param approve the list of organisation IDs to approve
     * @param deny    the list of organisation IDs to deny
     */
    public VerifyOrgDTO(List<Integer> approve, List<Integer> deny) {
        this.approve = approve;
        this.deny = deny;
    }

    /**
     * Gets the list of organisation IDs to approve.
     *
     * @return the list of organisation IDs to approve
     */
    public List<Integer> getApprove() {
        return approve;
    }

    /**
     * Sets the list of organisation IDs to approve.
     *
     * @param approve the list of organisation IDs to approve
     */
    public void setApprove(List<Integer> approve) {
        this.approve = approve;
    }

    /**
     * Gets the list of organisation IDs to deny.
     *
     * @return the list of organisation IDs to deny
     */
    public List<Integer> getDeny() {
        return deny;
    }

    /**
     * Sets the list of organisation IDs to deny.
     *
     * @param deny the list of organisation IDs to deny
     */
    public void setDeny(List<Integer> deny) {
        this.deny = deny;
    }
}
