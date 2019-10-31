package com.ttzv.item.ui.mainAppWindow;

import com.ttzv.item.entity.User;
import com.ttzv.item.entity.UserData;
import com.ttzv.item.entity.UserHolder;
import com.ttzv.item.db.UserDaoDatabaseImpl;
import com.ttzv.item.uiUtils.UiObjectsWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.sql.SQLException;
import java.util.List;

public class SearchWindow extends AnchorPane {

    private FXMLLoader fxmlLoader;
    private Stage stage;
    private MainWindow mainWindow;
    private UiObjectsWrapper uiObjectsWrapper;
    List<User> foundUsers;

    public SearchWindow(UiObjectsWrapper uiObjectsWrapper) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/searchwdw.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            AnchorPane root = fxmlLoader.load();
            stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeOnFocusChange(stage);

        this.uiObjectsWrapper = uiObjectsWrapper;
        mainWindow = (MainWindow) uiObjectsWrapper.retrieveObject(uiObjectsWrapper.MainWindow);
    }

    private void closeOnFocusChange(Stage stage){
        stage.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                //System.out.println(newValue);
                stage.close();
            }
        });
    }



    private void selectionHandler(){
        this.resultsList.setOnMouseClicked((MouseEvent event) -> {
            if(event.getClickCount() == 2){
                int selectedIndex = this.resultsList.getSelectionModel().getSelectedIndex();
                String selectedUserGUID = this.resultsList.getSelectionModel().getSelectedItem().get(0).toString();
                for (User u : foundUsers) {
                    if(u.compareGUID(selectedUserGUID)){
                        UserHolder.clear();
                        UserHolder.addUser(u);
                        if(!mainWindow.isInfoBarAssetsVisible()){
                            mainWindow.infoBarAssetsVisible(true);
                        }
                        mainWindow.changeUser();
                        break;
                    }
                }
            }
        });
/*
        this.listwvResults.setOnMouseClicked((MouseEvent event) -> {
            if(event.getClickCount() == 2){
                User selectedUser = this.listwvResults.getSelectionModel().getSelectedItem();
                if(selectedUser != null){
                    UserHolder.clear();
                    UserHolder.addUser(selectedUser);
                    if(!mainWindow.isInfoBarAssetsVisible()){
                        mainWindow.infoBarAssetsVisible(true);
                    }
                   mainWindow.changeUser();
                }
            }
        });
*/
    }

    public void show() {
        this.stage.show();
    }

    @FXML
    private void initialize(){
        selectionHandler();
    }

    @FXML
    private Label labelCity;

    @FXML
    private TextField txtfSearch;

    @FXML
    private TableView<ObservableList> resultsList;

    @FXML
    private Label labResultCount;


    @FXML
    void btnPerformAction(ActionEvent event) throws SQLException {
        UserDaoDatabaseImpl userDaoDatabaseImpl = new UserDaoDatabaseImpl();

        foundUsers = userDaoDatabaseImpl.globalSearch(this.txtfSearch.getText(), 0);
        buildTableView(foundUsers);

        this.labResultCount.setText( Integer.toString(foundUsers.size()) );

    }

    public void buildTableView(List<User> foundData){

        this.resultsList.getColumns().clear();

        String[] columns = User.columns;

        ObservableList<ObservableList> data = FXCollections.observableArrayList();

        for (int i = 0; i < columns.length; i++) {
            final int j = i;
            String u = columns[i];
            TableColumn<ObservableList, String> col = new TableColumn<ObservableList, String>(u);
            col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                    //System.out.println("Data no = " + j + " | " + param.getValue().get(j).toString());
                    return new SimpleStringProperty(param.getValue().get(j).toString());
                }
            });

            this.resultsList.getColumns().addAll(col);
        }

        for (User u : foundData) {
            ObservableList<String> row = FXCollections.observableArrayList();
            for (UserData ud :
                    u.getUserInformationMap().keySet()) {
                row.add(u.getUserInformationMap().get(ud));
            }
            data.add(row);
        }



        this.resultsList.setItems(data);

    }





}
