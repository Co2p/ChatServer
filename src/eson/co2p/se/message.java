package eson.co2p.se;

import java.io.*;
import java.util.ArrayList;

/**
 * Includes all messages sent from the the chatserver to the nameserver and clients
 * Including some other functions used in breaking down and creating a message such as
 * div4 - checks if a number is divisible by four and if it is not, rounds it up to four
 * and getTime - returns the time in UNIX-time
 * @author Isidor, Tony and Gordon on 2014-10-15
 */
public class message {
    //#==============================================#
    //#  Messages sent to nameServer by the server   #
    //#==============================================#
    /**
     * creates a message to be sent to a server asking for a connection
     *
     * @return  the created pdu with headers.
     */
    public static byte[] reqisterNS(){
        int nameLength = catalogue.getName().getBytes().length;
        PDU rawdata = new PDU(8 + div4(nameLength));
        rawdata.setByte(0,(byte)OpCodes.REG);
        rawdata.setByte(1, (byte) nameLength);
        rawdata.setShort(2, (short) catalogue.getServerPort());
        System.out.println("Port:'" + catalogue.getServerPort() + "'");
        rawdata.setSubrange(4, catalogue.getServerInet().getAddress());
        System.out.println("This server ip:" + catalogue.getServerInet().toString());
        try {
            rawdata.setSubrange(8, catalogue.getName().getBytes("UTF-8"));
        }catch(UnsupportedEncodingException e){
            System.out.println("Error encoding serverName: " + e);
            e.printStackTrace();
        }
        return rawdata.getBytes();
    }

    /**
     * Creates a keepAlive message to the nameserver
     *
     * @return the created message
     */
    public static byte[] keepAlive(){
        PDU rawdata = new PDU(4);
        rawdata.setByte(0,(byte)OpCodes.ALIVE);
        rawdata.setByte(1, (byte)userList.getConnected());
        rawdata.setShort(2, (short)catalogue.getIdNumber());

        return rawdata.getBytes();
    }
    //#==============================================#
    //#        Just some methods used by most        #
    //#    other methods across this class/package   #
    //#==============================================#
    /**
     *  Checks if the message received by the server is a registration message
     *  Sent by the client
     *
     * @param message   the message that's going to be checked
     * @return  true if reg-message, otherwise false
     */
    public static boolean checkRegConf(byte[] message){
        PDU rawdata = new PDU(message, message.length);
        if(getOp(message) == OpCodes.ACK){
            catalogue.setIdNumber(rawdata.getShort(2));
            System.out.println("ID: '" + catalogue.getIdNumber() + "'" );
            return true;
        }else{
            System.out.println("Got message, didn't get ACK " + rawdata.getByte(0));
            return false;
        }
    }
    /**
     * Gets the op of a given message retrieved by the server
     *
     * @param data the message retrieved by the server
     * @return  the op-code of the message
     */
    public static int getOp(byte[] data){
        PDU rawData = new PDU(data, data.length);
        return rawData.getByte(0);
    }

    /**
     * div4 tests if and int is divisible by four, if it isn't return the
     * rounded up number to ciel that's divisible by four.
     *
     * @param testInt the int to be tested if it is modulus 4
     * @return the int that's tested plus ciel modulus 4 of that int
     */
    public static int div4(int testInt){
        int ret = 0;
        if((testInt % 4) != 0){
            ret = (4 -(testInt % 4));
        }
        return testInt + ret;
    }

