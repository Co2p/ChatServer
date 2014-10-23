package eson.co2p.se;

import java.net.*;

/**
 * Object used for creating a keep-alive message that's supposed to be sent to the
 * nameserver once every <10seconds, the handling of sending and creating the message is
 * in this class
 *
 * @author Gordon Tony Isidor
 */
public class KeepAlive {
    /**
     * general Constructor for the keepAlive message, calls the method used for sending and
     * creating the message
     */
    public KeepAlive(){
        try {
            KeepServerAlive();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends the keepAlive message to a given nameserver extracted from catalogue, and
     * sends the message every 2400ms.
     * @throws SocketException
     */
    private void KeepServerAlive() throws SocketException {
        DatagramSocket clientSocket = new DatagramSocket();
        while(true){
            //  Creates the keepAlive message
            byte[] sendData = message.keepAlive();
            try {
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, catalogue.getNameServerInet(), catalogue.getNameServerPort());
                clientSocket.send(sendPacket);
            }catch (Exception e){
                System.out.print("Failed send keep alive message");
                e.printStackTrace();
            }
            //  Even if the message didn't got sent, sleep for 2400 ms and then go trough the loop again
            //  trying to send the message again, this creates a fail-safe as the server has a time-out for
            //  servers every 10 seconds, so even if two keepalive messages gets lost in the cyberspace
            //  it doesn't get disconnected
            try {
                System.out.print("now sleeping\n");
                Thread.sleep(2400);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }
}
