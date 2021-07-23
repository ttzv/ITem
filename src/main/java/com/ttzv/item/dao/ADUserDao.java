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
    ADUser_n findByGUID(String guid);
    void saveMultiple(List<ADUser_n> adUsers);
    void updateMultiple(List<ADUser_n> adUsers);
    void deleteMultiple(List<ADUser_n> adUsers);
    List<ADUser_n> findMatchesInDN(String text);

    void updateADUser(ADUser_n adUser);
}
