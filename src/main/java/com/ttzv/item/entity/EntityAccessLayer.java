package com.ttzv.item.entity;

import java.util.List;

public interface EntityAccessLayer<T> extends EntityDAO<T> {

    String ASCENDING = "ASCENDING";
    String DESCENDING = "DESCENDING";
    /**
     * Sort entities in List by default identifier
     * @param entities list of entities to sort
     * @return list of sorted entities
     */
    public List<T> sort(List<T> entities, String order);
    public List<T> sortBy(List<T> entities, String order, String key);
    public List<T> searchGlobally(String text);
    public boolean refreshEntityList();

}
