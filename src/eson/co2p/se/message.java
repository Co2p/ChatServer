package eson.co2p.se;

import com.sun.xml.internal.ws.org.objectweb.asm.Opcodes;

import java.io.*;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.zip.GZIPOutputStream;

/**
 * Created by isidor on 2014-10-15.
 *
 *  Includes all messages sent from the the chatserver to the nameserver and clients
 */
public class message {

    /**
     * creates a message to be sent to a server asking for connection
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

    public static byte[] keepAlive(){
        PDU rawdata = new PDU(4);
        rawdata.setByte(0,(byte)OpCodes.REG);
        rawdata.setByte(1, (byte)catalogue.getNrClients());
        rawdata.setShort(2, (short)catalogue.getIdNumber());

        return rawdata.getBytes();
    }

    public static boolean checkRegConf(byte[] message){
        PDU rawdata = new PDU(message, message.length);
        if(rawdata.getByte(0) == OpCodes.ACK){
            catalogue.setIdNumber(rawdata.getShort(2));
            System.out.println("ID: '" + catalogue.getIdNumber() + "'" );
            return true;
        }else{
            System.out.println("Got message, didn't get ACK");
            return false;
        }
    }
    //#==============================================#
    //#                Below is all                  #
    //#       Messages sent to clients by server     #
    //#==============================================#
    public static byte[] nickNames(){
        int connected = catalogue.getNrClients();
        PDU rawdata = new PDU(4);
        rawdata.setByte(0, (byte)OpCodes.NICKS);
        rawdata.setByte(1, (byte)connected);
        try {
            for (int i = 0; i < connected; i++) {
                int currentSize = rawdata.length();
                int nickLength = catalogue.getClient(i).getNickname().getBytes().length;
                rawdata.extendTo(currentSize + nickLength + 1);
                rawdata.setSubrange(currentSize, (catalogue.getClient(i).getNickname() + "\0").getBytes("UTF-8"));
            }
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }
        if(rawdata.length()%4!=0){
            rawdata.extendTo(rawdata.length()+rawdata.length()%4);
        }
        return rawdata.getBytes();
    }

    public static byte[] userJoined(User user){
        PDU rawdata = null;
        try {
            int nickLength = user.getNickname().getBytes("UTF-8").length;
            rawdata = new PDU(8 + div4(nickLength));
            rawdata.setByte(0, (byte) OpCodes.UJOIN);
            rawdata.setByte(1, (byte) nickLength);
            rawdata.setInt(4, getTime());
            rawdata.setSubrange(8, user.getNickname().getBytes("UTF-8"));
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return rawdata.getBytes();
    }

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

            user.setNickname(newNick);

        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return rawdata.getBytes();
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
        if((4 -(testInt % 4)) != 0){
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
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+2"));
        calendar.clear();
        calendar.set(2011, Calendar.OCTOBER, 1);
        return (int)(calendar.getTimeInMillis() / 1000L);
    }
}
