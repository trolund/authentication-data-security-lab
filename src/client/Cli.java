package client;

import javassist.NotFoundException;
import shared.Colors;
import shared.Credentials;
import shared.DataPacked;
import shared.IPrintServer;
import shared.dto.PrintParams;
import shared.exceptions.Unauthorized;

import java.rmi.RemoteException;
import java.util.Scanner;

public class Cli {

    private final IPrintServer ps;
    private final CommandHelper helper;
    private int sessionID;

    public Cli(IPrintServer ps) {
        this.ps = ps;
        helper = new CommandHelper();
    }

    public void beginCliSession(){
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
            }catch (Unauthorized e){
                System.err.println("Operation was unauthorized.");
            } catch (Exception e){
                System.err.println("Operation failed.");
                e.printStackTrace();
            }
        }
    }

    public void Command(String command) throws RemoteException, Unauthorized, NotFoundException {

        String[] args = command.split(" ");
        switch (args[0]){
            case "hey":
                System.out.println("Hello my friend");
                break;
            case "print":
                String fileName = helper.getArgValue(args,"-f");
                String printerName = helper.getArgValue(args,"-p");

                ps.print(new DataPacked(sessionID, new PrintParams(fileName,printerName)));
                break;
        }
    }

    private boolean login(Scanner input){
        Credentials c = new Credentials();

        System.out.print(Colors.ANSI_BLUE + "email: " + Colors.ANSI_RESET); c.setUsername(input.nextLine());
        System.out.print(Colors.ANSI_BLUE + "Password: " + Colors.ANSI_RESET); c.setPassword(input.nextLine());

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
}
