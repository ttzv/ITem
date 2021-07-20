package com.ttzv.item.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "users")
public @Getter @Setter class ADUser_n {

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
    private Date whenCreated;

    @Column(name = "mail")
    private String email;

    @Column(name = "distinguishedname")
    private String distinguishedName;

    @Column(name = "objectsid")
    private String objectSid;

    @Column(name = "useraccountcontrol")
    private String userAccountControl;

    @Column(name = "lockouttime")
    private Date lockoutTime;

    @OneToOne(mappedBy = "adUser", cascade = CascadeType.ALL)
    private UserDetail_n detail;

    public Office getOffice(){
        if(this.detail != null){
            return this.detail.getOffice();
        }
        return null;
    }

    public UserDetail_n getDetail(){
        if(this.detail != null){
            try {
                if(this.detail.getStorage() == null) this.detail.deserializeStorage();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return this.detail;
        }
        return null;
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
}
