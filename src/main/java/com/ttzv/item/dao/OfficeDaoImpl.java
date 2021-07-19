package com.ttzv.item.dao;

import com.ttzv.item.entity.Office;
import org.hibernate.Session;

import java.util.List;

public class OfficeDaoImpl implements OfficeDAO{
    @Override
    public List<Office> getOffices() {
        Session session = DbSession.openSession();
        List<Office> allOffices = session.createQuery(" from Office").getResultList();
        session.close();
        return allOffices;
    }
}
