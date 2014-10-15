package eson.co2p.se;

import java.net.InetAddress;

/**
 * Created by gordon on 15/10/14.
 */
public class catalogue {

    private static int idNumber;
    private static String name;
    private static server server = new server();
    private static server nameServer = new server();

    catalogue(){}

    /**
     * Sets the ID number that the client received from the server
     * @param idNumber the ID number
     */
    public static void setIdNumber(int idNumber) {
        catalogue.idNumber = idNumber;
    }

    /**
     * Sets the user name
     * @param name user name
     */
    public static void setName(String name) {
        catalogue.name = name;
    }

    /**
     * Set the name server properties
     * @param ip server address
     * @param port server port
     */
    public static void setNameServer(InetAddress ip, int port){
        nameServer.setIp(ip);
        nameServer.setPort(port);
    }

    /**
     * Set the server properties
     * @param ip server adress
     * @param port server port
     */
    private static void setServer(InetAddress ip, int port){
        server.setIp(ip);
        server.setPort(port);
    }

    /**
     * Returns the client ID number
     * @return ID number
     */
    public static int getIdNumber() {
        return idNumber;
    }

    /**
     * Returns the user name
     * @return user name
     */
    public static String getName() {
        return name;
    }

    /**
     * Returns the nameserver as a server object
     * @return the server object
     */
    public static server getNameServer(){
        return nameServer;
    }

    public static InetAddress getNameServerInet(){
        return nameServer.getIp();
    }

    public static int getNameServerPort(){
        return nameServer.getPort();
    }

    public static server getServer(){
        return server;
    }

    public static InetAddress getServerInet(){
        return server.getIp();
    }

    public static int getServerPort(){
        return server.getPort();
    }
}
