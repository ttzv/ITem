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
    private String cityId;
    private String cityName;
    private String cityType;
    private String cityPhone;
    private String cityFax;
    private String position;
    private String userPhone;
    private String userMPhone;

    /**
     * Main array containing all possible user data identifiers, to add new modify UserColumns enum
     */
    public static final String[] columns = UserData.getAllColumns();

    /**
     * Array containing user data identifiers used when importing from LDAP
     * Used only for LDAP import
     */
    public static final UserData[] insertColumns = {UserData.userGUID,
                                                    UserData.samaccountname,
                                                    UserData.givenname,
                                                    UserData.sn,
                                                    UserData.displayname,
                                                    UserData.useraccountcontrol,
                                                    UserData.mail,
                                                    UserData.whenCreated,
                                                    UserData.cityId};


    public User(String... data) {
        LinkedList<String> dataList = new LinkedList<>(Arrays.asList(data));

        this.userInformationMap = new LinkedHashMap<>();
        if (data.length <= columns.length){
            for (int i = 0; i <= columns.length - 1 ; i++) {
                try {dataList.get(i).toString();}
                catch (IndexOutOfBoundsException e){
                   //System.out.println(" user index " + i +" filled by empty string");
                    dataList.add("");
                } catch (NullPointerException e){
                    dataList.set(i,"");
                }
                /*if(dataList.get(i) == null){
                    dataList.set(i, "");
                }*/
                    this.userInformationMap.put(UserData.getById(i), dataList.get(i));
                //System.out.println(this.userInformationMap);
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

    public String getCityId() {
        return userInformationMap.get(UserData.cityId);
    }

    public String getCityName() {
        return userInformationMap.get(UserData.cityName);
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

    public String[] getAllData(){
        ArrayList<String> complete = new ArrayList<>();
        Arrays.asList(UserData.values()).forEach(identifier -> {
            String data = userInformationMap.get(identifier);
            if(!data.isEmpty()) {
                complete.add(data);
            }
        });
        return complete.toArray(new String[0]);
    }

    public LinkedHashMap<UserData, String> getUserInformationMap() {
        return userInformationMap;
    }

    public boolean compareGUID(String guid){
        return getUserGUID().equals(guid);
    }

    /**
     * Determine which information is different from user given in parameter
     * @return List of String userdata identifiers if differences were found, otherwise empty list
     */
    public List<String> findDiff(User user){
        List<String> diff = new ArrayList<>();
        for (UserData ud : insertColumns) {
            if(!this.userInformationMap.get(ud).equals(user.getUserInformationMap().get(ud))){
                diff.add(ud.toString());
            }
        }
        return diff;
    }

    @Override
    public String toString() {
        return Arrays.toString(getComplete());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(getUserGUID(), user.getUserGUID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserGUID());
    }
}