package server.services.interfaces;

import server.data.models.User;

public interface IAuthService {
    void load();
    boolean haveAccess(String username, String action);
}
