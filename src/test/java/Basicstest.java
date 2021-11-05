import client.Cli;
import org.junit.jupiter.api.Test;
import server.AuthMethod;
import server.PrintServer;

import static org.junit.jupiter.api.Assertions.*;

public class Basicstest {

    @Test
    void printTest() {
        try{
            PrintServer printServer = new PrintServer(AuthMethod.policies);
            Cli cli = new Cli(printServer);

            cli.Command("print -f filename.pdf -p office_printer");

            assertFalse(printServer.getLatestInMemoryLog().contains("ERROR"));
            assertTrue(printServer.getLatestInMemoryLog().contains("office_printer"));
            assertTrue(printServer.getLatestInMemoryLog().contains("filename.pdf"));
        }catch (Exception e){

        }
    }
}
