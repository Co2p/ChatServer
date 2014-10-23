package eson.co2p.se;

import java.net.InetAddress;
import java.util.ArrayList;

/**
 * Saves states and information about the server
 * @author Gordon, Isidor, Tony 23 October 2014
 */
public class catalogue {
    //TODO rensa hela den här klassen på onödiga metoder och variabler
    private static int idNumber;
    private static boolean keepAlive;

    static ArrayList<byte[]> ClientMessages = new ArrayList<byte[]>();
    static ArrayList<Integer> ClientMessagesID = new ArrayList<Integer>();

    private static String[] OldClientMessages = new String[256];
    private static String name;
    private static int port;
    private static server nameServer = new server();
    private static server thisServer = new server();
    private static boolean ClientListenerAlive = false;
    public static boolean threadSafeChek = true;
    private static int idTop = 0;
    private static boolean Messagelistfilled = false;
    private static ArrayList <ArrayList> LastMessage = new ArrayList <ArrayList>();
    private static int Message_ID = 1;



    static ArrayList<Thread> Threads = new ArrayList<Thread>();

    catalogue(){}



    private static ArrayList<byte[]> ClientMessages2 = new ArrayList<byte[]>();
    private static ArrayList<byte[]> QuoeMessages = new ArrayList<byte[]>();
    private static ArrayList<Integer> ClientMessagesID2 = new ArrayList<Integer>();
    private static ArrayList<Integer> QuoeIds = new ArrayList<Integer>();
    private static int index = 0;
    private static boolean initsoflistsdone = false;
    private static boolean QuoeInuse = false;
    private static boolean Safeaddmessage = true;

    public static void addNewId(int index){
        int NewId = addtoid();
        ClientMessagesID2.set(index, NewId);
    }
    public static boolean AddMessade(int index,byte[] mess ){
        fillArrays();
        if(Safeaddmessage){
            System.out.println("ts: true");
            Safeaddmessage = false;
            System.out.println("Adding message on pos: " + index);
            ClientMessages2.set(index,mess);
            addNewId(index);
            Safeaddmessage = true;
            System.out.println("ts: false");
            return true;
        }else {
            return false;
        }
    }

    public static ArrayList<Integer> getQuoeids(){
        return new ArrayList<Integer>(QuoeIds);
    }
    public static int GetIndex(){
        return index;
    }

    public static ArrayList<byte[]> getQuoeMessages(){
        return new ArrayList<byte[]>(QuoeMessages);
    }


    public static boolean addItemToQuoue(byte[] mess, int ID){
        if(QuoeInuse == false){
            QuoeInuse = true;
            QuoeMessages.add(mess);
            QuoeIds.add(ID);
            QuoeInuse = false;
            return true;
        }else{ return false;}
    }

    public static int addtoid(){
        index ++;
        return index;
    }
    public static boolean fillArrays(){
        if(!initsoflistsdone){
            for(int g = 0; g < 256; g++){
                byte[] byt = new byte[]{1};
                ClientMessages2.add(byt);
                ClientMessagesID2.add(addtoid());
                initsoflistsdone = true;
                }
            }
        return initsoflistsdone;
    }
    public static ArrayList<Integer> getmasID(){
        fillArrays();
        return new ArrayList<Integer>(ClientMessagesID2);
    }
    public static ArrayList<byte[]> getCNameMessages(){
        fillArrays();
        return new ArrayList<byte[]>(ClientMessages2);
    }















    public static ArrayList<ArrayList> GetMessageQuoe(){
        return LastMessage;
    }
    public static void AddToLastMessage(ArrayList obj){
        if(LastMessage.size() < 100){
            LastMessage.add(obj);
        }
        else{
            LastMessage.add(0,obj);//probably need to remove the position 101 here...
        }
    }
    public static int UpdateMessageId(){
        Message_ID ++;
        System.out.println("updating id \n ");
        return Message_ID;
    }
    public static ArrayList<byte[]> GetMessageList(){
        return  ClientMessages;
    }
    public static ArrayList<Integer> GetMessageListID(){
        return  ClientMessagesID;
    }

    public static void setMessage (int Index, byte[] message){
        //TODO: is this safe?
        //TODO:FUUUUCK!
        if(fillmessagelist()){
            System.out.println("setting client message and id\n ");
            ClientMessages.set(Index ,message);
            ClientMessagesID.set(Index ,UpdateMessageId());
            System.out.println("done updating id's\n ");
        }
    }
    private static boolean fillmessagelist(){
       if(!Messagelistfilled){
           Messagelistfilled = true;
           byte[] lol = new byte[]{0};
           int g = -1;
           for(int i = 0;i < 256; i++){
               ClientMessages.add(lol);
               ClientMessagesID.add(g);
           }
           ArrayList<Object> Obj = new ArrayList<Object>();
           Obj.add(Message_ID);
           Obj.add(lol);
           LastMessage.add(Obj);
       }
        return true;
    }

    /**
     * adds a new thread to the clientlist used in keeping track of all the threads
     * @param athread   the thread to be added to the list
     */
    public static void addThread(Thread athread){
        Threads.add(athread);
    }

    //TODO ta bort detta?
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


    public static void setThisServer(InetAddress ip, int port){
        thisServer.setIp(ip);
        thisServer.setPort(port);
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

    /**
     * Sets the server name
     * @param name server name
     */
    public static void setName(String name) {
        catalogue.name = name;
    }

    /**
     * Sets the server port
     * @param port the server port
     */
    public static void setPort(int port){
        catalogue.port = port;
    }
    /**
     * Gets the server port
     */
    public static int getPort(){
        return catalogue.port;
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
     * Returns the server name
     * @return server name
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
