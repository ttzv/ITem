package com.ttzv.item.service;

import com.ttzv.item.entity.ADUser_n;
import com.ttzv.item.entity.Office;

import java.util.List;
import java.util.Map;

public interface ADUserService {

    List<ADUser_n> getAll();
    List<ADUser_n> getPaginated(int firstResult, int maxResults);
    void saveADUser(ADUser_n adUser);
    void updateADUser(ADUser_n adUser);
    Map<String, List<ADUser_n>> updateTableFrom(List<ADUser_n> otherUserList);
    Map<String, List<ADUser_n>> autoBindOffices(List<Office> offices);
    List<ADUser_n> usersFromOffice(String officeName);
}
