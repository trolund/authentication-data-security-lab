package server.services.interfaces;

import server.data.models.User;

public interface IUserService {
    User getUser(String email);
    int createUser(User u);
    void deleteUser(String email);
}
