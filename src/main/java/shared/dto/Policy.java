package shared.dto;

public class Policy {

    public String username;
    public Roles role;

    public Policy(String username, Roles role) {
        this.username = username;
        this.role = role;
    }
}
