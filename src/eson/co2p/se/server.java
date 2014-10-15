package eson.co2p.se;

import java.net.InetAddress;

/**
 * Created by gordon on 15/10/14.
 */
public class server {
    private InetAddress ip;
    private String name;
    private int port;
    private int connected;

    public server(){
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
