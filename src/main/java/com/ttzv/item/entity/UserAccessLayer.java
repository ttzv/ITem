package com.ttzv.item.entity;

import com.ttzv.item.dao.UserDaoDatabaseImpl;
import com.ttzv.item.dao.UserDaoLdapImpl;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

public class UserAccessLayer implements EntityAccessLayer<User> {

    private List<User> userList;

    @Override
    public List<User> sort(List<User> entities, Integer order) {

        return null;
    }

    @Override
    public List<User> sortBy(List<User> entities, Integer order, String key) {
        return null;
    }

    @Override
    public List<User> searchGlobally(String text) {
        return null;
    }

    @Override
    public boolean refreshEntityList() {
        return false;
    }

    @Override
    public EntityAccessLayer getInstance(EntityDAO<User> entityDAO) {
        return null;
    }

    @Override
    public List<User> getAllEntities() {
        try {
            EntityDAO<User> usersDB = new UserDaoDatabaseImpl();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    public User getEntity(String id) {
        return null;
    }

    @Override
    public boolean updateEntity(User entity) {
        return false;
    }

    @Override
    public boolean deleteEntity(User entity) {
        return false;
    }

    public boolean updateDatabase(){
        try {
            EntityDAO<User> usersLDAP = new UserDaoLdapImpl();
        } catch (NamingException e) {
            e.printStackTrace();
        }

        return false;
    }
}
