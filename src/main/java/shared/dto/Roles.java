package shared.dto;

public enum Roles {
    ADMIN, // Alice is managing the print server
    TECHNICIAN,
    POWER_USER,
    BASIC; // basic users

    @Override
    public String toString() {
        switch (this){
            case ADMIN: {
                return "Admin";
            }
            case TECHNICIAN: {
                return "Technician";
            }
            case POWER_USER: {
                return "PowerUser";
            }
            case BASIC: {
                return "Basic";
            }
            default: {
                return "none";
            }
        }
    }
}
