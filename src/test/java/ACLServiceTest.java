import org.junit.jupiter.api.Test;
import server.services.ACLService;
import server.services.RBACService;

import static org.junit.jupiter.api.Assertions.*;

public class ACLServiceTest {
    @Test
    public void basic(){
        ACLService service = new ACLService("src/main/java/server/policies.csv");
        service.load();

        assertTrue(service.haveAccess("bob", "print"));
        assertFalse(service.haveAccess("bob", "queue"));
    }

    @Test
    public void basicRoles(){
        RBACService service = new RBACService("src/main/java/server/");
        service.load();

        assertTrue(service.haveAccess("bob", "print"));
        assertFalse(service.haveAccess("bob", "queue"));
    }
}
