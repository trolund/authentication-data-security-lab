package server;

import server.data.models.Session;
import server.services.LogService;
import server.services.interfaces.ILogService;
import server.services.interfaces.ISessionService;
import server.services.SessionService;
import shared.IAuth;
import shared.IPrintServer;
import shared.exceptions.Unauthorized;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class AuthMiddelware implements InvocationHandler, Serializable {

    private final IPrintServer obj;
    private final ISessionService sessionService;
    private final ILogService logService;

    public static IPrintServer newInstance(IPrintServer obj) {
        return (IPrintServer) java.lang.reflect.Proxy.newProxyInstance(obj.getClass().getClassLoader(),
                obj.getClass().getInterfaces(), new AuthMiddelware(obj));
    }

    private AuthMiddelware(IPrintServer obj){
        this.obj = obj;
        sessionService = new SessionService();
        logService = new LogService();
        System.out.println(" -- Setup of Auth middelware -- ");
    }

    @Override
    public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
        Object result;
        try {
            // run auth on all other then login and logout.
           if(!m.getName().contains("login")){
               IAuth o = (IAuth) args[0];
               int sessionId = o.getToken();
               processRequest(sessionId, m.getName());
           }
           result = m.invoke(obj, args);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }

        return result;
    }

    private void processRequest(int sessionId, String action) throws Unauthorized {
        Session s = sessionService.getValidSession(sessionId);
        System.out.println(s);

        // check users sessionid.
        if(s != null){
            System.out.println("Invalid session id, id: " + sessionId);
            throw new Unauthorized("Invalid session id, id: " + sessionId);
        }
        // System.out.println("Session id validted, id: " + sessionId);
        // log user activity
        // logService.addToServerLog(s);

    }

}
