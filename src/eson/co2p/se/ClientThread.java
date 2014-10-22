package eson.co2p.se;
import javax.naming.ldap.UnsolicitedNotification;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
/**
 * Created by Tony on 22/10/2014.
 */
public class ClientThread {
    Socket ClientSocket;
    ServerSocket ServerSock;
    private PrintStream outToServer;


    public ClientThread(byte[] messageByte,  Socket CurrentConnection, ServerSocket ClientScann) throws IOException {
        ClientSocket = CurrentConnection;
        ServerSock = ClientScann;
        System.out.println("clientthread: lollaflefd");
        PDU temp = new PDU(messageByte, messageByte.length);// är detta korekt?
        String Usernamr = checkReg(temp);
        System.out.println("clientthread pdu: " + temp);
        System.out.println("clientthread username: " + Usernamr);
        if(!Usernamr.equals(null)){
            System.out.println("try to send answere....");
            outToServer = new PrintStream(ClientSocket.getOutputStream(), true);
            System.out.println("printstream redy...");
            RegSendMessage();
        }
    }
    private void RegSendMessage(){
        try {
            System.out.println("writing to server");
            outToServer.write(message.nickNames());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to send join message");
        }
    }



    private String checkReg (PDU data){
        //Kolla om op-koden är REG, annars skicka tillbaks null
        String nick = null;

        if(data.getByte(0) == OpCodes.JOIN){
            int nickLength = data.getByte(1);
            try {
                nick = new String(data.getSubrange(4, nickLength), "UTF-8");
            }catch(UnsupportedEncodingException e){
                e.printStackTrace();
            }
        }
        return nick;
    }
}
