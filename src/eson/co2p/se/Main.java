package eson.co2p.se;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Main {

    public static void main(String[] args) {
        catalogue.setName("Glorious Chat");
        try {
            catalogue.setNameServer(InetAddress.getByName("itchy.cs.umu.se"), 1337);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
