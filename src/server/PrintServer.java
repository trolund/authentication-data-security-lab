package server;

import javassist.NotFoundException;
import server.data.mocking.IMockUserData;
import server.data.mocking.MockUserData;
import shared.dto.Job;
import server.data.models.Session;
import server.data.models.User;
import server.printer.IPrinter;
import server.printer.Printer;
import server.services.*;
import server.services.interfaces.ILogService;
import server.services.interfaces.ISessionService;
import server.services.interfaces.IUserService;
import shared.Credentials;
import shared.DataPacked;
import shared.IPrintServer;
import shared.dto.*;
import shared.exceptions.NotStarted;
import shared.exceptions.Unauthorized;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class PrintServer extends UnicastRemoteObject implements IPrintServer {

    private final PasswordService passwordService;
    private final IUserService userService;
    private final ISessionService sessionService;
    private final ArrayList<String> inMemoryLog;
    private final HashMap<String, String> config;
    private final List<IPrinter> printers;
    private final ILogService logService;
    private static boolean isStarted = true;

    public PrintServer() throws RemoteException {
        userService = new UserService();
        passwordService = new PasswordService();
        sessionService = new SessionService();
        logService = new LogService();
        inMemoryLog = new ArrayList();
        config = new HashMap<>();
        printers = new ArrayList<>();

        printers.add(new Printer("303A"));
        printers.add(new Printer("101"));
        printers.add(new Printer("308"));

        // seeding user data
        IMockUserData mockUserData = new MockUserData();
        mockUserData.createMockUsers();
    }

    // done
    @Override
    public synchronized void print(DataPacked<PrintParams> params) throws Unauthorized, NotFoundException, NotStarted {
        User u = processRequest(params.getToken(), "print");
        IPrinter printer = getPrinter(params.getPayload().getPrinter(), u.getUserId());

        // send job to printer
        printer.addJob(new Job(params.getPayload().getFilename(), params.getPayload().getPrinter()));

        serverLog("The file: " + params.getPayload().getFilename() + " will be printed on: " + params.getPayload().getPrinter(), u.getUserId());
    }

    // done
    @Override
    public synchronized Collection<Job> queue(DataPacked<QueueParams> params) throws Unauthorized, NotFoundException, NotStarted {
        User u = processRequest(params.getToken(), "queue");
        IPrinter printer = getPrinter(params.getPayload().getPrinter(), u.getUserId());

        return printer.getQueue();
    }

    @Override
    public synchronized void topQueue(DataPacked<TopQueueParams> params) throws Unauthorized, NotFoundException, NotStarted {
        User u = processRequest(params.getToken(), "topQueue");
        IPrinter printer = getPrinter(params.getPayload().getPrinter(), u.getUserId());

        printer.moveOnTop(params.getPayload().getJob());
    }

    // done
    @Override
    public synchronized void start(DataPacked<Object> params) throws Unauthorized, NotStarted {
        User u = processRequest(params.getToken(), "start");
        isStarted = true;
        serverLog("startet print server", u.getUserId());
    }

    // done
    @Override
    public synchronized void stop(DataPacked<Object> params) throws Unauthorized, NotStarted {
        User u = processRequest(params.getToken(), "stop");
        isStarted = false;
        serverLog("stopped print server", u.getUserId());
        System.exit(0);
    }

    // done
    @Override
    public synchronized void restart(DataPacked<Object> params) throws Unauthorized, NotStarted {
        User u = processRequest(params.getToken(), "restart");
        try {
            isStarted = false;
            resetQueues();
            Thread.sleep(2000);
            isStarted = true;
            serverLog("Printer have restarted.", u.getUserId());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized String status(DataPacked<StatusParams> params) throws Unauthorized, NotFoundException, NotStarted {
        User u = processRequest(params.getToken(), "status");
        IPrinter printer = getPrinter(params.getPayload().getPrinter(), u.getUserId());

        return printer.getStatus();
    }

    // done
    @Override
    public synchronized String readConfig(DataPacked<String> params) throws Unauthorized, NotStarted {
        User u = processRequest(params.getToken(), "start");
        String value = config.get(params.getPayload());
        String conf = "config: [ value: " + params.getPayload() + " = " + value + " ]";
        serverLog(conf, u.getUserId());
        return conf;
    }

    // done
    @Override
    public synchronized void setConfig(DataPacked<SetConfigParams> params) throws Unauthorized, NotStarted {
        User u = processRequest(params.getToken(), "setConfig");
        config.put(params.getPayload().getParameter(), params.getPayload().getValue());
        serverLog("config have been set on: " + params.getPayload().getParameter(), u.getUserId());
    }

    // done
    @Override
    public synchronized int login(DataPacked<Credentials> params) throws NotFoundException, Unauthorized {
            String userName = params.getPayload().getUsername();
            String password = params.getPayload().getPassword();

            User user = userService.getUser(userName);

            if(user != null){
                if(passwordService.correctPassword(password, user.getPassword())){
                    int sessionID = sessionService.addSession(user);

                    serverLog("User: " + user.getEmail() + " was login and have session id: " + sessionID, user.getUserId());
                    return sessionID;
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
        Optional<IPrinter> p = printers.stream().filter((x) -> x.getName() == name).findAny();
        IPrinter printer = p.get();

        if(printer == null){
            String msg = "The printer " + name + " was not found.";
            serverLog(msg, userID, true);
            throw new NotFoundException(msg);
        }

        return printer;
    }

    public ArrayList<String> getInMemoryLog() {
        return inMemoryLog;
    }

    public String getLatestInMemoryLog() {
        return inMemoryLog.get(inMemoryLog.size()-1);
    }

    @Override
    public synchronized void logout(DataPacked<Object> params) {

    }

    private void serverLog(String msg, int userID){
        serverLog(msg, userID,false);
    }

    private User processRequest(int sessionId, String msg) throws Unauthorized, NotStarted {
        Session s = sessionService.getValidSession(sessionId);

        if(!isStarted){
            String logMsg = "Server have NOT been started. please run the start command.";
            serverLog(logMsg, s.getUser().getUserId(), true);
            throw new NotStarted(logMsg);
        }

        // check users sessionID.
        if(s == null){
            serverLog("Invalid session id, id: " + sessionId, s.getUser().getUserId(), true);
            throw new Unauthorized("Invalid session id, id: " + sessionId);
        }

        // log user activity
        serverLog(msg, s.getUser().getUserId());

        return s.getUser();
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
}
