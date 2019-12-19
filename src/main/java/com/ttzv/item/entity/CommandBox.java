package com.ttzv.item.entity;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.naming.NamingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CommandBox {

    private EntityDAO<CommandItem> commandItemEntityDAO;
    private List<CommandItem> commandItemList;
    private List<Integer> commandItemUidList;

    public CommandBox(EntityDAO<CommandItem> commandItemEntityDAO) throws SQLException, NamingException, GeneralSecurityException, IOException {
        if(commandItemEntityDAO != null) {
            this.commandItemEntityDAO = commandItemEntityDAO;
            this.commandItemList = commandItemEntityDAO.getAllEntities();
            commandItemUidList = commandItemList.stream().map(c -> Integer.parseInt(c.getUid())).collect(Collectors.toList());
        } else {
            this.commandItemList = new ArrayList<>();
            this.commandItemUidList = new ArrayList<>();
        }
    }

    public List<CommandItem> getAll(){
        return commandItemList;
    }

    public void reload() throws SQLException, NamingException, GeneralSecurityException, IOException {
        this.commandItemList = commandItemEntityDAO.getAllEntities();
        this.commandItemUidList = commandItemList.stream().map(c -> Integer.parseInt(c.getUid())).collect(Collectors.toList());
    }

    public Integer getNextUid(){
        Collections.sort(commandItemUidList);
        if(commandItemUidList != null && commandItemUidList.size() > 0) {
            return commandItemUidList.get(commandItemUidList.size() - 1) + 1;
        } else {
            return null;
        }
    }

    public void update(CommandItem commandItem) throws IOException, SQLException {
        commandItemList.add(commandItem);
        commandItemUidList.add(Integer.parseInt(commandItem.getUid()));
        commandItemEntityDAO.updateEntity(commandItem);
    }

    public void remove(CommandItem commandItem) throws IOException, SQLException {
        commandItemList.remove(commandItem);
        commandItemUidList.remove(Integer.valueOf(commandItem.getUid()));
        commandItemEntityDAO.deleteEntity(commandItem);
    }



}
