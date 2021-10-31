package server.services.interfaces;

import shared.dto.Roles;
import shared.exceptions.Unauthorized;

public interface IPolicyService {
    void getRoles();

    boolean haveRole(String username, Roles role);
    boolean haveAllRoles(String username, Roles[] roles);
    boolean haveSomeRoles(String username, Roles[] roles);
    void haveRoleThrow(String username, Roles role) throws Unauthorized;
    void haveSomeRolesThrow(String username, Roles[] roles) throws Unauthorized;
    void haveAllRolesThrow(String username, Roles[] roles) throws Unauthorized;
}
