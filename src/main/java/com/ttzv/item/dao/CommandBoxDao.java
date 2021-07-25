package com.ttzv.item.dao;

import com.ttzv.item.entity.CommandBox;

import java.util.List;

public interface CommandBoxDao {

    List<CommandBox> getAllCommands();
    void saveCommand(CommandBox commandBox);
    void updateCommand(CommandBox commandBox);
    void deleteCommand(String uid);
    void deleteCommand(CommandBox commandBox);

}
