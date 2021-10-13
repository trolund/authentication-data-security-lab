package client;

import server.Server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Connection<T> {

    private T s;
    private static int NUM_OF_RETRIES = 10;

    private boolean createConnection(){
        try {
            s = (T) Naming.lookup(Server.url);
            return true;
        } catch (NotBoundException e) {
            System.out.println("Failed to bound to the server.");
            // e.printStackTrace();
        } catch (MalformedURLException e) {
            System.out.println("Malformed URL.");
            // e.printStackTrace();
        } catch (RemoteException e) {
            System.out.println("Server failed to start.");
           // e.printStackTrace();
        }
        return false;
    }

    public boolean connect(){
        if(!isConnected()){
            int count = 0;

            while (!createConnection()){
                System.out.println("try to connect... (" + count + ")");
                count++;

                if(count >= NUM_OF_RETRIES){
                    break;
                }
            }

            if(count == NUM_OF_RETRIES){
                System.out.println("Make sure that the server have been started.");
            }
        }else{
            System.out.println("Connection is already active.");
        }

        return isConnected();
    }

    public boolean isConnected(){
        return s != null;
    }

    public T getConnection(){
        return s;
    }

}
