package com.ttzv.itmg.ad;

import java.util.Arrays;
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

    public static final String[] columns = {"userGUID",
                                            "samaccountname",
                                            "givenname",
                                            "sn",
                                            "displayname",
                                            "useraccountcontrol",
                                            "mail",
                                            "whenCreated",
                                            "initmailpass",
                                            "city",
                                            "name",
                                            "type",
                                            "phone",
                                            "fax",
                                            "position",
                                            "userphone",
                                            "usermphone"};

    public static final String[] insertColumns = {columns[0],
                                                  columns[1],
                                                  columns[2],
                                                  columns[3],
                                                  columns[4],
                                                  columns[5],
                                                  columns[6],
                                                  columns[7],
                                                  columns[9]};
    public User(String... data) {
        for (String d : data){
            if(d == null || d.isEmpty()){
                this.add("");
            } else {
                this.add(d);
            }
        }
    }

    public String getUserGUID(){ return this.get(0); }

    public String getSamAccountName(){
      return this.get(1);
    }

    public String getGivenName(){
        return this.get(2);
    }

    public String getSn(){
        return this.get(3);
    }

    public String getDisplayName(){
        return this.get(4);
    }

    public String getUserAccountControl(){
        return this.get(5);
    }

    public String getMail(){return this.get(6);}

    public String getWhenCreated(){return this.get(7);}

    public String getInitMailPass(){return this.get(8);}

    public String getCity(){return this.get(10);}

    public String getCityType(){return this.get(11);}

    public String getCityPhone(){return this.get(12);}

    public String getCityFax(){return this.get(13);}

    public String getPosition(){return this.get(14);}

    public String getUserPhone(){return this.get(15);}

    public String getUserMPhone(){return this.get(16);}

    public String[] getComplete(){
        return this.toArray(new String [0]);
    }

    @Override
    public String toString() {
        return Arrays.toString(getComplete());
    }
}