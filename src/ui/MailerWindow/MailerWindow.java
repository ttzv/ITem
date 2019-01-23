package ui.MailerWindow;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.List;

public class MailerWindow extends AnchorPane {

    private TabBuilder tabBuilder;

    @FXML
    public Label lab1;

    @FXML
    public Label labTabLoad;

    @FXML
    private TextField txtUser;

    @FXML
    private TextField txtLog;

    @FXML
    private TextField txtPass;

    @FXML
    private Button btnTestAdd;

    @FXML
    private TabPane tabPane;


    @FXML
    void btnSendAction(ActionEvent event) {

    }


    @FXML
    public void labTabLoadEvent(){
        tabBuilder.promptForChooser();
        tabBuilder.build();
        this.tabPane.getTabs().addAll(tabBuilder.getViewTabList());

        promptLabelEnabler();
    }

    public void initialize(){
        this.tabBuilder = new TabBuilder();
        if(tabBuilder.isReady()){
            System.out.println(tabBuilder.isReady());
            this.tabBuilder.preload();
            this.tabPane.getTabs().addAll(tabBuilder.getViewTabList());
        }
        promptLabelEnabler();
        addPassFieldHandler();
    }

    public MailerWindow() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("mailerWindow.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void promptLabelEnabler(){
        if(this.tabPane.getTabs().size() > 0){
            this.labTabLoad.setVisible(false);
        }
        else{
            this.labTabLoad.setVisible(true);
        }
    }

    public void addPassFieldHandler(){
        this.txtPass.textProperty().addListener((observable, oldValue, newValue) -> {
            tabBuilder.getSelectedTab().getParser().setFlaggedPassword(newValue);
            tabBuilder.getSelectedTab().getParser().reparse();
            tabBuilder.getSelectedTab().reload();
        });
    }



}
