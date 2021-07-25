package com.ttzv.item.uiUtils;

import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.*;

public class TableViewCreator<T> {

    private TableView<T> tableView;

    private TableViewBuilder builder;
    private ObservableList<T> alldata;
    private boolean restoredUnfiltered;

    public TableViewCreator(TableView<T> tableView) {
        this.tableView = tableView;
    }

    /**
     * Filter TableView contents based on text
     * @param filter searched text
     */
    public void filter(String filter){
        tableView.setItems(alldata.filtered(t -> t.toString().contains(filter)));
    }


    public TableViewBuilder builder(){
        if (this.builder == null) {
            this.builder = new TableViewBuilder();
        }
        return this.builder;
    }

   public class TableViewBuilder{

        private final List<TableColumn<T, String>> listOfColumns;

        public TableViewBuilder() {
            this.listOfColumns = new ArrayList<>();
        }

        public TableViewBuilder addColumn(String name, String key){
            TableColumn<T, String> tableColumn = new TableColumn<>(name);
            tableColumn.setCellValueFactory(new PropertyValueFactory<>(key));
            this.listOfColumns.add(tableColumn);
            return this;
        }

       /**
        * Use if table already has columns defined, in FXML for example
        * @param index index of column
        * @param newKey key that PropertyValueFactory will use to map data to this column
        * @return
        */
        public TableViewBuilder editColumn(int index, String newKey){
            tableView.getColumns().get(index)
                    .setCellValueFactory(new PropertyValueFactory<>(newKey));
            return this;
        }

        private String getColumnName(int index){
            return tableView.getColumns()
                    .get(index)
                    .getText();
        }

        public TableViewBuilder setItems(List items){
            if( alldata != null ) {
                alldata.clear();
                alldata.addAll(items);
            } else {
                tableView.getItems().addAll(items);
                alldata = tableView.getItems();
            }
            return this;
        }

        public void build(){
            if (this.listOfColumns.size() > 0){
                tableView.getColumns().addAll(this.listOfColumns);
            }

        }

   }


}
