package com.ttzv.item.dao;

import com.ttzv.item.entity.*;

import javax.naming.NamingException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDetailDaoDatabaseImpl extends DatabaseHandler implements EntityDAO<UserDetail> {
    private final String TABLE_USER_DETAILS = "user_details";

    private KeyMapper keyMapper;

    public UserDetailDaoDatabaseImpl() throws SQLException {
        super();
        keyMapper = new KeyMapper(KeyMapper.KEY_MAP_JSON_PATH, User.class);
        if(!tablesReady(TABLE_USER_DETAILS)){
            createTables();
        }
    }

    @Override
    public void createTables() throws SQLException {
        String sql = "CREATE TABLE " + TABLE_USER_DETAILS + " ( " +
                "id SERIAL PRIMARY KEY," +
                UserDetailData.guid.getDbKey(keyMapper) + " VARCHAR UNIQUE REFERENCES " + UserDaoDatabaseImpl.TABLE_USERS + " (" + UserData.objectGUID.getDbKey(keyMapper)+ ")," +
                UserDetailData.position.getDbKey(keyMapper) + " VARCHAR," +
                UserDetailData.initMailPass.getDbKey(keyMapper) + " VARCHAR," +
                UserDetailData.notes.getDbKey(keyMapper) + " VARCHAR )";
        executeUpdate(sql);
    }

    @Override
    public List<UserDetail> getAllEntities() throws SQLException, IOException, NamingException {
        String query = "SELECT * FROM " + TABLE_USER_DETAILS;
        List<UserDetail> userList = new ArrayList<>();
        for (List<String> list :
                executeQuery(query)) {
            //create User objects with replaced keys to provide out-of-the-box compatibility with other objects
            //1.Create user using DynamicEntity
            //2.Replace all Keys in entityMap to familiar User object keys
            //3.Add as new object to list
            userList.add(new UserDetail(DynamicEntity.newDynamicEntity()
                    .process(list)
                    .replaceKeys(
                            keyMapper, KeyMapper.OBJECTKEY))
            );
        }
        return userList;
    }

    @Override
    public UserDetail getEntity(String id) throws SQLException, IOException, NamingException {
        return null;
    }

    @Override
    public boolean updateEntity(UserDetail entity) throws SQLException, IOException {
        return false;
    }

    @Override
    public boolean deleteEntity(UserDetail entity) throws SQLException, IOException {
        return false;
    }
}
