package eson.co2p.se;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Call the server with
 */
public class Main {

    public static void main(String[] args) throws IOException {

        try {
            catalogue.setNameServer(InetAddress.getByName("itchy.cs.umu.se"), 1337);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        catalogue.setName("EN KUKSERVER");

        // creat an reg server object and start the function to keep the server alive

        RegNameServer RegServer = new RegNameServer();
        RegServer.regserver();


        server ThisServer = new server();
        while(ThisServer.StartServer()){;} //UEeWhatIDidThere?

    }
}
