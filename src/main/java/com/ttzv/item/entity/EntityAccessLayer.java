package com.ttzv.item.entity;

import java.util.List;

public interface EntityAccessLayer<T> extends EntityDAO<T> {

    static Integer ASCENDING = 1;
    static Integer DESCENDING = 2;

    /**
     * Sort entities in List by default identifier
     * @param entities list of entities to sort
     * @return list of sorted entities
     */
    public List<T> sort(List<T> entities, Integer order);
    public List<T> sortBy(List<T> entities, Integer order, String key);
    public List<T> searchGlobally(String text);
    public boolean refreshEntityList();
    public EntityAccessLayer getInstance(EntityDAO<T> entityDAO);

}
