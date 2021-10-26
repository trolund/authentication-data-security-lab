package client;

import javassist.NotFoundException;
import shared.Colors;
import shared.Credentials;
import shared.DataPacked;
import shared.IPrintServer;
import shared.dto.*;
import shared.exceptions.NotStarted;
import shared.exceptions.Unauthorized;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;
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

        System.out.println(Colors.ANSI_GREEN + "login successful" + Colors.ANSI_RESET);
        System.out.println();

        System.out.println(Colors.ANSI_CYAN + "Welcome to the print server CLI interface: " + Colors.ANSI_RESET);
        while (true){
            try {
                Thread.sleep(100);
                System.out.print(">> "); Command(input.nextLine());
            }catch (Unauthorized e){
                System.err.println("Operation was unauthorized.");
            }catch (NotFoundException e){
                System.err.println("The printer was not found.");
            }catch (NotStarted e){
                System.err.println("The printer-server have not been started jet, use the start command before any further actions.");
            } catch (Exception e){
                System.err.println("Operation failed.");
            }
        }
    }

    public void Command(String command) throws RemoteException, Unauthorized, NotFoundException, NotStarted {

        String[] args = command.split(" ");
        switch (args[0]){
            case "start":
                ps.start(new DataPacked(sessionID, null));
                System.out.println("Server has started");
                break;
            case "stop":
                ps.stop(new DataPacked(sessionID, null));
                System.out.println("Server will be stopped");
                break;
            case "restart":
                ps.restart(new DataPacked(sessionID, null));
                System.out.println("Server will be restarted in a moment");
                break;
            case "status":
                String printer = helper.getArgValue(args,"-p");
                String status = ps.status(new DataPacked(sessionID, new StatusParams(printer)));
                System.out.println("[STATUS]"  + "[" + printer +"] : "  + status);
                break;
            case "readConfig":
                String option = helper.getArgValue(args,"-o");
                System.out.println(ps.readConfig(new DataPacked(sessionID, option)));
            case "setConfig":
                ps.setConfig(new DataPacked(sessionID, new SetConfigParams(helper.getArgValue(args,"-o"), helper.getArgValue(args,"-v"))));
            case "queue":
                Collection jobs = ps.queue(new DataPacked(sessionID, new QueueParams(helper.getArgValue(args,"-p"))));
                printJobs(jobs);
                break;
            case "top-queue":
                ps.topQueue(new DataPacked(sessionID,
                        new TopQueueParams(
                                helper.getArgValue(args,"-p"),
                                Integer.parseInt(helper.getArgValue(args,"-j")
                                )
                        )
                ));
                break;
            case "print":
                String fileName = helper.getArgValue(args,"-f");
                String printerName = helper.getArgValue(args,"-p");

                ps.print(new DataPacked(sessionID, new PrintParams(fileName,printerName)));
                break;
            default:
                System.out.println("Command: " + args[0] + " was not found");
        }
    }

    private void printJobs(Collection jobs){
        jobs.forEach(job -> System.out.println(job));
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
