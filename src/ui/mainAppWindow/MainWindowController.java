package ui.mainAppWindow;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import ui.ADWindow.ADWindow;
import ui.MailerWindow.MailerWindow;
import ui.crmWindow.CrmWindow;
import ui.gSuiteWindow.GSuiteWindow;
import ui.sceneControl.ScenePicker;

public class MainWindowController {

    private ScenePicker scenePicker;

    public MainWindowController(){
        scenePicker = new ScenePicker();
        scenePicker.addAll(new MailerWindow(), new ADWindow(), new CrmWindow(), new GSuiteWindow());
    }

    @FXML
    public Label statusBar;
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
    public Pane contentPane;

    public void goScn1(ActionEvent actionEvent) {
        selectScene(0);
    }

    public void goScn2(ActionEvent actionEvent) {
        selectScene(1);
    }

    public void goScn3(ActionEvent actionEvent) {
        selectScene(2);
    }

    public void goScn4(ActionEvent actionEvent) {
        selectScene(3);
    }

    private void selectScene(int index){
        this.contentPane.getChildren().clear();
        this.contentPane.getChildren().add(scenePicker.getPane(index));
    }
}
