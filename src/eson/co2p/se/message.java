package eson.co2p.se;

import sun.jvm.hotspot.runtime.Bytes;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by isidor on 2014-10-15.
 *
 *  Handles the abstract creation of messages, includes all methods shared by
 *  all messageclasses.
 */
public class message {
    private PDU rawdata;

    public byte[] register(){
        addOp(OpCodes.REG);
        rawdata.extendTo(8 + div4(catalogue.getName().length()));
        rawdata.setByte(1, (byte) catalogue.getName().length());
        rawdata.setShort(2, (short) catalogue.getServerPort());
        rawdata.setSubrange(4, catalogue.getServer().getIp().getAddress());
        rawdata.setSubrange(8, catalogue.getName().getBytes());

        return getData();
    }

    public byte[] getServerMessage(){
        addOp(OpCodes.GETLIST);
        rawdata.extendTo(4);
        return getData();
    }

    public byte[] connectToServerMessage(String username){
        addOp(OpCodes.JOIN);
        int usernameLength = username.getBytes().length;
        rawdata.extendTo(4 + div4(usernameLength));
        rawdata.setByte(1, (byte) usernameLength);
        rawdata.setSubrange(4, username.getBytes());
        return getData();
    }

    public byte[] quitServer(){
        addOp(OpCodes.QUIT);
        rawdata.extendTo(4);
        return getData();
    }

    public byte[] changeNick(String nickname){
        catalogue.setName(nickname);
        addOp(OpCodes.CHNICK);
        rawdata.extendTo(4 + div4(nickname.getBytes().length));
        rawdata.setByte(1, (byte) nickname.getBytes().length);
        try {
            rawdata.setSubrange(4, nickname.getBytes("UTF-8"));
        }catch(UnsupportedEncodingException e){
            System.out.println("Unsupported Encoding Exception: " + e);
        }
        return getData();
    }

    /**
     *
     *  String message = the message to be sent
     *  int type = type of the message, eg. 0 = ordinary text, 1=compressed message
     *  2=crypt message 3=compressed and crypt message
     */
    public byte[] sendMessage(String message, int type, int checksum){
        addOp(OpCodes.MESSAGE);
        rawdata.extendTo(12 + div4(message.getBytes().length +
                catalogue.getName().getBytes().length));
        rawdata.setByte(1,(byte)type);
        rawdata.setByte(2, (byte)catalogue.getName().getBytes().length);
        rawdata.setByte(3, (byte)checksum);
        rawdata.setShort(4, (short) message.getBytes().length);
        rawdata.setInt(8, getTime());
        try {
            rawdata.setSubrange(12, message.getBytes("UTF-8"));
            rawdata.setSubrange(12 + message.getBytes().length, catalogue.getName().getBytes("UTF-8"));
        }catch(UnsupportedEncodingException e){
            System.out.println("Unsupported Encoding Exception: " + e);
        }
        return rawdata.getBytes();
    }

    void addOp(int op){
        rawdata = new PDU(1);
        rawdata.setByte(0, (byte)op);
    }

    public byte[] getData(){
        return rawdata.getBytes();
    }

    public PDU getRawdata(){
        return rawdata;
    }

    public static int div4(int testInt){
        int ret = 0;
        if((4 -(testInt % 4)) != 0){
            ret = (4 -(testInt % 4));
        }
        return testInt + ret;
    }

    public int getTime(){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+2"));
        calendar.clear();
        calendar.set(2011, Calendar.OCTOBER, 1);
        return (int)(calendar.getTimeInMillis() / 1000L);
    }
}

