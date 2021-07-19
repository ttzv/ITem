package com.ttzv.item.service;

import com.ttzv.item.dao.ADUserDao;
import com.ttzv.item.dao.ADUserDaoImpl;
import com.ttzv.item.entity.ADUser_n;
import com.ttzv.item.entity.UserDetail_n;

import java.io.IOException;
import java.util.List;

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
}
