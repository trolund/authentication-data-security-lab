import org.junit.jupiter.api.Test;
import org.springframework.security.core.parameters.P;
import server.PrintServer;
import shared.DataPacked;
import shared.dto.SetConfigParams;
import shared.exceptions.NotStarted;
import shared.exceptions.Unauthorized;

import java.rmi.RemoteException;

public class anoTest {

    @Test
    public void test() throws NotStarted, Unauthorized, RemoteException {
        PrintServer p = new PrintServer();
        p.readConfig(new DataPacked("", "hej"));
    }
}
