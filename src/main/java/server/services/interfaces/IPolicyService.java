package server.services.interfaces;

import shared.dto.Roles;

public interface IPolicyService {
    void getRoles();

    boolean haveRole(String username, Roles role);
    boolean haveAllRoles(String username, Roles[] roles);
    boolean haveSomeRoles(String username, Roles[] roles);
}
