package ui.mainAppWindow;

import ad.User;
import ad.UserHolder;
import db.DbCon;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ui.mailerWindow.MailerWindow;
import uiUtils.UiObjectsWrapper;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SearchWindow {

    private FXMLLoader fxmlLoader;
    private Stage stage;
    private MainWindow mainWindow;
    private UiObjectsWrapper uiObjectsWrapper;

    public SearchWindow(UiObjectsWrapper uiObjectsWrapper) {
        this.uiObjectsWrapper = uiObjectsWrapper;
        fxmlLoader = new FXMLLoader(getClass().getResource("searchwdw.fxml"));
        stage = new Stage(StageStyle.UNDECORATED);
        closeOnFocusChange(stage);
        mainWindow = (MainWindow) uiObjectsWrapper.retrieveObject(uiObjectsWrapper.MainWindow);
    }

    private void closeOnFocusChange(Stage stage){
        stage.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                System.out.println(newValue);
                stage.close();
            }
        });
    }

    public void load(){
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            stage.setScene(new Scene(root));
        }

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

    public void show() throws IOException {
        stage.show();
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
