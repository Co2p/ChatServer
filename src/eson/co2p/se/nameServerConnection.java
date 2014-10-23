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
    //TODO this constructor is never called, is it needed?
    public nameServerConnection(){
        while (catalogue.keepAlive()){
            try {
                alive();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
}

    /**
     *  Alive sends an alive-message to the nameserver keeping the connection between
     *  the two alive whilst the server is on
     * @throws Exception
     */
    public void alive() throws Exception{
        DatagramSocket clientSocket = new DatagramSocket();

        byte[] receiveData = new byte[65507];
        byte[] sendData = outputStream;

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, catalogue.getNameServerInet(), catalogue.getNameServerPort());

        clientSocket.send(sendPacket);

        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        clientSocket.receive(receivePacket);

        receivePacket.getData();

        clientSocket.close();
    }
}
