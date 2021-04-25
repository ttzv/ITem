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
import java.util.Iterator;
import java.util.List;

public class UserDaoDatabaseImpl extends DatabaseHandler implements EntityDAO<ADUser> {
    //User object is constructed from two tables, first represents data retrieved from LDAP, second is for additional data about User updated in application
    protected final static String TABLE_USERS = "users";
    private KeyMapper keyMapper;
    private String uniqueID;

    public UserDaoDatabaseImpl() throws SQLException, IOException, GeneralSecurityException {
        super();
        keyMapper = new KeyMapper(KeyMapper.KEY_MAP_JSON_PATH, ADUser.class);
        if(!tablesReady(TABLE_USERS)){
            createTables();
        }
        uniqueID = UserData.objectGUID.getDbKey();
    }

    @Override
    public void createTables() throws SQLException {
        String sql = "CREATE TABLE " + TABLE_USERS + " (" +
                "id SERIAL PRIMARY KEY," +
                UserData.objectGUID.getDbKey() + " VARCHAR UNIQUE," + "\n" +
                UserData.samaccountname.getDbKey() + " VARCHAR," + "\n" +
                UserData.givenname.getDbKey() + " VARCHAR," + "\n" +
                UserData.sn.getDbKey() + " VARCHAR," + "\n" +
                UserData.displayname.getDbKey() + " VARCHAR," + "\n" +
                UserData.distinguishedName.getDbKey() + " VARCHAR," + "\n" +
                UserData.city.getDbKey() + " VARCHAR," + "\n" +
                UserData.whenCreated.getDbKey() + " VARCHAR," + "\n" +
                UserData.mail.getDbKey() + " VARCHAR," + "\n" +
                UserData.useraccountcontrol.getDbKey() + " VARCHAR );";
        PreparedStatement preparedStatement = getConnection().prepareStatement(sql);
        executeUpdate(preparedStatement);
    }

    @Override
    public List<ADUser> getAllEntities() throws SQLException {
        String query = "SELECT * FROM " + TABLE_USERS;
        List<ADUser> ADUserList = new ArrayList<>();
        for (List<String> list :
                executeQuery(query)) {
            //create User objects with replaced keys to provide out-of-the-box compatibility with other objects
            //1.Create user using DynamicEntity
            //2.Replace all Keys in entityMap to familiar User object keys
            //3.Add as new object to list
            ADUserList.add(new ADUser(DynamicEntity.newDynamicEntity()
                    .process(list)
                    .replaceKeys(
                            keyMapper, KeyMapper.OBJECTKEY))
            );
        }
        return ADUserList;
    }

    @Override
    public ADUser getEntity(String id) throws SQLException {
        String query = "SELECT * FROM " + TABLE_USERS + "\n"
                + " WHERE " + TABLE_USERS + "." + UserData.objectGUID.getDbKey() + "='" + id + "';";
        List<String> result = Utility.unNestList(executeQuery(query));
        assert result != null;
        return new ADUser(DynamicEntity.newDynamicEntity()
                .process(result)
                .replaceKeys(
                keyMapper, KeyMapper.OBJECTKEY)
        );
    }

    @Override
    public boolean updateEntity(ADUser entity) throws SQLException {
        DynamicEntity uEntity = entity.getEntity().replaceKeys(keyMapper, KeyMapper.DBKEY);
        if(!update(TABLE_USERS, keyMapper, uEntity, uniqueID)){
            System.out.println("Nothing updated, inserting");
            insert(TABLE_USERS, keyMapper, uEntity);
        }
        return true;
    }

    @Override
    public boolean deleteEntity(ADUser entity) throws SQLException {
        String sql = "DELETE FROM " + TABLE_USERS +
                " WHERE " + TABLE_USERS + "." + UserData.objectGUID.getDbKey() + "='" + entity.getGUID() + "'";
        PreparedStatement preparedStatement = getConnection().prepareStatement(sql);
        executeUpdate(preparedStatement);
        return true;
    }

    @Override
    public int[] syncDataSourceWith(EntityDAO<ADUser> source) throws SQLException, IOException, NamingException, GeneralSecurityException {
        List<ADUser> currentList = this.getAllEntities();
        List<ADUser> newerList = source.getAllEntities();
        //All unique elements in ldap
        List<ADUser> uniqueLdap = new ArrayList<>(newerList);
        uniqueLdap.removeAll(currentList);
        if(uniqueLdap.size() > 0) {
            for (ADUser u : uniqueLdap) {
                updateEntity(u);
            }
        }
        //All unique elements in db (not in ldap so needs to be deleted)
        List<ADUser> uniqueDb = new ArrayList<>(newerList);
        uniqueDb.removeAll(newerList);
        for (ADUser ADUser : uniqueDb) {
            deleteEntity(ADUser); //todo: optimize sql - batch delete
        }
        //updating changed users
        Collections.sort(currentList);
        Collections.sort(newerList);
        Iterator<ADUser> iteratorNewerList = newerList.listIterator();
        Iterator<ADUser> iteratorCurrentList = currentList.listIterator();
        int updateCount = 0;
        while(iteratorNewerList.hasNext() && iteratorCurrentList.hasNext()){
            ADUser newerADUser = iteratorNewerList.next();
            ADUser currADUser = iteratorCurrentList.next();
            if(newerADUser.hasDifferentVals(currADUser)){
                System.out.println(newerADUser + "\n" + currADUser);
                updateEntity(newerADUser);
                updateCount++;
            }
        }

        return new int[]{uniqueLdap.size(), uniqueDb.size(), updateCount};
    }

}