package com.ttzv.item.entity;

import javax.naming.NamingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserHolder {

    private EntityDAO<ADUser> userEntityDAO;
    private List<ADUser> ADUserList;
    private List<ADUser> newADUsers;

    private ADUser currentADUser;
    private int currentIndex;

    public UserHolder(EntityDAO<ADUser> userEntityDAO) throws SQLException, IOException, NamingException, GeneralSecurityException {
        List<ADUser> ADUsers = new ArrayList<>();
        if(userEntityDAO != null) {
            this.userEntityDAO = userEntityDAO;
            this.ADUserList = userEntityDAO.getAllEntities();
        } else {
            this.ADUserList = ADUsers;
        }
    }

    public List<ADUser> getAllUsers() {
        Collections.sort(ADUserList);
        return ADUserList;
    }

    public ADUser getUser(String id){
        ADUser fADUser = null;
        for (ADUser u : ADUserList) {
            if(u.getGUID().equals(id)){
                fADUser = u;
            }
        }
        return fADUser;
    }

    public List<ADUser> getNewest(int numberOfUsers){
        List<ADUser> newADUserList = getAllUsers();
        if(newADUserList.size() <= 0){
            return null;
        }

        return getAllUsers().subList(0, numberOfUsers);
    }


    public ADUser getCurrentUser() {
        return this.currentADUser;
    }

    public  void setCurrentUser(ADUser bcurrentADUser) {
        this.currentADUser = bcurrentADUser;
    }

    public  void addUser (ADUser u){
        if(newADUsers == null){
            newADUsers = new ArrayList<>();
            currentIndex = 0;
        }
        newADUsers.add(u);
    }

    public  void clear(){
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

    public UserHolder syncAndRefresh(EntityDAO<ADUser> daoToSyncWith) throws SQLException, IOException, NamingException, GeneralSecurityException {
        userEntityDAO.syncDataSourceWith(daoToSyncWith);
        this.ADUserList = this.userEntityDAO.getAllEntities();
        return this;
    }


}
