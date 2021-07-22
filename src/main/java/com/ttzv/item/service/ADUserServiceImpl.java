package com.ttzv.item.service;

import com.ttzv.item.dao.ADUserDao;
import com.ttzv.item.dao.ADUserDaoImpl;
import com.ttzv.item.entity.ADUser_n;
import com.ttzv.item.entity.UserDetail_n;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ADUserServiceImpl implements ADUserService {

    private ADUserDao adUserDao;

    public ADUserServiceImpl(){
        adUserDao = new ADUserDaoImpl();
    }

    @Override
    public List<ADUser_n> getAll() {
        return adUserDao.getADUsers();
    }

    @Override
    public List<ADUser_n> getPaginated(int firstResult, int maxResults) {
        return adUserDao.getADUsers(firstResult, maxResults);
    }

    @Override
    public void saveADUser(ADUser_n adUser) {
        adUserDao.saveADUser(adUser);
    }

    @Override
    public void updateADUser(ADUser_n adUser) {
        UserDetail_n userDetail = adUser.getDetail();
        if(userDetail != null){
            try {
                userDetail.serializeStorage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        adUserDao.updateADUser(adUser);
    }

    @Override
    public Map<String, List<ADUser_n>> updateTableFrom(List<ADUser_n> otherUserList) {
        Map<String, List<ADUser_n>> log = new HashMap<>();
        // update with new records from other data storage
        List<ADUser_n> toSave = new ArrayList<>();
        List<ADUser_n> toUpdate = new ArrayList<>();
        for (ADUser_n adUserOther:
              otherUserList) {
            ADUser_n adUserDb = adUserDao.findByGUID(adUserOther.getObjectGUID());
            if(adUserDb == null ){
                toSave.add(adUserOther);
            } else {
                if(false){
                    adUserDb.merge(adUserOther);
                    toUpdate.add(adUserDb);
                }
            }
        }
        adUserDao.saveMultiple(toSave);
        adUserDao.updateMultiple(toUpdate);
        // handle deletion of records that no longer exists in other data storage
        otherUserList.removeAll(adUserDao.getADUsers());
        adUserDao.deleteMultiple(otherUserList);
        log.put("Created", toSave);
        log.put("Updated", toUpdate);
        log.put("Deleted", otherUserList);
        return log;
    }
}
