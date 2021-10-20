package client;

import javassist.NotFoundException;
import shared.Colors;
import shared.DataPacked;
import shared.IPrintServer;
import shared.Credentials;
import shared.dto.PrintParams;
import shared.exceptions.Unauthorized;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.OptionalInt;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Client {

    private static IPrintServer ps;
    private static String token;

    public static void main(String[] args) {
        // connect to server.
        connect();

        // the cli.
        Scanner input = new Scanner(System.in);

        System.out.println(Colors.ANSI_CYAN + "Skip login? [y: yes, n: no]: " + Colors.ANSI_RESET);
        boolean skip = input.nextLine().equals("y");

        if(!skip){
            System.out.println(Colors.ANSI_CYAN + "Print-server login: " + Colors.ANSI_RESET);
            while (!login(input));
        }

        System.out.println(Colors.ANSI_CYAN + "Welcome to the print server CLI interface: " + Colors.ANSI_RESET);
        while (true){
            try {
                System.out.print(">> "); Command(input.nextLine());
            }catch (Exception e){
                System.err.println("Operation failed.");
            }
        }
    }

    private static void Command(String command) throws RemoteException {
        String[] args = command.split(" ");
        switch (args[0]){
            case "hey":
                System.out.println("Hello my friend");
                break;
            case "print":
                String fileName = getArgValue(args,"-f");
                String printerName = getArgValue(args,"-p");

                ps.print(new DataPacked(new PrintParams(fileName,printerName)));
                break;
        }
    }

    private static String getArgValue(String[] args, String flag){
        int index = Arrays.stream(args)
                .map(x -> x.equals(flag))
                .collect(Collectors.toList())
                .indexOf(flag);

        return args[index+1];
    }

    private static void connect(){
        Connection<IPrintServer> con = new Connection<IPrintServer>();

        if(con.connect()){
            ps = con.getConnection();
        }else{
            System.err.println("Failed to connect");
            return;
        }
    }

    private static boolean login(Scanner input){
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