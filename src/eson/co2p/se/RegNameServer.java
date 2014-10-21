package eson.co2p.se;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

/**
 * Retrieves the active servers from the name server
 *
 * Created by gordon on 07/10/14.
 */

public class RegNameServer {
    /**
     * Sends a getserverlist message to the nameserver from catalogue, and then
     * handles the return from the server.
     *
     * @return  a list of all servers returned by the nameservers
     * @throws Exception
     */
    public void regserver() throws SocketException {
        DatagramSocket clientSocket = new DatagramSocket();
        byte[] sendData = message.getServerMessage();
        byte[] receiveData = new byte[65507];
        ArrayList<Integer> format = new ArrayList<Integer>();
        ArrayList<Object> content = new ArrayList<Object>();
        try {
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, catalogue.getNameServerInet(), catalogue.getNameServerPort());
            clientSocket.send(sendPacket);
        }catch (Exception e){
            System.out.print("Failed to send packet");
            e.printStackTrace();
        }
        //Waits for greg answere
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        try {
            clientSocket.receive(receivePacket);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }



        receivePacket.getData();


        ArrayList serverlist = servers.getServerList();

        //Print all info on servers, this is just for testing purposes
        servers.printServers();

        clientSocket.close();
        return servers;
    }
}
