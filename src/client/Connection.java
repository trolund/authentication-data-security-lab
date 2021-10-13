package client;

import server.Server;
import server.transport.SslClientSocketFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

public class Connection<T> {

    private T s;
    private static int NUM_OF_RETRIES = 10;

    private boolean createConnection(){
        try {
            SslClientSocketFactory csf = new SslClientSocketFactory("client", "clientpw");
            Registry registry = LocateRegistry.getRegistry(Server.domain, Server.port, csf);

            s = (T) registry.lookup(Server.url);
            return true;
        } catch (NotBoundException e) {
            System.err.println("Failed to bound to the server.");
            // e.printStackTrace();
        } catch (MalformedURLException e) {
            System.err.println("Malformed URL.");
            // e.printStackTrace();
        } catch (RemoteException e) {
            System.err.println("Server failed to start.");
           // e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
