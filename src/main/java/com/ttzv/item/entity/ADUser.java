package com.ttzv.item.entity;

import com.ttzv.item.utility.Utility;
import org.hibernate.annotations.Entity;
import org.hibernate.annotations.Table;


import java.util.*;

/**
 * Basic User "bean" class used for various operations in this application. Supports creating User from objects implementing DynamicEntity interface.
 */

public class ADUser implements DynamicEntityCompatible, Comparable<ADUser>{

    private DynamicEntity userEntity;

    private String guid;
    private String samaccountname;
    private String givenname;
    private String sn;
    private String displayname;
    private String useraccountcontrol;
    private String mail;
    private String whenCreated;
    private String distinguishedName;
    private String city;
    private String whenChanged;

    public ADUser(DynamicEntity userEntity)
    {
        this.userEntity = userEntity;
        this.city = Utility.polonize(Utility.extractCityFromDn(userEntity.getValue(UserData.distinguishedName.toString())));
        if(!userEntity.getKeys().contains(UserData.city.toString())) {
            userEntity.add(Utility.polonize(UserData.city.toString()), this.city);
        }
        if(userEntity.getValue(UserData.whenCreated.toString()).contains("0Z")) {
            this.whenCreated = Utility.formatLdapDate(userEntity.getValue(UserData.whenCreated.toString()));
            userEntity.setValue(UserData.whenCreated.toString(), this.whenCreated);
        }
    }

    @Override
    public String getUniqueIdentifier() {
        return getGUID();
    }

    @Override
    public DynamicEntity getEntity() {
        return userEntity;
    }

    //standard getters and setters to allow compatibility with rest of the app.
    public String getGivenName() {
        return userEntity.getValue(UserData.givenname.toString());
    }

    public void setGivenName(String givenName) {
        this.userEntity.setValue(UserData.sn.toString(), givenName);
    }

    public String getSn() {
        return userEntity.getValue(UserData.sn.toString());
    }

    public void setSn(String sn) {
        this.userEntity.setValue(UserData.sn.toString(), sn);
    }

    public String getDisplayName() {
        return userEntity.getValue(UserData.displayname.toString());
    }

    public void setDisplayName(String displayName) {
        this.userEntity.setValue(UserData.displayname.toString(), displayName);
    }

    public String getUserAccountControl() {
        return userEntity.getValue(UserData.useraccountcontrol.toString());
    }

    public void setUserAccountControl(String userAccountControl) {
        this.userEntity.setValue(UserData.useraccountcontrol.toString(), userAccountControl);
    }

    public String getMail() {
        return userEntity.getValue(UserData.mail.toString());
    }

    public void setMail(String mail) {
        this.userEntity.setValue(UserData.mail.toString(), mail);
    }

    public String getWhenCreated() {
        return userEntity.getValue(UserData.whenCreated.toString());
    }

   /* public String getWhenChanged() {
        return userEntity.getValue(UserData.whenChanged.toString());
    }*/

    public String getDistinguishedName() {
        return userEntity.getValue(UserData.distinguishedName.toString());
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.userEntity.setValue(UserData.city.toString(), city);
    }

    public void setDistinguishedName(String distinguishedName) {
        this.userEntity.setValue(UserData.distinguishedName.toString(), distinguishedName);
    }

    public String getGUID() {
        return userEntity.getValue(UserData.objectGUID.toString());
    }

    public String getSamAccountName() {
        return userEntity.getValue(UserData.samaccountname.toString());
    }

    public void setSamAccountName(String samAccountName) {
        this.userEntity.setValue(UserData.samaccountname.toString(), samAccountName);
    }

    @Override
    public int compareTo(ADUser o) {
//        Date userCreationDate = Utility.parseDate(this.getWhenCreated(), Utility.globalDateFormat());
//        Date comparedUserCreationDate = Utility.parseDate(o.getWhenCreated(), Utility.globalDateFormat());
//        if(userCreationDate != null && comparedUserCreationDate != null)
//            return userCreationDate.compareTo(comparedUserCreationDate) * -1;
//        else
            return -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ADUser ADUser = (ADUser) o;
        return userEntity.getValue(UserData.objectGUID.toString()).equals(ADUser.userEntity.getValue(UserData.objectGUID.toString()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(userEntity.getValue(UserData.objectGUID.toString()));
    }

    @Override
    public String toString() {
        return userEntity.getList().toString();
    }

    public Set<String> getUpdatedKeys(ADUser ADUser){
        Set<String> keys = null;
        if(this.equals(ADUser)){
            keys = new HashSet<>();
            for (String key : this.getEntity().getKeys()) {
                String thisValue = this.getEntity().getValue(key);
                String comparedValue = ADUser.getEntity().getValue(key);
                if (!thisValue.equals(comparedValue)){
                    keys.add(key);
                }
            }
        }
        return keys;
    }

    public boolean hasDifferentVals(ADUser ADUser) {
        //checking every value if user is the same, returns true on first different value
        if (this.equals(ADUser)) {
            List<String> thisList = this.getEntity().getList();
            List<String> userList = ADUser.getEntity().getList();
            if (thisList.equals(userList)) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
}