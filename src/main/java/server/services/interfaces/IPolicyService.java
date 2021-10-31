package server.services.interfaces;

import shared.dto.Roles;

public interface IPolicyService {
    void getRoles();

    boolean haveRole(String username, Roles role);
}
