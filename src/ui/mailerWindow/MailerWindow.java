package ui.mailerWindow;

import file.MailMsgParser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import pass.PasswordGenerator;
import sender.Sender;
import sun.security.util.Password;
import utility.Utility;

import java.io.IOException;
import java.util.Arrays;

public class MailerWindow extends AnchorPane {

    private TabBuilder tabBuilder;
    private Sender sender;

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
    private ComboBox<String> cbxDomain;


    @FXML
    void btnPassGenerate(ActionEvent event) {
        PasswordGenerator passwordGenerator = new PasswordGenerator.PasswordGeneratorBuilder()
                .useDigits(true)
                .useLower(true)
                .useUpper(true)
                .usePunctuation(true).build();
        this.txtPass.setText(passwordGenerator.generate(8));
    }


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
        passFieldEvent();
        loginFieldEvent();
        tabChangedEvent();
        userFieldEvent();

        this.sender = new Sender();

        this.cbxDomain.getItems().addAll(Arrays.asList(sender.getDomainSuffix()));
        this.cbxDomain.getSelectionModel().selectFirst();
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

    public void loginFieldEvent() {
        this.txtLog.textProperty().addListener((observable, oldValue, newValue) -> {
            MailMsgParser tempParserHolder = tabBuilder.getSelectedTab().getParser();
            tempParserHolder.setFlaggedLogin(newValue);
            tempParserHolder.reparse();
            tabBuilder.getSelectedTab().reload();
        });
    }

        public void passFieldEvent(){
        this.txtPass.textProperty().addListener((observable, oldValue, newValue) -> {
            MailMsgParser tempParserHolder = tabBuilder.getSelectedTab().getParser();
            tempParserHolder.setFlaggedPassword(newValue);
            tempParserHolder.reparse();
            tabBuilder.getSelectedTab().reload();
        });
    }

    public void tabChangedEvent(){
        for (ViewTab vt : tabBuilder.getViewTabList()) {
            vt.setOnSelectionChanged(event -> {
                ViewTab selectedTab = tabBuilder.getSelectedTab();
                if(selectedTab != null) {
                    MailMsgParser tempParserHolder = tabBuilder.getSelectedTab().getParser();
                    tempParserHolder.setFlaggedLogin(this.txtLog.getText());
                    tempParserHolder.setFlaggedPassword(this.txtPass.getText());
                    tempParserHolder.reparse();
                    tabBuilder.getSelectedTab().reload();
                }
            });
        }

    }

    public void userFieldEvent(){
        this.txtUser.textProperty().addListener((observable, oldValue, newValue) -> {
            this.txtLog.setText(Utility.reformatUserInput(newValue));
        });
    }

}
