package com.ttzv.item.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "command_box")
public @Getter @Setter
class CommandBox {

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

    @Override
    public String toString() {
        return "CommandBox_n{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", tags='" + tags + '\'' +
                ", uid='" + uid + '\'' +
                '}';
    }
}
