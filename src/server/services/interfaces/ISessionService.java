package server.services.interfaces;

import server.data.models.Session;
import server.data.models.User;

public interface ISessionService {
    int addSession(User user);
    Session getValidSession(int sessionId);
    boolean isValidSession(int sessionId);
}
