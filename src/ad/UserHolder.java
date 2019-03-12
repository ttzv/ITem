package ad;

import java.util.ArrayList;
import java.util.List;

public class UserHolder {

    private static List<User> newUsers;

    private static User currentUser;
    private static int currentIndex;

    public static User getCurrentUser() {
        return newUsers.get(currentIndex);
    }

    public static void setCurrentUser(User bcurrentUser) {
        currentUser = bcurrentUser;
    }

    public static void addUser (User u){
        if(newUsers == null){
            newUsers = new ArrayList<>();
            currentIndex = 0;
        }
        newUsers.add(u);
    }

    public static void clear(){
        if(newUsers != null) {
            newUsers.clear();
        }
    }

    public static User next(){
        if(currentIndex >= newUsers.size() - 1)
            currentIndex = 0;
        else currentIndex ++;

        return newUsers.get(currentIndex);
    }

    public static User previous(){
        if(currentIndex <= 0){
            currentIndex = newUsers.size()-1;
        } else currentIndex--;

        return newUsers.get(currentIndex);
    }

    public static int getCurrentIndex() {
        return currentIndex;
    }

    public static int getMaxCount(){
        return newUsers.size();
    }


}
