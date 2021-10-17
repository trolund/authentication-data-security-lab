package server;

import com.auth0.jwt.exceptions.JWTVerificationException;
import javassist.NotFoundException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import server.data.HibernateUtil;
import server.data.models.Job;
import server.data.models.User;
import server.services.PasswordService;
import server.services.SeedingService;
import server.services.TokenService;
import shared.Credentials;
import shared.DataPacked;
import shared.IPrintServer;
import shared.dto.*;
import shared.exceptions.Unauthorized;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class PrintServer extends UnicastRemoteObject implements IPrintServer {

    private Session session;
    private TokenService tokenService;
    private PasswordService passwordService;

    protected PrintServer() throws RemoteException {
        tokenService = new TokenService();
        session = HibernateUtil.getSessionFactory().openSession();

        // seeding user data
        SeedingService seedingService = new SeedingService();
        seedingService.createMockUsers();
    }

    @Override
    public synchronized void print(DataPacked<PrintParams> params) {
        try {
            tokenService.verifyTokenThrowOnInvalid(params.getToken());

            System.out.println("The file': " + params.getPayload().getFilename() + " is being printed on: " + params.getPayload().getPrinter());
        } catch (Unauthorized unauthorized) {
            unauthorized.printStackTrace();
        }
    }

    @Override
    public synchronized ArrayList<Job> queue(DataPacked<QueueParams> params) {
        try {
            tokenService.verifyTokenThrowOnInvalid(params.getToken());

            return new ArrayList<>();
        } catch (Unauthorized unauthorized) {
            unauthorized.printStackTrace();
            return null;
        }
    }

    @Override
    public synchronized void topQueue(DataPacked<TopQueueParams> params) {
        try {
            tokenService.verifyTokenThrowOnInvalid(params.getToken());

        } catch (Unauthorized unauthorized) {
            unauthorized.printStackTrace();
        }
    }

    @Override
    public synchronized void start(DataPacked<Object> params) {
        try {
            tokenService.verifyTokenThrowOnInvalid(params.getToken());

        } catch (Unauthorized unauthorized) {
            unauthorized.printStackTrace();
        }
    }

    @Override
    public synchronized void stop(DataPacked<Object> params) {
        try {
            tokenService.verifyTokenThrowOnInvalid(params.getToken());

        } catch (Unauthorized unauthorized) {
            unauthorized.printStackTrace();
        }
    }

    @Override
    public synchronized void restart(DataPacked<Object> params) {
        try {
            tokenService.verifyTokenThrowOnInvalid(params.getToken());

        } catch (Unauthorized unauthorized) {
            unauthorized.printStackTrace();
        }
    }

    @Override
    public synchronized void status(DataPacked<StatusParams> params) {
        try {
            tokenService.verifyTokenThrowOnInvalid(params.getToken());

        } catch (Unauthorized unauthorized) {
            unauthorized.printStackTrace();
        }
    }

    @Override
    public synchronized void readConfig(DataPacked<StatusParams> params) {
        try {
            tokenService.verifyTokenThrowOnInvalid(params.getToken());

        } catch (Unauthorized unauthorized) {
            unauthorized.printStackTrace();
        }
    }

    @Override
    public synchronized void setConfig(DataPacked<SetConfigParams> params) {
        try {
            tokenService.verifyTokenThrowOnInvalid(params.getToken());

        } catch (Unauthorized unauthorized) {
            unauthorized.printStackTrace();
        }
    }

    @Override
    public synchronized String login(DataPacked<Credentials> params) throws NotFoundException, Unauthorized {
        try {
            String hql = "SELECT U.email FROM User U WHERE U.email = '" + params.getPayload().getUsername() + "'";

            Query<User> query = session.createQuery(hql);
            User user = query.getSingleResult();

            if(user != null){
                if(passwordService.correctPassword(params.getPayload().getPassword(), user.getPassword())){
                    return tokenService.createJWT(params.getPayload().getUsername());
                }
                else{
                    throw new Unauthorized("Password was incorrect.");
                }
            }else{
                throw new NotFoundException("The user does not exist in the system.");
            }

        } catch (JWTVerificationException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public synchronized void logout(DataPacked<Object> params) {

    }
}
