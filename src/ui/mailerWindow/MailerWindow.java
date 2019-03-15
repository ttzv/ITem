package ui.mailerWindow;

import ad.UserHolder;
import db.DbCon;
import db.PgStatement;
import file.MailMsgParser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.AnchorPane;
import pass.PasswordGenerator;
import properties.Cfg;
import sender.Sender;
import ui.mainAppWindow.MainWindow;
import pwSafe.PHolder;
import uiUtils.LimitableTextField;
import uiUtils.StatusBar;
import utility.Utility;

import javax.mail.MessagingException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;

public class MailerWindow extends AnchorPane {

    private TabBuilder tabBuilder;
    private Sender sender;
    private final MainWindow mainWindow;

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
        PasswordGenerator passwordGenerator = new PasswordGenerator.PasswordGeneratorBuilder()
                .useDigits(true)
                .useLower(true)
                .useUpper(true)
                .usePunctuation(true).build();
        this.txtPass.setText(passwordGenerator.generate(8));
    }


    @FXML
    void btnSendAction(ActionEvent event) {
        mainWindow.setStatusBarText("Wysłano do " + this.sender.getReceiverAddress());
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

        try {
            savePass();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void savePass() throws SQLException {
        DbCon dbCon = new DbCon();
        dbCon.loadCfgCredentials();
        dbCon.initConnection();
        dbCon.customQuery(PgStatement.update("users", "initmailpass", PgStatement.apostrophied(this.txtPass.getText()), "samaccountname=" + PgStatement.apostrophied(UserHolder.getCurrentUser().getSamAccountName())) );
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

        this.txtUser.setRegexFilter(LimitableTextField.NAME_ONLY);
        this.txtLog.setRegexFilter(LimitableTextField.RESTRICT_SYMBOLS);

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


    public MailerWindow(MainWindow mainWindow) {
        this.mainWindow = mainWindow;

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
            this.txtLog.setText(fInput);
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
