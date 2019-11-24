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
        /*//EntityDAO<User> userDao = new UserDaoLdapImpl();
        //userDao.getAllEntities();
        EntityDAO<User> entityDAOuserdb = new UserDaoDatabaseImpl();
        EntityDAO<UserDetail> entityDAOuserdetdb = new UserDetailDaoDatabaseImpl();
        EntityDAO<Phone> entityDAOphonedb = new PhoneDaoDatabaseImpl();
        EntityDAO<City> entityDAOcitydb = new CityDaoDatabaseImpl();
        EntityDAO<User> entityDAOuserLdap = new UserDaoLdapImpl();
        System.out.println(
                entityDAOuserLdap.getAllEntities()
                        .get(1)
                        .getEntity()
                        .replaceKeys(new KeyMapper(KeyMapper.KEY_MAP_JSON_PATH, User.class), KeyMapper.LDAPKEY)
                        .setSeparator("=")
                        .getList("'")
        );
        System.out.println("----SYNC UPDATE----\n\n");
        entityDAOuserdb.syncDataSourceWith(entityDAOuserLdap);

        User user = entityDAOuserdb.getEntity("1f75cf91-f7ea-4cea-bf12-b202d0d13806");
        System.out.println(user);
        user.setCity("randomcrap");
        user.setMail("test@test.test");
        user.setGivenName("Random");
        System.out.println(user);
        entityDAOuserdb.updateEntity(user);
        System.out.println(entityDAOuserdb.getEntity("1f75cf91-f7ea-4cea-bf12-b202d0d13806"));*/

        EntityDAO<User> userDbEntityDAO = new UserDaoDatabaseImpl();
        EntityDAO<UserDetail> entityDAOuserdetdb = new UserDetailDaoDatabaseImpl();
        EntityDAO<Phone> entityDAOphonedb = new PhoneDaoDatabaseImpl();
        EntityDAO<City> entityDAOcitydb = new CityDaoDatabaseImpl();
        UserComboWrapper comboWrapper = new UserComboWrapper(
                entityDAOcitydb.getAllEntities(),
                entityDAOphonedb.getAllEntities(),
                entityDAOuserdetdb.getAllEntities()
                );
        //City testcity = comboWrapper.getCityOf(userDbEntityDAO.getEntity("2c664e79-9362-4196-8796-d9a4805f6691"));
        //System.out.println(testcity);
        /*testcity.setName("Warszawa");
        testcity.setPostalCode("111111");
        testcity.setLandLineNumber("2222222");
        testcity.setFaxNumber("123123123");
        entityDAOcitydb.updateEntity(testcity);*/

        UserHolder userHolder = new UserHolder(userDbEntityDAO);

        User user = userHolder.getNewest(1).get(0);

        System.out.println(user.getEntity().getMap());
        user.getEntity().replaceKeys(new KeyMapper(KeyMapper.KEY_MAP_JSON_PATH, User.class), KeyMapper.FXKEY);
        System.out.println(user.getEntity().getMap());

    }

}
