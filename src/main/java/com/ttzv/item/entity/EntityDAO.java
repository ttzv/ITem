package com.ttzv.item.entity;

import javax.naming.NamingException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface EntityDAO<T> {

    public List<T> getAllEntities() throws SQLException, IOException, NamingException;
    public T getEntity(String id) throws SQLException, IOException;
    public boolean updateEntity(T entity) throws SQLException, IOException;
    public boolean deleteEntity(T entity) throws SQLException, IOException;

}
