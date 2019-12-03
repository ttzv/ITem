package com.ttzv.item.dao;

import com.ttzv.item.entity.*;
import com.ttzv.item.utility.Utility;

import javax.naming.NamingException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PhoneDaoDatabaseImpl extends DatabaseHandler implements EntityDAO<Phone> {

    private final String TABLE_PHONE = "phones";
    private KeyMapper keyMapper;

    public PhoneDaoDatabaseImpl() throws SQLException {
        super();
        keyMapper = new KeyMapper(KeyMapper.KEY_MAP_JSON_PATH, Phone.class);
        if(!tablesReady(TABLE_PHONE)){
            createTables();
        }
    }

    @Override
    public void createTables() throws SQLException {
        String sql = "CREATE TABLE " + TABLE_PHONE + " (" +
                "id SERIAL PRIMARY KEY," +
                PhoneData.ownerid.getDbKey(keyMapper) + " VARCHAR UNIQUE REFERENCES " + UserDaoDatabaseImpl.TABLE_USERS + " (" + UserData.objectGUID.getDbKey() + ")," +
                PhoneData.imei.getDbKey(keyMapper) + " VARCHAR," +
                PhoneData.model.getDbKey(keyMapper) + " VARCHAR," +
                PhoneData.number.getDbKey(keyMapper) + " VARCHAR," +
                PhoneData.pin.getDbKey(keyMapper) + " VARCHAR," +
                PhoneData.puk.getDbKey(keyMapper) + " VARCHAR )";
        executeUpdate(sql);
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
        DynamicEntity uEntity = entity.getEntity().excludeKey(PhoneData.ownerid.toString()).replaceKeys(keyMapper, KeyMapper.DBKEY).setSeparator("=");
        String criteriumOfUpdating = keyMapper.getMapping(PhoneData.ownerid.toString()).get(KeyMapper.DBKEY) + "='" + entity.getOwnerid() + "'";
        String sql = updateSql(TABLE_PHONE, uEntity.getList("'"), criteriumOfUpdating);
        if(!executeUpdate(sql)){
            System.out.println("Nothing updated, inserting");
            insert(entity);
        }
        return false;
    }

    @Override
    public boolean deleteEntity(Phone entity) throws SQLException {
        String query = "DELETE FROM " + TABLE_PHONE +
                " WHERE " + TABLE_PHONE + "." + PhoneData.ownerid.getDbKey(keyMapper) + "='" + entity.getOwnerid() + "'";
        executeQuery(query);
        return false;
    }

    @Override
    public int[] syncDataSourceWith(EntityDAO<Phone> entityDAO) throws SQLException, NamingException, IOException {
        return new int[0];
    }

    //todo: maybe move to superclass?
    private void insert(Phone phone) throws SQLException {
        List<String> dbKeys = keyMapper.getAllMappingsOf(KeyMapper.DBKEY);
        List<String> values = dbKeys.stream()
                .map(
                        k->phone.getEntity().replaceKeys(keyMapper, KeyMapper.DBKEY)
                                .getValue(k))
                .collect(Collectors.toList());
        String sql = insertSql(TABLE_PHONE, keyMapper.getAllMappingsOf(KeyMapper.DBKEY), Collections.singletonList(values));
        executeUpdate(sql);
    }

}
