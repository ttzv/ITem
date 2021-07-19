package com.ttzv.item.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

    @Column(name = "storage")
    private String storageJSON;

    @Transient
    private Map<String, Object> storage;

    @Column(name = "landline_number")
    private String landlineNumber;

    @Column(name = "phone_number")
    private String phoneNumber;

    public Integer getOfficeId() {
        if(office != null) {
            return office.getId();
        }
        return null;
    }

    @Override
    public String toString() {
        return "UserDetail_n{" +
                "id=" + id +
                ", officeId=" + getOfficeId() +
                ", position='" + position + '\'' +
                ", storage='" + storage + '\'' +
                ", landlineNumber='" + landlineNumber + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }

    void deserializeStorage() throws JsonProcessingException{
        ObjectMapper objectMapper = new ObjectMapper();
        if (storageJSON == null || storageJSON.isEmpty()){
            storage = new HashMap<>();
        } else {
            storage = objectMapper.readValue(storageJSON, HashMap.class);
        }
    }

    public void serializeStorage() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        storageJSON = objectMapper.writeValueAsString(storage);
    }

    public void updateStorage(String key, String val){
        storage.put(key, val);
    }

    public void deleteFromStorage(String key){
        storage.remove(key);
    }

}
