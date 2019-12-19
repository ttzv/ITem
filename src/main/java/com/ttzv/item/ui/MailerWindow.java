package com.ttzv.item.ui;

import com.ttzv.item.dao.UserComboWrapper;
import com.ttzv.item.entity.UserDetail;
import com.ttzv.item.entity.UserHolder;
import com.ttzv.item.file.MailMsgParser;
import com.ttzv.item.pass.PasswordGenerator;
import com.ttzv.item.pass.WordListPasswordGenerator;
import com.ttzv.item.properties.Cfg;
import com.ttzv.item.pwSafe.PHolder;
import com.ttzv.item.sender.Sender;
import com.ttzv.item.uiUtils.TabBuilder;
import com.ttzv.item.uiUtils.ViewTab;
import com.ttzv.item.uiUtils.UiObjectsWrapper;
import com.ttzv.item.utility.Utility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.AnchorPane;
import ttzv.uiUtils.LimitableTextField;

import javax.mail.MessagingException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;

public class MailerWindow extends AnchorPane {

    private TabBuilder tabBuilder;
    private Sender sender;
    private final MainWindow mainWindow;
    private UserHolder userHolder;
    private UserComboWrapper userComboWrapper;

    @FXML
    public Label lab1;

    @FXML
    private Label labTabLoadLabel;

    @FXML
    private LimitableTextField txtUser;

    @FXML
    private LimitableTextField txtLog;

    @FXML
    private TextField txtPass;

    @FXML
    private Button btnTestAdd;

    @FXML
    private TabPane tabPane;

    @FXML
    private ComboBox<String> cbxDomain;

    @FXML
    private Label labTopic;

    @FXML
    private Label labAddress;






