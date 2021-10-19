package server;

import server.services.ISessionService;
import server.services.SessionService;
import shared.Credentials;
import shared.DataPacked;
import shared.IAuth;
import shared.IPrintServer;
import shared.exceptions.Unauthorized;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Locale;

public class AuthMiddelware implements InvocationHandler, Serializable {

    private IPrintServer obj;
    private ISessionService sessionService;

    public static IPrintServer newInstance(IPrintServer obj) throws RemoteException {
        return (IPrintServer) java.lang.reflect.Proxy.newProxyInstance(obj.getClass().getClassLoader(),
                obj.getClass().getInterfaces(), new AuthMiddelware(obj));
    }

    private AuthMiddelware(IPrintServer obj) throws RemoteException {
        super();
        this.obj = obj;
        sessionService = new SessionService();
    }

    @Override
    public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
        Object result;
        try {
           if(!m.getName().toLowerCase().contains("login")){
               if (m.getName().startsWith("function")) {
                   IAuth o = (IAuth) args[0];
                   int sessionId = o.getToken();
                   Auth(sessionId);
               }
           }
           result = m.invoke(obj, args);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        } catch (Exception e) {
            throw new RuntimeException("unexpected invocation exception: " + e.getMessage());
        }
        return result;
    }

    private void Auth(int sessionId) throws Unauthorized {
        if(!sessionService.isValidSession(sessionId)){
            System.out.println("Invalid session id, id: " + sessionId);
            throw new Unauthorized("Invalid session id, id: " + sessionId);
        }
        System.out.println("Session id validted, id: " + sessionId);
    }

}
