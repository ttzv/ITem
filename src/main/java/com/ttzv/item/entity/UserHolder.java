package com.ttzv.item.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserHolder {

    private static UserHolder userHolder;

    private final List<ADUser_n> storedADUsers;

    private EntityDAO<ADUser> userEntityDAO;
    private List<ADUser> ADUserList;
    private List<ADUser> newADUsers;

    private ADUser_n currentADUser;
    private int currentIndex;

    private UserHolder(){
        storedADUsers = new ArrayList<>();
    }

    public static UserHolder getHolder(){
        if (userHolder == null){
            userHolder = new UserHolder();
        }
        return userHolder;
    }

    public List<ADUser> getAllUsers() {
        Collections.sort(ADUserList);
        return ADUserList;
    }

    public ADUser_n find(String id){
        return storedADUsers
                .stream()
                .filter(adUser_n -> adUser_n.getObjectGUID() == id)
                .findFirst()
                .orElse(null);
    }

    public List<ADUser> getNewest(int numberOfUsers){
        List<ADUser> newADUserList = getAllUsers();
        if(newADUserList.size() <= 0){
            return null;
        }

        return getAllUsers().subList(0, numberOfUsers);
    }


    public ADUser_n getCurrentUser() {
        return this.currentADUser;
    }

    public void setCurrentUser(ADUser_n bcurrentADUser) {
        this.currentADUser = bcurrentADUser;
    }

    public  void addUser (ADUser u){
        if(newADUsers == null){
            newADUsers = new ArrayList<>();
            currentIndex = 0;
        }
        newADUsers.add(u);
    }

    public void clear(){
        if(newADUsers != null) {
            newADUsers.clear();
        }
    }

    public ADUser next(){
        if(currentIndex >= newADUsers.size() - 1)
            currentIndex = 0;
        else currentIndex ++;

        return newADUsers.get(currentIndex);
    }

    public ADUser previous(){
        if(currentIndex <= 0){
            currentIndex = newADUsers.size()-1;
        } else currentIndex--;

        return newADUsers.get(currentIndex);
    }

    public  int getCurrentIndex() {
        if(currentIndex >= newADUsers.size()) {
            currentIndex = 0;
            return 0;
        } else {
            return currentIndex;
        }
    }

    public  int getMaxCount(){
        return newADUsers.size();
    }

    public ADUser getFirst(){
        if(this.ADUserList == null || this.ADUserList.size() <= 0){
            return null;
        }
        Collections.sort(ADUserList);
        return this.ADUserList.get(0);
    }

    public void setADUsers(List<ADUser_n> adUsers){
        storedADUsers.clear();
        adUsers.sort((o1, o2) -> {
            LocalDateTime o1Time = o1.getWhenCreated();
            LocalDateTime o2Time = o2.getWhenCreated();
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
