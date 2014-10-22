package eson.co2p.se;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: derp
 * Date: 2014-10-21
 * Time: 16:21
 * To change this template use File | Settings | File Templates.
 */
public class KeepAlive {

    public KeepAlive(){
        try {
            KeepServerAlive();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    private void KeepServerAlive() throws SocketException {
        DatagramSocket clientSocket = new DatagramSocket();
        while(true){
            InetAddress myip = null;
            catalogue.setThisServer(myip, 2222);
            byte[] sendData = message.keepAlive();
            try {
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, catalogue.getNameServerInet(), catalogue.getNameServerPort());
                clientSocket.send(sendPacket);
            }catch (Exception e){
                System.out.print("Failed send keep alive message");
                e.printStackTrace();
            }
            try {
                System.out.print("now sleeping\n");
                Thread.sleep(2400);
                System.out.print("sleept\n");
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }
}
