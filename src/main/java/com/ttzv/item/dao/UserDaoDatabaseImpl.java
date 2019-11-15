package com.ttzv.item.dao;

import com.ttzv.item.entity.*;
import com.ttzv.item.utility.Utility;

import javax.naming.NamingException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class UserDaoDatabaseImpl extends DatabaseHandler implements EntityDAO<User> {
    //User object is constructed from two tables, first represents data retrieved from LDAP, second is for addidional data about User updated in application
    protected final static String TABLE_USERS = "users";
    private KeyMapper keyMapper;

    public UserDaoDatabaseImpl() throws SQLException {
        super();
        keyMapper = new KeyMapper(KeyMapper.KEY_MAP_JSON_PATH, User.class);
        if(!tablesReady(TABLE_USERS)){
            createTables();
        }
    }

    @Override
    public void createTables() throws SQLException {
        String sql = "CREATE TABLE " + TABLE_USERS + " (" +
                "id SERIAL PRIMARY KEY," +
                UserData.objectGUID.getDbKey(keyMapper) + " VARCHAR UNIQUE," + "\n" +
                UserData.samaccountname.getDbKey(keyMapper) + " VARCHAR," + "\n" +
                UserData.givenname.getDbKey(keyMapper) + " VARCHAR," + "\n" +
                UserData.sn.getDbKey(keyMapper) + " VARCHAR," + "\n" +
                UserData.displayname.getDbKey(keyMapper) + " VARCHAR," + "\n" +
                UserData.distinguishedName.getDbKey(keyMapper) + " VARCHAR," + "\n" +
                UserData.city.getDbKey(keyMapper) + " VARCHAR," + "\n" +
                UserData.whenCreated.getDbKey(keyMapper) + " VARCHAR," + "\n" +
                UserData.whenChanged.getDbKey(keyMapper) + " VARCHAR," + "\n" +
                UserData.mail.getDbKey(keyMapper) + " VARCHAR," + "\n" +
                UserData.useraccountcontrol.getDbKey(keyMapper) + " VARCHAR );";
        executeUpdate(sql);
    }

    @Override
    public List<User> getAllEntities() throws SQLException {
        String query = "SELECT * FROM " + TABLE_USERS;
        List<User> userList = new ArrayList<>();
        for (List<String> list :
                executeQuery(query)) {
            //create User objects with replaced keys to provide out-of-the-box compatibility with other objects
            //1.Create user using DynamicEntity
            //2.Replace all Keys in entityMap to familiar User object keys
            //3.Add as new object to list
            userList.add(new User(DynamicEntity.newDynamicEntity()
                    .process(list)
                    .replaceKeys(
                            keyMapper, KeyMapper.OBJECTKEY))
            );
        }
        return userList;
    }

    @Override
    public User getEntity(String id) throws SQLException {
        String query = "SELECT * FROM " + TABLE_USERS + "\n"
                + " WHERE " + TABLE_USERS + "." + UserData.objectGUID.getDbKey() + "='" + id + "';";
        List<String> result = Utility.unNestList(executeQuery(query));
        assert result != null;
        return new User(DynamicEntity.newDynamicEntity()
                .process(result)
                .replaceKeys(
                keyMapper, KeyMapper.OBJECTKEY)
        );
    }

    @Override
    public boolean updateEntity(User entity) throws SQLException {
        DynamicEntity uEntity = entity.getEntity()
                .replaceKeys(
                        keyMapper, KeyMapper.DBKEY
                ).setSeparator("=");
        String criteriumOfUpdating = keyMapper.getMapping(UserData.objectGUID.toString()).get(KeyMapper.DBKEY) + "='" + entity.getGUID() + "';";
        String sql = updateSql(TABLE_USERS, uEntity.getList("'"), criteriumOfUpdating);
        executeUpdate(sql);
        return true; //todo
    }

    @Override
    public boolean deleteEntity(User entity) throws SQLException {
        String sql = "DELETE FROM " + TABLE_USERS +
                " WHERE " + TABLE_USERS + "." + UserData.objectGUID.getDbKey() + "='" + entity.getGUID() + "'";
        executeUpdate(sql);
        return true;
    }

    @Override
    public int[] syncDataSourceWith(EntityDAO<User> source) throws SQLException, IOException, NamingException {
        List<User> currentList = this.getAllEntities();
        List<User> newerList = source.getAllEntities();
        //All unique elements in ldap
        List<User> uniqueLdap = new ArrayList<>(newerList);
        uniqueLdap.removeAll(currentList);
        if(uniqueLdap.size() > 0) {
            insert(uniqueLdap);
        }
        //All unique elements in db (not in ldap so needs to be deleted)
        List<User> uniqueDb = new ArrayList<>(newerList);
        uniqueDb.removeAll(newerList);
        for (User user : uniqueDb) {
            deleteEntity(user); //todo: optimize sql - batch delete
        }
        //updating changed users
        Collections.sort(currentList);
        Collections.sort(newerList);
        Iterator<User> iteratorNewerList = newerList.listIterator();
        Iterator<User> iteratorCurrentList = currentList.listIterator();
        int updateCount = 0;
        while(iteratorNewerList.hasNext() && iteratorCurrentList.hasNext()){
            User newerUser = iteratorNewerList.next();
            User currUser = iteratorCurrentList.next();
            if(newerUser.hasDifferentVals(currUser)){
                updateEntity(newerUser);
                updateCount++;
            }
        }

        return new int[]{uniqueLdap.size(), uniqueDb.size(), updateCount};
    }

    private void insert (User user) throws SQLException {
        List<String> ldapUniqueKeys = keyMapper.getAllMappingsOf(KeyMapper.DBKEY);
        List<String> values = ldapUniqueKeys.stream()
                .map(
                        k -> user.getEntity()
                                .getValue(
                                        keyMapper.getCorrespondingMapping(k, KeyMapper.OBJECTKEY)))
                .collect(Collectors.toList()
                );
        System.out.println(ldapUniqueKeys + "\n" + values);
        String sql = insertSql(TABLE_USERS, ldapUniqueKeys, Collections.singletonList(values));
        executeUpdate(sql);
    }

    private void insert (List<User> users) throws SQLException {
        List<String> ldapUniqueKeys = keyMapper.getAllMappingsOf(KeyMapper.DBKEY);
        List<List<String>> multiValues = new ArrayList<>();
        for (User u : users) {
            multiValues.add(ldapUniqueKeys.stream()
                    .map(
                            k -> u.getEntity()
                                    .getValue(
                                            keyMapper.getCorrespondingMapping(k, KeyMapper.OBJECTKEY)))
                    .collect(Collectors.toList())
            );
        }
        String sql = insertSql(TABLE_USERS, ldapUniqueKeys, multiValues);
        executeUpdate(sql);
    }


}