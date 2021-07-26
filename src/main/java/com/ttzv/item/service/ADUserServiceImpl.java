package com.ttzv.item.service;

import com.ttzv.item.dao.ADUserDao;
import com.ttzv.item.dao.ADUserDaoImpl;
import com.ttzv.item.entity.ADUser_n;
import com.ttzv.item.entity.Office;
import com.ttzv.item.entity.UserDetail_n;
import com.ttzv.item.utility.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        if(otherUserList == null) return null;
        // update with new records from other data storage
        List<ADUser_n> toSave = new ArrayList<>();
        List<ADUser_n> toUpdate = new ArrayList<>();
        for (ADUser_n adUserOther :
              otherUserList) {
            ADUser_n adUserDb = adUserDao.findByGUID(adUserOther.getObjectGUID());
            if(adUserDb == null ){
                toSave.add(adUserOther);
            } else {
                if(!adUserDb.equals(adUserOther)){
                    adUserDb.merge(adUserOther);
                    toUpdate.add(adUserDb);
                }
            }
        }
        adUserDao.saveMultiple(toSave);
        adUserDao.updateMultiple(toUpdate);
        // handle deletion of records that no longer exists in other data storage
        otherUserList.removeAll(adUserDao.getADUsers());
        List<ADUser_n> toDelete = otherUserList.stream().map(otherUser -> adUserDao.findByGUID(otherUser.getObjectGUID())).collect(Collectors.toList());
        adUserDao.deleteMultiple(toDelete);

        Map<String, List<ADUser_n>> log = new HashMap<>();
        log.put("Created", toSave);
        log.put("Updated", toUpdate);
        log.put("Deleted", otherUserList);
        return log;
    }

    @Override
    public Map<String, List<ADUser_n>> autoBindOffices(List<Office> offices) {
        List<ADUser_n> updated = new ArrayList<>();
        if(offices.size() > 0){
            for (Office office :
                    offices) {
                List<ADUser_n> usersInOffice = usersFromOffice(Utility.replaceAccents(office.getLocation().toLowerCase()).toLowerCase());
                usersInOffice.forEach(adUser_n -> adUser_n.getDetail().setOffice(office));
                adUserDao.updateMultiple(usersInOffice);
            }
        }
        Map<String, List<ADUser_n>> log = new HashMap<>();
        log.put("Updated", updated);
        return log;
    }

    @Override
    public List<ADUser_n> usersFromOffice(String officeName) {
        return adUserDao.findMatchesInDN(officeName);
    }


}