    @FXML
    void btnPassGenerate(ActionEvent event) {
        String generatedString = new String();
        String passConfig = Cfg.getInstance().retrieveProp(Cfg.PASS_GEN_METHOD);
        if(passConfig.equals(Cfg.PROPERTY_PASS_RANDOM)) {
            PasswordGenerator passwordGenerator = new PasswordGenerator.PasswordGeneratorBuilder()
                    .useDigits(true)
                    .useLower(true)
                    .useUpper(true)
                    .usePunctuation(true).build();
            generatedString = passwordGenerator.generate(8);
        } else if(passConfig.equals(Cfg.PROPERTY_PASS_PATTERN)) {
            WordListPasswordGenerator wordListPasswordGenerator = new WordListPasswordGenerator(Cfg.getInstance().retrieveProp(Cfg.PASS_GEN_PATTERN));
            try {
                generatedString = wordListPasswordGenerator.getGeneratedString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.txtPass.setText(generatedString);
    }


    @FXML
    void btnSendAction(ActionEvent event) {

        sender.setSmtpHost(Cfg.getInstance().retrieveProp(Cfg.SMTP_HOST));
        sender.setSmtpPort(Cfg.getInstance().retrieveProp(Cfg.SMTP_PORT));
        sender.setSmtpStartTLS(Cfg.getInstance().retrieveProp(Cfg.SMTP_TLS));

        sender.setSenderPassword(PHolder.mail);

        sender.setSenderAddress(Cfg.getInstance().retrieveProp(Cfg.SMTP_LOGIN));

        sender.validate();
        sender.initSession();

        sender.setMsgSubject(tabBuilder.getSelectedTab().getParser().getFlaggedTopic());
        try {
            sender.setMsg(tabBuilder.getSelectedTab().getParser().getOutputString());
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        sender.sendMail();

        String statusText = "Wysłano do " + this.sender.getReceiverAddress();

        String savePass = Cfg.getInstance().retrieveProp(Cfg.SAVEPASS);
        if(tabBuilder.getSelectedTab().getName().toLowerCase().contains("powitanie") && savePass.equals("true")) {
            try {
                savePass();
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
            statusText = statusText.concat(", zapisano hasło w bazie");
        }



        //mainWindow.setStatusBarText(statusText);

    }

    private void savePass() throws SQLException, IOException {
        UserDetail userDetail = this.userComboWrapper.getDetailOf(userHolder.getCurrentUser());
        userDetail.setInitMailPass(this.txtPass.getText());
        userComboWrapper.updateUserDetail(userDetail);
    }

    @FXML
    void btnClearAction(ActionEvent event) {
        this.txtUser.clear();
        this.txtLog.clear();
        this.txtPass.clear();
    }

    @FXML
    void btnCopyAction(ActionEvent event) {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(this.txtPass.getText());
        clipboard.setContent(content);
    }


    @FXML
    public void labTabLoadEvent(){
        tabBuilder.promptForChooser();
        tabBuilder.build();
        this.tabPane.getTabs().addAll(tabBuilder.getViewTabList());

        promptLabelEnabler();
        tabChangedEvent();
        //Improve topic loading, line below is not cool
        this.labTopic.setText(this.tabBuilder.getSelectedTab().getParser().getFlaggedTopic());
    }

    @FXML
    void btnAddTabs(ActionEvent event) {
        tabBuilder.promptForChooser();
        //tabBuilder.build();
        this.tabPane.getTabs().addAll(tabBuilder.getViewTabList());
        promptLabelEnabler();
    }


    @FXML
    void btnDelTab(ActionEvent event) {
        this.tabPane.getTabs().remove(this.tabBuilder.getSelectedTab());
    }

    @FXML
    public void initialize(){

        this.tabBuilder = new TabBuilder();
        if(tabBuilder.isReady()){
            this.tabBuilder.preload();
            this.tabPane.getTabs().addAll(tabBuilder.getViewTabList());
            this.labTopic.setText(tabBuilder.getSelectedTab().getParser().getFlaggedTopic());
        }

        configureTextFilters();

        promptLabelEnabler();
        passFieldEvent();
        loginFieldEvent();
        tabChangedEvent();
        userFieldEvent();
        cbxDomainEvent();

        this.sender = new Sender();

        this.cbxDomain.getItems().addAll(Arrays.asList(sender.getDomainSuffix()));
        this.cbxDomain.getSelectionModel().selectFirst();

        sender.setAddressSuffix(this.cbxDomain.getSelectionModel().getSelectedItem());

    }

    public void configureTextFilters() {
        String userRegex = Cfg.getInstance().retrieveProp(Cfg.USER_REGEX);
        if (userRegex.isEmpty()) {
            this.txtUser.setRegexFilter(Cfg.getInstance().retrieveProp(Cfg.LTF_NAME_ONLY));
        } else {
            this.txtUser.setRegexFilter(userRegex);
        }

        String loginRegex = Cfg.getInstance().retrieveProp(Cfg.LOGIN_REGEX);
        if(!loginRegex.isEmpty()) {
            this.txtLog.setRegexFilter(loginRegex);
        } else {
            this.txtLog.setRegexFilter(Cfg.getInstance().retrieveProp(Cfg.LTF_RESTRICT_SYMBOLS));
        }
    }


    public MailerWindow(UiObjectsWrapper uiObjectsWrapper, UserHolder userHolder, UserComboWrapper userComboWrapper) {
        this.mainWindow = (MainWindow) uiObjectsWrapper.retrieveObject(uiObjectsWrapper.MainWindow);
        this.userHolder = userHolder;
        this.userComboWrapper = userComboWrapper;
        uiObjectsWrapper.registerObject(uiObjectsWrapper.MailerWindow, this);


        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/mailerWindow.fxml"));
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
            this.labTabLoadLabel.setVisible(false);
        }
        else{
            this.labTabLoadLabel.setVisible(true);
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
                    this.labTopic.setText(tempParserHolder.getFlaggedTopic());
                }
            });
        }

    }

    public void userFieldEvent(){
        this.txtUser.textProperty().addListener((observable, oldValue, newValue) -> {
            String fInput = Utility.reformatUserInput(newValue);
            if(Cfg.getInstance().retrieveProp(Cfg.AUTOFILL_LOGIN).equals("true")) {
                this.txtLog.setText(fInput);
            }
            this.sender.setAddressPrefix(fInput);
            updateAddressLabelText();
        });
    }

    public void updateAddressLabelText(){
        this.labAddress.setText(this.sender.getReceiverAddress());
    }

    public void cbxDomainEvent(){
        this.cbxDomain.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            this.sender.setAddressSuffix(newValue);
            updateAddressLabelText();
        });
    }

    public void setUserName(String userName){
        this.txtUser.setText(userName);
    }

}
