package com.ttzv.item.dao;

import com.ttzv.item.entity.ADUser_n;

import java.util.List;

public interface ADUserDao {

    List<ADUser_n> getADUsers();
    List<ADUser_n> getADUsers(int firstResult, int maxResults);
    ADUser_n getADUser(int id);
    ADUser_n getFirst();
    ADUser_n getLast();
    ADUser_n getNewest();
    void saveADUser(ADUser_n adUser);

    void updateADUser(ADUser_n adUser);
}
