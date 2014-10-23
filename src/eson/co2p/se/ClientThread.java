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
    private ArrayList<byte[]> ChekMessages(){
        ArrayList<ArrayList> Messages = new ArrayList<ArrayList>(catalogue.GetMessageQuoe());
        ArrayList<byte[]> Messagestosend = new ArrayList<byte[]>();
        int FoundMessages = 0;
        int mplus = 0;
        int greatestId = -1;
        for(ArrayList<Object> M :Messages){
            for(Object A :M){
                if(A.getClass().equals(Integer.class)){
                    int a = (Integer)A;
                    if(a > LastMessageId){
                        if(a > greatestId){
                            greatestId = a;
                        }
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
        if(greatestId != -1){
            if(greatestId > LastMessageId){
                LastMessageId = greatestId;
            }
        }
        return Messagestosend;
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
                ArrayList<byte[]> Messagesleft = ChekMessages();
                System.out.println("Values: " +Messagesleft.size()+" empty?: "+ Messagesleft.isEmpty());
                if (!Messagesleft.isEmpty()){
                    System.out.print("messages left: " + Messagesleft + "\n");
                    int inde = 0;
                    ArrayList<byte[]> Messagestosend_copy = new ArrayList<byte[]>(Messagesleft);
                    ArrayList<Integer> removelist = new ArrayList<Integer>();
                    for(byte[] sendbyte :Messagesleft){
                        try {
                            outToServer.write(sendbyte);
                            //System.out.println("Sent message!");
                        } catch (IOException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
                        removelist.add(inde);

                        inde ++;
                    }
                    for(int j = 0; j < removelist.size(); j++){
                        Messagestosend_copy.remove(removelist.get(j));
                    }
                }


                try {
                    bytesRead = Recived_Data.read(messageByte);
                } catch (Exception e) {;}
                System.out.print("............\n");
                if (bytesRead > 8) {
                    System.out.print("getting  op\n");
                    int Opcode = message.getOp(messageByte);
                    System.out.print("op is:" + Opcode + "\n");
                    if(Opcode == OpCodes.JOIN){
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
                        System.out.println("Handeling op code 10 \n ");
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
