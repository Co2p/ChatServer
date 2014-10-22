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
        catalogue.setName("Isidors Superbra server som inte funkar");
        RegNameServer RegServer = new RegNameServer();
        try {
            RegServer.regserver();
        } catch (SocketException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        System.out.println("got here1");
        server StartListen = new server(catalogue.getServerPort());
        while(true){;}







        /*
        catalogue.setName("Glorious Chat");
        try {
            catalogue.setNameServer(InetAddress.getByName("itchy.cs.umu.se"), 1337);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        final Thread nameServerCoThread;

        nameServerCoThread = new Thread(new Runnable() {
            @Override
            public void run() {
                new nameServerConnection();
            }
        });

        nameServerCoThread.start();
        */
    }
}
