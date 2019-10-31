package com.ttzv.item.dao;

import com.ttzv.item.entity.EntityDAO;
import com.ttzv.item.entity.KeyMapper;
import com.ttzv.item.entity.Phone;

import java.sql.SQLException;
import java.util.List;

public class PhoneDaoDatabaseImpl extends DatabaseHandler implements EntityDAO<Phone> {

    private final String TABLE_PHONE = "phones";
    private KeyMapper<Phone> keyMapper;

    public PhoneDaoDatabaseImpl() throws SQLException {
        super();
        keyMapper = new KeyMapper<>(KeyMapper.KEY_MAP_JSON_PATH);
    }

    @Override
    public List<Phone> getAllEntities() {
        return null;
    }

    @Override
    public Phone getEntity(String id) {
        return null;
    }

    @Override
    public boolean updateEntity(Phone entity) {
        return false;
    }

    @Override
    public boolean deleteEntity(Phone entity) {
        return false;
    }

    @Override
    public boolean executeUpdate(String sql) {
        return false;
    }

    @Override
    public List<List<String>> executeQuery(String query) {
        return null;
    }
}
