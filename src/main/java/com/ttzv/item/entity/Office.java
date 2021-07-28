package com.ttzv.item.entity;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "offices")
public class Office {

    @Id @GeneratedValue
    @Column
    private int id;

    @Column
    private String name;

    @Column(name = "name_2")
    private String name2;

    @Column
    private String location;

    @Column(name = "location_2")
    private String location2;

    @Column
    private String phonenumber;

    @Column
    private String landline;

    @Column
    private String fax;

    @Column
    private String postalcode;

    @OneToMany(mappedBy = "office")
    private Set<UserDetail_n> userDetails;

    @Override
    public String toString() {
        return "Office{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", name2='" + name2 + '\'' +
                ", location='" + location + '\'' +
                ", location2='" + location2 + '\'' +
                ", phonenumber='" + phonenumber + '\'' +
                ", landline='" + landline + '\'' +
                ", fax='" + fax + '\'' +
                ", postalcode='" + postalcode + '\'' +
                '}';
    }

    public Set<ADUser_n> getUsers(){
        if (!this.userDetails.isEmpty()){
            return this.userDetails.stream().map(UserDetail_n::getAdUser).collect(Collectors.toSet());
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Office office = (Office) o;
        return id == office.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation2() {
        return location2;
    }

    public void setLocation2(String location2) {
        this.location2 = location2;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getLandline() {
        return landline;
    }

    public void setLandline(String landline) {
        this.landline = landline;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    public Set<UserDetail_n> getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(Set<UserDetail_n> userDetails) {
        this.userDetails = userDetails;
    }
}
