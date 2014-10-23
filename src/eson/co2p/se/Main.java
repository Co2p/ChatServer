package eson.co2p.se;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Main
 * The main method used for setting all server values, calling the nameserver
 * and starting the serverobject
 * @author Isidor Tony and Gordon
 */
public class Main {
    //  Default values
    private static String nameServerAddress = "itchy.cs.umu.se";
    private static int nameServerPort = 1337;
    private static String serverName = "GIT's dödsserver för öppet homosexuella";
    private static int serverPort = 8888;

    /**
     * Main method, handles input from terminal and adds the nameserver, calls the method for
     * registering to the nameserver and then creates a new serverobject,
     *
     * @param args  Nameserver (IP or hostname), Nameserver Port, Name of server and port of server
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        getArgs(args);

        // create a reg server object and start the function to keep the server alive

        RegNameServer RegServer = new RegNameServer();
        RegServer.regserver();


        server ThisServer = new server();
        while(ThisServer.StartServer()){;} //UEeWhatIDidThere?
    }

    /**
     * Gets the args from main and checks so each value is the correct value
     * if any wrong value is inserted than use the default values
     * @param args  the args entered as the server is started
     */
    private static void getArgs(String[] args){
        if(args.length == 4) {
            nameServerAddress = args[0];
            nameServerPort = Integer.parseInt(args[1]);
            serverName = args[2];
            serverPort = Integer.parseInt(args[3]);
        }else{
            System.out.println("Wrong args entered, should be:'Nameserver address, " +
                    "Nameserver port, name of server, port of server'");
            System.out.println("Default values used");
        }
        //  sets nameserver address and port
        try {
            catalogue.setNameServer(InetAddress.getByName(nameServerAddress), nameServerPort);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        //  sets name and port of server
        catalogue.setName(serverName);
        catalogue.setPort(serverPort);
    }
}
