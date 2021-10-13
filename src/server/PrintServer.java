package server;

import shared.Credentials;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class PrintServer extends UnicastRemoteObject implements IPrintServer {

    protected PrintServer() throws RemoteException {
    }

    @Override
    public void print(String filename, String printer) {

    }

    @Override
    public ArrayList<String> queue(String printer) {
        return null;
    }

    @Override
    public void topQueue(String printer, int job) {

    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void restart() {

    }

    @Override
    public void status(String printer) {

    }

    @Override
    public void readConfig(String parameter) {

    }

    @Override
    public void setConfig(String parameter, String value) {

    }

    @Override
    public synchronized String login(Credentials credentials) {
        return "Dette er en token";
    }

    @Override
    public void logout(String token) {

    }
}
