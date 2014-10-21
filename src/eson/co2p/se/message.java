package eson.co2p.se;

import java.io.*;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.zip.GZIPOutputStream;

/**
 * Created by isidor on 2014-10-15.
 *
 *  Includes all messages sent from the client to the nameserver or chatserver
 */
public class message {

    /**
     * creates a message that's supposed to be sent to the nameserver
     * asking for a list of servers
     *
     * @return the created pdu with headers
     */
    /*public static byte[] getServerMessage(){
        PDU rawdata = new PDU(4);
        rawdata.setByte(0,(byte)OpCodes.GETLIST);
        return rawdata.getBytes();
    }*/

    /**
     * creates a message to be sent to a server asking for connection
     *
     * @return  the created pdu with headers.
     */
    public static byte[] reqisterNS(){
        int nameLength = div4(catalogue.getName().getBytes().length);
        PDU rawdata = new PDU(8 + nameLength);
        rawdata.setByte(0,(byte)OpCodes.REG);
        rawdata.setByte(1, (byte) nameLength);
        rawdata.setShort(2, (short)catalogue.getServerPort());
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
            catalogue.setIdNumber(rawdata.getShort(1));
            return true;
        }else{
            System.out.println("Got message, didn't get ACK");
            return false;
        }
    }

    /**
     * changes the nick of the user
     *
     * @param nickname the name to be changed to
     * @return the created PDU with headers and nick
     */
    public static byte[] changeNick(String nickname){
        PDU rawdata = new PDU(4 + div4(nickname.getBytes().length));
        catalogue.setName(nickname);
        rawdata.setByte(0,(byte)OpCodes.CHNICK);
        rawdata.setByte(1, (byte)div4(nickname.getBytes().length));
        try {
            rawdata.setSubrange(4, nickname.getBytes("UTF-8"));
        }catch(UnsupportedEncodingException e){
            System.out.println("Unsupported Encoding Exception: " + e);
        }
        return rawdata.getBytes();
    }

    /**
     *  String message = the message to be sent
     *  int type = type of the message, eg. 0 = ordinary text, 1=compressed message
     *  2=crypt message 3=compressed and crypt message
     *
     *  @param message the message to be sent
     *  @param type ordinary/compressed/crypt
     *  @return the converted bytearray containing the PDU header + message
     */
    /*public static byte[] sendMessage(String message, int type, int Tabid){
        PDU rawdata = null;
        try {
            byte[] msgByte = message.getBytes("UTF-8");
            //add encryption or compression based on the typ-input
            switch(type){
                case 1:
                    msgByte = compress(msgByte);
                    break;
                case 2:
                    msgByte = enCrypt(msgByte,Tabid);
                    break;
                case 3:
                    msgByte = enCrypt(compress(msgByte),Tabid);
                    break;
            }
            rawdata = new PDU(12 + div4(msgByte.length));
            rawdata.setByte(0, (byte) OpCodes.MESSAGE);
            rawdata.setByte(1,(byte)type);
            rawdata.setShort(4, (short)div4(msgByte.length));
            rawdata.setSubrange(12, msgByte);
            rawdata.setByte(3, Checksum.calc(rawdata.getBytes(), rawdata.length()));
        }catch(UnsupportedEncodingException e){
            System.out.println("Unsupported Encoding Exception: " + e);
        }
        return rawdata.getBytes();
    }*/

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
