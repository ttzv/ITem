package ui.settingsWindow;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import properties.Cfg;
import pwSafe.Crypt;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Date;

public class SettingsWindow extends AnchorPane {

    private Crypt cMail;
    private Crypt cLdap;
    private Crypt cDb;


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
    private CheckBox cbxRememberLdap;

    @FXML
    private TextField fieldDbLogin;

    @FXML
    private TextField fieldDbUrl;

    @FXML
    private PasswordField fieldDbPass;

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
        System.out.println("mail: " + fieldPass.getText());

        if(!noStore) {
            cfg.saveFile();
        }
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
        System.out.println("ldap: " + fieldLdapPass.getText());

        PHolder.ldap = this.fieldLdapPass.getText().toCharArray();

        if(!noStore) {
            cfg.saveFile();
        }
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
        System.out.println("db: " + fieldDbPass.getText());

        PHolder.db = this.fieldDbPass.getText().toCharArray();

        if(!noStore) {
            cfg.saveFile();
        }
    }





}
