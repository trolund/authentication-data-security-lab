package server;

import server.transport.SslClientSocketFactory;
import server.transport.SslServerSocketFactory;
import shared.Colors;
import shared.IPrintServer;

import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server {

    public static int port = 3000;
    public static String domain = "localhost";
    public static String serviceName = "print-server";
    public static String url = "rmi://" + domain + ":" + port + "/" + serviceName;

    public static void main(String[] arg) throws Exception {
        setSettings();

        Registry registry = LocateRegistry.createRegistry(3000,
                new SslRMIClientSocketFactory(),
                new SslRMIServerSocketFactory(null, null, true));

        IPrintServer printServer = new PrintServer();

        IPrintServer stub = (IPrintServer) UnicastRemoteObject.exportObject(printServer, 0);

        registry.bind(url, stub);
        System.out.print(Colors.ANSI_GREEN + "print-server started on : " + Colors.ANSI_RESET);
        System.out.print(Colors.ANSI_BLUE + url + Colors.ANSI_RESET);
    }

    private static void setSettings() {
        String serverPwd = "registrypw";

        System.setProperty("javax.net.ssl.debug", "all");

        System.setProperty("javax.net.ssl.keyStore", "src/shared/keys/registry.ks");
        System.setProperty("javax.net.ssl.keyStorePassword", serverPwd);
        System.setProperty("javax.net.ssl.trustStore", "src/shared/keys/registry.ts");
        System.setProperty("javax.net.ssl.trustStorePassword", serverPwd);
    }
}
