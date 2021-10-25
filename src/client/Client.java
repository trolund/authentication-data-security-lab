package client;

import shared.IPrintServer;

public class Client {

    private static IPrintServer ps;

    public static void main(String[] args) {
        // connect to server.
        connect();

        // the cli.
        Cli cli = new Cli(ps);
        cli.beginCliSession();
    }

    private static void connect(){
        Connection<IPrintServer> con = new Connection();

        if(con.connect()){
            ps = con.getConnection();
        }else{
            // System.err.println("Failed to connect");
            throw new RuntimeException("Failed to connect");
        }
    }

}