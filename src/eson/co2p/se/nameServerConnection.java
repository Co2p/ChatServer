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
    private catalogue catalogue = new catalogue();
    byte[] outputStream;

    private void fillArrayLists(){
        getServerMessage NameServerPing = new getServerMessage(OpCodes.ALIVE);
        System.out.println(NameServerPing.getRawdata().length());
        outputStream  = NameServerPing.getData();
        System.out.println("Length of outputStream = " + outputStream.length);
        //kolla på råbinärkoden
        for (byte b : outputStream) {
            System.out.println(Integer.toBinaryString(b & 255 | 256).substring(1));
        }
    }

    public void alive() throws Exception{
        fillArrayLists();
        System.out.println("Filled array list!");

        //BufferedReader nameserverAnswere = new BufferedReader(new InputStreamReader("OP: 1"));
        //String nameserverAnswere = LOL ;
        DatagramSocket clientSocket = new DatagramSocket();

        byte[] sendData = new byte[65507];
        byte[] receiveData = new byte[65507];
        //String sentence = nameserverAnswere.readLine();
        //sendData = nameserverAnswere.getBytes();
        //sendData = loll;
        sendData = outputStream;
        //sendData = nameserverAnswere.getBytes();

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, catalogue.getNameServerInet(), catalogue.getNameServerPort());
        //DatagramPacket sendPacket3 = new DatagramPacket();

        clientSocket.send(sendPacket);

        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        clientSocket.receive(receivePacket);
        //Här hanteras all inläsning av serverns utskick av serverdata som är kopplade
        //Till namnservern
        serverList servers = new serverList(receivePacket.getData());
        ArrayList serverlist = servers.getServerList();
        //Print all info on servers, this is just for testing purposes
        for(int i = 0; i < serverlist.size(); i++){
            System.out.println("---" + servers.getServer((String)serverlist.get(i)).getName() + "---");
            System.out.println("ip: " + servers.getServer((String)serverlist.get(i)).getIp());
            System.out.println("port: " + servers.getServer((String)serverlist.get(i)).getPort());
            System.out.println("connected clients: " + servers.getServer((String)serverlist.get(i)).getConnected());

        }
        clientSocket.close();
    }
}
