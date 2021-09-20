package com.ttzv.item.service;

import com.ttzv.item.dao.ADUserDao;
import com.ttzv.item.dao.ADUserDaoImpl;
import com.ttzv.item.entity.ADUser;
import com.ttzv.item.entity.Office;
import com.ttzv.item.entity.UserDetail;
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
    public List<ADUser> getAll() {
        return adUserDao.getADUsers();
    }

    @Override
    public List<ADUser> getPaginated(int firstResult, int maxResults) {
        return adUserDao.getADUsers(firstResult, maxResults);
    }

    @Override
    public void saveADUser(ADUser adUser) {
        adUserDao.saveADUser(adUser);
    }

    @Override
    public void updateADUser(ADUser adUser) {
        UserDetail userDetail = adUser.getDetail();
        if(userDetail != null){
            try {
                userDetail.serializeStorage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        adUserDao.updateADUser(adUser);
    }

    /**
     *
     * @param otherUserList
     * @return Map that contains created, updated and deleted ADUsers. Keys are: Created, Updated, Deleted
     */
    @Override
    public Map<String, List<ADUser>> updateTableFrom(List<ADUser> otherUserList) {
        if(otherUserList == null) return null;
        // update with new records from other data storage
        List<ADUser> toSave = new ArrayList<>();
        List<ADUser> toUpdate = new ArrayList<>();
        for (ADUser adUserOther :
              otherUserList) {
            ADUser adUserDb = adUserDao.findByGUID(adUserOther.getObjectGUID());
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
        List<ADUser> toDelete = adUserDao.getADUsers();
        toDelete.removeAll(otherUserList);
        adUserDao.deleteMultiple(toDelete);

        Map<String, List<ADUser>> log = new HashMap<>();
        log.put("Created", toSave);
        log.put("Updated", toUpdate);
        log.put("Deleted", otherUserList);
        return log;
    }

    /**
     *
     * @param offices
     * @return Map that contains updated ADUsers, Key: Updated
     */
    @Override
    public Map<String, List<ADUser>> autoBindOffices(List<Office> offices) {
        List<ADUser> updated = new ArrayList<>();
        if(offices.size() > 0){
            for (Office office :
                    offices) {
                List<ADUser> usersInOffice = usersFromOffice(Utility.replaceAccents(office.getLocation().toLowerCase()).toLowerCase());
                List<ADUser> toUpdate = usersInOffice.stream().filter(adUser -> {
                    Office currentOffice = adUser.getDetail().getOffice();
                    if(currentOffice == null || !currentOffice.equals(office)){
                        adUser.getDetail().setOffice(office);
                        return true;
                    } else return false;
                }).collect(Collectors.toList());
                adUserDao.updateMultiple(toUpdate);
                updated.addAll(toUpdate);
            }
        }
        Map<String, List<ADUser>> log = new HashMap<>();
        log.put("Updated", updated);
        return log;
    }

    @Override
    public List<ADUser> usersFromOffice(String officeName) {
        return adUserDao.findMatchesInDN(officeName);
    }


}
