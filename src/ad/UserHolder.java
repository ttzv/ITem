package ad;

public class UserHolder {

    private static User currentUser;

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User bcurrentUser) {
        currentUser = bcurrentUser;
    }
}
