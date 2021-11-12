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

        AuthMethod authMethod;

        if (arg.length > 0 && arg[0].equals("acl")) {
            authMethod = AuthMethod.ACL;
            System.out.println(Colors.ANSI_GREEN + "print-server config to use: ACL" + Colors.ANSI_RESET);
        } else if (arg.length > 0 && arg[0].equals("rbac")) {
            authMethod = AuthMethod.RBAC;
            System.out.println(Colors.ANSI_GREEN + "print-server config to use: RBAC" + Colors.ANSI_RESET);
        } else {
            System.out.println(Colors.ANSI_RED + "print-server config to use: RBAC (default)" + Colors.ANSI_RESET);
            authMethod = AuthMethod.RBAC;
        }

        // Secure channel factories
        SslClientSocketFactory csf = new SslClientSocketFactory("client", "clientpw");
        SslServerSocketFactory ssf = new SslServerSocketFactory("registry", "registrypw");

        Registry registry = LocateRegistry.createRegistry(port, csf, ssf);

        // create server object
        IPrintServer service = new PrintServer(authMethod);

        // bind service
        registry.rebind(url, service);

        System.out.print(Colors.ANSI_GREEN + "print-server started on : " + Colors.ANSI_RESET);
        System.out.print(Colors.ANSI_BLUE + url + Colors.ANSI_RESET);
        System.out.println();
    }
}
