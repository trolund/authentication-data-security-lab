package shared;

import javassist.NotFoundException;
import shared.dto.Job;
import shared.dto.*;
import shared.exceptions.NotStarted;
import shared.exceptions.Unauthorized;

import java.rmi.RemoteException;
import java.util.Collection;

public interface IPrintServer extends java.rmi.Remote {

    void print(DataPacked<PrintParams> params) throws RemoteException, Unauthorized, NotFoundException, NotStarted;   // prints file filename on the specified printer
    Collection<Job> queue(DataPacked<QueueParams> params) throws RemoteException, Unauthorized, NotFoundException, NotStarted;   // lists the print queue for a given printer on the user's display in lines of the form <job number>   <file name>
    void topQueue(DataPacked<TopQueueParams> params) throws RemoteException, Unauthorized, NotFoundException, NotStarted;   // moves job to the top of the queue
    void start(DataPacked<Object> params) throws RemoteException, Unauthorized, NotStarted;   // starts the print server
    void stop(DataPacked<Object> params) throws RemoteException, Unauthorized, NotStarted;   // stops the print server
    void restart(DataPacked<Object> params) throws RemoteException, Unauthorized, NotStarted;   // stops the print server, clears the print queue and starts the print server again
    String status(DataPacked<StatusParams> params) throws RemoteException, Unauthorized, NotFoundException, NotStarted;  // prints status of printer on the user's display
    String readConfig(DataPacked<String> params) throws RemoteException, Unauthorized, NotStarted;   // prints the value of the parameter on the user's display
    void setConfig(DataPacked<SetConfigParams> params) throws RemoteException, Unauthorized, NotStarted; // sets the parameter to value
    String login(DataPacked<Credentials> params) throws RemoteException, NotFoundException, Unauthorized; // use username and password to obtain token.
    void logout(DataPacked<Object> params) throws RemoteException, NotStarted, Unauthorized; // use token to signout.

}
