package com.ttzv.item.ui.controller;

import com.ttzv.item.dao.DbSession;
import com.ttzv.item.properties.Cfg;
import com.ttzv.item.pwSafe.PHolder;
import com.ttzv.item.sender.Sender;
import com.ttzv.item.service.LdapService;
import com.ttzv.item.service.LdapServiceImpl;
import com.ttzv.item.sms.SmsApiClient;
import com.ttzv.item.uiUtils.DialogFactory;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import ttzv.uiUtils.LimitableTextField;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Optional;

public class SettingsController extends AnchorPane {

    private final Cfg AppConfiguration = Cfg.getInstance();

    @FXML
    public Button btnAcceptDbSettings;

    @FXML
    public CheckBox cbxDbEmbed;

    @FXML
    private TextField fieldHost;

    @FXML
    private TextField fieldPort;

    @FXML
    private CheckBox cbxTls;

    @FXML
    private TextField fieldLogin;

    @FXML
    private PasswordField fieldPass;

    @FXML
    private CheckBox cbxRemember;

    @FXML
    private ProgressIndicator prgMail;

    @FXML
    private ImageView okImgMail;


    @FXML
    private TextField txtf_smsLogin;

    @FXML
    private TextField txtf_smsSenderName;

    @FXML
    private PasswordField pwdf_smsPassword;

    @FXML
    private ImageView okImgSms;

    @FXML
    private ProgressIndicator prgSms;

    @FXML
    private CheckBox checkBox_rememberSmsPass;


    @FXML
    private TextField fieldLdapUrl;

    @FXML
    private TextField fieldLdapPort;

    @FXML
    private TextField fieldLdapAcc;

    @FXML
    private PasswordField fieldLdapPass;

    @FXML
    private TextField fieldLdapSearchBase;

    @FXML
    private ProgressIndicator prgLdap;

    @FXML
    private ImageView okImgLdap;

    @FXML
    private CheckBox cbxRememberLdap;

    @FXML
    private TextField fieldDbUrl;

    @FXML
    private TextField fieldDbLogin;

    @FXML
    private PasswordField fieldDbPass;

    @FXML
    private ImageView okImgDb;

    @FXML
    private ProgressIndicator prgDb;

    @FXML
    private CheckBox cbxRememberDb;

    @FXML
    private CheckBox cBoxAutoMailSavePass;

    @FXML
    private CheckBox cBoxAutoFillLogin;

    @FXML
    private CheckBox cBoxAlwaysOpenDir;

    @FXML
    private TextField txtfUserRegex;

    @FXML
    private TextField txtfLoginRegex;

    @FXML
    private RadioButton rbPassRandom;

    @FXML
    private RadioButton rbPassWords;

    @FXML
    private LimitableTextField txtfPassPattern;

    @FXML
    private Button btnDefaultPassPattern;

