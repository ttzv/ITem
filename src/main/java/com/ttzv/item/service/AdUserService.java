package com.ttzv.item.service;

import com.ttzv.item.dao.DbSession;
import com.ttzv.item.entity.ADUser_n;

import java.util.List;

public class AdUserService {

    public List<ADUser_n> getAll(){
        return DbSession.openSession().createQuery("from ADUser_n", ADUser_n.class).getResultList();
    }
}
