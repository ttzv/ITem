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

public class PhoneDaoDatabaseImpl extends DatabaseHandler implements EntityDAO<Phone> {

    private final String TABLE_PHONE = "phones";
    private String uniqueID;
    private KeyMapper keyMapper;

    public PhoneDaoDatabaseImpl() throws SQLException, IOException, GeneralSecurityException {
        super();
        keyMapper = new KeyMapper(KeyMapper.KEY_MAP_JSON_PATH, Phone.class);
        if(!tablesReady(TABLE_PHONE)){
            createTables();
        }
        uniqueID = PhoneData.ownerid.getDbKey();
    }

    @Override
    public void createTables() throws SQLException {
        String sql = "CREATE TABLE " + TABLE_PHONE + " (" +
                "id SERIAL PRIMARY KEY," +
                PhoneData.ownerid.getDbKey() + " VARCHAR UNIQUE REFERENCES " + UserDaoDatabaseImpl.TABLE_USERS + " (" + UserData.objectGUID.getDbKey() + ")," +
                PhoneData.imei.getDbKey() + " VARCHAR," +
                PhoneData.model.getDbKey() + " VARCHAR," +
                PhoneData.number.getDbKey() + " VARCHAR," +
                PhoneData.pin.getDbKey() + " VARCHAR," +
                PhoneData.puk.getDbKey() + " VARCHAR )";
        PreparedStatement preparedStatement = getConnection().prepareStatement(sql);
        executeUpdate(preparedStatement);
    }

    @Override
    public List<Phone> getAllEntities() throws SQLException {
        String query = "SELECT * FROM " + TABLE_PHONE;
        List<Phone> phoneList = new ArrayList<>();
        for (List<String> list :
                executeQuery(query)) {
            phoneList.add(new Phone(DynamicEntity.newDynamicEntity()
                    .process(list)
                    .replaceKeys(
                            keyMapper, KeyMapper.OBJECTKEY))
            );
        }
        return phoneList;
    }

    @Override
    public Phone getEntity(String id) throws SQLException {
        String mappedKey = keyMapper.getMapping(PhoneData.ownerid.toString()).get(KeyMapper.DBKEY);
        String query = "SELECT * FROM " + TABLE_PHONE +
                " WHERE " + TABLE_PHONE + "." + mappedKey + "='" + id + "'";
        List<String> result = Utility.unNestList(executeQuery(query));
        assert result != null;
        return new Phone(DynamicEntity.newDynamicEntity()
                .process(result)
                .replaceKeys(keyMapper, KeyMapper.OBJECTKEY));
    }

    @Override
    public boolean updateEntity(Phone entity) throws SQLException {
        DynamicEntity uEntity = entity.getEntity().replaceKeys(keyMapper, KeyMapper.DBKEY).setSeparator("=");
        if(!update(TABLE_PHONE, keyMapper, uEntity, uniqueID)){
            System.out.println("Nothing updated, inserting");
            insert(TABLE_PHONE, keyMapper, uEntity);
        }
        return false;
    }

    @Override
    public boolean deleteEntity(Phone entity) throws SQLException {
        String sql = "DELETE FROM " + TABLE_PHONE +
                " WHERE " + TABLE_PHONE + "." + PhoneData.ownerid.getDbKey(keyMapper) + "='" + entity.getOwnerid() + "'";
        PreparedStatement preparedStatement = getConnection().prepareStatement(sql);
        executeUpdate(preparedStatement);
        return false;
    }

    @Override
    public int[] syncDataSourceWith(EntityDAO<Phone> entityDAO) throws SQLException, NamingException, IOException {
        return new int[0];
    }

}