    @FXML
    public void initialize() throws IOException, GeneralSecurityException {

        //mail
        this.fieldHost.setText(AppConfiguration.retrieveProp(Cfg.SMTP_HOST));
        this.fieldPort.setText(AppConfiguration.retrieveProp(Cfg.SMTP_PORT));
        this.fieldLogin.setText(AppConfiguration.retrieveProp(Cfg.SMTP_LOGIN));

        this.fieldPass.setText(new String(PHolder.Mail()));

        String tlsSetting = AppConfiguration.retrieveProp(Cfg.SMTP_TLS);
        this.cbxTls.setSelected(tlsSetting.equals("true"));

        String remCbxSetting = AppConfiguration.retrieveProp("SettSaveCbx");
        this.cbxRemember.setSelected(remCbxSetting.equals("true"));

        //ldap
        this.fieldLdapUrl.setText(AppConfiguration.retrieveProp(Cfg.LDAP_URL));
        this.fieldLdapPort.setText(AppConfiguration.retrieveProp(Cfg.LDAP_PORT));
        this.fieldLdapAcc.setText(AppConfiguration.retrieveProp(Cfg.LDAP_ACC));
        this.fieldLdapSearchBase.setText(AppConfiguration.retrieveProp(Cfg.LDAP_SEARCH_BASE));

        this.fieldLdapPass.setText(new String(PHolder.Ldap()));

        String remCbxLdapSetting = AppConfiguration.retrieveProp("SettSaveLdapCbx");
        this.cbxRememberLdap.setSelected(remCbxLdapSetting.equals("true"));

        //db
        String dbUseEmbedded = AppConfiguration.retrieveProp(Cfg.DB_EMBEDDED);
        this.cbxDbEmbed.setSelected(dbUseEmbedded.equals("true"));
        this.fieldDbUrl.setText(AppConfiguration.retrieveProp(Cfg.DB_URL));
        this.fieldDbLogin.setText(AppConfiguration.retrieveProp(Cfg.DB_LOGIN));
        updateDbControlsState();

        this.fieldDbPass.setText(new String(PHolder.Db()));

        String remCbxDbSetting = AppConfiguration.retrieveProp("SettSaveDbCbx");
        this.cbxRememberDb.setSelected(remCbxDbSetting.equals("true"));

        resetMailIndicators();
        resetLdapIndicators();
        resetDbIndicators();
        resetSmsIndicators();

        String savePassSetting = AppConfiguration.retrieveProp(Cfg.SAVEPASS);
        this.cBoxAutoMailSavePass.setSelected(savePassSetting.equals("true"));

        String autoFillLogin = AppConfiguration.retrieveProp(Cfg.AUTOFILL_LOGIN);
        this.cBoxAutoFillLogin.setSelected(autoFillLogin.equals("true"));

        String alwaysOpenDir = AppConfiguration.retrieveProp(Cfg.DIR_ALWAYSOPEN);
        this.cBoxAlwaysOpenDir.setSelected(alwaysOpenDir.equals("true"));

        this.txtfUserRegex.setText(AppConfiguration.retrieveProp(Cfg.USER_REGEX));
        this.txtfLoginRegex.setText(AppConfiguration.retrieveProp(Cfg.LOGIN_REGEX));

        //password generator settings

        final ToggleGroup toggleGroup = new ToggleGroup();
        rbPassRandom.setToggleGroup(toggleGroup);
        rbPassRandom.setUserData("Random");
        rbPassWords.setToggleGroup(toggleGroup);
        rbPassWords.setUserData("Pattern");

        txtfPassPattern.setRegexFilter("[WNS]*");
        txtfPassPattern.setText("WWNS");

        btnDefaultPassPattern.setOnAction(event -> {
            this.txtfPassPattern.setText("WWNS");
        });

        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            boolean isDisabled = toggleGroup.getSelectedToggle().getUserData()=="Random";
                this.txtfPassPattern.setDisable(isDisabled);
                this.btnDefaultPassPattern.setDisable(isDisabled);
                if(isDisabled){
                    AppConfiguration.setProperty(Cfg.PASS_GEN_METHOD, Cfg.PROPERTY_PASS_RANDOM);
                } else {
                    AppConfiguration.setProperty(Cfg.PASS_GEN_METHOD, Cfg.PROPERTY_PASS_PATTERN);
                }
            try {
                AppConfiguration.saveFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        String passGenMethod = AppConfiguration.retrieveProp(Cfg.PASS_GEN_METHOD);
        if(passGenMethod.equals(Cfg.PROPERTY_PASS_PATTERN)){
            rbPassWords.setSelected(true);
        } else {
            rbPassRandom.setSelected(true);
        }

        this.txtfPassPattern.focusedProperty().addListener((observable, oldValue, newValue) -> {
            String text = this.txtfPassPattern.getText();
            if(!text.isEmpty()){
                AppConfiguration.setProperty(Cfg.PASS_GEN_PATTERN, text);
            }
        });

        //sms
        this.txtf_smsLogin.setText(AppConfiguration.retrieveProp(Cfg.SMSAPI_LOGIN));
        this.txtf_smsSenderName.setText(AppConfiguration.retrieveProp(Cfg.SMSAPI_SENDER));

        this.pwdf_smsPassword.setText(new String(PHolder.Sms()));

        String remSmsSettings = AppConfiguration.retrieveProp("SettSaveSmsCbx");
        this.checkBox_rememberSmsPass.setSelected(remSmsSettings.equals("true"));

    }

    @FXML
    void btnAcceptDbSettingsEvent() throws IOException {
        btnDbPerformAction(false);
    }

    @FXML
    void btnAcceptLdapSettingsEvent() throws IOException {
        btnLdapPerformAction(false);
    }

    @FXML
    public void btnAcceptSettingsEvent() throws IOException {
        btnMailPerformAction(false);
    }

    @FXML
    void btnAcceptMiscSettingsEvent(ActionEvent event) throws IOException {
        btnMiscPerformAction();
        AppConfiguration.saveFile();
    }

    @FXML
    void btnA_saveSmsSett(ActionEvent event) throws IOException {
        btnSmsPerformAction(false);
        AppConfiguration.saveFile();
    }

    @FXML
    public void btnSaveAllSettingsEvent() throws IOException {
        btnDbPerformAction(true);
        btnLdapPerformAction(true);
        btnMailPerformAction(true);
        btnSmsPerformAction(true);
        btnMiscPerformAction();

        AppConfiguration.saveFile();
    }



    private void btnMailPerformAction(boolean noStore) throws IOException {
        Cfg cfg = AppConfiguration;

        cfg.setProperty(Cfg.SMTP_HOST, this.fieldHost.getText());
        cfg.setProperty(Cfg.SMTP_PORT, this.fieldPort.getText());
        if(this.cbxTls.isSelected()) {
            cfg.setProperty(Cfg.SMTP_TLS, "true");
        } else {
            cfg.setProperty(Cfg.SMTP_TLS, "false");
        }

        cfg.setProperty(Cfg.SMTP_LOGIN, this.fieldLogin.getText());

        if(this.cbxRemember.isSelected()) {
            try {
                PHolder.getCrypt(PHolder.MAIL_FILEID).safeStore(this.fieldPass.getText());
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            }
            cfg.setProperty("SettSaveCbx", "true");
        } else {
            PHolder.getCrypt(PHolder.MAIL_FILEID).erase();
            cfg.setProperty("SettSaveCbx", "false");
        }

        PHolder.Mail = this.fieldPass.getText().toCharArray();

        if(this.cbxTls.isSelected()) {
            cfg.setProperty(Cfg.SMTP_TLS, "true");
        } else {
            cfg.setProperty(Cfg.SMTP_TLS, "false");
        }

        if(!noStore) {
            cfg.saveFile();
        }

        testMailCredentials();


    }

    private void btnLdapPerformAction(boolean noStore) throws IOException{

        AppConfiguration.setProperty(Cfg.LDAP_URL, this.fieldLdapUrl.getText());
        AppConfiguration.setProperty(Cfg.LDAP_PORT, this.fieldLdapPort.getText());
        AppConfiguration.setProperty(Cfg.LDAP_ACC, this.fieldLdapAcc.getText());
        AppConfiguration.setProperty(Cfg.LDAP_SEARCH_BASE, this.fieldLdapSearchBase.getText());
        if(this.cbxRememberLdap.isSelected()) {
            try {
                PHolder.getCrypt(PHolder.LDAP_FILEID).safeStore(this.fieldLdapPass.getText());
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            }
            AppConfiguration.setProperty("SettSaveLdapCbx", "true");
        } else {
            AppConfiguration.setProperty("SettSaveLdapCbx", "false");
            PHolder.getCrypt(PHolder.LDAP_FILEID).erase();
        }

        PHolder.Ldap = this.fieldLdapPass.getText().toCharArray();

        if(!noStore) {
            AppConfiguration.saveFile();
        }

        testLDAPCredentials();


    }

    private void btnDbPerformAction(boolean noStore) throws IOException {
        Cfg cfg = AppConfiguration;
        if(cbxDbEmbed.isSelected()){
            cfg.setProperty(Cfg.DB_EMBEDDED, "true");
            confirmCloseDialog();
        } else {
            cfg.setProperty(Cfg.DB_EMBEDDED, "false");
            cfg.setProperty(Cfg.DB_URL, this.fieldDbUrl.getText());
            cfg.setProperty(Cfg.DB_LOGIN, this.fieldDbLogin.getText());
            if(this.cbxRememberDb.isSelected()) {
                try {
                    PHolder.getCrypt(PHolder.DB_FILEID).safeStore(this.fieldDbPass.getText());
                } catch (GeneralSecurityException e) {
                    e.printStackTrace();
                }
                cfg.setProperty("SettSaveDbCbx", "true");
            } else {
                cfg.setProperty("SettSaveDbCbx", "false");
                PHolder.getCrypt(PHolder.DB_FILEID).erase();
            }

            PHolder.Db = this.fieldDbPass.getText().toCharArray();
            testDBCredentials();
        }
        if(!noStore) {
            cfg.saveFile();
        }
    }

    private void btnMiscPerformAction() {
        if(cBoxAutoMailSavePass.isSelected()){
            AppConfiguration.setProperty(Cfg.SAVEPASS, "true");
        } else {
            AppConfiguration.setProperty(Cfg.SAVEPASS, "false");
        }

        if(cBoxAutoFillLogin.isSelected()){
            AppConfiguration.setProperty(Cfg.AUTOFILL_LOGIN, "true");
        } else {
            AppConfiguration.setProperty(Cfg.AUTOFILL_LOGIN, "false");
        }

        if(cBoxAlwaysOpenDir.isSelected()){
            AppConfiguration.setProperty(Cfg.DIR_ALWAYSOPEN, "true");
        } else {
            AppConfiguration.setProperty(Cfg.DIR_ALWAYSOPEN, "false");
        }

        if (!this.txtfUserRegex.getText().isEmpty()){
            AppConfiguration.setProperty(Cfg.USER_REGEX, this.txtfUserRegex.getText());
        }

        if (!this.txtfLoginRegex.getText().isEmpty()){
            AppConfiguration.setProperty(Cfg.LOGIN_REGEX, this.txtfLoginRegex.getText());
        }

        //MailerController mailerController = (MailerController) uiObjectsWrapper.retrieveObject(uiObjectsWrapper.MailerWindow);
        //mailerController.configureTextFilters();


    }

    private void btnSmsPerformAction(boolean noStore) throws IOException {
        Cfg cfg = AppConfiguration;

        cfg.setProperty(Cfg.SMSAPI_LOGIN, this.txtf_smsLogin.getText());
        cfg.setProperty(Cfg.SMSAPI_SENDER, this.txtf_smsSenderName.getText());
        if(this.checkBox_rememberSmsPass.isSelected()) {
            try {
                PHolder.getCrypt(PHolder.SMS_FILEID).safeStore(this.pwdf_smsPassword.getText());
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            }
            cfg.setProperty("SettSaveSmsCbx", "true");
        } else {
            cfg.setProperty("SettSaveSmsCbx", "false");
            PHolder.getCrypt(PHolder.SMS_FILEID).erase();
        }

        PHolder.Sms = this.pwdf_smsPassword.getText().toCharArray();

        if(!noStore) {
            cfg.saveFile();
        }

        testSmsCredentials();
    }

    private void testMailCredentials(){

    Task isValid = new Task<Boolean>(){
        @Override
        protected Boolean call() throws Exception {
            Sender sender = new Sender();
            sender.setSenderAddress(fieldLogin.getText());
            sender.setSenderPassword(PHolder.Mail);
            sender.setSmtpHost(fieldHost.getText());
            sender.setSmtpPort(fieldPort.getText());
            sender.setSmtpStartTLS(AppConfiguration.retrieveProp(Cfg.SMTP_TLS));
            sender.initSession();
            sender.initConnection();
            return Boolean.TRUE;
        }
    };

        isValid.setOnRunning(event -> {
            this.prgMail.setVisible(true);
            this.prgMail.setProgress(-1);
        });
        isValid.setOnSucceeded(event -> {
            this.prgMail.setVisible(false);
            showTemporaryImg(this.okImgMail);
        });
        isValid.setOnFailed(event -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(isValid.getException().toString());
            alert.showAndWait();
            resetMailIndicators();
        });

        new Thread(isValid).start();


    }

