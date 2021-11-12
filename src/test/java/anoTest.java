import org.junit.jupiter.api.Test;
import server.AuthMethod;
import server.PrintServer;
import shared.DataPacked;
import shared.exceptions.NotStarted;
import shared.exceptions.Unauthorized;

import java.rmi.RemoteException;

public class anoTest {

    @Test
    public void test() throws NotStarted, Unauthorized, RemoteException {
        PrintServer p = new PrintServer(AuthMethod.RBAC);
        p.readConfig(new DataPacked("", "hej"));
    }
}
