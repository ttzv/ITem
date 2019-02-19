package ui.mainAppWindow;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import ui.adWindow.ADWindow;
import ui.mailerWindow.InfoWindow;
import ui.mailerWindow.MailerWindow;
import ui.crmWindow.CrmWindow;
import ui.gSuiteWindow.GSuiteWindow;
import ui.sceneControl.ScenePicker;
import ui.settingsWindow.SettingsWindow;
import uiUtils.StatusBar;

public class MainWindowController {

    private ScenePicker scenePicker;

    @FXML
    public Button tabTest;
    @FXML
    public TabPane tabPane;
    @FXML
    public Pane infoPane;
    @FXML
    public Button scene1;
    @FXML
    public Button scene2;
    @FXML
    public Button scene3;
    @FXML
    public Button scene4;
    @FXML
    public AnchorPane contentPane;
    @FXML
    public StatusBar statusBar;

    public MainWindowController(){
        scenePicker = new ScenePicker();
        scenePicker.addAll(new MailerWindow(), new ADWindow(), new CrmWindow(), new GSuiteWindow(), new SettingsWindow());

    }

    public void initialize(){

        loadOnStart();
    }



    public void goScn1(ActionEvent actionEvent) {
        selectScene(0);
        statusBar.setVanishingText("Selected Scene 1");
    }

    public void goScn2(ActionEvent actionEvent) {
        selectScene(1);
        statusBar.setVanishingText("Selected Scene 2");
    }

    public void goScn3(ActionEvent actionEvent) {
        selectScene(2);
        statusBar.setVanishingText("Selected Scene 3");
    }

    public void goScn4(ActionEvent actionEvent) {
        selectScene(3);
        statusBar.setVanishingText("Selected Scene 4");
    }

    public void goScn5(ActionEvent actionEvent) {
        selectScene(4);
        statusBar.setVanishingText("Selected Scene 5");
    }

    private void selectScene(int index){
        //this.contentPane = scenePicker.getPane(index);

        //this.contentPane.getChildren().clear();
        Pane paneToSet = scenePicker.getPane(index);
        this.contentPane.getChildren().setAll(paneToSet);
        AnchorPane.setRightAnchor(paneToSet,0.);
        AnchorPane.setLeftAnchor(paneToSet, 0.);
        AnchorPane.setTopAnchor(paneToSet, 0.);
        AnchorPane.setBottomAnchor(paneToSet, 0.);

        this.scenePicker.setActiveScene(index);
    }

    private void loadOnStart(){
        int active = scenePicker.getActiveScene();
        if(active >= 0) {
            selectScene(active);
        }
    }

    public void showMailSett(ActionEvent actionEvent) {
        InfoWindow infoWindow = new InfoWindow();
        infoWindow.getStage().show();
        }

}
