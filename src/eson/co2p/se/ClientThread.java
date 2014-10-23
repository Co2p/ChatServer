package eson.co2p.se;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

/**
 * Created by Tony on 22/10/2014.
 */
public class ClientThread implements Runnable {

    private Socket passedSocket;
    private Integer ThreadUserId;

    private String LastUser = "default";
    private String MyName = "default";

    private boolean FirstRun = true;

    private PrintStream outToServer;
    public DataInputStream Recived_Data;

    public ClientThread(Socket gotensocked) throws IOException {
        this.passedSocket = gotensocked;
        this.passedSocket.setSoTimeout(100);
        Recived_Data = new DataInputStream(this.passedSocket.getInputStream());
        outToServer = new PrintStream(this.passedSocket.getOutputStream(), true);
        System.out.print("made inputs/outputs ant socket..");
    }


    public boolean NewUserChek(){
        if(FirstRun){
            FirstRun = false;
            return false;
        }
        if(userList.GetLastUser().equals(MyName) || userList.GetLastUser().equals(LastUser)){
            return false;
        }
        else{
            LastUser = userList.GetLastUser();
            return true;
        }


    }
    @Override
    public void run() {

        while(true) {//TODO this is sh1t, can't stop, fix it later

            byte[] messageByte = new byte[1000];

            System.out.print("reading data");

            int bytesRead = 0;

            if(!NewUserChek()) {

                try {
                    bytesRead = Recived_Data.read(messageByte);
                } catch (Exception e) {;}
                System.out.print("............\n");
                if (bytesRead > 8) {
                    PDU temp = new PDU(messageByte, messageByte.length);
                    String Usernamr = checkReg(temp);
                    System.out.println("Username: " + Usernamr + "\n");
                    if (!Usernamr.isEmpty()) {
                        MyName = Usernamr;
                        User user = createUser(Usernamr);
                        ThreadUserId = userList.addUser(user);
                        if (!ThreadUserId.equals(null)) {//om medelandet är ett Join
                            try {
                                outToServer.write(message.nickNames());
                                userList.setLastUser(MyName);
                                LastUser = userList.GetLastUser();
                                System.out.print("Sent accept!\n");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } else {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        System.out.println("not able to sleep: " + e);
                    }
                }
            }else{
                try {
                    System.out.println("Sending Ujoind! user: "+ userList.GetLastUser());
                    outToServer.write(message.userJoined(LastUser));
                    LastUser = userList.GetLastUser();
                } catch (IOException e) {e.printStackTrace();}
            }
            //do shit, shits done!
        }
    }

    private User createUser(String name){
        User user = new User(name);
        return user;
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
