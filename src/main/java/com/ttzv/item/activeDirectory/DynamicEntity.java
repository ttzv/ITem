package com.ttzv.item.activeDirectory;

import com.ttzv.item.utility.Utility;

import java.awt.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * DynamicEntity class allows to normalize Objects that are created by providing list of key-value data stored in concantenated strings (or other String-type classes) and separated by
 * some defined Separator. Objects created by this class can retrieve their key-value data and further process or be provided for other objects.
 */
public class DynamicEntity {
    private final String SEPARATOR = Utility.DEFAULT_ENTITY_SEPARATOR;
    private Map<String, String> entityMap;

    public DynamicEntity(){
        entityMap = new HashMap<>();
    }

    /**
     * Process List of key-value data separated by Separator and generate a Map of these values
     * @param vals list of values to process
     */
    public DynamicEntity process(List<String> vals){
        for (String val :
                vals) {
            String [] splitted = val.split(SEPARATOR);
            add(splitted[0], splitted[1]);
        }
        return this;
    }

    /**
     * Return separator of this Entity
     */
    public String getSeparator(){
        return SEPARATOR;
    }

    /**
     * Return one of values of this entity by identifying it with a key
     * @param key identifier to search by
     * @return value stored under given key
     */
    public String getValue(String key) {
        if(entityMap.containsKey(key)) {
            return entityMap.getOrDefault(key, null);
        } else {
            System.err.println("Key " + key + "not found as valid key");
            return null;
        }
    }

    /**
     * Return whole Map of this entity
     * @return Map of entity
     */
    public Map<String, String> getMap() {
        return entityMap;
    }

    /**
     * Returns Set of keys used in this entity
     * @return keys used in this entity
     */
    public Set<String> getKeys() {
        return entityMap.keySet();
    }

    /**
     * Add new key-value pair to this entity
     * @param key key to add to this entity, must be different than existing keys
     * @param value value to store under given key
     * @return true if key is unique and pair was added correctly, otherwise false
     */
    public boolean add(String key, String value){
        if(entityMap.containsKey(key)){
            return false;
        } else {
            entityMap.put(key, value);
            return true;
        }
    }

    /**
     * Set value under given key to a new value
     * Stringhis method can only change values paired with existing keys. It cannot add new key-value pairs.
     * @param key key to look for
     * @param value value to set
     * @return true if value was correctly replaced or false when key doesn't exist
     */
    public boolean setValue(String key, String value) {
        return (this.entityMap.replace(key, value) != null);
    }

    public static DynamicEntity newDynamicEntity(){
        return new DynamicEntity();
    }

}
