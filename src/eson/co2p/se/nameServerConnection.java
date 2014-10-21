package eson.co2p.se;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;

/**
 * Created by gordon on 10/10/14.
 *
 * Sends Alive messages to the nameserver.
 */
public class nameServerConnection {

    private ArrayList<Integer> format = new ArrayList<Integer>();
    private ArrayList<Object> content = new ArrayList<Object>();
    byte[] outputStream;

    public nameServerConnection(){
        while (catalogue.keepAlive()){
            try {
                alive();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
}



    public void alive() throws Exception{
        System.out.println("Filled array list!");

        //BufferedReader nameserverAnswere = new BufferedReader(new InputStreamReader("OP: 1"));
        //String nameserverAnswere = LOL ;
        DatagramSocket clientSocket = new DatagramSocket();

        byte[] sendData = new byte[65507];
        byte[] receiveData = new byte[65507];

        sendData = outputStream;

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, catalogue.getNameServerInet(), catalogue.getNameServerPort());

        clientSocket.send(sendPacket);

        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        clientSocket.receive(receivePacket);

        receivePacket.getData();

        clientSocket.close();
    }
}
