package com.ttzv.item.dao;

import com.ttzv.item.entity.ADUser_n;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class ADUserDaoImpl implements ADUserDao{

    @Override
    public List<ADUser_n> getADUsers() {
        Session session = DbSession.openSession();
        List<ADUser_n> allUsers = session.createQuery(" from ADUser_n").getResultList();
        session.close();
        return allUsers;
    }

    @Override
    public List<ADUser_n> getADUsers(int firstResult, int maxResults) {
        Session session = DbSession.openSession();
        Query query = session.createQuery(" from ADUser_n")
                .setFirstResult(firstResult)
                .setMaxResults(maxResults);
        List<ADUser_n> paginated = query.getResultList();
        session.close();
        return paginated;
    }

    @Override
    public ADUser_n getADUser(int id) {
        Session session = DbSession.openSession();
        ADUser_n user = session.get(ADUser_n.class, id);
        session.close();
        return user;
    }

    @Override
    public ADUser_n getFirst() {
        Session session = DbSession.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<ADUser_n> criteriaQuery = criteriaBuilder.createQuery(ADUser_n.class);
        Root<ADUser_n> root = criteriaQuery.from(ADUser_n.class);
        criteriaQuery
                .select(root)
                .orderBy(criteriaBuilder.asc(root.get("id")));
        ADUser_n user = session.createQuery(criteriaQuery)
                .setMaxResults(1).getSingleResult();
        session.close();
        return user;
    }

    @Override
    public ADUser_n getLast() {
        Session session = DbSession.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<ADUser_n> criteriaQuery = criteriaBuilder.createQuery(ADUser_n.class);
        Root<ADUser_n> root = criteriaQuery.from(ADUser_n.class);
        criteriaQuery
                .select(root)
                .orderBy(criteriaBuilder.desc(root.get("id")));
        ADUser_n user = session.createQuery(criteriaQuery)
                .setMaxResults(1).getSingleResult();
        session.close();
        return user;
    }

    @Override
    public ADUser_n getNewest() {
        Session session = DbSession.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<ADUser_n> criteriaQuery = criteriaBuilder.createQuery(ADUser_n.class);
        Root<ADUser_n> root = criteriaQuery.from(ADUser_n.class);
        criteriaQuery
                .select(root)
                .orderBy(criteriaBuilder.desc(root.get("whencreated")));
        ADUser_n user = session.createQuery(criteriaQuery)
                .setMaxResults(1).getSingleResult();
        session.close();
        return user;
    }

    @Override
    public void saveADUser(ADUser_n adUser) {
        Session session = DbSession.openSession();
        Transaction t = session.beginTransaction();
        session.save(adUser);
        t.commit();
        session.close();
    }

    @Override
    public ADUser_n findByGUID(String guid) {
        Session session = DbSession.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<ADUser_n> criteriaQuery = criteriaBuilder.createQuery(ADUser_n.class);
        Root<ADUser_n> root = criteriaQuery.from(ADUser_n.class);
        criteriaQuery
                .select(root)
                .where(criteriaBuilder.equal(root.get("objectGUID"), guid));
        ADUser_n user = session.createQuery(criteriaQuery)
                .getResultList().stream().findFirst().orElse(null);
        session.close();
        return user;
    }

    @Override
    public void saveMultiple(List<ADUser_n> adUsers) {
        Session session = DbSession.openSession();
        Transaction t = session.beginTransaction();
        for (ADUser_n adUser :
                adUsers) {
            session.save(adUser);
        }
        t.commit();
        session.close();
    }

    @Override
    public void updateMultiple(List<ADUser_n> adUsers) {
        Session session = DbSession.openSession();
        Transaction t = session.beginTransaction();
        for (ADUser_n adUser :
                adUsers) {
            session.update(adUser);
        }
        t.commit();
        session.close();
    }

    @Override
    public void deleteMultiple(List<ADUser_n> adUsers) {
        Session session = DbSession.openSession();
        Transaction t = session.beginTransaction();
        for (ADUser_n adUser :
                adUsers) {
            session.delete(adUser);
        }
        t.commit();
        session.close();
    }

    @Override
    public void updateADUser(ADUser_n adUser) {
        Session session = DbSession.openSession();
        Transaction t = session.beginTransaction();
        session.update(adUser);
        t.commit();
        session.close();
    }

}
