package com.ttzv.item.dao;

import com.ttzv.item.entity.ADUser_n;
import com.ttzv.item.entity.CommandBox_n;
import com.ttzv.item.entity.Office;
import com.ttzv.item.entity.UserDetail_n;
import com.ttzv.item.properties.Cfg;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.spi.ServiceException;

public class DbSession{

    private static SessionFactory sessionInstance;

    private DbSession(){}

    private static SessionFactory getSessionFactory() {
        if(sessionInstance == null) {
            Configuration configuration = new Configuration()
                    .configure();
            if (useEmbeddedDb()) { h2ConfigurationModify(configuration); }
            sessionInstance = configuration.addAnnotatedClass(ADUser_n.class)
                    .addAnnotatedClass(Office.class)
                    .addAnnotatedClass(CommandBox_n.class)
                    .addAnnotatedClass(UserDetail_n.class)
                    .buildSessionFactory();
        }
        return sessionInstance;
    }

    public static Session openSession() throws ServiceException{
        return getSessionFactory().openSession();
    }

    private static void h2ConfigurationModify(Configuration configuration){
       configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect")
               .setProperty("hibernate.connection.driver_class", "org.h2.Driver")
               .setProperty("hibernate.connection.url", "jdbc:h2:file:C:/data/sample")
               .setProperty("hibernate.connection.username", "sa")
               .setProperty("hibernate.connection.password", "");
    }

    private static boolean useEmbeddedDb(){
        return Cfg.getInstance().retrieveProp(Cfg.DB_EMBEDDED).equals("true");
    }

}
