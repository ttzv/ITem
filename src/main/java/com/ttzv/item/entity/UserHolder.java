package com.ttzv.item.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserHolder {

    private static UserHolder userHolder;
    private final List<ADUser_n> storedADUsers;
    private ADUser_n currentADUser;

    private UserHolder(){
        storedADUsers = new ArrayList<>();
    }

    public static UserHolder getHolder(){
        if (userHolder == null){
            userHolder = new UserHolder();
        }
        return userHolder;
    }

    public ADUser_n find(String id){
        return storedADUsers
                .stream()
                .filter(adUser_n -> adUser_n.getObjectGUID() == id)
                .findFirst()
                .orElse(null);
    }

    public ADUser_n getCurrentUser() {
        return this.currentADUser;
    }

    public void setCurrentUser(ADUser_n bcurrentADUser) {
        this.currentADUser = bcurrentADUser;
    }


    public void setADUsers(List<ADUser_n> adUsers){
        storedADUsers.clear();
        adUsers.sort((o1, o2) -> {

            LocalDateTime o1Time = o1.getWhenCreated();
            LocalDateTime o2Time = o2.getWhenCreated();
            if(o1Time == null || o2Time == null) return 0;
            if(o1Time.isBefore(o2Time)) return 1;
            else if(o1Time.isEqual(o2Time)) return 0;
            else return -1;
        });
        storedADUsers.addAll(adUsers);
    }

    public List<ADUser_n> getADUsers(){
        return storedADUsers;
    }


}
