package com.ttzv.item.dao;

import com.ttzv.item.entity.*;
import com.ttzv.item.utility.Utility;

import javax.naming.NamingException;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
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
                UserData.objectGUID.getDbKey(keyMapper) + " VARCHAR UNIQUE," +
                UserData.samaccountname.getDbKey(keyMapper) + " VARCHAR," +
                UserData.givenname.getDbKey(keyMapper) + " VARCHAR," +
                UserData.sn.getDbKey(keyMapper) + " VARCHAR," +
                UserData.displayname.getDbKey(keyMapper) + " VARCHAR," +
                UserData.distinguishedName.getDbKey(keyMapper) + " VARCHAR," +
                UserData.city.getDbKey(keyMapper) + " VARCHAR," +
                UserData.whenCreated.getDbKey(keyMapper) + " VARCHAR," +
                UserData.whenChanged.getDbKey(keyMapper) + " VARCHAR," +
                UserData.mail.getDbKey(keyMapper) + " VARCHAR," +
                UserData.useraccountcontrol.getDbKey(keyMapper) + " VARCHAR)";
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
        String query = "SELECT * FROM " + TABLE_USERS
                + " WHERE " + TABLE_USERS + "." + UserData.objectGUID.getDbKey(keyMapper) + "='" + id + "'";
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
        String query = "DELETE FROM " + TABLE_USERS +
                " WHERE " + TABLE_USERS + "." + UserData.objectGUID.getDbKey(keyMapper) + "='" + entity.getGUID() + "'";
        executeQuery(query);
        return true;
    }

    public int[] updateDataSourceFrom(EntityDAO<User> source) throws SQLException, IOException, NamingException {
        List<User> currentList = this.getAllEntities();
        List<User> newerList = source.getAllEntities();
        //All unique elements in ldap
        newerList.removeAll(currentList);
        for (User user : newerList) {
            insert(user);
        }
        //All unique elements in db (not in ldap so needs to be deleted)
        currentList.removeAll(source.getAllEntities());
        for (User user : currentList) {
            deleteEntity(user);
        }
        return new int[]{newerList.size(), currentList.size()};
    }

    private void insert (User user) throws SQLException {
        List<String> ldapUniqueKeys = keyMapper.getAllMappingsOf(KeyMapper.LDAPKEY)
                .stream()
                .map(k -> keyMapper.getCorrespondingMapping(k, KeyMapper.DBKEY))
                .collect(Collectors.toList());
        List<String> values = ldapUniqueKeys.stream().map(k -> user.getEntity().getValue(k)).collect(Collectors.toList());
        String sql = insertSql(TABLE_USERS, ldapUniqueKeys, values);
        executeUpdate(sql);
    }


}