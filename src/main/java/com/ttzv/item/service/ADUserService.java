package com.ttzv.item.service;

import com.ttzv.item.entity.ADUser;
import com.ttzv.item.entity.Office;

import java.util.List;
import java.util.Map;

public interface ADUserService {

    List<ADUser> getAll();
    List<ADUser> getPaginated(int firstResult, int maxResults);
    void saveADUser(ADUser adUser);
    void updateADUser(ADUser adUser);
    Map<String, List<ADUser>> updateTableFrom(List<ADUser> otherUserList);
    Map<String, List<ADUser>> autoBindOffices(List<Office> offices);
    List<ADUser> usersFromOffice(String officeName);
}
