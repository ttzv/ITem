package com.ttzv.item.entity;

import com.ttzv.item.entity.KeyMapper;
import com.ttzv.item.utility.Utility;

import java.util.*;
import java.util.stream.Collectors;


/**
 * DynamicEntity class allows to normalize Objects that are created by providing list of key-value data stored in concantenated strings (or other String-type classes) and separated by
 * some defined Separator. Objects created by this class can retrieve their key-value data and further process or be provided for other objects.
 */
public class DynamicEntity {

    private String separator = Utility.DEFAULT_ENTITY_SEPARATOR;
    private Map<String, String> entityMap;
    private KeyMapper keyMapper;

    public DynamicEntity(){
        entityMap = new HashMap<>();
    }

    public DynamicEntity(Map<String, String> entityMap){
        this.entityMap = entityMap;
    }

    /**
     * Process List of key-value data separated by Separator and generate a Map of these values
     * @param vals list of values to process
     */
    public DynamicEntity process(List<String> vals){
        for (String val : vals) {
            String [] splitted = val.split(separator);
            if(splitted.length == 2) {
                add(splitted[0], splitted[1]);
            } else {
                add(splitted[0], "");
                //System.err.println("Split entity array length is not equal to 2, value: " + val);
            }
        }
        return this;
    }

    /**
     * Use this method to replace entity keys to provide compatibility with other objects.
     * If a mapping does not have a valid assigned keyType the resulting map will not have this mapping included.
     * @param mapper - KeyMapper to use in this operation
     */
    public DynamicEntity replaceKeys(KeyMapper mapper, int keyType) {
        Map<String, String> replacedMap = new HashMap<>();
        for (String key : entityMap.keySet()) { //todo: optimize if keys are the same
            String newKey = mapper.getCorrespondingMapping(key, keyType);
            if (newKey == null) newKey="";
            if(!newKey.isEmpty()) {
                replacedMap.put(newKey, entityMap.get(key));
            } else {
                System.err.println("Cannot replace key " + key + " with keyType: " + keyType + "\n" +
                        "because it is empty.");
            }
        }
        //this.entityMap = replacedMap;
        return new DynamicEntity(replacedMap);
    }

    /**
     * Removes k-v pair from DynamicEntity map
     * @param key key that should be
     * @return
     */

    public DynamicEntity excludeKey(String key){
        Map<String, String> tempMap = new HashMap<>(this.entityMap);
        tempMap.remove(key);
        return new DynamicEntity(tempMap);
    }

    /**
     * Return separator of this Entity
     */
    public String getSeparator(){
        return separator;
    }

    public DynamicEntity setSeparator(String separator) {
        this.separator = separator;
        return this;
    }

    /**
     * Return one of values of this entity by identifying it with a key
     * @param key identifier to search by
     * @return value stored under given key or String empty string if no value was found
     */
    public String getValue(String key) {
        String val = "";
        if(entityMap.containsKey(key)) {
            val = entityMap.getOrDefault(key, "");
        } else {
            System.err.println("Key " + key + " not found as valid key");
        }
        return val;
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
            System.err.println("Cannot add value: \"" + value + "\" because Key: \"" + key + "\" is already assigned to a value: \"" + this.getValue(key) + "\"\n" +
                    "use setValue() instead.");
            return false;
        } else {
            entityMap.put(key, value);
            return true;
        }
    }

    /**
     * Set value under given key to a new value
     * This method can only change values paired with existing keys. It cannot add new key-value pairs.
     * @param key key to look for
     * @param value value to set
     * @return true if value was correctly replaced or false when key doesn't exist
     */
    public boolean setValue(String key, String value) {
        return (this.entityMap.replace(key, value) != null);
    }

    /**
     * Constructs a List of key-value parameters from this entityMap. This look almost the same as before calling process() method
     * except it takes into consideration any changed parameters such as replaced keys (using KeyMapper) and changed separator.
     * This method should make integration with different data storages easier
     * @param valWrap - String parameter, allows to wrap all Values in returned list between provided symbol. Created to use with Database updating.
     * @return List constructed from entityMap.
     */
    public List<String> getList (String valWrap){
        List<String> returnedlist = this.entityMap.keySet().stream().map(k -> k + separator + valWrap + entityMap.get(k) + valWrap).collect(Collectors.toList());
        Collections.sort(returnedlist);
        return returnedlist;
    }

    /**
     * Constructs a List same as getList(String valWrap) but without wrapping character.
     * @return List where Values are not wrapped in anything
     */
    public List<String> getList (){
       return this.getList("");
    }

    /**
     * Removes a mapping from this entityMap
     * @param key key that should be removed with corresponding value
     * @return this dynamicEntity with removed mapping
     */
    public DynamicEntity removePair (String key){
        this.entityMap.remove(key);
        return this;
    }

    public static DynamicEntity newDynamicEntity(){
        return new DynamicEntity();
    }


}
