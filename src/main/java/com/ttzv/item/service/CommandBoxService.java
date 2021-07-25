package com.ttzv.item.service;

import com.ttzv.item.dao.CommandBoxDao;
import com.ttzv.item.entity.CommandBox;

import java.util.List;

public interface CommandBoxService {

    List<CommandBox> getAllCommands();
    void saveCommand(CommandBox commandBox);
    void updateCommand(CommandBox commandBox);
    void deleteCommand(CommandBox commandBox);
    void deleteCommand(String uid);
}
