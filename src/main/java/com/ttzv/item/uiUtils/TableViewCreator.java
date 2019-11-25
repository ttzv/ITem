package com.ttzv.item.uiUtils;

import com.ttzv.item.entity.FXMapCompatible;
import com.ttzv.item.entity.KeyMapper;
import com.ttzv.item.entity.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.util.*;

public class TableViewCreator {

    private TableView<Map> tableView;
    private Map<String, List<TableColumn>> columnGroups;
    private KeyMapper keyMapper;

    public TableViewCreator(TableView<Map> tableView) {
        this.tableView = tableView;
        keyMapper = new KeyMapper(KeyMapper.KEY_MAP_JSON_PATH, User.class);
    }

    public void createFromMap(TableViewBuilder builder){

        //populate observablelist with maps
        ObservableList<Map> alldata = FXCollections.observableArrayList(builder.listOfMaps);
        //set tableview contents
        tableView.setItems(alldata);
        tableView.getColumns().setAll(builder.getListOfColumns());
    }


    public static TableViewBuilder builder(){
        return new TableViewBuilder();
    }





   public static class TableViewBuilder{

        private List<TableColumn<Map, String>> listOfColumns;

        private List<Map> listOfMaps;

        public TableViewBuilder() {
            this.listOfColumns = new ArrayList<>();
            this.listOfMaps = new ArrayList<>();
        }

        public TableViewBuilder addColumn(String name, String key){
            addNewColumn(name, key);
            return this;
        }

        public TableViewBuilder addColumns(Map<String, String> nameKeyPairs){
            nameKeyPairs.forEach(this::addNewColumn);
            return this;
        }

        public TableViewBuilder addColumns(FXMapCompatible fxMapCompatible){
            for (Map.Entry<String,String> entry :
                    fxMapCompatible.getFXNameToIdPairs().entrySet()) {
                if(!entry.getValue().isEmpty()) {
                    addNewColumn(entry.getValue(), entry.getKey());
                }
            }
            return this;
        }

        public TableViewBuilder groupColumnsUnder(String parentColumnName){
            TableColumn<Map, String> tableColumn = new TableColumn<>(parentColumnName);
            List<TableColumn<Map,String>> list = new ArrayList<>(listOfColumns);
            listOfColumns.clear();
            tableColumn.getColumns().setAll(list);
            this.listOfColumns.add(tableColumn);
            return this;
        }


        private void addNewColumn (String name, String key){
            TableColumn<Map, String> tableColumn = new TableColumn<>(name);
            tableColumn.setCellValueFactory(new MapValueFactory<>(key));
            tableColumn.setCellFactory(cellFactoryForMap());
            tableColumn.setId(key);
            this.listOfColumns.add(tableColumn);
        }

        public TableViewBuilder addRow (String key, String value){
            Map<String, String> map = new HashMap<>();
            map.put(key, value);
            this.listOfMaps.add(map);
            return this;
        }

        public TableViewBuilder addRows(List<? extends FXMapCompatible> list){
            list.forEach(fxMapCompatible -> this.listOfMaps.add(fxMapCompatible.getFXMap()));
            return this;
        }

       private Callback<TableColumn<Map, String>, TableCell<Map, String>> cellFactoryForMap (){
           return new Callback<TableColumn<Map, String>, TableCell<Map, String>>() {
               @Override
               public TableCell<Map, String> call(TableColumn<Map, String> mapStringTableColumn) {
                   return new TextFieldTableCell<>(new StringConverter<String>() {
                       @Override
                       public String toString(String s) {
                           return s;
                       }
                       @Override
                       public String fromString(String s) {
                           return s;
                       }
                   });
               }
           };
       }

       public List<TableColumn<Map, String>> getListOfColumns() {
           return listOfColumns;
       }

       public List<Map> getListOfMaps() {
           return listOfMaps;
       }



   }


}
