package com.ttzv.item.dao;

import com.ttzv.item.entity.*;
import com.ttzv.item.utility.Utility;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PhoneDaoDatabaseImpl extends DatabaseHandler implements EntityDAO<Phone> {

    private final String TABLE_PHONE = "phones";
    private KeyMapper keyMapper;

    public PhoneDaoDatabaseImpl() throws SQLException {
        super();
        keyMapper = new KeyMapper(KeyMapper.KEY_MAP_JSON_PATH, Phone.class);
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
        String criteriumOfUpdating = keyMapper.getMapping(PhoneData.ownerid.toString()).get(KeyMapper.DBKEY) + "='" + entity.getOwnerid() + "'";
        String sql = updateSql(TABLE_PHONE, uEntity.getList("'"), criteriumOfUpdating);

        return executeUpdate(sql);
    }

    @Override
    public boolean deleteEntity(Phone entity) {
        return false;
    }

}
