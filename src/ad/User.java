package ad;

import java.util.LinkedList;

/**
 * Class for convenient storing information about every user in separate list
 * indices legend:
 * 0 - samAccountName
 * 1 - givenName
 * 2 - sn
 * 3 - displayName
 * 4 - userAccountControl
 * Null values should be checked by parser and null string inserted if needed
 */
public class User extends LinkedList<String> {

    public static final String[] columns = {"samaccountname",
                                            "givenname",
                                            "sn",
                                            "displayname",
                                            "useraccountcontrol",
                                            "mail",
                                            "whenCreated",
                                            "name",
                                            "type",
                                            "phone",
                                            "fax"};
    public User(String... data) {
        for (String d : data){
            this.add(d);
        }
    }

    public String getSamAccountName(){
      return this.get(0);
    }

    public String getGivenName(){
        return this.get(1);
    }

    public String getSn(){
        return this.get(2);
    }

    public String getDisplayName(){
        return this.get(3);
    }

    public String getUserAccountControl(){
        return this.get(4);
    }

    public String getMail(){return this.get(5);}

    public String getWhenCreated(){return this.get(6);}

    public String getCity(){return this.get(7);}

    public String getCityType(){return this.get(8);}

    public String getCityPhone(){return this.get(9);}

    public String getCityFax(){return this.get(10);}

    public String[] getComplete(){
        return this.toArray(new String [0]);
    }




}
