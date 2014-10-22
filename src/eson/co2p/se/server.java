package eson.co2p.se;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by gordon on 15/10/14.
 */
public class server {
    private InetAddress ip;
    private String name;
    private int port;
    private int connected;
    private ServerSocket ClientScan = null;
    private Socket Current_Connection;

    public server(int Port) throws IOException {
        port = Port;
        try{
            ClientScan = new ServerSocket(port);
            System.out.println("Socket Created");
        }
        catch(Exception e){
        }
        ListenForClients();//TODO: en chekerklass som kör denna fler ggr vid behov, avoid ddos...
    }


    public void ListenForClients(){
        while(true){
            final Thread ClientAnswereThread;
            ClientAnswereThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        new ServerFirstClientThread(Current_Connection,ClientScan,port);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            ClientAnswereThread.start();
        }
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

    /**
     * Set how many clients are connected to the server
     * @param connected connected clients
     */
    public void setConnected(int connected){
        this.connected = connected;
    }

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

    /**
     * Get the number of connected clients to the server
     * @return connected clients
     */
    public int getConnected(){
        return this.connected;
    }
}
