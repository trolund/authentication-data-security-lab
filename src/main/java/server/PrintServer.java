package server;

import javassist.NotFoundException;
import server.data.mocking.IMockUserData;
import server.data.mocking.MockUserData;
import server.services.interfaces.IAuthService;
import shared.dto.Job;
import server.data.models.Session;
import server.data.models.User;
import server.printer.IPrinter;
import server.printer.Printer;
import server.services.*;
import server.services.interfaces.ILogService;
import server.services.interfaces.ISessionService;
import server.services.interfaces.IUserService;
import shared.dto.Credentials;
import shared.DataPacked;
import shared.IPrintServer;
import shared.dto.*;
import shared.exceptions.NotStarted;
import shared.exceptions.Unauthorized;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class PrintServer extends UnicastRemoteObject implements IPrintServer {

    private final PasswordService passwordService;
    private final IUserService userService;
    private final ISessionService sessionService;
    private final ArrayList<String> inMemoryLog;
    private final HashMap<String, String> config;
    private final List<IPrinter> printers;
    private final ILogService logService;
    private final IAuthService authService;
    private static boolean isStarted = false;
    private AuthMethod authMethod;

    public PrintServer(AuthMethod authMethod) throws RemoteException {
        this.authMethod = authMethod;
        userService = new UserService();
        passwordService = new PasswordService();
        sessionService = new SessionService();
        logService = new LogService();
        inMemoryLog = new ArrayList();
        config = new HashMap<>();
        printers = new ArrayList<>();

        authService = setupAuthService();
        authService.load();

        setupPrinters();

        // seeding user data
        IMockUserData mockUserData = new MockUserData();
        mockUserData.createMockUsers();
    }

    private IAuthService setupAuthService() {
        final IAuthService authService;
        if(authMethod == AuthMethod.ACL){
            authService = new ACLService("src/main/java/server/policies.csv");
        } else {
            authService = new RBACService("src/main/java/server/");
        }
        return authService;
    }

    // done
    @Override
    public synchronized void print(DataPacked<PrintParams> params) throws Unauthorized, NotFoundException, NotStarted {
        User u = processRequest(params.getToken(), "print");

        if(u == null){
            return;
        }

        IPrinter printer = getPrinter(params.getPayload().getPrinter(), u.getUserId());

        // send job to printer
        printer.addJob(new Job(params.getPayload().getFilename(), params.getPayload().getPrinter()));

        serverLog("The file: " + params.getPayload().getFilename() + " will be printed on: " + params.getPayload().getPrinter(), u.getUserId());
    }

    // done
    @Override
    public synchronized Collection<Job> queue(DataPacked<QueueParams> params) throws Unauthorized, NotFoundException, NotStarted {
        User u = processRequest(params.getToken(), "queue");

        if(u == null){
            return null;
        }

        IPrinter printer = getPrinter(params.getPayload().getPrinter(), u.getUserId());

        return printer.getQueue();
    }

    @Override
    public synchronized void topQueue(DataPacked<TopQueueParams> params) throws Unauthorized, NotFoundException, NotStarted {
        User u = processRequest(params.getToken(), "topQueue");

        if(u == null){
            return;
        }

        IPrinter printer = getPrinter(params.getPayload().getPrinter(), u.getUserId());

        printer.moveOnTop(params.getPayload().getJob());
    }

    // done
    @Override
    public synchronized void start(DataPacked<Object> params) throws Unauthorized, NotStarted {
        User u = processRequest(params.getToken(), "start");

        if(u == null){
            return;
        }

        isStarted = true;
        setupAuthService();
        serverLog("startet print server", u.getUserId());
    }

    // done
    @Override
    public synchronized void stop(DataPacked<Object> params) throws Unauthorized, NotStarted {
        User u = processRequest(params.getToken(), "stop");

        if(u == null){
            return;
        }

        isStarted = false;
        serverLog("stopped print server", u.getUserId());

        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(1000);
                System.exit(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    // done
    @Override
    public synchronized void restart(DataPacked<Object> params) throws Unauthorized, NotStarted {
        User u = processRequest(params.getToken(), "restart");

        if(u == null){
            return;
        }

        try {
            isStarted = false;
            resetQueues();
            Thread.sleep(2000);
            isStarted = true;
            setupAuthService();
            serverLog("Printer have restarted.", u.getUserId());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized String status(DataPacked<StatusParams> params) throws Unauthorized, NotFoundException, NotStarted {
        User u = processRequest(params.getToken(), "status");
        IPrinter printer = getPrinter(params.getPayload().getPrinter(), u.getUserId());

        if(u == null){
            return null;
        }

        return printer.getStatus();
    }

    // done
    @Override
    public synchronized String readConfig(DataPacked<String> params) throws Unauthorized, NotStarted {
        User u = processRequest(params.getToken(), "start");

        if(u == null){
            return null;
        }

        String value = config.get(params.getPayload());
        String conf = "config: [ value: " + params.getPayload() + " = " + value + " ]";
        serverLog(conf, u.getUserId());
        return conf;
    }

    // done
    @Override
    public synchronized void setConfig(DataPacked<SetConfigParams> params) throws Unauthorized, NotStarted {
        User u = processRequest(params.getToken(), "setConfig");

        if(u == null){
            return;
        }

        config.put(params.getPayload().getParameter(), params.getPayload().getValue());
        serverLog("config have been set on: " +
                params.getPayload().getParameter() +
                " with the value of: " +
                params.getPayload().getValue(),
                u.getUserId());
    }

    // done
    @Override
    public synchronized String login(DataPacked<Credentials> params) throws NotFoundException, Unauthorized {
            String userName = params.getPayload().getUsername();
            String password = params.getPayload().getPassword();

            User user = userService.getUser(userName);

            if(user != null){
                if(passwordService.correctPassword(password, user.getHashedPassword())){
                    String token = sessionService.addSession(user);

                    serverLog("User: " + user.getUsername() + " was login and have token: " + token, user.getUserId());
                    return token;
                }
                else{
                    serverLog("Password was incorrect.", user.getUserId(), true);
                    throw new Unauthorized("Password was incorrect.");
                }
            }else{
                serverLog("The user does not exist in the system.", user.getUserId(), true);
                throw new NotFoundException("The user does not exist in the system.");
            }
    }

    private void resetQueues(){
        printers.forEach(p -> p.reset());
    }

    private IPrinter getPrinter(String name, int userID) throws NotFoundException {
        IPrinter printer = printers.stream()
                        .filter((x) -> x.getName()
                        .equals(name))
                        .findAny()
                        .orElse(null);

        if(printer == null){
            String msg = "The printer " + name + " was not found.";
            serverLog(msg, userID, true);
            throw new NotFoundException(msg);
        }

        return printer;
    }

    public String getLatestInMemoryLog() {
        return inMemoryLog.get(inMemoryLog.size()-1);
    }

    @Override
    public synchronized void logout(DataPacked<Object> params) throws Exception {
        Session s = sessionService.getValidSession(params.getToken());

        if(s != null){
            sessionService.endSession(params.getToken());
            serverLog("logout user", s.getUser().getUserId(), true);
        }else{
            serverLog("Failed to logout user", -1, true);
            throw new Exception("Failed to logout");
        }
    }

    private void serverLog(String msg, int userID){
        serverLog(msg, userID,false);
    }

    private User processRequest(String token, String command) throws Unauthorized, NotStarted {
        if(token == null || token.length() == 0){
            serverLog("No token was given", -1, true);
            throw new Unauthorized("No token was given");
        }

        Session s = sessionService.getValidSession(token);
        try{

            if(!isStarted & command != "start"){
                String logMsg = "Server have NOT been started. please run the start command.";
                serverLog(logMsg, s.getUser().getUserId(), true);
                throw new NotStarted(logMsg);
            }

            // check users sessionID.
            if(s == null){
                serverLog("Invalid token, token: " + token, -1, true);
                throw new Unauthorized("Invalid token, token: " + token);
            }

            if(!authService.haveAccess(s.getUser().getUsername(), command)){
                throw new Unauthorized("Unauthorized access to command: " + command);
            }

            // log user activity
            serverLog(command, s.getUser().getUserId());

            return s.getUser();
        }catch (NotStarted e){
            serverLog(command + " failed because the server have not been started", s.getUser().getUserId(), true);
            throw e;
        }catch (Unauthorized e){
            serverLog(command + " failed because the user was unauthorized", s == null ? -1 : s.getUser().getUserId(), true);
            throw e;
        }
    }

    private void serverLog(String msg, int userID, boolean error){
        String es = "[ERROR] ";
        String info = "[INFO] ";
        String ui = "[" + userID +  "] ";
        inMemoryLog.add((error ? es : info) + msg);
        if(error){
            System.err.println(es + ui + msg);
            logService.addToServerLog(userID, es + msg);
        }else {
            System.out.println(info + ui + msg);
            logService.addToServerLog(userID, info + msg);
        }
    }

    private void setupPrinters(){
        Runnable p1 = new Printer("303A");
        Thread t1 = new Thread(p1);
        t1.start();

        Runnable p2 = new Printer("101");
        Thread t2 = new Thread(p2);
        t2.start();

        printers.add((IPrinter) p1);
        printers.add((IPrinter) p2);
    }
}
