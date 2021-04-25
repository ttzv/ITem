package com.ttzv.item.entity;

import lombok.Data;
import org.hibernate.annotations.Columns;

import javax.persistence.*;

@Entity
@Table(name= "ad_users")
public @Data class ADUser_n {
    @Id @GeneratedValue
    @Column(name="id")
    private int id;

    @Column(name="objectguid")
    private String objectGUID;

    @Column(name = "givenname")
    private String givenName;

    public ADUser_n() {
    }

}
