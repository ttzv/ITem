package com.ttzv.item.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "command_box")
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
