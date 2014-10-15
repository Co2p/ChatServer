package eson.co2p.se;

import java.net.InetAddress;

/**
 * Created by gordon on 15/10/14.
 */
public class catalogue {

    private static int idNumber;
    private static String nick;
    private static server server = new server();
    private static server nameServer = new server();

    catalogue(){}

    /**
     * Sets the ID number that the client received from the server
     * @param idNumber the ID number
     */
    public void setIdNumber(int idNumber) {
        this.idNumber = idNumber;
    }

    /**
     * Sets the user name
     * @param name user name
     */
    public void setName(String name) {
        this.nick = name;
    }

    /**
     * Set the name server properties
     * @param ip server address
     * @param port server port
     */
    public void setNameServer(InetAddress ip, int port){
        nameServer.setIp(ip);
        nameServer.setPort(port);
    }

    /**
     * Set the server properties
     * @param ip server adress
     * @param port server port
     */
    private void setServer(InetAddress ip, int port){
        server.setIp(ip);
        server.setPort(port);
    }

    /**
     * Returns the client ID number
     * @return ID number
     */
    public int getIdNumber() {
        return idNumber;
    }

    /**
     * Returns the user name
     * @return user name
     */
    public String getNick() {
        return nick;
    }

    /**
     * Returns the nameserver as a server object
     * @return the server object
     */
    public server getNameServer(){
        return nameServer;
    }

    public InetAddress getNameServerInet(){
        return nameServer.getIp();
    }

    public int getNameServerPort(){
        return nameServer.getPort();
    }

    public server getServer(){
        return server;
    }

    public InetAddress getServerInet(){
        return server.getIp();
    }

    public int getServerPort(){
        return server.getPort();
    }
}
