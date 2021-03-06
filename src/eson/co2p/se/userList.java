package eson.co2p.se;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Stores users, has a upper limit of 255 at any given time, also handles the
 * information gathered by threads checking for Users connecting and disconnecting
 * @author Gordon Tony and Isidor on 22/10/14.
 */
public class userList {
    private static ArrayList<User> users = new ArrayList<User>();
    private static ArrayList<Integer> removed = new ArrayList<Integer>();
    private static Hashtable<String, Integer> userhash = new Hashtable<String, Integer>();
    private static boolean first=true;
    private static int Connected = 0;
    private static String LastUser = "default";


    /**
     * returns the latest connected user to the server
     * @return  the latest connected user
     */
    public static String GetLastUser(){
        return LastUser;
    }

    /**
     *
     */
    public static boolean isUser(String name){
        return userhash.containsKey(name);
    }

    /**
     * sets the latest user connected to the server (used by UJOIN)
     * @param Name  Nickname of the latest connected user
     */
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
            ID = removed.remove(removed.size() - 1);
            userhash.put(user.getNickname(), ID);
            user.setID(ID);
            users.set(ID, user);
        }else{
            System.out.println("UserList full!");
            return -1;
        }
        return ID;
    }

    /**
     * Sets the user to null and adds the index of the user as the next place to add a user
     * @param ID user ID
     */
    public static void removeUser(int ID){
        String tempusername = users.get(ID).getNickname();
        users.set(ID, null);
        userhash.remove(tempusername);
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

    /**
     * returns the id-number of a username
     * @param userName  the username to check
     * @return  the id of the username
     */
    public static int getID(String userName) {
        if (userhash.contains(userName)) {
            return userhash.get(userName);
        }
        return -1;
    }

    /**
     *  returns a list containing all connected users as object "User"
     * @return userlist containing all users connected
     */
    public static ArrayList<User> getUserList(){
        return users;
    }

    /**
     * returns the number of connected users on the server
     * @return  number of connected users
     */
    public static int getConnected(){
        return Connected;
    }

    /**
     * for testing purposes only
     */
    public static void printAvailableIds(){
        for(Integer ID: removed){
            System.out.println("ID: " + ID);
        }
    }

    public static String swapNick(String oldNick, String newNick) {
        /*boolean exists;
        int number = 0;
        do {
            exists = false;
            if (userhash.contains(newNick)) {
                exists = true;
                newNick = newNick + number;
                number++;
            }
        } while (exists);*/

        if(userList.isUser(newNick)) {
            int number = 0;
            while (userList.isUser(newNick + number)) {
                number++;
            }
            newNick = newNick + number;
        }

        int ID = getID(oldNick);
        userhash.remove(oldNick);
        userhash.put(newNick, ID);

        return newNick;
    }

    /**
     * Runs the first time a user is added, adds 0 -> 254 the arraylist so the user can be added
     */
    private static void firstRun(){
        for(int i = 255; i >= 0; i--){
            removed.add(i);
            users.add(null);
        }
        first=false;
    }

}
