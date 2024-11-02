package cs203.ftms.user.dto;

import java.util.List;

public class VerifyOrgDTO {
   
    private List<Integer> approve;
    private List<Integer> deny;

    public VerifyOrgDTO() {
    }

    public VerifyOrgDTO(List<Integer> approve, List<Integer> deny) {
        this.approve = approve;
        this.deny = deny;
    }

    public List<Integer> getApprove() {
        return approve;
    }

    public void setApprove(List<Integer> approve) {
        this.approve = approve;
    }

    public List<Integer> getDeny() {
        return deny;
    }

    public void setDeny(List<Integer> deny) {
        this.deny = deny;
    }

    
    
    
}
