package server;

import shared.Colors;

import java.rmi.Naming;

public class Server {

    public static int port = 3000;
    public static String url = "rmi://localhost:" + port + "/print-server";

    public static void main(String[] arg) throws Exception {
        java.rmi.registry.LocateRegistry.createRegistry(port);

        IPrintServer s = new PrintServer();

        Naming.rebind(url, s);
        System.out.print(Colors.ANSI_GREEN + "print-server started on : " + Colors.ANSI_RESET);
        System.out.print(Colors.ANSI_BLUE + url + Colors.ANSI_RESET);
    }
}
