package client;

import shared.DataPacked;
import shared.IPrintServer;
import shared.Credentials;


import java.rmi.RemoteException;
import java.util.Scanner;

public class Client {

    private static IPrintServer ps;
    private static String token;

    public static void main(String[] args) throws RemoteException {
        Connection<IPrintServer> con = new Connection<IPrintServer>();

        if(con.connect()){
            ps = con.getConnection();
        }else{
            System.err.println("Failed to connect");
            return;
        }

        ps.start();

        System.out.println("Print-server login: ");
        while (!login()){
            System.err.println("Password or username was wrong.");
        }
    }

    private static boolean login(){
        Scanner input = new Scanner(System.in);

        Credentials c = new Credentials();

        System.err.print("Username: "); c.setUsername(input.nextLine());
        System.err.print("Password: "); c.setPassword(input.nextLine());

        String token = null;
        try {
            token = ps.login(new DataPacked(c));
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