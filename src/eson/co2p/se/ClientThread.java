package eson.co2p.se;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Handles the main threads of the program
 * //TODO explain more thorough
 * @author Tony Isidor and Gordon on 22/10/2014.
 */
public class ClientThread implements Runnable {

    private Socket passedSocket;
    private Integer ThreadUserId;

    ArrayList<byte[]> Messagestosend = new ArrayList<byte[]>();

    private String LastUser = "default";
    private String MyName = "default";

    private boolean FirstRun = true;
    private int LastMessageId = -1;

    private PrintStream outToServer;
    public DataInputStream Recived_Data;

    public ClientThread(Socket gotensocked) throws IOException {
        this.passedSocket = gotensocked;
        this.passedSocket.setSoTimeout(100);
        Recived_Data = new DataInputStream(this.passedSocket.getInputStream());
        outToServer = new PrintStream(this.passedSocket.getOutputStream(), true);
        System.out.print("made inputs/outputs ant socket..");
    }
    private int ChekMessages(){
        ArrayList<ArrayList> Messages = catalogue.GetMessageQuoe();
        int FoundMessages = 0;
        int mplus = 0;
        for(ArrayList<Object> M :Messages){
            for(Object A :M){
                if(A.getClass().equals(Integer.class)){
                    int a = (Integer)A;
                    if(a > LastMessageId){
                        if(Messages.get(mplus).get(0).getClass().equals(byte[].class)){
                            byte[] mess = (byte[])Messages.get(mplus).get(0);
                            Messagestosend.add(mess);
                            FoundMessages ++;
                        }
                        //this message isen't sent
                    }
                }
            }
            mplus ++;
        }
        return FoundMessages;
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
                int Messagesleft = ChekMessages();
                if (Messagesleft >= 1){
                    int inde = 0;
                    for(byte[] sendbyte :Messagestosend){
                        try {
                            outToServer.write(sendbyte);
                        } catch (IOException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
                        Messagestosend.remove(inde);
                        inde ++;
                    }
                }
                try {
                    bytesRead = Recived_Data.read(messageByte);
                } catch (Exception e) {;}
                System.out.print("............\n");
                if (bytesRead > 8) {
                    int Opcode = message.getOp(messageByte);
                    if(Opcode == OpCodes.REG){
                        PDU temp = new PDU(messageByte, messageByte.length);
                        String Usernamr = checkReg(temp);
                        System.out.println("Username: " + Usernamr + "\n");
                        if (!Usernamr.equals(null)) {
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
                    } else if(Opcode == OpCodes.MESSAGE){
                        byte[] BS = message.reMessage(messageByte, ThreadUserId);
                        catalogue.setMessage(ThreadUserId,BS);
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
