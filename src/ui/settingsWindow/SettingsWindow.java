package ui.settingsWindow;

import ad.LDAPParser;
import db.DbCon;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import properties.Cfg;
import pwSafe.Crypt;
import pwSafe.PHolder;
import sender.Sender;
import uiUtils.TitledBorder;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class SettingsWindow extends AnchorPane {

    private Crypt cMail;
    private Crypt cLdap;
    private Crypt cDb;


    @FXML
    private TitledBorder containerMailSett;

    @FXML
    private TextField fieldHost;

    @FXML
    private TextField fieldPort;

    @FXML
    private CheckBox cbxTls;

    @FXML
    private TextField fieldLogin;

    @FXML
    private CheckBox cbxRemember;

    @FXML
    private PasswordField fieldPass;

    @FXML
    private ProgressIndicator prgMail;

    @FXML
    private ImageView okImgMail;

    @FXML
    private TitledBorder containerLdapSett;

    @FXML
    private TextField fieldLdapAcc;

    @FXML
    private TextField fieldLdapUrl;

    @FXML
    private TextField fieldLdapPort;

    @FXML
    private PasswordField fieldLdapPass;

    @FXML
    private ProgressIndicator prgLdap;

    @FXML
    private ImageView okImgLdap;

    @FXML
    private CheckBox cbxRememberLdap;

    @FXML
    private TitledBorder containerDbSett;

    @FXML
    private TextField fieldDbLogin;

    @FXML
    private TextField fieldDbUrl;

    @FXML
    private PasswordField fieldDbPass;

    @FXML
    private ImageView okImgDb;

    @FXML
    private ProgressIndicator prgDb;

    @FXML
    private CheckBox cbxRememberDb;



    public SettingsWindow() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("settingsWindow.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void initialize(){

        //mail
        this.fieldHost.setText(Cfg.getInstance().retrieveProp(Cfg.SMTP_HOST));
        this.fieldPort.setText(Cfg.getInstance().retrieveProp(Cfg.SMTP_PORT));
        this.fieldLogin.setText(Cfg.getInstance().retrieveProp(Cfg.SMTP_LOGIN));

        this.cMail = new Crypt("mCr");
        if(this.cMail.exists()){
            try {
                PHolder.mail = cMail.read();
                this.fieldPass.setText(new String(PHolder.mail));
            } catch (IOException | GeneralSecurityException e) {
                e.printStackTrace();
            }
        }

        String tlsSetting = Cfg.getInstance().retrieveProp(Cfg.SMTP_TLS);
        if(tlsSetting.equals("true")){
            this.cbxTls.setSelected(true);
        } else {
            this.cbxTls.setSelected(false);
        }

        String remCbxSetting = Cfg.getInstance().retrieveProp("SettSaveCbx");
        if(remCbxSetting.equals("true")){
            this.cbxRemember.setSelected(true);
        } else {
            this.cbxRemember.setSelected(false);
        }

        //ldap
        this.fieldLdapUrl.setText(Cfg.getInstance().retrieveProp(Cfg.LDAP_URL));
        this.fieldLdapPort.setText(Cfg.getInstance().retrieveProp(Cfg.LDAP_PORT));
        this.fieldLdapAcc.setText(Cfg.getInstance().retrieveProp(Cfg.LDAP_ACC));

        this.cLdap = new Crypt("lCr");
        if(this.cLdap.exists()){
            try {
                PHolder.ldap = cLdap.read();
                this.fieldLdapPass.setText(new String(PHolder.ldap));
            } catch (IOException | GeneralSecurityException e) {
                e.printStackTrace();
            }
        }
        String remCbxLdapSetting = Cfg.getInstance().retrieveProp("SettSaveLdapCbx");
        if(remCbxLdapSetting.equals("true")){
            this.cbxRememberLdap.setSelected(true);
        } else {
            this.cbxRememberLdap.setSelected(false);
        }

        //db
        this.fieldDbUrl.setText(Cfg.getInstance().retrieveProp(Cfg.DB_URL));
        this.fieldDbLogin.setText(Cfg.getInstance().retrieveProp(Cfg.DB_LOGIN));

        this.cDb = new Crypt("dCr");
        if(this.cDb.exists()){
            try {
                PHolder.db = cDb.read();
                this.fieldDbPass.setText(new String(PHolder.db));
            } catch (IOException | GeneralSecurityException e) {
                e.printStackTrace();
            }
        }
        String remCbxDbSetting = Cfg.getInstance().retrieveProp("SettSaveDbCbx");
        if(remCbxDbSetting.equals("true")){
            this.cbxRememberDb.setSelected(true);
        } else {
            this.cbxRememberDb.setSelected(false);
        }

        this.okImgDb.setVisible(false);
        this.okImgLdap.setVisible(false);
        this.okImgMail.setVisible(false);

        this.containerMailSett.focusedProperty().addListener(observable -> {
            resetMailIndicators();
        });
        this.containerLdapSett.focusedProperty().addListener(observable -> {
            resetLdapIndicators();
        });
        this.containerDbSett.focusedProperty().addListener(observable -> {
            resetDbIndicators();
        });

        //TODO: make ui for DB and LDAP connection, part below is temporary solution, no ui for this yet
        /*DbCon dbCon = null;
        try {
            dbCon = new DbCon();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            dbCon.ldapToDb();
        } catch (SQLException e) {
            e.printStackTrace();
        }*/

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
    public void btnSaveAllSettingsEvent() throws IOException {
        btnDbPerformAction(true);
        btnLdapPerformAction(true);
        btnMailPerformAction(true);
        Cfg.getInstance().saveFile();
    }

    private void btnMailPerformAction(boolean noStore) throws IOException {
        Cfg cfg = Cfg.getInstance();

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
                cMail.safeStore(this.fieldPass.getText());
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            }
            cfg.setProperty("SettSaveCbx", "true");
        } else {
            cMail.erase();
            cfg.setProperty("SettSaveCbx", "false");
        }

        PHolder.mail = this.fieldPass.getText().toCharArray();

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
        Cfg cfg = Cfg.getInstance();

        cfg.setProperty(Cfg.LDAP_URL, this.fieldLdapUrl.getText());
        cfg.setProperty(Cfg.LDAP_PORT, this.fieldLdapPort.getText());
        cfg.setProperty(Cfg.LDAP_ACC, this.fieldLdapAcc.getText());
        if(this.cbxRememberLdap.isSelected()) {
            try {
                cLdap.safeStore(this.fieldLdapPass.getText());
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            }
            cfg.setProperty("SettSaveLdapCbx", "true");
        } else {
            cfg.setProperty("SettSaveLdapCbx", "false");
            cLdap.erase();
        }

        PHolder.ldap = this.fieldLdapPass.getText().toCharArray();

        if(!noStore) {
            cfg.saveFile();
        }

        testLDAPCredentials();


    }

    private void btnDbPerformAction(boolean noStore) throws IOException {
        Cfg cfg = Cfg.getInstance();

        cfg.setProperty(Cfg.DB_URL, this.fieldDbUrl.getText());
        cfg.setProperty(Cfg.DB_LOGIN, this.fieldDbLogin.getText());
        if(this.cbxRememberDb.isSelected()) {
            try {
                cDb.safeStore(this.fieldDbPass.getText());
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            }
            cfg.setProperty("SettSaveDbCbx", "true");
        } else {
            cfg.setProperty("SettSaveDbCbx", "false");
            cDb.erase();
        }

        PHolder.db = this.fieldDbPass.getText().toCharArray();

        if(!noStore) {
            cfg.saveFile();
        }

        testDBCredentials();

    }

    private void testMailCredentials(){

    Task isValid = new Task<Boolean>(){
        @Override
        protected Boolean call() throws Exception {
            Sender sender = new Sender();
            sender.setSenderAddress(fieldLogin.getText());
            sender.setSenderPassword(PHolder.mail);
            sender.setSmtpHost(fieldHost.getText());
            sender.setSmtpPort(fieldPort.getText());
            sender.setSmtpStartTLS(Cfg.getInstance().retrieveProp(Cfg.SMTP_TLS));
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
                LDAPParser ldapParser = new LDAPParser();
                ldapParser.setLdap_URL(fieldLdapUrl.getText());
                ldapParser.setLdap_port(fieldLdapPort.getText());
                ldapParser.setAd_adminUser(fieldLdapAcc.getText());
                ldapParser.setAd_adminPass(PHolder.ldap);
                ldapParser.initializeLdapContext();
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
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(isValid.getException().toString());
            alert.showAndWait();
            resetLdapIndicators();
        });

        new Thread(isValid).start();

    }

    private void testDBCredentials(){
        Task isValid = new Task() {
            @Override
            protected Object call() throws Exception {
                DbCon dbCon = new DbCon();
                dbCon.setDbUrl(fieldDbUrl.getText());
                dbCon.setDbUser(fieldDbLogin.getText());
                dbCon.setDbPass(PHolder.db);
                dbCon.initConnection();
                return null;
            }
        };

        isValid.setOnRunning(event -> {
            this.prgDb.setVisible(true);
            this.prgDb.setProgress(-1);
        });
        isValid.setOnSucceeded(event -> {
            this.prgDb.setVisible(false);
            showTemporaryImg(this.okImgDb);
        });
        isValid.setOnFailed(event -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(isValid.getException().toString());
            alert.showAndWait();
            resetDbIndicators();
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

    private void showTemporaryImg(Node node){
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                System.out.println("visible");
                Platform.runLater(() -> node.setVisible(true));
                Thread.sleep(1750);
                Platform.runLater(() -> node.setVisible(false));
                System.out.println("reset");
                return null;
            }
        };

        new Thread(task).start();

    }





}
