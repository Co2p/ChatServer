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
        //initsialize old message lists
        byte[] nb = new byte[]{1};
        ArrayList<Integer> Ids = catalogue.getmasID();
        ArrayList<byte[]> names = catalogue.getCNameMessages();
        for(int b = 0; b < Ids.size(); b++){
            OldMessagesIDs.add(0);
        }
        for(int b = 0; b < names.size(); b++){
            OldMessages.add(nb);
        }

        for(int b = 0; b < Ids.size(); b++){
            OldMessagesIDs.set(b,Ids.get(b));
        }
        for(int b = 0; b < names.size(); b++){
            OldMessages.set(b,names.get(b));
        }
    }
    @Override
    public void run() {
        MakeOldMessageList();
        System.out.println("\nstart update thread....\n");
        System.out.println("OldMessagesIDs: " +OldMessagesIDs.size() + " OldMessages: " + OldMessages.size());

        while(true){
            ArrayList<Integer> Ids = catalogue.getmasID();
            ArrayList<byte[]> names = catalogue.getCNameMessages();
            for(int b = 0; b < Ids.size(); b++){
                //System.out.println("size:" + Ids.size());
                if(!Ids.get(b).equals(OldMessagesIDs.get(b))){
                    OldMessagesIDs.set(b,Ids.get(b));
                    OldMessages.set(b,names.get(b));

                    //om id på positionen inte är densamma, detta är ett nytt medelande!!
                    //uppdatera gammedelandet och lägg till medelande i kön.
                    while (!catalogue.addItemToQuoue(names.get(b),Ids.get(b))){;}
                    System.out.println("\nadded message to qyuo\n");
                }

            }

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        }

        //To change body of implemented methods use File | Settings | File Templates.
    }
}
