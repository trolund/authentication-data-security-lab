package shared;

import javassist.NotFoundException;
import server.data.models.Job;
import shared.dto.*;
import shared.exceptions.Unauthorized;

import java.rmi.RemoteException;
import java.util.Collection;

public interface IPrintServer extends java.rmi.Remote {

    void print(DataPacked<PrintParams> params) throws RemoteException, Unauthorized;   // prints file filename on the specified printer
    Collection<Job> queue(DataPacked<QueueParams> params) throws RemoteException, Unauthorized;   // lists the print queue for a given printer on the user's display in lines of the form <job number>   <file name>
    void topQueue(DataPacked<TopQueueParams> params) throws RemoteException, Unauthorized;   // moves job to the top of the queue
    void start(DataPacked<Object> params) throws RemoteException, Unauthorized;   // starts the print server
    void stop(DataPacked<Object> params) throws RemoteException, Unauthorized;   // stops the print server
    void restart(DataPacked<Object> params) throws RemoteException, Unauthorized;   // stops the print server, clears the print queue and starts the print server again
    String status(DataPacked<StatusParams> params) throws RemoteException, Unauthorized;  // prints status of printer on the user's display
    void readConfig(DataPacked<String> params) throws RemoteException, Unauthorized;   // prints the value of the parameter on the user's display
    void setConfig(DataPacked<SetConfigParams> params) throws RemoteException, Unauthorized; // sets the parameter to value
    int login(DataPacked<Credentials> params) throws RemoteException, NotFoundException, Unauthorized; // use username and password to obtain token.
    void logout(DataPacked<Object> params) throws RemoteException; // use token to signout.

}
