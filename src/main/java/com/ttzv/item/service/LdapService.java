package com.ttzv.item.service;

import com.ttzv.item.entity.ADUser_n;

import java.util.List;

public interface LdapService{

    List<ADUser_n> getAll();
    void unlockADUser(ADUser_n adUser);

}
