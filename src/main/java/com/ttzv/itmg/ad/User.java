package com.ttzv.itmg.ad;

import java.util.*;

/**
 * Class for convenient storing information about every user in separate list
 * indices legend:
 * 0 - samAccountName
 * 1 - givenName
 * 2 - sn
 * 3 - displayName
 * 4 - userAccountControl
 * Null values should be checked by parser and empty string inserted if needed
 */
public class User {

    private LinkedHashMap<UserData, String> userInformationMap;

    private String userGUID;
    private String samAccountName;
    private String givenName;
    private String sn;
    private String displayName;
    private String userAccountControl;
    private String mail;
    private String whenCreated;
    private String initMailPass;
    private String city;
    private String name;
    private String type;
    private String cityPhone;
    private String fax;
    private String position;
    private String userPhone;
    private String userMPhone;

    /**
     * Main array containing all possible user data identifiers, modify this at first.
     */
    public static final String[] columns = UserData.getAllColumns();

    /**
     * Array containing user data identifiers used when importing from LDAP
     */
    public static final UserData[] insertColumns = {UserData.userGUID,
                                                    UserData.samaccountname,
                                                    UserData.givenname,
                                                    UserData.sn,
                                                    UserData.displayname,
                                                    UserData.useraccountcontrol,
                                                    UserData.mail,
                                                    UserData.whenCreated,
                                                    UserData.city};


    public User(String... data) {
        LinkedList<String> dataList = new LinkedList<>(Arrays.asList(data));

        this.userInformationMap = new LinkedHashMap<>();
        if (data.length <= columns.length){
            for (int i = 0; i <= columns.length - 1 ; i++) {
                try {dataList.get(i);} catch (IndexOutOfBoundsException e){} finally {
                    dataList.add("");
                }
                    this.userInformationMap.put(UserData.getById(i), dataList.get(i));
            }
        } else {
            System.err.println("User: Too many elements in User object (" + data.length + "), current max is (" + columns.length);
        }
    }


    public String getUserGUID() {
        return userInformationMap.get(UserData.userGUID);
    }

    public String getSamAccountName() {
        return userInformationMap.get(UserData.samaccountname);
    }

    public String getGivenName() {
        return userInformationMap.get(UserData.givenname);
    }

    public String getSn() {
        return userInformationMap.get(UserData.sn);
    }

    public String getDisplayName() {
        return userInformationMap.get(UserData.displayname);
    }

    public String getUserAccountControl() {
        return userInformationMap.get(UserData.useraccountcontrol);
    }

    public String getMail() {
        return userInformationMap.get(UserData.mail);
    }

    public String getWhenCreated() {
        return userInformationMap.get(UserData.whenCreated);
    }

    public String getInitMailPass() {
        return userInformationMap.get(UserData.initmailpass);
    }

    public String getCity() {
        return userInformationMap.get(UserData.city);
    }

    public String getName() {
        return userInformationMap.get(UserData.name);
    }

    public String getCityType() {
        return userInformationMap.get(UserData.type);
    }

    public String getCityPhone() {
        return userInformationMap.get(UserData.phone);
    }

    public String getCityFax() {
        return userInformationMap.get(UserData.fax);
    }

    public String getPosition() {
        return userInformationMap.get(UserData.position);
    }

    public String getUserPhone() {
        return userInformationMap.get(UserData.userphone);
    }

    public String getUserMPhone() {
        return userInformationMap.get(UserData.usermphone);
    }

    public String[] getComplete(){
        ArrayList<String> complete = new ArrayList<>();
        Arrays.asList(insertColumns).forEach(identifier -> {
            String data = userInformationMap.get(identifier);
            if(!data.isEmpty()) {
                complete.add(data);
            }
        });
    return complete.toArray(new String[0]);
    }

//    @Override
//    public String toString() {
//        return Arrays.toString(getComplete());
//    }
}