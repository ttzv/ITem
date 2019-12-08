package com.ttzv.item.entity;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/*System.err.println("Key \"" + s + "\" already exists in mappings list \n" +
                            "given list was:\n" +
                            Arrays.asList(mappings) + "\n" +
                            "Duplicate found in list of mappings:\n" +
                            list);*/
public class KeyMapper {

    //default keys todo: put these in Map<Integer,String>
    public static final int OBJECTKEY = 0;
    public static final int LDAPKEY = 1;
    public static final int DBKEY = 2;
    public static final int FXKEY = 3;
    public static final String KEY_MAP_JSON_PATH = "mappings.json";

    private Path jsonPath;

    private Map mappingGroups;
    private List<List<String>> mappingsList;

    private ObjectMapper objectMapper;

    private Class <? extends DynamicEntityCompatible> objClass;

    public KeyMapper(Path jsonPath, Class<? extends DynamicEntityCompatible> objClass){
        this.jsonPath = jsonPath;
        this.objClass = objClass;
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

    public KeyMapper(String jsonPath, Class<? extends DynamicEntityCompatible> objClass){
        this(Paths.get(jsonPath), objClass);
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
            String duplicate = checkDuplicate(mappingsList, mappings);
            if (!duplicate.isEmpty()) {
                System.err.println("Key: \"" + duplicate + "\" from provided mappings:\n" +
                        mappings + "\n" +
                        "already exists in mappings list in group \"" + group + "\"");
            } else {
                mappingsList.add(new ArrayList<>(mappings));
            }
        }
        objectMapper.writeValue(jsonPath.toFile(), mappingGroups);
        return true;
    }

    public boolean addMapping(List<String> mappings) throws IOException {
        return this.addMapping(objClass.getSimpleName(), mappings);
    }

    public boolean addMapping(String... mappings) throws IOException {
        return addMapping(Arrays.asList(mappings));
    }

    private List<String> getMapping (String group, String key) {
        List<List<String>> mappingsList;
        mappingsList = getMappingGroup(group);
        assert mappingsList != null;
        for (List<String> list : mappingsList) {
            if (list.contains(key)) {
                return list;
            }
        }
        System.err.println("Mapping for \"" + key + "\" not found in group \"" + group + "\"");
        return null;
    }

    public List<String> getMapping (String key) {
        return this.getMapping(objClass.getSimpleName(), key);
    }

    public void updateMapping(String group, String existingkey, int indexToUpdate, String newKey) throws IOException {
        List<String> mapping = getMapping(group, existingkey);
        if(mapping != null && indexToUpdate < mapping.size()) {
            mapping.set(indexToUpdate, newKey);
        }
        objectMapper.writeValue(jsonPath.toFile(), mappingGroups);
    }

    public void updateMapping (String existingkey, int indexToUpdate, String newKey) throws IOException {
        this.updateMapping(objClass.getSimpleName(), existingkey, indexToUpdate, newKey);
    }

    public List<String> getAllMappingsOf(String group, int keyType){
        List<String> list = new ArrayList<>();
        List<List<String>> mappings = null;
        mappings = getMappingGroup(group);
        assert mappings != null;
        for (List<String> ilist : mappings) {
            if(keyType < ilist.size())
                list.add(ilist.get(keyType));
        }
        return list;
    }

    public List<String> getAllMappingsOf(int keyType){
        return this.getAllMappingsOf(objClass.getSimpleName(), keyType);
    }

    public String getCorrespondingMapping(String group, String key, int keyType){
        List<String> cMapList = getMapping(group, key);
        if (cMapList == null) return null;
        else {
            String cMapping = cMapList.get(keyType);
            if (cMapping.isEmpty()) System.err.println("");
            return cMapping;
        }
    }

    public String getCorrespondingMapping(String key, int keyType){
        return this.getCorrespondingMapping(objClass.getSimpleName(), key, keyType);
    }

    public int getKeyTypeOfMapping(String group, String key){
        return getMapping(group, key).indexOf(key);
    }

