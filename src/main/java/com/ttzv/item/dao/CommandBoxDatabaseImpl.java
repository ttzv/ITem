package com.ttzv.item.dao;

import com.ttzv.item.entity.CommandItem;
import com.ttzv.item.entity.CommandItemData;
import com.ttzv.item.entity.DynamicEntity;
import com.ttzv.item.entity.EntityDAO;
import com.ttzv.item.utility.Utility;

import javax.naming.NamingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandBoxDatabaseImpl extends DatabaseHandler implements EntityDAO<CommandItem> {

    private final String TABLE_COMMBOX = "command_box";

    public CommandBoxDatabaseImpl() throws GeneralSecurityException, SQLException, IOException {
        super();
        if(!tablesReady(TABLE_COMMBOX)){
            createTables();
        }
    }

    @Override
    public void createTables() throws SQLException {
        String sql = "CREATE TABLE " + TABLE_COMMBOX + " (" +
                "id SERIAL PRIMARY KEY," +
                CommandItemData.title.getDbKey() + " VARCHAR, " +
                CommandItemData.content.getDbKey() + " VARCHAR, " +
                CommandItemData.tags.getDbKey() + " VARCHAR" +
                CommandItemData.uid.getDbKey() + "VARCHAR UNIQUE)";
        executeUpdate(sql);
    }

    @Override
    public List<CommandItem> getAllEntities() throws SQLException, IOException, NamingException, GeneralSecurityException {
        String query = "SELECT * FROM " + TABLE_COMMBOX;
        List<CommandItem> commandList = new ArrayList<>();
        for (List<String> list : executeQuery(query)) {
            commandList.add(new CommandItem(DynamicEntity.newDynamicEntity()
                    .process(list))
            );
            System.out.println(list);
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
        DynamicEntity uEntity = entity.getEntity().setSeparator("=");
        String criteriumOfUpdating = CommandItemData.uid.getDbKey() + "='" + entity.getUid() + "'";
        String sql = updateSql(TABLE_COMMBOX, uEntity.getList("'"), criteriumOfUpdating);
        if(!executeUpdate(sql)){
            System.out.println("Nothing updated, inserting");
            insert(entity);
        }
        return false;
    }

    @Override
    public boolean deleteEntity(CommandItem entity) throws SQLException, IOException {
        String sql = "DELETE FROM " + TABLE_COMMBOX +
                " WHERE " + TABLE_COMMBOX + "." + CommandItemData.uid.getDbKey() + "='" + entity.getUid() + "'";
        executeUpdate(sql);
        return true;
    }

    @Override
    public int[] syncDataSourceWith(EntityDAO<CommandItem> entityDAO) throws SQLException, NamingException, IOException, GeneralSecurityException {
        return new int[0];
    }

    private void insert(CommandItem commandItem) throws SQLException {
        List<String> columns = new ArrayList<>();
        columns.add(CommandItemData.title.getDbKey());
        columns.add(CommandItemData.content.getDbKey());
        columns.add(CommandItemData.tags.getDbKey());
        columns.add(CommandItemData.uid.getDbKey());

        List<String> values = new ArrayList<>();
        values.add(commandItem.getEntity().getValue(CommandItemData.title.getDbKey()));
        values.add(commandItem.getEntity().getValue(CommandItemData.content.getDbKey()));
        values.add(commandItem.getEntity().getValue(CommandItemData.tags.getDbKey()));
        values.add(commandItem.getEntity().getValue(CommandItemData.uid.getDbKey()));

        String sql = insertSql(TABLE_COMMBOX, columns, Collections.singletonList(values));
        executeUpdate(sql);
    }
}
