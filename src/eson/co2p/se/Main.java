package eson.co2p.se;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Call the server with
 */
public class Main {
    private static String nameServerAddress = "itchy.cs.umu.se";
    private static int nameServerPort = 1337;
    private static String serverName = "default";
    private static int serverPort = 8080;

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
