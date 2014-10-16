package eson.co2p.se;

import java.net.InetAddress;
import java.util.ArrayList;

/**
 * Created by gordon on 15/10/14.
 */
public class catalogue {

    private static int idNumber;
    private static boolean keepAlive;
    private static ArrayList<server> clients = new ArrayList<server>();
    private static String name;
    private static server nameServer = new server();
    private static server thisServer = new server();

    catalogue(){}

    /**
     * Sets the ID number that the client received from the server
     * @param idNumber the ID number
     */
    public static void setIdNumber(int idNumber) {
        catalogue.idNumber = idNumber;
    }

    /**
     * Decides if the server should stay alive
     * @param bool set true to let the server continue
     */
    public static void keepAlive(boolean bool){
        catalogue.keepAlive = bool;
    }

    public static void addClient(server client){
        clients.add(client);
    }

    public static void addClient(InetAddress ip, int port, String name){
        server client = new server();
        client.setIp(ip);
        client.setPort(port);
        client.setName(name);

        addClient(client);
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
     * Decides if the server should stay alive
     * @return is true to keep the server alive
     */
    public static boolean keepAlive(){
        return keepAlive;
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

    /**
     * Returns this server as a server object
     * @return this server object
     */
    public static server getServer(){
        return thisServer;
    }

    /**
     * Returns this servers InetAddress
     * @return this servers InetAddress
     */
    public static InetAddress getServerInet(){
        return thisServer.getIp();
    }

    /**
     * Returns this servers active port
     * @return this servers port
     */
    public static int getServerPort(){
        return thisServer.getPort();
    }

}
