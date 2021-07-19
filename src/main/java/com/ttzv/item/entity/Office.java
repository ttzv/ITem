package com.ttzv.item.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "offices")
public @Getter @Setter
class Office {

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
}
