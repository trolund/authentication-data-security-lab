package client;

import server.Server;
import shared.IPrintServer;
import shared.Credentials;


import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMIClientSocketFactory;
import java.util.Scanner;

public class Client {

    private static IPrintServer ps;
    private static String token;

    public static void main(String[] args) throws RemoteException {
        Connection<IPrintServer> con = new Connection();
        setSettings();

        RMIClientSocketFactory csf = new SslRMIClientSocketFactory();
        if(con.connect(csf)){
            ps = con.getConnection();
        }else{
            System.err.println("Failed to connect");
            return;
        }

        System.out.println("Print-server login: ");
        while (!login()){
            System.err.println("Password or username was wrong.");
        }
    }


    private static void setSettings() {
        String pass = "clientpw";
        System.setProperty("javax.net.ssl.debug", "all");
        System.setProperty("javax.net.ssl.keyStore", "src/shared/keys/client.ks");
        System.setProperty("javax.net.ssl.keyStorePassword", pass);
        System.setProperty("javax.net.ssl.trustStore", "src/shared/keys/client.ts");
        System.setProperty("javax.net.ssl.trustStorePassword", pass);
    }

    private static boolean login() {
        Scanner input = new Scanner(System.in);

        Credentials c = new Credentials();

        System.err.print("Username: ");
        c.setUsername(input.nextLine());
        System.err.print("Password: ");
        c.setPassword(input.nextLine());

        String token = null;
        try {
            token = ps.login(c);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return token != null;
    }

    public static void logStatus(IPrintServer ps) throws RemoteException {
        System.out.println("---------- ");
        // status
        System.out.println("---------- ");
    }

}