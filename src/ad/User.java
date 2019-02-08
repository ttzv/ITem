package ad;

import java.util.LinkedList;

/**
 * Class for convenient storing information about every user in 
 */
public class UserDataRow extends LinkedList<String> {

    public UserDataRow(String... data) {
        for (String d : data){
            this.add(d);
        }
    }
}
