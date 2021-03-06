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

    String ClientIp = "null";


    private String LastUser = "default";
    private String MyName = "default";

    private boolean FirstRun = true;
    private boolean FirstRun2 = true;
    private int LastMessageId = -1;

    private PrintStream outToServer;
    public DataInputStream Recived_Data;

    public ClientThread(Socket gotensocked) throws IOException {
        this.passedSocket = gotensocked;
        this.passedSocket.setSoTimeout(50);
        this.Recived_Data = new DataInputStream(this.passedSocket.getInputStream());
        this.outToServer = new PrintStream(this.passedSocket.getOutputStream(), true);
        System.out.print("made inputs/outputs ant socket..");
    }

    private boolean GetfirstId(){
        if(this.FirstRun2){
            this.LastMessageId = catalogue.GetIndex();
            this.FirstRun2 = false;
        }
        return true;
    }

    private ArrayList<byte[]> ChekMessages(){
        ArrayList<Integer> QIds = catalogue.getQuoeids();
        ArrayList<byte[]> Qnames = catalogue.getQuoeMessages();
        ArrayList<byte[]> Messages = new ArrayList<byte[]>();
        GetfirstId();
        //System.out.println("adding message.. " + QIds.size());
        for(int g = 0; g < QIds.size(); g++ ){
            if(this.LastMessageId < QIds.get(g)){
                Messages.add(Qnames.get(g));
                //System.out.println("adding message: " + Qnames.get(g) + " .. " + g);
                this.LastMessageId = QIds.get(g);
            }
        }
        return Messages;
    }

    public boolean NewUserChek(){
        if(this.FirstRun){
            this.FirstRun = false;
            return false;
        }
        if(userList.GetLastUser().equals(this.MyName) || userList.GetLastUser().equals(LastUser)){
            return false;
        }
        else{
            this.LastUser = userList.GetLastUser();
            return true;
        }


    }
    @Override
    public void run() {



        while(true) {//TODO this is sh1t, can't stop, fix it later

            byte[] messageByte = new byte[1000];
            byte[] messageByteNew;
            //System.out.print("reading data");

            int bytesRead = 0;

            if(!NewUserChek()) {
                ArrayList<byte[]> Messagesleft = ChekMessages();
                //System.out.println("Values: " +Messagesleft.size()+" empty?: "+ Messagesleft.isEmpty());
                if (!Messagesleft.isEmpty()){
                    //System.out.print("messages left: " + Messagesleft + "\n");
                    int inde = 0;
                    ArrayList<byte[]> Messagestosend_copy = new ArrayList<byte[]>(Messagesleft);
                    ArrayList<Integer> removelist = new ArrayList<Integer>();

                    for(byte[] sendbyte :Messagesleft){
                        try {
                            outToServer.write(sendbyte);
                            //System.out.println("Sent message!" + new String(sendbyte, "UTF-8") + "\n");
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
                //TODO java.lang.NegativeArraySizeException on line 122 when client dc unexpected
                try {
                    messageByteNew = new byte[bytesRead];
                }catch(NegativeArraySizeException e){
                    messageByteNew = message.userLeaved(userList.getUser(ThreadUserId));
                    userList.removeUser(ThreadUserId);
                    while(!catalogue.RemoveHashKey(ClientIp));
                    //System.out.println("Leaving...");
                    while (!catalogue.AddMessade(ThreadUserId,messageByteNew));
                    //userList.printAvailableIds();
                    break;
                }

                for(int i = 0; i < bytesRead; i++ ){
                    messageByteNew[i] =  messageByte[i];
                }
                //System.out.print("............\n");
                if (bytesRead > 3) {
                    //System.out.print("getting  op\n");
                    int Opcode = message.getOp(messageByteNew);
                    //System.out.print("op is:" + Opcode + "\n");

                    if(Opcode == OpCodes.JOIN){
                        PDU temp = new PDU(messageByteNew, messageByteNew.length);
                        String Usernamr = checkReg(temp).replaceAll("\0", "");
                        //System.out.println("Username: " + Usernamr + "\n");
                        if (!Usernamr.equals(null)) {
                            if(userList.isUser(Usernamr)) {
                                int number = 0;
                                while (userList.isUser(Usernamr + number)) {
                                    number++;
                                }
                                Usernamr = Usernamr + number;
                            }
                            this.MyName = Usernamr;
                            User user = createUser(Usernamr);
                            //TODO userlist.adduser returnerar -1 om listan är full.. hantera!
                            ThreadUserId = userList.addUser(user);
                            if (!ThreadUserId.equals(null)) {//om medelandet är ett Join
                                try {
                                    ClientIp = passedSocket.getRemoteSocketAddress().toString();
                                    int h;
                                    while(true){
                                        h = catalogue.ExistInHashNadd(ClientIp);
                                        if(h != -1){
                                            break;
                                        }
                                    }
                                    if(h <= 500) { //only accepting 5 users whit the same ip
                                        //System.out.println("Client ip: " + ClientIp);
                                        outToServer.write(message.nickNames());
                                        userList.setLastUser(this.MyName);
                                        this.LastUser = userList.GetLastUser();
                                        System.out.print("Sent accept!\n");
                                    }
                                    else{
                                        System.out.print("Diden't accept user, alredy got 5 from the same ip!!\n");
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } else if(Opcode == OpCodes.MESSAGE){
                        byte[] BS = message.reMessage(messageByteNew, ThreadUserId);
                        //System.out.println("Handeling op code 10 , thread" + ThreadUserId +" \n ");
                        //catalogue.setMessage(ThreadUserId,BS);
                        while (!catalogue.AddMessade(ThreadUserId,BS));

                    }else if (Opcode == OpCodes.QUIT){
                        byte[] ret = message.userLeaved(userList.getUser(ThreadUserId));
                        System.out.println("Trying to handle user left");
                        userList.removeUser(ThreadUserId);
                        while(!catalogue.RemoveHashKey(ClientIp));
                        System.out.println("Leaving...");
                        while (!catalogue.AddMessade(ThreadUserId,ret));
                        //userList.printAvailableIds();
                        break;
                    }else if(Opcode == OpCodes.CHNICK){
                        System.out.println("Found user trying to change nick");
                        //PDU msg = new PDU(messageByte, messageByte.length);
                        PDU msg = new PDU(messageByteNew, messageByteNew.length);
                        String newNick = null;
                        try {
                            newNick = new String(msg.getSubrange(4, msg.getByte(1)), "UTF-8").replaceAll("\0", "");
                        }catch(UnsupportedEncodingException e){
                            e.printStackTrace();
                        }
                        if(newNick != null) {
                            User user = userList.getUser(ThreadUserId);
                            String oldNick = user.getNickname();
                            user.setNickname(userList.swapNick(user.getNickname(), newNick));
                            byte[] ret = message.changeNick(oldNick, user.getNickname());
                            System.out.println("Trying to handle user changing nick: '" + user.getNickname() + "'");
                            while (!catalogue.AddMessade(ThreadUserId,ret));
                        }else{
                            System.out.println("Can't handle user changing nick");
                        }
                    }
                    } else {
                        try {
                            Thread.sleep(20);
                        } catch (InterruptedException e) {
                            System.out.println("not able to sleep: " + e);
                        }
                    }
            }else{
                try {
                    //System.out.println("Sending Ujoind! user: "+ userList.GetLastUser());
                    outToServer.write(message.userJoined(this.LastUser));
                    this.LastUser = userList.GetLastUser();
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
