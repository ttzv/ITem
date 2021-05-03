package com.ttzv.item.dao;

import com.ttzv.item.entity.ADUser_n;
import com.ttzv.item.entity.CommandBox_n;
import com.ttzv.item.entity.Office;
import com.ttzv.item.entity.UserDetail_n;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class DbSession{

    private static SessionFactory sessionInstance;

    private DbSession(){}

    private static SessionFactory getSessionFactory(){
        if(sessionInstance == null) {
            sessionInstance = new Configuration()
                    .configure()
                    .addAnnotatedClass(ADUser_n.class)
                    .addAnnotatedClass(Office.class)
                    .addAnnotatedClass(CommandBox_n.class)
                    .addAnnotatedClass(UserDetail_n.class)
                    .buildSessionFactory();
        }
        return sessionInstance;
    }

    public static Session openSession(){
        return getSessionFactory().openSession();
    }


}
