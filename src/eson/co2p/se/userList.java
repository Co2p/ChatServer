package eson.co2p.se;

import java.util.ArrayList;
import java.util.Queue;

/**
 * Stores users, has a upper limit of 255 at any given time
 * @author gordon on 22/10/14.
 */
public class userList {
    private static ArrayList<User> users = new ArrayList<User>();
    private static ArrayList<Integer> removed = new ArrayList<Integer>();
    //private static Queue<Integer> removed;
    private static boolean first=true;
    private static int Connected = 0;
    private static String LastUser = null;



    public static String GetLastUser(){
        return LastUser;
    }
    public static void setLastUser(String Name){
        LastUser = Name;
    }


    /**
     * Adds a user to the array of active users
     * @param user the user to be added
     */
    public static Integer addUser(User user){
        Integer ID = null;
        if(first){
            firstRun();
        }
        if (!removed.isEmpty()){
            Connected++;
            ID = removed.get(removed.size()-1);
            removed.remove(removed.size()-1);
            user.setID(ID);
            users.set(ID, user);
        }else{
            System.out.println("UserList full!");
        }
        return ID;
    }

    /**
     * Sets the user to null and adds the index of the user as the next place to add a user
     * @param ID user ID
     */
    //TODO if user disconnects by quitting problem, than user don't get removed the list
    public static void removeUser(int ID){
        users.set(ID, null);
        removed.add(ID);
        Connected--;
    }

    /**
     * Returns the user in the given index (null if there is no user)
     * @param i user index
     * @return User object
     * @see eson.co2p.se.User
     */
    public static User getUser(int i){
        return users.get(i);
    }

    public static ArrayList<User> getUserList(){
        return users;
    }

    public static int getConnected(){
        return Connected;
    }

    /**
     * Runs the first time a user is added, adds 254-0 to the arraylist so the user can be added
     */
    private static void firstRun(){
        for(int i = 0; i < 255; i++){
            removed.add(i);
            users.add(null);
        }
        first=false;
    }

}
