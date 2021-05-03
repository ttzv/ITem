package com.ttzv.item.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public @Getter @Setter class CommandBox_n {

    @Id
    @GeneratedValue
    @Column
    private int id;

    @Column
    private String title;
    @Column
    private String content;
    @Column
    private String tags;
    @Column
    private String uid;

}
