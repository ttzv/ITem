package com.ttzv.item.ui.controller;

import com.ttzv.item.entity.ADUser_n;
import com.ttzv.item.entity.UserHolder;
import com.ttzv.item.file.MailMsgParser;
import com.ttzv.item.pass.PasswordGenerator;
import com.ttzv.item.pass.WordListPasswordGenerator;
import com.ttzv.item.properties.Cfg;
import com.ttzv.item.pwSafe.PHolder;
import com.ttzv.item.sender.Sender;
import com.ttzv.item.service.ADUserService;
import com.ttzv.item.service.ADUserServiceImpl;
import com.ttzv.item.uiUtils.DialogFactory;
import com.ttzv.item.uiUtils.TabBuilder;
import com.ttzv.item.uiUtils.ViewTab;
import com.ttzv.item.utility.Utility;
import jakarta.mail.MessagingException;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ttzv.uiUtils.LimitableTextField;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class MailerController extends AnchorPane {

    private final Cfg AppConfiguration = Cfg.getInstance();

    private TabBuilder tabBuilder;
    private Sender sender;

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
        String generatedString = "";
        String passConfig = AppConfiguration.retrieveProp(Cfg.PASS_GEN_METHOD);
        if(passConfig.equals(Cfg.PROPERTY_PASS_RANDOM)) {
            PasswordGenerator passwordGenerator = new PasswordGenerator.PasswordGeneratorBuilder()
                    .useDigits(true)
                    .useLower(true)
                    .useUpper(true)
                    .usePunctuation(true).build();
            generatedString = passwordGenerator.generate(8);
        } else if(passConfig.equals(Cfg.PROPERTY_PASS_PATTERN)) {
            WordListPasswordGenerator wordListPasswordGenerator = new WordListPasswordGenerator(AppConfiguration.retrieveProp(Cfg.PASS_GEN_PATTERN));
            try {
                generatedString = wordListPasswordGenerator.getGeneratedString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.txtPass.setText(generatedString);
    }


    @FXML
    void btnSendAction(ActionEvent event) throws IOException {
        Stage infoWindowStage = DialogFactory.getWaitWindow();

        Task<Boolean> wait = new Task<>() {
            @Override
            protected Boolean call() throws GeneralSecurityException, IOException, MessagingException {
                sender.setSmtpHost(AppConfiguration.retrieveProp(Cfg.SMTP_HOST));
                sender.setSmtpPort(AppConfiguration.retrieveProp(Cfg.SMTP_PORT));
                sender.setSmtpStartTLS(AppConfiguration.retrieveProp(Cfg.SMTP_TLS));

                sender.setSenderPassword(PHolder.Mail());

                sender.setSenderAddress(AppConfiguration.retrieveProp(Cfg.SMTP_LOGIN));

                sender.validate();
                sender.initSession();

                sender.setMsgSubject(tabBuilder.getSelectedTab().getParser().getFlaggedTopic());
                sender.setMsg(tabBuilder.getSelectedTab().getParser().getOutputString());

                sender.sendMail();

                String savePass = AppConfiguration.retrieveProp(Cfg.SAVEPASS);
                if(!txtPass.getText().isBlank() && savePass.equals("true")) {
                    String name = tabBuilder.getSelectedTab()
                            .getName()
                            .replace(".html", "");
                    savePass(name);
                }
                return Boolean.TRUE;
            }
        };
        wait.setOnRunning(workerStateEvent -> {
            infoWindowStage.show();
        });
        wait.setOnSucceeded(workerStateEvent -> {
            infoWindowStage.close();
        });
        wait.setOnFailed(workerStateEvent -> {
            infoWindowStage.close();
            DialogFactory.showAlert(Alert.AlertType.ERROR, wait.getException().toString());
        });
        new Thread(wait).start();

    }

    private void savePass(String name) {
        UserHolder userHolder = UserHolder.getHolder();
        ADUser_n adUser = userHolder.getCurrentUser();
        adUser.getDetail().updateStorage(name, txtPass.getText());
        ADUserService adUserService = new ADUserServiceImpl();
        adUserService.updateADUser(adUser);
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
        try {
            tabBuilder.promptForChooser();
        } catch (IOException e) {
            e.printStackTrace();
            DialogFactory.showAlert(Alert.AlertType.WARNING, e.toString());
        }
        tabBuilder.build();
        this.tabPane.getTabs().addAll(tabBuilder.getViewTabList());

        promptLabelEnabler();
        tabChangedEvent();
        //Improve topic loading, line below is not cool
        this.labTopic.setText(this.tabBuilder.getSelectedTab().getParser().getFlaggedTopic());
    }

    @FXML
    void btnAddTabs(ActionEvent event) {
        try {
            tabBuilder.promptForChooser();
        } catch (IOException e) {
            e.printStackTrace();
            DialogFactory.showAlert(Alert.AlertType.WARNING, e.toString());
        }
        //tabBuilder.build();
        this.tabPane.getTabs().clear();
        this.tabPane.getTabs().addAll(tabBuilder.getViewTabList());
        promptLabelEnabler();
    }


    @FXML
    void btnDelTab(ActionEvent event) {
        ViewTab viewTab = this.tabBuilder.getSelectedTab();
        try {
            this.tabBuilder.getMsgFileChooser().removePath(viewTab.getPath());
        } catch (IOException e) {
            e.printStackTrace();
            DialogFactory.showAlert(Alert.AlertType.WARNING, e.toString());
        }

        this.tabBuilder.getViewTabList().remove(viewTab);
        this.tabPane.getTabs().remove(viewTab);

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

        this.cbxDomain.getItems().addAll(sender.getDomainSuffix());
        this.cbxDomain.getSelectionModel().selectFirst();

        sender.setAddressSuffix(this.cbxDomain.getSelectionModel().getSelectedItem());

    }

    public void configureTextFilters() {
        String userRegex = AppConfiguration.retrieveProp(Cfg.USER_REGEX);
        if (userRegex.isEmpty()) {
            this.txtUser.setRegexFilter(AppConfiguration.retrieveProp(Cfg.LTF_NAME_ONLY));
        } else {
            this.txtUser.setRegexFilter(userRegex);
        }

        String loginRegex = AppConfiguration.retrieveProp(Cfg.LOGIN_REGEX);
        if(!loginRegex.isEmpty()) {
            this.txtLog.setRegexFilter(loginRegex);
        } else {
            this.txtLog.setRegexFilter(AppConfiguration.retrieveProp(Cfg.LTF_RESTRICT_SYMBOLS));
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
            if(AppConfiguration.retrieveProp(Cfg.AUTOFILL_LOGIN).equals("true")) {
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

    @FXML
    public void addDomainBtnAction(ActionEvent actionEvent) throws IOException {
        String newDomain = DialogFactory.textInputDialog("Add domain", "Enter domain");
        ObservableList<String> domains = cbxDomain.getItems();
        if(newDomain != null && !domains.contains(newDomain)){
            domains.add(newDomain);
            sender.getDomainSuffix().add(0, newDomain);
            AppConfiguration.setProperty(Cfg.MAIL_DOMAINS, sender.getDomainSuffix().toString());
            AppConfiguration.saveFile();
        }
    }

}
