package com.ttzv.item.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "users")
public @Getter @Setter class ADUser_n {

    @Id @GeneratedValue
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
    private String whenCreated;

    @Column(name = "mail")
    private String email;

    @Column(name = "distinguishedname")
    private String distinguishedName;

    @Column(name = "objectsid")
    private String objectSid;

    @Column(name = "useraccountcontrol")
    private String userAccountControl;

    @Column(name = "lockouttime")
    private String lockoutTime;

    @OneToOne(mappedBy = "adUser")
    private UserDetail_n detail;

    public Office getOffice(){
        if(this.detail != null){
            return this.detail.getOffice();
        }
        return null;
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
