package eson.co2p.se;

import javax.naming.ldap.UnsolicitedNotification;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Tony on 22/10/2014.
 */
public class ServerFirstClientThread {
    Socket CurrentConnection;
    ServerSocket ClientScann;
    int port;


    DataInputStream Buffert_In = null;
    DataOutputStream Data_Out = null;

    public ServerFirstClientThread(Socket Current_Connection, ServerSocket ClientScan, int Port) throws IOException {
        CurrentConnection = Current_Connection;
        ClientScann = ClientScan;
        port = Port;
        ScanForClients();
    }
    //TODO: en chekerclass för denna tråd, så en ny kan startas om denna konstant blir upptagen, chek görs i server.class
    private void ScanForClients() throws IOException {
        while(true) {
            try {
                CurrentConnection = ClientScann.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            DataInputStream Input = null;
            OutputStream out = null;
            try {
                Input = new DataInputStream(CurrentConnection.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                out = (CurrentConnection.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            Buffert_In = Input;
            Data_Out = new DataOutputStream(out);
            //TODO: Skicka svar , ny funktion...
            if(!Buffert_In.equals(null) && !Data_Out.equals(null)) {
                byte[] messageByte = new byte[1000];
                int bytesRead = Input.read(messageByte);
                if(bytesRead > 8) {
                    PDU temp = new PDU(messageByte, messageByte.length);// är detta korekt?
                    checkReg(temp);//TODO: isidor måste ta hand om clientförfrågn någonstans...
                }else {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        System.out.println("not able to sleep: " + e);
                    }
                }
            }
            else{
                System.out.println("Failed to read buffert");
            }
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