    public int getKeyTypeOfMapping(String key){
        return this.getKeyTypeOfMapping(objClass.getSimpleName(), key);
    }

    private String checkDuplicate(List<List<String>> list, List<String> s){
        List<String> flatList = list.stream().flatMap(Collection::stream).collect(Collectors.toList());
        for (String st : s) {
            if(flatList.contains(st) && !st.isEmpty()) {
                return st;
            }
        }
        return "";
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

    private Map<String, List<List<String>>> getMappingFromObjectIfJsonNotExist() throws IOException {
        try {
           this.mappingGroups = objectMapper.readValue(jsonPath.toFile(), Map.class);
        } catch (IOException e) {
            if(Files.size(jsonPath) == 0){
                this.mappingGroups = new HashMap<>();
            } else {
                e.printStackTrace();
                return null;
            }
        }
        return mappingGroups;
    }



    public static void main(String[] args) throws IOException {
        KeyMapper userMaps = new KeyMapper(KEY_MAP_JSON_PATH, User.class);
            userMaps.addMapping(UserData.objectGUID.toString(), "objectGUID", "guid", "");
            userMaps.addMapping(UserData.samaccountname.toString(), "sAMAccountName", "sam", "Login");
            userMaps.addMapping(UserData.givenname.toString(), "givenName", "firstname", "Imię");
            userMaps.addMapping(UserData.sn.toString(), "sn", "lastname", "Nazwisko");
            userMaps.addMapping(UserData.displayname.toString(), "displayName", "fullname", "");
            userMaps.addMapping(UserData.distinguishedName.toString(), "distinguishedName", "distinguishedname", "");
            userMaps.addMapping(UserData.city.toString(), "", "cityname", "Miasto");
            userMaps.addMapping(UserData.whenCreated.toString(), "whenCreated", "created", "Data utworzenia");
            //userMaps.addMapping(UserData.whenChanged.toString(), "whenChanged", "changed", "");
            userMaps.addMapping(UserData.mail.toString(), "mail", "mailaddress", "Adres mail");
            userMaps.addMapping(UserData.useraccountcontrol.toString(), "userAccountControl", "uac", "Status Konta");
        KeyMapper userDetailMaps = new KeyMapper(KeyMapper.KEY_MAP_JSON_PATH, UserDetail.class);
            userDetailMaps.addMapping(UserDetailData.guid.toString(), "", "guid");
            userDetailMaps.addMapping(UserDetailData.position.toString(), "", "position");
            userDetailMaps.addMapping(UserDetailData.initMailPass.toString(), "", "initmailpass");
            userDetailMaps.addMapping(UserDetailData.landLineNumber.toString(), "", "landline_number");
            userDetailMaps.addMapping(UserDetailData.notes.toString(), "", "notes");
        KeyMapper cityMaps = new KeyMapper(KeyMapper.KEY_MAP_JSON_PATH, City.class);
            cityMaps.addMapping(CityData.name.toString(), "", "name");
            cityMaps.addMapping(CityData.type.toString(), "", "type");
            cityMaps.addMapping(CityData.landLineNumber.toString(), "", "number_landline");
            cityMaps.addMapping(CityData.faxNumber.toString(), "", "number_fax");
            cityMaps.addMapping(CityData.postalCode.toString(), "", "postalcode");
        KeyMapper phoneMaps = new KeyMapper(KeyMapper.KEY_MAP_JSON_PATH, Phone.class);
            phoneMaps.addMapping(PhoneData.ownerid.toString(), "", "owner_guid");
            phoneMaps.addMapping(PhoneData.number.toString(), "", "number");
            phoneMaps.addMapping(PhoneData.model.toString(), "", "model");
            phoneMaps.addMapping(PhoneData.imei.toString(), "", "imei");
            phoneMaps.addMapping(PhoneData.pin.toString(), "", "pin");
            phoneMaps.addMapping(PhoneData.puk.toString(), "", "puk");

            userMaps.getMapping("mailaddress");
    }

}

