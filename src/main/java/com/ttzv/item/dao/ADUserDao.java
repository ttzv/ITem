package com.ttzv.item.dao;

import com.ttzv.item.entity.ADUser;

import java.util.List;

public interface ADUserDao {

    List<ADUser> getADUsers();
    List<ADUser> getADUsers(int firstResult, int maxResults);
    ADUser getADUser(int id);
    ADUser getFirst();
    ADUser getLast();
    ADUser getNewest();
    void saveADUser(ADUser adUser);
    ADUser findByGUID(String guid);
    void saveMultiple(List<ADUser> adUsers);
    void updateMultiple(List<ADUser> adUsers);
    void deleteMultiple(List<ADUser> adUsers);
    List<ADUser> findMatchesInDN(String text);

    void updateADUser(ADUser adUser);
}
