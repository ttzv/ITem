package com.ttzv.item.entity;

import com.ttzv.item.dao.DbSession;
import org.hibernate.Session;

public class Main {

    public static void main(String[] args) {
        Session session = DbSession.openSession();
        ADUser_n adUser_n = session.get(ADUser_n.class, 52);
        System.out.println(adUser_n);
        System.out.println(adUser_n.getDetail());
        System.out.println(adUser_n.getOffice());
        Office office = session.get(Office.class,1);
        System.out.println(office.getUsers());
        CommandBox_n commandBox_n = session.get(CommandBox_n.class, 10);
        System.out.println(commandBox_n);
        session.close();

    }
}