    private void testLDAPCredentials(){
        Task isValid = new Task() {
            @Override
            protected Object call() throws Exception {
                LdapService ldapService = new LdapServiceImpl();
            return null;
            }
        };

        isValid.setOnRunning(event -> {
            this.prgLdap.setVisible(true);
            this.prgLdap.setProgress(-1);
        });
        isValid.setOnSucceeded(event -> {
            this.prgLdap.setVisible(false);
            showTemporaryImg(this.okImgLdap);
        });
        isValid.setOnFailed(event -> {
            DialogFactory.showAlert(Alert.AlertType.ERROR, isValid.getException().toString());
            resetLdapIndicators();
        });

        new Thread(isValid).start();

    }

    private void testDBCredentials(){
        Task<Boolean> isValid = new Task<>() {
            @Override
            protected Boolean call() {
                try {
                    AppConfiguration.setProperty(Cfg.DB_URL, fieldDbUrl.getText());
                    AppConfiguration.setProperty(Cfg.DB_LOGIN, fieldDbLogin.getText());
                    DbSession.testDbCredentials();
                } catch (GeneralSecurityException | IOException e) {
                    e.printStackTrace();
                }

                return Boolean.TRUE;
            }
        };

        isValid.setOnRunning(event -> {
            this.prgDb.setVisible(true);
            this.prgDb.setProgress(-1);
        });
        isValid.setOnSucceeded(event -> {
            this.prgDb.setVisible(false);
            showTemporaryImg(this.okImgDb);
            confirmCloseDialog();
        });
        isValid.setOnFailed(event -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(isValid.getException().toString());
            alert.showAndWait();
            resetDbIndicators();
        });

