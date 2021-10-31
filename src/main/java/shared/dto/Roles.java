package shared.dto;

public enum Roles {
    ADMIN, // Alice is managing the print server
    TECHNICIAN,
    POWER_USER,
    BASIC, // basic users
    NONE;

    @Override
    public String toString() {
        switch (this){
            case ADMIN: {
                return "admin";
            }
            case TECHNICIAN: {
                return "technician";
            }
            case POWER_USER: {
                return "power_user";
            }
            case BASIC: {
                return "basic";
            }
            default: {
                return "none";
            }
        }
    }
}
