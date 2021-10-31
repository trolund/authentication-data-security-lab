import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import server.data.models.Session;
import server.data.models.User;
import server.services.interfaces.ISessionService;
import server.services.PasswordService;
import server.services.SessionService;
import server.services.UserService;
import server.services.interfaces.IPasswordService;
import server.services.interfaces.IUserService;

import static org.junit.jupiter.api.Assertions.*;

public class SessionTest {

    private IUserService userService;
    private IPasswordService passwordService;
    private ISessionService sessionService;


    public SessionTest() {
        passwordService = new PasswordService();
        userService = new UserService();
        sessionService = new SessionService();
    }

    @BeforeAll
    public static void init(){

    }
    @Test
    void getSession(){
        String email = "trolund@gmail.com";
        String hashedPassword = passwordService.hashPassword("Password123");
        userService.deleteUser(email);

        Integer userId = userService.createUser(new User(
                email,
                "Troels",
                "Lund",
                hashedPassword));

        User u = userService.getUser(email);
        String token = sessionService.addSession(u);
        Session s = sessionService.getValidSession(token);

        assertNotNull(s);

        // logout
        sessionService.endSession(token);

        Session s2 = sessionService.getValidSession(token);

        assertNull(s2);
    }

    @Test
    void AddSessionToken() {
        String email = "trolund@gmail.com";
        String hashedPassword = passwordService.hashPassword("Password123");
        userService.deleteUser(email);

        Integer userId = userService.createUser(new User(
                email,
                "Troels",
                "Lund",
                hashedPassword));

        User u = userService.getUser(email);

        assertNotNull(userId);
        assertEquals(email, u.getUsername());
        assertEquals(hashedPassword, u.getHashedPassword());

        String token = sessionService.addSession(u);
        assertNotNull(token);

        Session s = sessionService.getValidSession(token);

        assertNotNull(s);

        assertTrue(sessionService.isValidSession(token));
    }
}
