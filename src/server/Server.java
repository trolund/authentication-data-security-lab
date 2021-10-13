package server;

import server.transport.SslClientSocketFactory;
import server.transport.SslServerSocketFactory;
import shared.Colors;
import shared.IPrintServer;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {

    public static int port = 3000;
    public static String domain = "localhost";
    public static String serviceName = "print-server";
    public static String url = "rmi://" + domain + ":" + port + "/" + serviceName;

    public static void main(String[] arg) throws Exception {
        SslClientSocketFactory csf = new SslClientSocketFactory("client", "clientpw");
        SslServerSocketFactory ssf = new SslServerSocketFactory("registry", "registrypw");

        Registry registry = LocateRegistry.createRegistry(port, csf, ssf);

        IPrintServer s = new PrintServer();

        registry.rebind(url, s);
        System.out.print(Colors.ANSI_GREEN + "print-server started on : " + Colors.ANSI_RESET);
        System.out.print(Colors.ANSI_BLUE + url + Colors.ANSI_RESET);
    }
}
