package com.ttzv.item.activeDirectory;

import java.util.List;

public interface EntityDAO<T> {

    public List<T> getAllEntities();
    public T getEntity(String id);
    public boolean updateEntity(T entity);
    public boolean deleteEntity(T entity);

}
