package com.ttzv.item.dao;

import com.ttzv.item.entity.Office;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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

    @Override
    public List<String> officeNames() {
        Session session = DbSession.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<String> criteriaQuery = criteriaBuilder.createQuery(String.class);
        Root<Office> root = criteriaQuery.from(Office.class);
        criteriaQuery.select(root.get("name")).distinct(true);
        List<String> uniqOfficeNames = session.createQuery(criteriaQuery).getResultList();
        session.close();
        return uniqOfficeNames;
    }
}
