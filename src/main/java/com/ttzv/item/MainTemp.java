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

        EntityDAO<City> ecity = new CityDaoDatabaseImpl();
        /*EntityDAO<User> euser = new UserDaoDatabaseImpl();
        euser.getAllEntities();

        System.out.println(ecity.getAllEntities());
        System.out.println(euser.getAllEntities());*/

        City ctest = ecity.getAllEntities().get(0);

        System.out.println(ctest);

        ctest.setType("TESTOWA");
        ctest.setLandLineNumber("654654654");

        System.out.println(ctest);
        ecity.updateEntity(ctest);

        /*
        City city = new City("test4");

        city.setType("test");

        ecity.updateEntity(city);
        */


    }

}
