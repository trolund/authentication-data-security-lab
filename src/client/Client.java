package client;

import javassist.NotFoundException;
import shared.Colors;
import shared.DataPacked;
import shared.IPrintServer;
import shared.Credentials;
import shared.exceptions.Unauthorized;


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

        System.out.println("Print-server login: ");
        while (!login());
    }

    private static boolean login(){
        Scanner input = new Scanner(System.in);

        Credentials c = new Credentials();

        System.out.print(Colors.ANSI_BLUE + "email: " + Colors.ANSI_RESET); c.setUsername(input.nextLine());
        System.out.print(Colors.ANSI_BLUE + "Password: " + Colors.ANSI_RESET); c.setPassword(input.nextLine());

        int sessionID = -1;
        try {
            sessionID = ps.login(new DataPacked(c));
            System.out.println();
        } catch (RemoteException e) {

            System.err.println("Connection failed.. RMI..........");
        } catch (NotFoundException e){
            System.err.println("there is no user with that email in the system. maybe the username was wrong?");
        } catch (Unauthorized e){
            System.err.println("Password was wrong.");
        }

        return sessionID != -1;
    }

    public static void logStatus(IPrintServer ps) throws RemoteException {
        System.out.println("---------- ");
        // status
        System.out.println("---------- ");
    }

}