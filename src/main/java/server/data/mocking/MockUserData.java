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
        add(new User("alice", "Alice","Doe", "Password123"));
        add(new User("bob", "Bob","Doe", "Password123"));
        add(new User("cecilia", "Cecilia","Doe", "Password123"));
        add(new User("david", "David","Doe", "Password123"));
        add(new User("erica", "Erica","Doe", "Password123"));
        add(new User("fred", "Fred","Doe", "Password123"));
        add(new User("george", "George","Doe", "Password123"));
        add(new User("henry", "Henry","Doe", "Password123"));
        add(new User("ida", "Ida","Doe", "Password123"));
    }};

    public MockUserData() {
        passwordService = new PasswordService();
        userService = new UserService();
    }

    public void createMockUsers(){
        for (User us: users) {
            String email = us.getUsername();
            String hashedPassword = passwordService.hashPassword(us.getHashedPassword());
            userService.deleteUser(email);

            int userId = userService.createUser(new User(
                    email,
                    us.getFirstName(),
                    us.getLastName(),
                    hashedPassword));
        }
    }
}
