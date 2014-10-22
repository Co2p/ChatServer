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

    /**
     * Adds a user to the array of active users
     * @param user the user to be added
     */
    public static void addUser(User user){
        if (first){
            firstRun();
        }

        if (!removed.isEmpty()){
            users[removed.remove(0)] = user;
        }
    }

    /**
     * Sets the user to null and adds the index of the user as the next place to add a user
     * @param i user index
     */
    public static void removeUser(int i){
        users[i] = null;
        removed.add(0);
    }

    /**
     * Returns the user in the given index (null if there is no user)
     * @param i user index
     * @return User object
     * @see eson.co2p.se.User
     */
    public static User getUser(int i){
        return users[i];
    }

    /**
     * Runs the first time a user is added, adds 254-0 to the arraylist so the user can be added
     */
    private static void firstRun(){
        int i = 254;
        for (User user : users) {
            removed.add(i);
            i--;
        }
        first=false;
    }

}
