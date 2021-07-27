package com.ttzv.item.entity;

import com.fasterxml.jackson.core.JsonProcessingException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "users")
public class ADUser_n {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name = "objectguid", unique = true, nullable = false)
    private String objectGUID;

    @Column(name = "givenname")
    private String givenName;

    @Column(name = "sn")
    private String sn;

    @Column(name = "displayname")
    private String displayName;

    @Column(name = "samaccountname")
    private String sAMAccountName;

    @Column(name = "whencreated")
    private LocalDateTime whenCreated;

    @Column(name = "mail")
    private String email;

    @Column(name = "distinguishedname")
    private String distinguishedName;

    @Column(name = "objectsid")
    private String objectSid;

    @Column(name = "useraccountcontrol")
    private String userAccountControl;

    @Column(name = "lockouttime")
    private LocalDateTime lockoutTime;

    @OneToOne(mappedBy = "adUser", cascade = CascadeType.ALL)
    private UserDetail_n detail;

    public Office getOffice(){
        if(this.detail != null){
            return this.detail.getOffice();
        }
        return null;
    }

    public UserDetail_n getDetail(){
        if(detail == null) {
            detail = new UserDetail_n();
            detail.setAdUser(this);
        }
        try {
            if(this.detail.getStorage() == null) this.detail.deserializeStorage();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return this.detail;
    }

    public String getOfficeLocation(){
        Office office = getOffice();
        if (office != null){
            return office.getLocation() ;
        }
        return "";
    }

    public String getPhoneNumber(){
        if (this.detail != null){
            return this.detail.getPhoneNumber();
        }
        return "";
    }

    public String getLandLineNumber(){
        if (this.detail != null){
            return this.detail.getLandlineNumber();
        }
        return "";
    }

    public String getCity(){
        if(this.detail != null && this.detail.getOffice() != null){
            return this.detail.getOffice().getName() + " " + this.detail.getOffice().getName2();
        }
        return "";
    }

    public String getPosition(){
        if (this.detail != null){
            return this.detail.getPosition();
        }
        return "";
    }

    public void setPosition(String position){
        if (this.detail != null){
            this.detail.setPosition(position);
        }
    }

    @Override
    public String toString() {
        return "ADUser_n{" +
                "id=" + id +
                ", objectGUID='" + objectGUID + '\'' +
                ", givenName='" + givenName + '\'' +
                ", sn='" + sn + '\'' +
                ", displayName='" + displayName + '\'' +
                ", sAMAccountName='" + sAMAccountName + '\'' +
                ", whenCreated='" + whenCreated + '\'' +
                ", email='" + email + '\'' +
                ", distinguishedName='" + distinguishedName + '\'' +
                ", objectSid='" + objectSid + '\'' +
                ", userAccountControl='" + userAccountControl + '\'' +
                ", lockoutTime='" + lockoutTime + '\'' +
                '}';
    }

    public void merge(ADUser_n other){

        setGivenName(other.getGivenName());
        setSn(other.getSn());
        setDisplayName(other.getDisplayName());
        setSAMAccountName(other.getSAMAccountName());
        setEmail(other.getEmail());
        setDistinguishedName(other.getDistinguishedName());
        setSn(other.getSn());
        setUserAccountControl(other.getUserAccountControl());
        setLockoutTime(other.getLockoutTime());
        setWhenCreated(other.getWhenCreated());
        setObjectSid(other.getObjectSid());


    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ADUser_n adUser_n = (ADUser_n) o;

        return Objects.equals(givenName, adUser_n.givenName)
                && Objects.equals(objectGUID, adUser_n.objectGUID)
                && Objects.equals(objectSid, adUser_n.objectSid)
                && Objects.equals(sn, adUser_n.sn)
                && Objects.equals(displayName, adUser_n.displayName)
                && Objects.equals(sAMAccountName, adUser_n.sAMAccountName)
                && Objects.equals(email, adUser_n.email)
                && Objects.equals(distinguishedName, adUser_n.distinguishedName)
                && Objects.equals(userAccountControl, adUser_n.userAccountControl)
                && Objects.equals(whenCreated, adUser_n.whenCreated)
                && Objects.equals(lockoutTime, adUser_n.lockoutTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(objectGUID, objectSid);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getObjectGUID() {
        return objectGUID;
    }

    public void setObjectGUID(String objectGUID) {
        this.objectGUID = objectGUID;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getSAMAccountName() {
        return sAMAccountName;
    }

    public void setSAMAccountName(String sAMAccountName) {
        this.sAMAccountName = sAMAccountName;
    }

    public LocalDateTime getWhenCreated() {
        return whenCreated;
    }

    public void setWhenCreated(LocalDateTime whenCreated) {
        this.whenCreated = whenCreated;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDistinguishedName() {
        return distinguishedName;
    }

    public void setDistinguishedName(String distinguishedName) {
        this.distinguishedName = distinguishedName;
    }

    public String getObjectSid() {
        return objectSid;
    }

    public void setObjectSid(String objectSid) {
        this.objectSid = objectSid;
    }

    public String getUserAccountControl() {
        return userAccountControl;
    }

    public void setUserAccountControl(String userAccountControl) {
        this.userAccountControl = userAccountControl;
    }

    public LocalDateTime getLockoutTime() {
        return lockoutTime;
    }

    public void setLockoutTime(LocalDateTime lockoutTime) {
        this.lockoutTime = lockoutTime;
    }

    public void setDetail(UserDetail_n detail) {
        this.detail = detail;
    }
}


