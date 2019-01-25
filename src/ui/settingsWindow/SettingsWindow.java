package ui.settingsWindow;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import properties.Cfg;

import java.io.IOException;

public class SettingsWindow extends AnchorPane {


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
        this.fieldHost.setText(Cfg.getInstance().retrieveProp(Cfg.SMTP_HOST));
        this.fieldPort.setText(Cfg.getInstance().retrieveProp(Cfg.SMTP_PORT));
        this.fieldLogin.setText(Cfg.getInstance().retrieveProp(Cfg.SMTP_LOGIN));
        this.fieldPass.setText(Cfg.getInstance().retrieveProp(Cfg.SMTP_PASS));

        String tlsSetting = Cfg.getInstance().retrieveProp(Cfg.SMTP_TLS);
        if(tlsSetting.equals("true")){
            this.cbxTls.setSelected(true);
        } else {
            this.cbxTls.setSelected(false);
        }

        String remCbxSetting = Cfg.getInstance().retrieveProp("SettSaveCbx");
        if(remCbxSetting.equals("true")){
            this.cbxTls.setSelected(true);
        } else {
            this.cbxRemember.setSelected(false);
        }
    }

    public void btnAcceptSettingsEvent() throws IOException {
        Cfg cfg = Cfg.getInstance();

        cfg.setProperty(Cfg.SMTP_HOST, this.fieldHost.getText());
        cfg.setProperty(Cfg.SMTP_PORT, this.fieldPort.getText());
        if(this.cbxTls.isSelected()) {
            cfg.setProperty(Cfg.SMTP_TLS, "true");
        } else {
            cfg.setProperty(Cfg.SMTP_TLS, "false");
        }

        cfg.setProperty(Cfg.SMTP_LOGIN, this.fieldLogin.getText());

        if(this.cbxRemember.isSelected()){
            cfg.setProperty(Cfg.SMTP_PASS, this.fieldPass.getText());
        } else {
            cfg.setProperty(Cfg.SMTP_PASS, "");
        }

        if(this.cbxTls.isSelected()) {
            cfg.setProperty(Cfg.SMTP_TLS, "true");
        } else {
            cfg.setProperty(Cfg.SMTP_TLS, "false");
        }

        if(this.cbxRemember.isSelected()) {
            cfg.setProperty("SettSaveCbx", "true");
        } else {
            cfg.setProperty("SettSaveCbx", "false");
        }

        cfg.saveFile();

    }


}
