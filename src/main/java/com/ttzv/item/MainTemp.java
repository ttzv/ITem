package com.ttzv.item;

import com.ttzv.item.dao.*;
import com.ttzv.item.entity.*;
import com.ttzv.item.properties.Cfg;

import javax.naming.NamingException;
import java.io.IOException;
import java.sql.SQLException;

public class MainTemp {

    public static void main(String[] args) throws NamingException, IOException, SQLException {
        Cfg.getInstance().init(null);
        //EntityDAO<User> userDao = new UserDaoLdapImpl();
        //userDao.getAllEntities();
        EntityDAO<User> entityDAOuserdb = new UserDaoDatabaseImpl();
        EntityDAO<UserDetail> entityDAOuserddb = new UserDetailDaoDatabaseImpl();
        EntityDAO<Phone> entityDAOphonedb = new PhoneDaoDatabaseImpl();
        EntityDAO<City> entityDAOcitydb = new CityDaoDatabaseImpl();

    }


}
