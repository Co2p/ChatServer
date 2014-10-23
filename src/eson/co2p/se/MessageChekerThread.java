package eson.co2p.se;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: derp
 * Date: 2014-10-23
 * Time: 14:22
 * To change this template use File | Settings | File Templates.
 */
public class MessageChekerThread implements Runnable {
    ArrayList<byte[]> OldMessages = new ArrayList<byte[]>();
    ArrayList<Integer> OldMessagesIDs = new ArrayList<Integer>();
    public MessageChekerThread(){
        MakeOldMessageList();
    }

    public void MakeOldMessageList(){
        byte[] lol = new byte[]{0};
        int idlol = -1;
        for(int i = 0;i < 256; i++){
            OldMessages.add(lol);
            OldMessagesIDs.add(idlol);
        }
    }
    @Override
    public void run() {
        while(true){
            ArrayList<byte[]> CurrentList = catalogue.GetMessageList();
            ArrayList<Integer> CurrentListId = catalogue.GetMessageListID();
            int g = 0;
            for(byte[] by : CurrentList) {
                if (by.equals(OldMessages.get(g)) && CurrentListId.get(g).equals(OldMessagesIDs)){
                    //not an new message
                }
                else{
                    ArrayList<Object> Obj = new ArrayList<Object>();
                    Obj.add(CurrentList.get(g));
                    Obj.add(CurrentListId.get(g));
                    catalogue.AddToLastMessage(Obj);
                    //found new message, making an object and adding it to the quoe
                }
                g ++;
                try {
                    Thread.sleep(25);
                } catch (InterruptedException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }

        }

        //To change body of implemented methods use File | Settings | File Templates.
    }
}
