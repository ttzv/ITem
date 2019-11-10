package com.ttzv.item.entity;

import com.ttzv.item.utility.Utility;

import java.util.Date;
import java.util.Objects;

/**
 * Basic User "bean" class used for various operations in this application. Supports creating User from objects implementing DynamicEntity interface.
 */
public class User implements DynamicEntityCompatible, Comparable<User>{

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
    private String whenChanged;

    public User(DynamicEntity userEntity) {
        this.userEntity = userEntity;
    }

    @Override
    public DynamicEntity getEntity() {
        return userEntity;
    }

    //standard getters and setters to allow compatibility with rest of the app.
    public String getGivenName() {
        return userEntity.getValue(UserData.sn.toString());
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

    public void setWhenCreated(String whenCreated) {
        this.userEntity.setValue(UserData.whenCreated.toString(), whenCreated);
    }

    public String getWhenChanged() {
        return userEntity.getValue(UserData.whenChanged.toString());
    }

    public void setWhenChanged(String whenChanged) {
        this.userEntity.setValue(UserData.whenChanged.toString(), whenChanged);
    }

    public String getDistinguishedName() {
        return userEntity.getValue(UserData.distinguishedName.toString());
    }

    public String getCity() {
        return userEntity.getValue(Utility.extractCityFromDn(UserData.distinguishedName.toString()));
    }

    public void setCity(String city) {
        this.userEntity.setValue(UserData.distinguishedName.toString(), city);
    }

    public void setDistinguishedName(String distinguishedName) {
        this.userEntity.setValue(UserData.distinguishedName.toString(), distinguishedName);
    }

    public String getPersonalPhoneNumber() {
        return userEntity.getValue(UserData.userphone.toString());
    }

    public void setPersonalPhoneNumber(String personalPhoneNumber) {
        this.userEntity.setValue(UserData.userphone.toString(), personalPhoneNumber);
    }

    public String getNotes() {
        return "NOT SET";
    }

    //todo: implement
    public void setNotes(String notes) {
        this.userEntity.setValue("notes", notes);
    }

    public String getGUID() {
        return Utility.formatObjectGUID(userEntity.getValue(UserData.objectGUID.toString()));
    }

    public String getSamAccountName() {
        return userEntity.getValue(UserData.samaccountname.toString());
    }

    public void setSamAccountName(String samAccountName) {
        this.userEntity.setValue(UserData.samaccountname.toString(), samAccountName);
    }

    public String getPosition() {
        return userEntity.getValue(UserData.position.toString());
    }

    public String getLandlineNumber() {
        return userEntity.getValue(UserData.userlandline.toString());
    }

    public String getInitMailPass() {
        return userEntity.getValue(UserData.initmailpass.toString());
    }

    public String getCityPhone() {
        return userEntity.getValue(UserData.cityPhone.toString());
    }

    public String getCityFax() {
        return userEntity.getValue(UserData.cityfax.toString());
    }

    @Override
    public int compareTo(User o) {
        Date userCreationDate = Utility.parseDate(this.getWhenCreated());
        Date comparedUserCreationDate = Utility.parseDate(o.getWhenCreated());
        if(userCreationDate != null && comparedUserCreationDate != null)
            return userCreationDate.compareTo(comparedUserCreationDate);
        else
            return -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userEntity.getValue(UserData.objectGUID.toString()).equals(user.userEntity.getValue(UserData.objectGUID.toString()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(userEntity.getValue(UserData.objectGUID.toString()));
    }
}