import org.junit.jupiter.api.Test;
import server.services.PolicyService;
import shared.dto.Roles;

import static org.junit.jupiter.api.Assertions.*;

public class PolicyServiceTest {
    @Test
    public void basic(){
        PolicyService service = new PolicyService("src/main/java/server/policy.json");
        assertTrue(service.haveRole("bob", Roles.ADMIN));
        assertFalse(service.haveRole("bob", Roles.BASIC));
    }
}
