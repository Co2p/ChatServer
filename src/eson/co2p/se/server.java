package eson.co2p.se;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Main method for the server, handles setting the connection of the name-server
 * and handling the threads of connected users
 * @author Tony, isidor and gordon on 15/10/14.
 */
public class server {

    private InetAddress ip;
    private String name;
    private int port;

    public boolean StartServer(){
        //TODO Get port from correct place, lol
        ServerSocket serverSocket = null;
        port = 8888;

        //TODO FIX client counter...
        int nrofconnections = 0;
        int maxconnections = 256;

        try {
            serverSocket = new ServerSocket(port);
            //every time an connection is made, start an new clientthread.

            while (nrofconnections < maxconnections) {
                System.out.print("Listening for clients..\n");
                Socket ListeningSocket = serverSocket.accept();
                System.out.print("making thread\n");
                Thread ClientThread = new Thread(new ClientThread(ListeningSocket));

                catalogue.addThread(ClientThread);
                System.out.print("Starting thread\n");
                ClientThread.start();
                System.out.print("thread started!\n");
            }

        } catch (IOException exp) {
            exp.printStackTrace();
        } finally {
            try {
                serverSocket.close();
            } catch (Exception e) {
            }
        }
        return true;
    }
    /**
     * Set the server ip
     * @param ip the ip
     */
    public void setIp(InetAddress ip){
        this.ip = ip;
    }

    /**
     * Set the server name
     * @param name server name
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * Set the server port
     * @param port port number
     */
    public void setPort(int port){
        this.port = port;
    }

    /*/**
     ** Set how many clients are connected to the server
     ** @param connected connected clients
     *
      *public void setConnected(int connected){
      *  this.connected = connected;
    }*/

    /**
     * Returns the server ip
     * @return server ip
     */
    public InetAddress getIp(){
        return this.ip;
    }

    /**
     * Get the server name
     * @return server name
     */
    public String getName(){
        return this.name;
    }

    /**
     * Get the server port
     * @return server port
     */
    public int getPort(){
        return this.port;
    }

}
