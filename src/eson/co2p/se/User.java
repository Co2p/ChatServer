package eson.co2p.se;

import java.net.InetAddress;

/**
 * User object, handles all information regarding users
 *
 * @author Isidor, Tony and Gordon on  2014-10-21.
 */
public class User {
    String nickname;
    int ID;

    /**
     * Constructor for user, adds a nickname and creates the object
     * @param nickname  the name of the connected user
     */
    public User(String nickname){
        setNickname(nickname);
    }

    /**
     * sets a new nickname for the user
     * @param nickname  the new nickname
     */
    public void setNickname(String nickname){
        this.nickname = nickname.replaceAll("\0", "");
    }

    /**
     * sets the ID number for the user
     * @param ID the ID for the user
     */
    public void setID(int ID){
        this.ID = ID;
    }

    /**
     * gets the nick of the user
     * @return  nickname
     */
    public String getNickname(){
        return nickname;
    }

    /**
     * returns the current ID of the user
     * @return  ID of the user
     */
    public int getID(){
        return ID;
    }

}
