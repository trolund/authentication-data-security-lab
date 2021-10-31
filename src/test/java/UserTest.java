import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import server.data.models.User;
import server.services.PasswordService;
import server.services.UserService;
import server.services.interfaces.IPasswordService;
import server.services.interfaces.IUserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserTest {

    private IUserService userService;
    private IPasswordService passwordService;


    public UserTest() {
        passwordService = new PasswordService();
        userService = new UserService();
    }

    @BeforeAll
    public static void init(){

    }

    @Test
    void CrudUser() {
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
    }
}
