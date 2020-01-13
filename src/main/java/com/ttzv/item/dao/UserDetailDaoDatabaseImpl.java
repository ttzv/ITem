package com.ttzv.item.dao;

import com.ttzv.item.entity.*;
import com.ttzv.item.utility.Utility;

import javax.naming.NamingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class UserDetailDaoDatabaseImpl extends DatabaseHandler implements EntityDAO<UserDetail> {
    private final String TABLE_USER_DETAILS = "user_details";

    private KeyMapper keyMapper;
    private String uniqueID;

    public UserDetailDaoDatabaseImpl() throws SQLException, IOException, GeneralSecurityException {
        super();
        keyMapper = new KeyMapper(KeyMapper.KEY_MAP_JSON_PATH, UserDetail.class);
        if(!tablesReady(TABLE_USER_DETAILS)){
            createTables();
        }
        uniqueID = UserDetailData.guid.getDbKey();
    }

    @Override
    public void createTables() throws SQLException {
        String sql = "CREATE TABLE " + TABLE_USER_DETAILS + " ( " +
                "id SERIAL PRIMARY KEY," +
                UserDetailData.guid.getDbKey() + " VARCHAR UNIQUE REFERENCES " + UserDaoDatabaseImpl.TABLE_USERS + " (" + UserData.objectGUID.getDbKey()+ ")," +
                UserDetailData.position.getDbKey() + " VARCHAR," +
                UserDetailData.initMailPass.getDbKey() + " VARCHAR," +
                UserDetailData.landLineNumber.getDbKey() + " VARCHAR," +
                UserDetailData.notes.getDbKey() + " VARCHAR )";
        PreparedStatement preparedStatement = getConnection().prepareStatement(sql);
        executeUpdate(preparedStatement);
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
        String mappedKey = keyMapper.getCorrespondingMapping(UserDetailData.guid.toString(), KeyMapper.DBKEY);
        String query = "SELECT * FROM " + TABLE_USER_DETAILS +
                " WHERE " + TABLE_USER_DETAILS + "." + mappedKey + "='" + id + "'";
        List<String> result = Utility.unNestList(executeQuery(query));
        assert result != null;
        return new UserDetail(DynamicEntity.newDynamicEntity()
                .process(result)
                .replaceKeys(keyMapper, KeyMapper.OBJECTKEY));
    }

    @Override
    public boolean updateEntity(UserDetail entity) throws SQLException, IOException {
        DynamicEntity uEntity = entity.getEntity().excludeKey(UserDetailData.guid.toString()).replaceKeys(keyMapper, KeyMapper.DBKEY);
        if(!update(TABLE_USER_DETAILS, keyMapper, uEntity, uniqueID)){
            System.out.println("Nothing updated, inserting");
            insert(TABLE_USER_DETAILS, keyMapper, uEntity);
        }
        return false;
    }

    @Override
    public boolean deleteEntity(UserDetail entity) throws SQLException, IOException {
        String sql = "DELETE FROM " + TABLE_USER_DETAILS +
                " WHERE " + TABLE_USER_DETAILS + "." + UserDetailData.guid.getDbKey(keyMapper) + "='" + entity.getGuid() + "'";
        PreparedStatement preparedStatement = getConnection().prepareStatement(sql);
        executeUpdate(preparedStatement);
        return false;
    }

    @Override
    public int[] syncDataSourceWith(EntityDAO<UserDetail> entityDAO) throws SQLException, NamingException, IOException {
        return new int[0];
    }
}
