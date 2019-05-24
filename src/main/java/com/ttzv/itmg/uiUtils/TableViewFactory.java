package com.ttzv.itmg.uiUtils;


import com.ttzv.itmg.ad.User;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.Map;

public class TableViewFactory<T, K> {

    private TableView tableView;
    private ObservableList<ObservableList> data;
    private Object[] columns;
    private List<Map<T, K>> listOfMappedContents;


    public TableViewFactory(List<Map<T, K>> listOfMappedContents) {
        columns = listOfMappedContents.get(0).keySet().toArray();
        this.listOfMappedContents = listOfMappedContents;
        tableView = new TableView();

    }

    public TableView buildTableView(){
        for (Object c :
                columns) {
            
        }
        return null;
    }




}
