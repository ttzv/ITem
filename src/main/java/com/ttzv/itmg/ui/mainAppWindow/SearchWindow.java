package com.ttzv.itmg.ui.mainAppWindow;

import com.ttzv.itmg.ad.User;
import com.ttzv.itmg.ad.UserHolder;
import com.ttzv.itmg.db.DbCon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import com.ttzv.itmg.uiUtils.UiObjectsWrapper;

import java.sql.SQLException;
import java.util.List;

public class SearchWindow extends AnchorPane {

    private FXMLLoader fxmlLoader;
    private Stage stage;
    private MainWindow mainWindow;
    private UiObjectsWrapper uiObjectsWrapper;

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
    private ListView<User> listwvResults;

    @FXML
    private Label labResultCount;


    @FXML
    void btnPerformAction(ActionEvent event) throws SQLException {
        DbCon dbCon = new DbCon();
        dbCon.loadCfgCredentials();
        dbCon.initConnection();

        List<User> foundUsers = dbCon.globalSearch(this.txtfSearch.getText(), 0);

        ObservableList<User> observableList = FXCollections.observableArrayList(foundUsers);

        this.listwvResults.setItems(observableList);

        this.labResultCount.setText( Integer.toString(foundUsers.size()) );

    }





}
