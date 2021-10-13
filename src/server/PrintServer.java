package server;

import com.auth0.jwt.exceptions.JWTVerificationException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import server.data.HibernateUtil;
import server.data.models.Job;
import server.data.models.User;
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

    protected PrintServer() throws RemoteException {
        tokenService = new TokenService();
        session = HibernateUtil.getSessionFactory().openSession();
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
    public synchronized String login(DataPacked<Credentials> params) {
        try {
            return tokenService.createJWT(params.getPayload().getUsername());
        } catch (JWTVerificationException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public synchronized void logout(DataPacked<Object> params) {

    }
}
