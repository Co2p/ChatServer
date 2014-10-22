package eson.co2p.se;

import java.net.InetAddress;
import java.util.ArrayList;

/**
 * Saves states and information about the server
 * @author gordon on 15/10/14.
 */
public class catalogue {

    private static int idNumber;
    private static boolean keepAlive;
    private static ArrayList<User> clients = new ArrayList<User>();
    private static String[] ClientMessages = new String[256];
    private static String[] OldClientMessages = new String[256];
    private static String name;
    private static server nameServer = new server();
    private static server thisServer = new server();
    private static boolean ClientListenerAlive = false;
    public static boolean threadSafeChek = true;
    private static int idTop = 0;

    catalogue(){}

    public static boolean GetClientListenerStatus (){
        return ClientListenerAlive;
    }
    public static boolean setClientListenerStatus (boolean status){
        if (threadSafeChek){
            threadSafeChek = false;
            ClientListenerAlive = status;
            threadSafeChek = true;
            return true;
        }
        else {
            return false;
        }
    }


    public static String GetMessage(int Index){
        String Mess = null;
        if (ClientMessages[Index].length() > 1) {
            Mess = ClientMessages[Index];
        }
        return Mess;
    }

    public static void SetMessage(int Index, String mess){
        OldClientMessages[Index] = ClientMessages[Index];
        ClientMessages[Index] = mess;

    }

    public static void setThisServer(InetAddress ip, int port){
        thisServer.setIp(ip);
        thisServer.setPort(port);
    }

    /**
     *  returns number of clients connected to the server
     * @return  number of clients
     */
    public static int getNrClients(){
        System.out.println("Client size: '" + clients.size() + "'");
        return clients.size();
    }

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

    public static void addClient(User client){
        clients.add(client);
    }

    public static void addClient(InetAddress ip, int port, String name){
        User client = new User(name, ip, port);
        addClient(client);
    }

    public static User getClient(int i){
        return clients.get(i);
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

    public static int getId(){
        return idTop++;
    }

}
