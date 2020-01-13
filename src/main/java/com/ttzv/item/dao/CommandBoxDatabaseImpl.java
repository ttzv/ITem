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

public class CommandBoxDatabaseImpl extends DatabaseHandler implements EntityDAO<CommandItem> {

    private final String TABLE_COMMBOX = "command_box";
    private KeyMapper keyMapper;
    private String uniqueID;

    public CommandBoxDatabaseImpl() throws GeneralSecurityException, SQLException, IOException {
        super();
        keyMapper = new KeyMapper(KeyMapper.KEY_MAP_JSON_PATH, CommandItem.class);
        if(!tablesReady(TABLE_COMMBOX)){
            createTables();
        }
        this.uniqueID = CommandItemData.uid.getDbKey();
    }

    @Override
    public void createTables() throws SQLException {
        String sql = "CREATE TABLE " + TABLE_COMMBOX + " (" +
                "id SERIAL PRIMARY KEY," +
                CommandItemData.title.getDbKey() + " VARCHAR, " +
                CommandItemData.content.getDbKey() + " VARCHAR, " +
                CommandItemData.tags.getDbKey() + " VARCHAR, " +
                CommandItemData.uid.getDbKey() + " VARCHAR UNIQUE)";
        PreparedStatement preparedStatement = super.getConnection().prepareStatement(sql);
        executeUpdate(preparedStatement);
    }

    @Override
    public List<CommandItem> getAllEntities() throws SQLException, IOException, NamingException, GeneralSecurityException {
        String query = "SELECT * FROM " + TABLE_COMMBOX;
        List<CommandItem> commandList = new ArrayList<>();
        for (List<String> list : executeQuery(query)) {
            commandList.add(new CommandItem(DynamicEntity.newDynamicEntity()
                    .process(list))
            );
        }
        return commandList;
    }

    @Override
    public CommandItem getEntity(String id) throws SQLException, IOException, NamingException {
        String query = "SELECT * FROM " + TABLE_COMMBOX +
                " WHERE " + TABLE_COMMBOX + "." + CommandItemData.uid.getDbKey() + "='" + id + "'";
        List<String> result = Utility.unNestList(executeQuery(query));
        assert result != null;
        return new CommandItem(DynamicEntity.newDynamicEntity()
                .process(result));
    }

    @Override
    public boolean updateEntity(CommandItem entity) throws SQLException, IOException {
        DynamicEntity dEntity = entity.getEntity();
        if(!update(TABLE_COMMBOX, keyMapper, dEntity, uniqueID)) {
            System.out.println("Nothing updated, inserting");
            insert(TABLE_COMMBOX, keyMapper, dEntity);
        }
        return false;
    }

    @Override
    public boolean deleteEntity(CommandItem entity) throws SQLException, IOException {
        String sql = "DELETE FROM " + TABLE_COMMBOX +
                " WHERE " + TABLE_COMMBOX + "." + CommandItemData.uid.getDbKey() + "='" + entity.getUid() + "'";
        PreparedStatement preparedStatement = getConnection().prepareStatement(sql);
        executeUpdate(preparedStatement);
        return true;
    }

    @Override
    public int[] syncDataSourceWith(EntityDAO<CommandItem> entityDAO) throws SQLException, NamingException, IOException, GeneralSecurityException {
        return new int[0];
    }

}
