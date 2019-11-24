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

public class CityDaoDatabaseImpl extends DatabaseHandler implements EntityDAO<City> {

    private final String TABLE_CITY = "city";
    private KeyMapper keyMapper;

    public CityDaoDatabaseImpl() throws SQLException {
        super();
        keyMapper = new KeyMapper(KeyMapper.KEY_MAP_JSON_PATH, City.class);
        if(!tablesReady(TABLE_CITY)){
            createTables();
        }
    }

    @Override
    public void createTables() throws SQLException {
        String sql = "CREATE TABLE " + TABLE_CITY + " (" +
                "id SERIAL PRIMARY KEY," +
                CityData.name.getDbKey(keyMapper) + " VARCHAR UNIQUE, " +
                CityData.landLineNumber.getDbKey(keyMapper) + " VARCHAR," +
                CityData.faxNumber.getDbKey(keyMapper) + " VARCHAR," +
                CityData.postalCode.getDbKey(keyMapper) + " VARCHAR)";
        executeUpdate(sql);
    }

    @Override
    public List<City> getAllEntities() throws SQLException {
        String query = "SELECT * FROM " + TABLE_CITY;
        List<City> cityList = new ArrayList<>();
        for (List<String> list : executeQuery(query)) {
            cityList.add(new City(DynamicEntity.newDynamicEntity()
                    .process(list)
                    .replaceKeys(
                            keyMapper, KeyMapper.OBJECTKEY))
            );
        }
        return cityList;
    }

    @Override
    public City getEntity(String id) throws SQLException {
        String mappedKey = keyMapper.getMapping(CityData.name.toString()).get(KeyMapper.DBKEY);
        String query = "SELECT * FROM " + TABLE_CITY +
                " WHERE " + TABLE_CITY + "." + mappedKey + "='" + id + "'";
        List<String> result = Utility.unNestList(executeQuery(query));
        assert result != null;
        return new City(DynamicEntity.newDynamicEntity()
                .process(result)
                .replaceKeys(keyMapper, KeyMapper.OBJECTKEY));
    }

    @Override
    public boolean updateEntity(City entity) throws SQLException {
        DynamicEntity uEntity = entity.getEntity().replaceKeys(keyMapper, KeyMapper.DBKEY).setSeparator("=");
        String criteriumOfUpdating = keyMapper.getMapping(CityData.name.toString()).get(KeyMapper.DBKEY) + "='" + entity.getName() + "'";
        String sql = updateSql(TABLE_CITY, uEntity.getList("'"), criteriumOfUpdating);
        if(!executeUpdate(sql)){
            System.out.println("Nothing updated, inserting");
            insert(entity);
        }
        return false;
    }

    @Override
    public boolean deleteEntity(City entity) throws SQLException {
        String query = "DELETE FROM " + TABLE_CITY +
                " WHERE " + TABLE_CITY + "." + CityData.name.getDbKey() + "='" + entity.getName() + "'";
        executeQuery(query);
        return true;
    }

    @Override
    public int[] syncDataSourceWith(EntityDAO<City> entityDAO) throws SQLException, NamingException, IOException {
        return new int[0];
    }

    private void insert(City city) throws SQLException {
        List<String> dbKeys = keyMapper.getAllMappingsOf(KeyMapper.DBKEY);
        List<String> values = dbKeys.stream()
                .map(
                        k->city.getEntity()
                                .getValue(keyMapper.getCorrespondingMapping(k, KeyMapper.OBJECTKEY)))
                .collect(Collectors.toList());
        String sql = insertSql(TABLE_CITY, keyMapper.getAllMappingsOf(KeyMapper.DBKEY), Collections.singletonList(values));
        executeUpdate(sql);
    }
}
