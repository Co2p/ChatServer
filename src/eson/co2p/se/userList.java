package eson.co2p.se;

import java.util.ArrayList;
import java.util.Queue;

/**
 * Stores users, has a upper limit of 255 at any given time
 * @author gordon on 22/10/14.
 */
public class userList {
    private static User[] users = new User[255];
    private static ArrayList<Integer> removed;
    private static boolean first=true;

    public static void addUser(User user){
        if (first){
            firstRun();
        }
        if (!removed.isEmpty()){
            users[removed.remove(0)] = user;
        }
    }

    public static void removeUser(int i){
        users[i] = null;
        removed.add(0);
    }

    public static User getUser(int i){
        return users[i];
    }

    private static void firstRun(){
        int i = 254;
        for (User user : users) {
            removed.add(i);
            i--;
        }
        first=false;
    }

}
