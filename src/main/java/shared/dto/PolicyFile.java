package shared.dto;

import java.util.ArrayList;

public class PolicyFile {

    public ArrayList<Policy> policies;

    public ArrayList<Policy> getPolicies() {
        return policies;
    }

    public void setPolicies(ArrayList<Policy> policies) {
        this.policies = policies;
    }
}
