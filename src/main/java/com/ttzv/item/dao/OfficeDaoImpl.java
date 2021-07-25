package com.ttzv.item.dao;

import com.ttzv.item.entity.Office;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class OfficeDaoImpl implements OfficeDao {
    @Override
    public List<Office> getOffices() {
        Session session = DbSession.openSession();
        List<Office> allOffices = session.createQuery(" from Office").getResultList();
        session.close();
        return allOffices;
    }

    @Override
    public void saveOffice(Office office) {
        Session session = DbSession.openSession();
        Transaction t = session.beginTransaction();
        session.save(office);
        t.commit();
        session.close();
    }

    @Override
    public void deleteOffice(Office office) {
        Session session = DbSession.openSession();
        Transaction t = session.beginTransaction();
        session.delete(office);
        t.commit();
        session.close();
    }

    @Override
    public void updateOffice(Office office) {
        Session session = DbSession.openSession();
        Transaction t = session.beginTransaction();
        session.update(office);
        t.commit();
        session.close();
    }
}
