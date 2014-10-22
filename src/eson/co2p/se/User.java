package eson.co2p.se;

import java.net.InetAddress;

/**
 * Created by root on 2014-10-21.
 */
public class User {
    String nickname;
    InetAddress ip;
    int port;
    int ID;

    public User(String nickname, InetAddress ip, int port){
        setNickname(nickname);
        setIp(ip);
        setPort(port);
    }

    public void setNickname(String nickname){
        this.nickname = nickname;
    }

    public void setIp(InetAddress ip){
        this.ip = ip;
    }

    public void setPort(int port){
        this.port = port;
    }

    public void setID(int ID){
        this.ID = ID;
    }

    public String getNickname(){
        return nickname;
    }

    public InetAddress getIp(){
        return ip;
    }
    //Gets port
    public int getPort(){
        return port;
    }
}
