package com.ttzv.item.entity;

import com.ttzv.item.dao.UserComboWrapper;

import javax.naming.NamingException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserHolder {

    private EntityDAO<User> userEntityDAO;
    private List<User> userList;
    private UserComboWrapper userComboWrapper;


    private List<User> newUsers;

    private User currentUser;
    private int currentIndex;

    public UserHolder(EntityDAO<User> userEntityDAO) throws SQLException, IOException, NamingException {
        this.userEntityDAO = userEntityDAO;
        this.userList = userEntityDAO.getAllEntities();
    }

    public List<User> getAllUsers() {
        Collections.sort(userList);
        return userList;
    }

    public User getUser(String id){
        User fUser = null;
        for (User u : userList) {
            if(u.getGUID().equals(id)){
                fUser = u;
            }
        }
        return fUser;
    }

    public List<User> getNewest(int numberOfUsers){
        return getAllUsers().subList(0, numberOfUsers);
    }

    public void refresh () throws SQLException, IOException, NamingException {
        this.userList = userEntityDAO.getAllEntities();
    }

    public  User getCurrentUser() {
        return this.currentUser;
    }

    public  void setCurrentUser(User bcurrentUser) {
        this.currentUser = bcurrentUser;
    }

    public  void addUser (User u){
        if(newUsers == null){
            newUsers = new ArrayList<>();
            currentIndex = 0;
        }
        newUsers.add(u);
    }

    public  void clear(){
        if(newUsers != null) {
            newUsers.clear();
        }
    }

    public  User next(){
        if(currentIndex >= newUsers.size() - 1)
            currentIndex = 0;
        else currentIndex ++;

        return newUsers.get(currentIndex);
    }

    public  User previous(){
        if(currentIndex <= 0){
            currentIndex = newUsers.size()-1;
        } else currentIndex--;

        return newUsers.get(currentIndex);
    }

    public  int getCurrentIndex() {
        if(currentIndex >= newUsers.size()) {
            currentIndex = 0;
            return 0;
        } else {
            return currentIndex;
        }
    }

    public  int getMaxCount(){
        return newUsers.size();
    }


}
