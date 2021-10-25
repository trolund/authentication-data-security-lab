package server;

import javassist.NotFoundException;
import server.data.mocking.IMockUserData;
import server.data.mocking.MockUserData;
import server.data.models.Job;
import server.data.models.Session;
import server.data.models.User;
import server.services.*;
import server.services.interfaces.ILogService;
import server.services.interfaces.ISessionService;
import server.services.interfaces.IUserService;
import shared.Credentials;
import shared.DataPacked;
import shared.IPrintServer;
import shared.dto.*;
import shared.exceptions.Unauthorized;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.PriorityBlockingQueue;

public class PrintServer extends UnicastRemoteObject implements IPrintServer {

    private final PasswordService passwordService;
    private final IUserService userService;
    private final ISessionService sessionService;
    private final ArrayList<String> inMemoryLog;
    private final HashMap<String, String> config;
    private final Queue<Job> queue;
    private final ILogService logService;

    public PrintServer() throws RemoteException {
        userService = new UserService();
        passwordService = new PasswordService();
        sessionService = new SessionService();
        logService = new LogService();
        inMemoryLog = new ArrayList();
        config = new HashMap<>();
        queue = new PriorityBlockingQueue<>();

        // seeding user data
        IMockUserData mockUserData = new MockUserData();
        mockUserData.createMockUsers();
    }

    @Override
    public synchronized void print(DataPacked<PrintParams> params) throws Unauthorized {
        User u = processRequest(params.getToken(), "print");
        serverLog("The file: " + params.getPayload().getFilename() + " is being printed on: " + params.getPayload().getPrinter(), u.getUserId());
    }

    @Override
    public synchronized Collection<Job> queue(DataPacked<QueueParams> params) throws Unauthorized {
        User u = processRequest(params.getToken(), "queue");
        return queue;
    }

    @Override
    public synchronized void topQueue(DataPacked<TopQueueParams> params) throws Unauthorized {
        User u = processRequest(params.getToken(), "topQueue");
  /*      int num = params.getPayload().getJob();
        String printer = params.getPayload().getPrinter();

        queue.add(new Job());
        Collections.swap(queue, url.indexOf(itemToMove), 0);*/
    }

    @Override
    public synchronized void start(DataPacked<Object> params) throws Unauthorized {
        User u = processRequest(params.getToken(), "start");
        serverLog("startet print server", u.getUserId());
    }

    @Override
    public synchronized void stop(DataPacked<Object> params) throws Unauthorized {
        User u = processRequest(params.getToken(), "stop");
        serverLog("stopped print server", u.getUserId());
    }

    @Override
    public synchronized void restart(DataPacked<Object> params) throws Unauthorized {
        User u = processRequest(params.getToken(), "restart");
        serverLog("Printer have restarted.", u.getUserId());
    }

    @Override
    public synchronized String status(DataPacked<StatusParams> params) throws Unauthorized {
        User u = processRequest(params.getToken(), "status");
        return inMemoryLog.get(inMemoryLog.size()-1);
    }

    @Override
    public synchronized void readConfig(DataPacked<String> params) throws Unauthorized {
        User u = processRequest(params.getToken(), "start");
        String value = config.get(params.getPayload());
        serverLog("config: [ value: " + params.getPayload() + " = " + value + " ]", u.getUserId());
    }

    @Override
    public synchronized void setConfig(DataPacked<SetConfigParams> params) throws Unauthorized {
        User u = processRequest(params.getToken(), "setConfig");
            config.put(params.getPayload().getParameter(), params.getPayload().getValue());
            serverLog("config have been set on: " + params.getPayload().getParameter(), u.getUserId());
    }

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

    private User processRequest(int sessionId, String msg) throws Unauthorized {
        Session s = sessionService.getValidSession(sessionId);

        // check users sessionid.
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
