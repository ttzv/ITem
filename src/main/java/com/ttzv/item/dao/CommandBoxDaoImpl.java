package com.ttzv.item.dao;

import com.ttzv.item.entity.ADUser_n;
import com.ttzv.item.entity.CommandBox;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

public class CommandBoxDaoImpl implements CommandBoxDao{
    @Override
    public List<CommandBox> getAllCommands() {
        Session session = DbSession.openSession();
        List<CommandBox> allCommands = session.createQuery(" from CommandBox").getResultList();
        session.close();
        return allCommands;
    }

    @Override
    public void saveCommand(CommandBox commandBox) {
        Session session = DbSession.openSession();
        Transaction t = session.beginTransaction();
        session.save(commandBox);
        t.commit();
        session.close();
    }

    @Override
    public void updateCommand(CommandBox commandBox) {
        Session session = DbSession.openSession();
        Transaction t = session.beginTransaction();
        session.update(commandBox);
        t.commit();
        session.close();
    }

    @Override
    public void deleteCommand(String uid) {
        Session session = DbSession.openSession();
        Transaction t = session.beginTransaction();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaDelete<CommandBox> criteriaDelete = criteriaBuilder.createCriteriaDelete(CommandBox.class);
        Root<CommandBox> root = criteriaDelete.from(CommandBox.class);
        criteriaDelete.where(criteriaBuilder.equal(root.get("uid"), uid));
        session.createQuery(criteriaDelete).executeUpdate();
        t.commit();
        session.close();
    }

    @Override
    public void deleteCommand(CommandBox commandBox) {
        Session session = DbSession.openSession();
        Transaction t = session.beginTransaction();
        session.delete(commandBox);
        t.commit();
        session.close();
    }

    @Override
    public Integer getLastUid() {
        Session session = DbSession.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<CommandBox> criteriaQuery = criteriaBuilder.createQuery(CommandBox.class);
        Root<CommandBox> root = criteriaQuery.from(CommandBox.class);
        criteriaQuery.select(root).orderBy(criteriaBuilder.desc(root.get("uid")));
        Integer lastUid = null;
        Optional<CommandBox> optCommand = session.createQuery(criteriaQuery).getResultList().stream().findFirst();
        session.close();
        if(optCommand.isPresent()) lastUid = Integer.parseInt(optCommand.get().getUid());
        return lastUid;
    }
}
