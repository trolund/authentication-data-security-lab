package server.services.interfaces;

import server.data.models.Session;
import server.data.models.User;

public interface ISessionService {
    String addSession(User user);
    Session getValidSession(Integer sessionId);
    boolean isValidSession(Integer sessionId);
    Session getValidSession(String token);
    boolean isValidSession(String token);
    void endSession(Integer sessionId);
    void endSession(String sessionId);
}
