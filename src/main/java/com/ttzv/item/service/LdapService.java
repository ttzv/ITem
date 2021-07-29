package com.ttzv.item.service;

import com.ttzv.item.entity.ADUser_n;

import javax.naming.NamingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public interface LdapService{

    List<ADUser_n> getAll() throws GeneralSecurityException, NamingException, IOException;
    void unlockADUser(ADUser_n adUser);

}