        new Thread(isValid).start();
    }

    private void testSmsCredentials(){
        Task isValid = new Task() {
            @Override
            protected Boolean call() throws Exception {
            SmsApiClient smsApiClient = new SmsApiClient(txtf_smsLogin.getText(), pwdf_smsPassword.getText());
            smsApiClient.getPoints();
            return Boolean.TRUE;
            }
        };

        isValid.setOnRunning(event -> {
            this.prgSms.setVisible(true);
            this.prgSms.setProgress(-1);
        });
        isValid.setOnSucceeded(event -> {
            this.prgSms.setVisible(false);
            showTemporaryImg(this.okImgSms);
        });
        isValid.setOnFailed(event -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(isValid.getException().toString());
            alert.showAndWait();
            resetSmsIndicators();
        });

        new Thread(isValid).start();
    }

    private void resetMailIndicators(){
        this.prgMail.setVisible(false);
        this.okImgMail.setVisible(false);
    }

    private void resetLdapIndicators(){
        this.prgLdap.setVisible(false);
        this.okImgLdap.setVisible(false);
    }

    private void resetDbIndicators(){
        this.prgDb.setVisible(false);
        this.okImgDb.setVisible(false);
    }

    private void resetSmsIndicators(){
        this.prgSms.setVisible(false);
        this.okImgSms.setVisible(false);
    }

    private void showTemporaryImg(Node node){
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                Platform.runLater(() -> node.setVisible(true));
                Thread.sleep(1750);
                Platform.runLater(() -> node.setVisible(false));
                return null;
            }
        };

        new Thread(task).start();

    }

    public void cbxEmbedDbActionEvent(ActionEvent actionEvent) {
        updateDbControlsState();
    }

    private void updateDbControlsState(){
        boolean selected = cbxDbEmbed.isSelected();
        fieldDbUrl.setDisable(selected);
        fieldDbLogin.setDisable(selected);
        fieldDbPass.setDisable(selected);
        cbxRememberDb.setDisable(selected);
    }

    private void confirmCloseDialog(){
        Alert alert = DialogFactory.buildAlert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Connected to database.");
        alert.setContentText("The application requires restart to use new database connection\nDo you wish to exit the application now?");
        Optional<ButtonType> choice = alert.showAndWait();
        choice.ifPresent(buttonType -> {
            if(buttonType == ButtonType.OK) Platform.exit();
        } );
    }
}
