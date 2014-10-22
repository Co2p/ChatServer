package eson.co2p.se;

import javax.naming.ldap.UnsolicitedNotification;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Tony on 22/10/2014.
 */
public class ServerFirstClientThread {
    Socket CurrentConnection;
    ServerSocket ClientScann;
    int port;
    byte[] messageByte = new byte[1000];

    public ServerFirstClientThread(Socket Current_Connection, ServerSocket ClientScan, int Port) throws IOException {
        while(!catalogue.setClientListenerStatus(true)){;}
        System.out.println("updated lol");
        CurrentConnection = Current_Connection;
        ClientScann = ClientScan;
        port = Port;
        System.out.println("start scan for clients");
        ScanForClients();
    }
    private void ScanForClients() throws IOException {
        while(true) {
            try {
                CurrentConnection = ClientScann.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }

            DataInputStream Input = null;

            try {
                Input = new DataInputStream(CurrentConnection.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

            int bytesRead = Input.read(messageByte);
            if(bytesRead > 8) {
                System.out.println("client found! "+ bytesRead);
                final Thread clientthread;
                clientthread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            new ClientThread(messageByte,CurrentConnection, ClientScann);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                clientthread.start();
                System.out.println("pased port to new client! new client thread started");
                while(!catalogue.setClientListenerStatus(false)){;}

                break;
            }else {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    System.out.println("not able to sleep: " + e);
                }
            }
        }
    }
}
