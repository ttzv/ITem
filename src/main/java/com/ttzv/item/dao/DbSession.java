package com.ttzv.item.dao;

import com.ttzv.item.entity.ADUser_n;
import com.ttzv.item.entity.CommandBox;
import com.ttzv.item.entity.Office;
import com.ttzv.item.entity.UserDetail_n;
import com.ttzv.item.properties.Cfg;
import com.ttzv.item.pwSafe.Crypt;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.spi.ServiceException;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class DbSession{

    private static SessionFactory sessionInstance;
    private static final Cfg AppConfiguration = Cfg.getInstance();

    private DbSession(){}

    private static SessionFactory getSessionFactory() {
        if(sessionInstance == null) {
            Configuration configuration = new Configuration()
                    .configure();
            try{
                userDbCustomCredentials(configuration);
            } catch (IOException | GeneralSecurityException e){
                System.err.println("Cannot read credentials.");
            }
            if (useEmbeddedDb()) { h2ConfigurationModify(configuration); }
            sessionInstance = configuration.addAnnotatedClass(ADUser_n.class)
                    .addAnnotatedClass(Office.class)
                    .addAnnotatedClass(CommandBox.class)
                    .addAnnotatedClass(UserDetail_n.class)
                    .addAnnotatedClass(CommandBox.class)
                    .buildSessionFactory();
        }
        return sessionInstance;
    }

    public static void testDbCredentials() throws GeneralSecurityException, IOException, ServiceException {
        Configuration configuration = new Configuration()
                .configure();
        userDbCustomCredentials(configuration);
        configuration.buildSessionFactory().openSession().close();
    }

    public static Session openSession() throws ServiceException{
        return getSessionFactory().openSession();
    }

    private static void userDbCustomCredentials(Configuration configuration) throws GeneralSecurityException, IOException {
        String pass = new String(Crypt.newCrypt("dCr").read());
        configuration.setProperty("hibernate.connection.url", "jdbc:postgresql://" + AppConfiguration.retrieveProp(Cfg.DB_URL))
                .setProperty("hibernate.connection.username", AppConfiguration.retrieveProp(Cfg.DB_LOGIN))
                .setProperty("hibernate.connection.password", pass);
    }

    private static void h2ConfigurationModify(Configuration configuration){
       configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect")
               .setProperty("hibernate.connection.driver_class", "org.h2.Driver")
               .setProperty("hibernate.connection.url", "jdbc:h2:file:C:/data/sample")
               .setProperty("hibernate.connection.username", "sa")
               .setProperty("hibernate.connection.password", "");
    }

    private static boolean useEmbeddedDb(){
        return AppConfiguration.retrieveProp(Cfg.DB_EMBEDDED).equals("true");
    }

}
