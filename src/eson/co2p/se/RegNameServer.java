package eson.co2p.se;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.io.*;

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
        //InetAddress adr = new InetAddress(InetAddress.getLoopbackAddress());
        InetAddress myip = null;
        try {
            myip = getPublicIp();
            System.out.println("IP: " + myip);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        catalogue.setThisServer(myip, 2222);
        byte[] sendData = message.reqisterNS();
        byte[] receiveData = new byte[65507];
        ArrayList<Integer> format = new ArrayList<Integer>();
        ArrayList<Object> content = new ArrayList<Object>();
        try {
            System.out.println(" " + catalogue.getNameServerInet() + catalogue.getNameServerPort());
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, catalogue.getNameServerInet(), catalogue.getNameServerPort());
            clientSocket.send(sendPacket);
        }catch (Exception e){
            System.out.print("Failed to send packet");
            e.printStackTrace();
        }
        //System.out.println("Sserver iddsds");
        //Waits for greg answere
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        try {
            clientSocket.receive(receivePacket);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        boolean Reged = message.checkRegConf(receivePacket.getData());
        //receivePacket.getData();
        if (Reged){
            System.out.println("Sserver idID: " + catalogue.getIdNumber());
            Thread KeepServerAlive;

            KeepServerAlive = new Thread(new Runnable() {
                @Override
                public void run() {
                    new KeepAlive();
                }
            });
            KeepServerAlive.start();
            System.out.println("Server is now set to keep alive");
        }
        while(true){;}
    }
    public InetAddress getPublicIp() throws Exception{
        URL whatismyip = new URL("http://checkip.amazonaws.com");
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));
            String ip = in.readLine();
            return InetAddress.getByName(ip);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