    /**
     * getTime returns the time in seconds since the 1970's
     *
     * @return the time in seconds since the 1970's
     */
    public static int getTime(){
        return (int)(System.currentTimeMillis() / 1000L);
    }
    //#==============================================#
    //#       Messages sent to clients by server     #
    //#==============================================#
    /**
     * reMessage takes a message from a client and adds a nickname and
     * time of sending out
     *
     * @param message   the incoming message
     * @param ID  the ID of the client sending the message
     * @return  new message with added nickname and timebytesd
     */
    public static byte[] reMessage(byte[] message, int ID) {
        PDU rawdata = new PDU(message, message.length);
        int checkSum = rawdata.getByte(3);
        if (ID != -1) { //  Check if the thread adding the message failed
            String nickname = userList.getUser(ID).getNickname();
            //  Calculate checksum
            //if (checkSum != Checksum.calc(rawdata.getBytes(), rawdata.getBytes().length)) {
            //    System.out.println("Checksum calculation returned wrong checksum");
            //} else {
                //  Add the nickname and time into the message header
                try{
                    System.out.println("message: " + new String(rawdata.getSubrange(12, rawdata.getByte(1)), "UTF-8") + "nick: " + nickname);
                }catch(Exception e){
                    e.printStackTrace();
                }
                rawdata.setByte(2, (byte) nickname.length());
                rawdata.setInt(8, getTime());
                int length = div4(rawdata.length());
                rawdata.extendTo(length + div4(nickname.length()));
                try {
                    rawdata.setSubrange(length, nickname.getBytes("UTF-8"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return rawdata.getBytes();
            //}
        }
        return null;
    }

    /**
     * returns a message containing all the connected users and their nicknames
     *
     * @return  the created bytearray containing connected users
     */
    public static byte[] nickNames(){
        ArrayList<User> users= userList.getUserList();
        int connected = userList.getConnected();
        PDU rawdata = new PDU(4);
        rawdata.setByte(0, (byte)OpCodes.NICKS);
        System.out.println("Hur många den tror är connectade: " + connected);
        rawdata.setByte(1, (byte)connected);
        int totSize = 0;
        try {
            for(User s:users){
                //  Check so that the user is not removed from the array
                if(s != null) {
                    int currentSize = rawdata.length();
                    int nickLength = s.getNickname().getBytes().length;
                    rawdata.extendTo(currentSize + nickLength + 1);
                    System.out.println("CurrentSize = " + currentSize + ", nickLength = " + nickLength);
                    rawdata.setSubrange(currentSize, (s.getNickname() + "\0").getBytes("UTF-8"));
                    System.out.println("NICK: " + s.getNickname());
                    totSize += (nickLength + 1);
                }
            }
            //  Insert length of all usernames
            rawdata.setShort(2, (short)totSize);
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }
        /*  The length of the message sent must be divisible by four, eg. if the message looks something like this:
         *  Robert\0A
         *  rnold\0Ka
         *  lle\0
         *  Add zeros to the last row so that the whole message is divisible by four
         */
        if(rawdata.length()%4!=0){
            rawdata.extendTo(rawdata.length()+rawdata.length()%4);
        }
        return rawdata.getBytes();
    }

    /**
     * Creates a message to be sent to all users connected to the server when an user
     * joins the server, includes nickname and time when server SENT the message
     *
     * @param nickname  the nickname of the user joining the server
     * @return  the created byte-array containing header and message
     */
    public static byte[] userJoined(String nickname){
        PDU rawdata = null;
        try {
            int nickLength = nickname.getBytes("UTF-8").length;
            rawdata = new PDU(8 + div4(nickLength));
            rawdata.setByte(0, (byte) OpCodes.UJOIN);
            rawdata.setByte(1, (byte) nickLength);
            rawdata.setInt(4, getTime());
            rawdata.setSubrange(8, nickname.getBytes("UTF-8"));
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return rawdata.getBytes();
    }

    /**
     * Creates a message saying that the server is shutting down
     * @return  the created byte-array containing the message
     */
    public static byte[] serverQuit(){
        PDU rawdata = new PDU(4);
        rawdata.setByte(0, (byte)OpCodes.QUIT);
        return rawdata.getBytes();
    }

    /**
     *  Creates a message if a user joined that's supposed to be
     *  sent to all users connected to the server
     * @param user  The user that joined
     * @return  the created byte-array containing the header and the message
     */
    public static byte[] userLeaved(User user){
        PDU rawdata = null;
        try {
            int nickLength = user.getNickname().getBytes("UTF-8").length;
            rawdata = new PDU(8 + div4(nickLength));
            rawdata.setByte(0, (byte) OpCodes.ULEAVE);
            rawdata.setByte(1, (byte) nickLength);
            rawdata.setInt(4, getTime());
            rawdata.setSubrange(8, user.getNickname().getBytes("UTF-8"));
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return rawdata.getBytes();
    }

    /**
     *  Sets and creates a message if a user requested a nickchange in the server
     *
     * @param user  the user that wants to change nick
     * @param newNick   the new nick of the user
     * @return  the created byte-array containing header and message
     */
    public static byte[] changeNick(User user, String newNick){
        PDU rawdata = null;
        try {
            int nickLength = user.getNickname().getBytes("UTF-8").length;
            int newNickLength = newNick.getBytes("UTF-8").length;

            rawdata = new PDU(8 + div4(nickLength) + div4(newNickLength));
            rawdata.setByte(0, (byte) OpCodes.UCNICK);
            rawdata.setByte(1, (byte) nickLength);
            rawdata.setByte(2, (byte) newNickLength);
            rawdata.setInt(4, getTime());
            rawdata.setSubrange(8, user.getNickname().getBytes("UTF-8"));
            rawdata.setSubrange(8 + div4(nickLength), newNick.getBytes("UTF-8"));
            //  Set a new nickname for the user in his/her object
            //TODO check so that this works across threads
            user.setNickname(newNick);

        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return rawdata.getBytes();
    }
}
