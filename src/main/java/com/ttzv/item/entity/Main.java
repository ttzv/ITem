package com.ttzv.item.entity;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Main {

    public static void main(String[] args) {
        SessionFactory sessionFactory = new Configuration()
                .configure()
                .addAnnotatedClass(ADUser_n.class)
                .buildSessionFactory();
        Session session = sessionFactory.openSession();
        ADUser_n adUser_n = session.get(ADUser_n.class, 1);
        System.out.println(adUser_n);
    }
}
