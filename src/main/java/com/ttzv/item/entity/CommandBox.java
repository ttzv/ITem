package com.ttzv.item.entity;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class CommandBox {

    private List<CommandItem> commandItemList;

    public CommandBox() {
    }

    public void add(CommandItem commandItem){
        commandItemList.add(commandItem);
    }

    public void remove (CommandItem commandItem){
        commandItemList.remove(commandItem);
    }

    public ObservableList<CommandItem> getObservableList(){
        return FXCollections.observableList(commandItemList);
    }

}
