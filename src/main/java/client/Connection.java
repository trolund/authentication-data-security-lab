package client;

import server.Server;
import server.transport.SslClientSocketFactory;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMIClientSocketFactory;

public class Connection<T> {

    private T s;
    private static int NUM_OF_RETRIES = 10;

    private synchronized boolean createConnection(RMIClientSocketFactory factory){
        try {
            Registry registry = LocateRegistry.getRegistry(Server.domain, Server.port, factory);

            s = (T) registry.lookup(Server.url);
            return true;
        } catch (NotBoundException e) {
            System.err.println("Failed to bound to the server.");
            // e.printStackTrace();
        } catch (RemoteException e) {
            System.err.println("Client failed to connect.");
           // e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public synchronized boolean connect() throws RuntimeException{
        try {
            if (!isConnected()) {
                int count = 0;

                SslClientSocketFactory csf = new SslClientSocketFactory("client", "clientpw");

                while (!createConnection(csf)) {
                    System.out.println("try to connect... (" + count + ")");
                    count++;

                    if (count >= NUM_OF_RETRIES) {
                        break;
                    }
                }

                if (count == NUM_OF_RETRIES) {
                    System.out.println("Make sure that the server have been started.");
                    throw new RuntimeException("Failed to connect");
                }
            } else {
                System.out.println("Connection is already active.");
            }

            return isConnected();
        }catch (Exception e){
            return false;
        }
    }

    public synchronized boolean isConnected(){
        return s != null;
    }

    public synchronized T getConnection(){
        return s;
    }

}
