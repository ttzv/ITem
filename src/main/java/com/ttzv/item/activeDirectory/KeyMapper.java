package com.ttzv.item.activeDirectory;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Key;
import java.util.*;

/*System.err.println("Key \"" + s + "\" already exists in mappings list \n" +
                            "given list was:\n" +
                            Arrays.asList(mappings) + "\n" +
                            "Duplicate found in list of mappings:\n" +
                            list);*/
public class KeyMapper<T> {

    //default keys
    public static final int OBJECTKEY = 0;
    public static final int LDAPKEY = 1;
    public static final int DBKEY = 2;
    public static final String KEY_MAP_JSON_PATH = "mappings.json";

    private Path jsonPath;

    private Map mappingGroups;
    private List<List<String>> mappingsList;

    private ObjectMapper objectMapper;

    private T mapperGroup;

    public KeyMapper(Path jsonPath){
        this.jsonPath = jsonPath;
        objectMapper = new ObjectMapper();
        if(!Files.exists(jsonPath)){
            try {
                if(jsonPath.getParent() != null)
                Files.createDirectories(jsonPath.getParent());
                Files.createFile(jsonPath.getFileName());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public KeyMapper(String jsonPath){
        this(Paths.get(jsonPath));
    }

    public boolean addMapping(String group, List<String> mappings) throws IOException {
        List<List<String>> mappingsList = getMappingGroup(group);
        if(mappingsList == null){
            mappingsList = new ArrayList<>();
            if(!addNewMappingGroup(group)){
                return false;
            } else {
                this.mappingGroups.put(group, mappingsList);
            }
        }
        if(mappings.size() < 2){
            System.err.println("Mapping requires at least 2 parameters");
            return false;
        }
        if(mappingsList.size() == 0){
            mappingsList.add(new ArrayList<>(mappings));
        } else {
            if (isDuplicate(mappingsList, mappings)) {
                System.err.println("One of keys in provided mappings:\n" +
                        Arrays.asList(mappings) + "\n" +
                        "already exists in mappings list in group \"" + group + "\"");
            } else {
                mappingsList.add(new ArrayList<>(mappings));
            }
        }
        objectMapper.writeValue(jsonPath.toFile(), mappingGroups);
        return true;
    }

    public boolean addMapping(List<String> mappings) throws IOException {
        return this.addMapping(mapperGroup.getClass().getSimpleName(), mappings);
    }

    public List<String> getMapping (String group, String key) {
        List<List<String>> mappingsList;
        mappingsList = getMappingGroup(group);
        for (List<String> list:
             mappingsList) {
            if (list.contains(key)) {
                return list;
            }
        }
        System.err.println("Mapping for \"" + key + "\" not found in group \"" + group + "\"");
        return null;
    }

    public List<String> getMapping (String key) {
        return this.getMapping(mapperGroup.getClass().getSimpleName(), key);
    }

    public void updateMapping(String group, String existingkey, int indexToUpdate, String newKey) throws IOException {
        List<String> mapping = getMapping(group, existingkey);
        if(mapping != null && indexToUpdate < mapping.size()) {
            mapping.set(indexToUpdate, newKey);
        }
        objectMapper.writeValue(jsonPath.toFile(), mappingGroups);
    }

    public void updateMapping (String existingkey, int indexToUpdate, String newKey) throws IOException {
        this.updateMapping(mapperGroup.getClass().getSimpleName(), existingkey, indexToUpdate, newKey);
    }

    public List<String> getAllMappingsOf(String group, int keyType){
        List<String> list = new ArrayList<>();
        List<List<String>> mappings = null;
        mappings = getMappingGroup(group);
        for (List<String> ilist :
                mappings) {
            if(keyType < ilist.size())
                list.add(ilist.get(keyType));
        }
        return list;
    }

    public List<String> getAllMappingsOf(int keyType){
        return this.getAllMappingsOf(mapperGroup.getClass().getSimpleName(), keyType);
    }

    public String getCorrespondingMapping(String group, String key, int keyType){
        String cMapping = getMapping(group, key).get(keyType);
        return cMapping;
    }

    public String getCorrespondingMapping(String key, int keyType){
        return this.getCorrespondingMapping(mapperGroup.getClass().getSimpleName(), key, keyType);
    }

    public int getKeyTypeofMapping(String group, String key){
        return getMapping(group, key).indexOf(key);
    }

    public int getKeyTypeofMapping(String key){
        return this.getKeyTypeofMapping(mapperGroup.getClass().getSimpleName(), key);
    }

    private boolean isDuplicate(List<List<String>> list, List<String> s){
        for (String string:
                s) {
            for (List<String> ilist:
                    list) {
                boolean contains = ilist.contains(string) || string.isBlank();
                if(contains){
                    return true;
                }
            }
        }
        return false;
    }


    private boolean addNewMappingGroup(String groupName) {
        return !this.mappingGroups.containsKey(groupName);
    }

    private List<List<String>> getMappingGroup(String group){
        try {
            return (List<List<String>>) getMappingFromObjectIfJsonNotExist().get(group);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Map getMappingFromObjectIfJsonNotExist() throws IOException {
        try {
           this.mappingGroups = objectMapper.readValue(jsonPath.toFile(), Map.class);
        } catch (IOException e) {
            if(Files.size(jsonPath) == 0){
                this.mappingGroups = new HashMap();
            } else {
                e.printStackTrace();
                return null;
            }
        }
        return mappingGroups;
    }



    public static void main(String[] args) throws IOException {
       /* KeyMapper keyMapper = new KeyMapper(Paths.get("mappings.json"));
        keyMapper.addMapping("User","a", "B");
        keyMapper.addMapping("Phone", "c", "d");
        keyMapper.addMapping("User","e", "f");
        System.out.println(keyMapper.getMapping("User","B"));
        System.out.println(keyMapper.getMapping("Phone","c"));
        keyMapper.addMapping("Phone", "g", "h");
        keyMapper.addMapping("Phone", "i", "j");
        keyMapper.addMapping("User", "1", "2", "3");
        System.out.println(keyMapper.getMapping("User","g"));
        System.out.println(keyMapper.getMapping("Phone","j"));
        keyMapper.updateMapping("User", "a", 0,"H");
        System.out.println(keyMapper.getMapping("User", "B"));
        System.out.println(keyMapper.getMapping("User", "2"));
        System.out.println(keyMapper.getAllMappingsOf("User", KeyMapper.LDAPKEY));
        System.out.println(keyMapper.getAllMappingsOf("User", KeyMapper.DBKEY));
        System.out.println(keyMapper.getAllMappingsOf("Phone", KeyMapper.LDAPKEY));
        System.out.println(keyMapper.getAllMappingsOf("Phone", KeyMapper.DBKEY));
        System.out.println(keyMapper.getAllMappingsOf("User", 2));*/

    }



}

