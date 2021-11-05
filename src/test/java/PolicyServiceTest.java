import org.junit.jupiter.api.Test;
import server.services.PolicyService;
import server.services.RolesService;
import shared.dto.Roles;

import static org.junit.jupiter.api.Assertions.*;

public class PolicyServiceTest {
    @Test
    public void basic(){
        PolicyService service = new PolicyService("src/main/java/server/policies.csv");
        service.load();

        assertTrue(service.haveAccess("Bob", "Print"));
        assertFalse(service.haveAccess("Bob", "Queue"));
    }

    @Test
    public void basicRoles(){
        RolesService service = new RolesService("src/main/java/server/");
        service.load();

        assertTrue(service.haveAccess("Bob", "Print"));
        assertFalse(service.haveAccess("Bob", "Queue"));
    }
}
