package com.ttzv.item.service;

import com.ttzv.item.dao.CommandBoxDao;
import com.ttzv.item.dao.CommandBoxDaoImpl;
import com.ttzv.item.entity.CommandBox;

import java.util.List;

public class CommandBoxServiceImpl implements CommandBoxService{

    private CommandBoxDao commandBoxDao;

    public CommandBoxServiceImpl(){
        commandBoxDao = new CommandBoxDaoImpl();
    }

    @Override
    public List<CommandBox> getAllCommands() {
        return null;
    }

    @Override
    public void saveCommand(CommandBox commandBox) {
        commandBoxDao.saveCommand(commandBox);
    }


    @Override
    public void updateCommand(CommandBox commandBox) {
        commandBoxDao.updateCommand(commandBox);
    }


    @Override
    public void deleteCommand(CommandBox commandBox) {
        commandBoxDao.deleteCommand(commandBox);
    }

    @Override
    public void deleteCommand(String uid) {
        commandBoxDao.deleteCommand(uid);
    }
}
