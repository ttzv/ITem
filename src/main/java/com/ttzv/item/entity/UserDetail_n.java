package com.ttzv.item.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "user_details")
public @Getter @Setter class UserDetail_n {

    @Id @GeneratedValue
    @Column
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "office_id")
    private Office office;

    @OneToOne
    @JoinColumn(name = "user_id")
    private ADUser_n adUser;

    @Column
    private String position;

    @Column
    private String storage;

    @Column(name = "landline_number")
    private String landlineNumber;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Override
    public String toString() {
        return "UserDetail_n{" +
                "id=" + id +
                ", officeId=" + office.getId() +
                ", position='" + position + '\'' +
                ", storage='" + storage + '\'' +
                ", landlineNumber='" + landlineNumber + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
