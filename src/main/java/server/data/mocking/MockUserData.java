package server.data.mocking;

import server.data.models.User;
import server.services.PasswordService;
import server.services.UserService;
import server.services.interfaces.IPasswordService;
import server.services.interfaces.IUserService;

import java.util.ArrayList;

public class MockUserData implements IMockUserData {

    private IPasswordService passwordService;
    private IUserService userService;

    private ArrayList<User> users = new ArrayList<User>() {{
        add(new User("t", "Troels","Lund", "1"));
    }};

    public MockUserData() {
        passwordService = new PasswordService();
        userService = new UserService();
    }

    public void createMockUsers(){
        for (User us: users) {
            String email = us.getEmail();
            String hashedPassword = passwordService.hashPassword(us.getPassword());
            userService.deleteUser(email);

            int userId = userService.createUser(new User(
                    email,
                    us.getFirstName(),
                    us.getLastName(),
                    hashedPassword));
        }
    }
}
