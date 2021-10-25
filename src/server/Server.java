package server;


import server.transport.SslClientSocketFactory;
import server.transport.SslServerSocketFactory;
import shared.Colors;
import shared.DataPacked;
import shared.IPrintServer;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {

    public static int port = 3000;
    public static String domain = "localhost";
    public static String serviceName = "print-server";
    public static String url = "rmi://" + domain + ":" + port + "/" + serviceName;

    public static void main(String[] arg) throws Exception {
        // Secure channel Factories
        SslClientSocketFactory csf = new SslClientSocketFactory("client", "clientpw");
        SslServerSocketFactory ssf = new SslServerSocketFactory("registry", "registrypw");

        Registry registry = LocateRegistry.createRegistry(port, csf, ssf);

        // Service with authentication middelware.
        IPrintServer service = new PrintServer();

        // bind service
        registry.rebind(url, service);

        System.out.print(Colors.ANSI_GREEN + "print-server started on : " + Colors.ANSI_RESET);
        System.out.print(Colors.ANSI_BLUE + url + Colors.ANSI_RESET);
        System.out.println();
    }
}
