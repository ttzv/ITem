package com.ttzv.itmg.uiUtils;


import javafx.scene.control.TableView;

import java.util.List;
import java.util.Map;
@Deprecated
public class TableViewFactory<T, K> {

    private TableView tableView;
    private Object[] columns;
    private List<Map<T, K>> listOfMappedContents;


    public TableViewFactory(List<Map<T, K>> listOfMappedContents) {
        columns = listOfMappedContents.get(0).keySet().toArray();
        this.listOfMappedContents = listOfMappedContents;
        tableView = new TableView();
    }

   /* public void buildTableView(TableView<T> tableView){
        for (Object c :
                columns) {
            TableColumn column = new TableColumn(c.toString());
            //column.setCellValueFactory();
            column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<T, K>, ObservableValue<K>>() {
                @Override
                public ObservableValue call(TableColumn.CellDataFeatures<T, K> param) {
                    return param.getValue().
                }
            });
            tableView.getColumns().addAll(column);



        }*/
}