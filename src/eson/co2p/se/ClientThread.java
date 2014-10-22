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

    private String LastUser = "";

    private PrintStream outToServer;
    public DataInputStream Recived_Data;

    public ClientThread(Socket gotensocked) throws IOException {
        this.passedSocket = gotensocked;
        Recived_Data = new DataInputStream(this.passedSocket.getInputStream());
        outToServer = new PrintStream(this.passedSocket.getOutputStream(), true);
        System.out.print("made inputs/outputs ant socket..");
    }


    public boolean NewUserChek(){
        if (!userList.GetLastUser().equals(LastUser)){
            LastUser = userList.GetLastUser();
            return true;
        }
        else{
            return false;
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
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.print("............");
                if (bytesRead > 8) {
                    PDU temp = new PDU(messageByte, messageByte.length);
                    String Usernamr = checkReg(temp);
                    System.out.println("Username: " + Usernamr + "\n");
                    if (!Usernamr.equals(null)) {
                        User user = createUser(Usernamr);
                        ThreadUserId = userList.addUser(user);
                        if (!ThreadUserId.equals(null)) {//om medelandet är ett Join
                            try {
                                outToServer.write(message.nickNames());
                                userList.setLastUser(Usernamr);
                                System.out.print("Sent accept!\n");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } else {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        System.out.println("not able to sleep: " + e);
                    }
                }
            }else{
                try {
                    outToServer.write(message.userJoined(LastUser));
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
