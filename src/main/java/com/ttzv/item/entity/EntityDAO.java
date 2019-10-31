package com.ttzv.item.entity;

import java.util.List;

public interface EntityDAO<T> {

    public List<T> getAllEntities();
    public T getEntity(String id);
    public boolean updateEntity(T entity);
    public boolean deleteEntity(T entity);

}
