package com.ttzv.item;

import com.ttzv.item.dao.*;
import com.ttzv.item.entity.*;
import com.ttzv.item.properties.Cfg;

import javax.naming.NamingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;

public class MainTemp {

    public static void main(String[] args) throws NamingException, IOException, SQLException, GeneralSecurityException {
        Cfg.getInstance().init(null);

        EntityDAO<City> ecity = new CityDaoDatabaseImpl();
        EntityDAO<User> euser = new UserDaoDatabaseImpl();
        User u = euser.getAllEntities().get(0);
        System.out.println(u);
    }
}
