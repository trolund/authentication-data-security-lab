package shared.dto;

public enum Roles {
    ADMIN, // Alice is managing the print server
    TECHNICIAN,
    POWER_USER,
    MAINTAINER,
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
            case MAINTAINER: {
                return "Maintainer";
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
