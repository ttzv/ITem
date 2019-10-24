package com.ttzv.item.activeDirectory;

import java.io.Serializable;
import java.util.Map;

/**
 * LDAP entity is any pair of key-value data that can be retrieved from directory. This data should be stored in single string (or other serializable format) separated by some symbol.
 * Symbol used for separating key from value can be defined using setSeparator() method. Every entity key and value can be retrieved from entity using getKey() and getValue() methods
 * @param <T> Type of serializable object that currently stores some key-value data (usually String)
 */
public interface LdapEntity<T extends Serializable> {

    public void setSeparator(T separator);
    public T getSeparator();
    public T getKeys();
    public T getValue(T key);
    public Map<T, T> getEntityMap();

}
