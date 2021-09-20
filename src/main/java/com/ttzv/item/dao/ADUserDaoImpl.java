package com.ttzv.item.dao;

import com.ttzv.item.entity.ADUser;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class ADUserDaoImpl implements ADUserDao{

    @Override
    public List<ADUser> getADUsers() {
        Session session = DbSession.openSession();
        List<ADUser> allUsers = session.createQuery(" from ADUser").getResultList();
        session.close();
        return allUsers;
    }

    @Override
    public List<ADUser> getADUsers(int firstResult, int maxResults) {
        Session session = DbSession.openSession();
        Query query = session.createQuery(" from ADUser")
                .setFirstResult(firstResult)
                .setMaxResults(maxResults);
        List<ADUser> paginated = query.getResultList();
        session.close();
        return paginated;
    }

    @Override
    public ADUser getADUser(int id) {
        Session session = DbSession.openSession();
        ADUser user = session.get(ADUser.class, id);
        session.close();
        return user;
    }

    @Override
    public ADUser getFirst() {
        Session session = DbSession.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<ADUser> criteriaQuery = criteriaBuilder.createQuery(ADUser.class);
        Root<ADUser> root = criteriaQuery.from(ADUser.class);
        criteriaQuery
                .select(root)
                .orderBy(criteriaBuilder.asc(root.get("id")));
        ADUser user = session.createQuery(criteriaQuery)
                .setMaxResults(1).getSingleResult();
        session.close();
        return user;
    }

    @Override
    public ADUser getLast() {
        Session session = DbSession.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<ADUser> criteriaQuery = criteriaBuilder.createQuery(ADUser.class);
        Root<ADUser> root = criteriaQuery.from(ADUser.class);
        criteriaQuery
                .select(root)
                .orderBy(criteriaBuilder.desc(root.get("id")));
        ADUser user = session.createQuery(criteriaQuery)
                .setMaxResults(1).getSingleResult();
        session.close();
        return user;
    }

    @Override
    public ADUser getNewest() {
        Session session = DbSession.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<ADUser> criteriaQuery = criteriaBuilder.createQuery(ADUser.class);
        Root<ADUser> root = criteriaQuery.from(ADUser.class);
        criteriaQuery
                .select(root)
                .orderBy(criteriaBuilder.desc(root.get("whencreated")));
        ADUser user = session.createQuery(criteriaQuery)
                .setMaxResults(1).getSingleResult();
        session.close();
        return user;
    }

    @Override
    public void saveADUser(ADUser adUser) {
        Session session = DbSession.openSession();
        Transaction t = session.beginTransaction();
        session.save(adUser);
        t.commit();
        session.close();
    }

    @Override
    public ADUser findByGUID(String guid) {
        Session session = DbSession.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<ADUser> criteriaQuery = criteriaBuilder.createQuery(ADUser.class);
        Root<ADUser> root = criteriaQuery.from(ADUser.class);
        criteriaQuery
                .select(root)
                .where(criteriaBuilder.equal(root.get("objectGUID"), guid));
        ADUser user = session.createQuery(criteriaQuery)
                .getResultList().stream().findFirst().orElse(null);
        session.close();
        return user;
    }

    @Override
    public void saveMultiple(List<ADUser> adUsers) {
        Session session = DbSession.openSession();
        Transaction t = session.beginTransaction();
        for (ADUser adUser :
                adUsers) {
            session.save(adUser);
        }
        t.commit();
        session.close();
    }

    @Override
    public void updateMultiple(List<ADUser> adUsers) {
        Session session = DbSession.openSession();
        Transaction t = session.beginTransaction();
        for (ADUser adUser :
                adUsers) {
            session.update(adUser);
        }
        t.commit();
        session.close();
    }

    @Override
    public void deleteMultiple(List<ADUser> adUsers) {
        Session session = DbSession.openSession();
        Transaction t = session.beginTransaction();
        for (ADUser adUser :
                adUsers) {
            session.delete(adUser);
        }
        t.commit();
        session.close();
    }

    @Override
    public List<ADUser> findMatchesInDN(String text) {
        Session session = DbSession.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<ADUser> criteriaQuery = criteriaBuilder.createQuery(ADUser.class);
        Root<ADUser> root = criteriaQuery.from(ADUser.class);
        criteriaQuery
                .select(root)
                .where(criteriaBuilder.like(criteriaBuilder.lower(root.get("distinguishedName")), String.format("%%ou=%s%%", text)));
        List<ADUser> users = session.createQuery(criteriaQuery)
                .getResultList();
        session.close();
        return users;
    }

    @Override
    public void updateADUser(ADUser adUser) {
        Session session = DbSession.openSession();
        Transaction t = session.beginTransaction();
        session.update(adUser);
        t.commit();
        session.close();
    }

}
