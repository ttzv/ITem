package ui.mainAppWindow;

import ad.LDAPParser;
import ad.UserHolder;
import db.DbCon;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import properties.Cfg;
import pwSafe.PHolder;
import ui.adWindow.ADWindow;
import ui.mailerWindow.InfoWindow;
import ui.mailerWindow.MailerWindow;
import ui.crmWindow.CrmWindow;
import ui.gSuiteWindow.GSuiteWindow;
import ui.sceneControl.ScenePicker;
import ui.settingsWindow.SettingsWindow;
import uiUtils.StatusBar;

import javax.naming.NamingException;
import java.io.IOException;
import java.sql.SQLException;

public class MainWindow extends AnchorPane {

    private ScenePicker scenePicker;

    private FXMLLoader fxmlLoader;


    @FXML
    private Label labelUsername;
    @FXML
    private Label labelCity;
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
    public AnchorPane contentPane;
    @FXML
    public StatusBar statusBar;

    public MainWindow(){
        fxmlLoader = new FXMLLoader(getClass().getResource("mainWindow.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public FXMLLoader getFxmlLoader() {
        return fxmlLoader;
    }

    @FXML
    public void initialize(){
        scenePicker = new ScenePicker();
        scenePicker.addAll(new MailerWindow(this), new ADWindow(), new CrmWindow(), new GSuiteWindow(), new SettingsWindow());
        labelCity.setText("");
        labelUsername.setText("");


        loadOnStart();
    }



    public void goScn1(ActionEvent actionEvent) {
        selectScene(0);
        statusBar.setVanishingText("Mailing - wysyłanie szablonów wiadomości z danymi dostępowymi");
    }
    @FXML
    public void goScn2(ActionEvent actionEvent) {
        selectScene(1);
        statusBar.setVanishingText("Selected Scene 2");
    }
    @FXML
    public void goScn3(ActionEvent actionEvent) {
        selectScene(2);
        statusBar.setVanishingText("Selected Scene 3");
    }
    @FXML
    public void goScn4(ActionEvent actionEvent) {
        selectScene(3);
        statusBar.setVanishingText("Selected Scene 4");
    }
    @FXML
    public void goScn5(ActionEvent actionEvent) {
        selectScene(4);
        statusBar.setVanishingText("Ustawienia połączeń z serwerem poczty, katalogiem LDAP, bazą danych itp.");
    }

    private void selectScene(int index){

        Pane paneToSet = scenePicker.getScene(index);
        this.contentPane.getChildren().setAll(paneToSet);
        AnchorPane.setRightAnchor(paneToSet,0.);
        AnchorPane.setLeftAnchor(paneToSet, 0.);
        AnchorPane.setTopAnchor(paneToSet, 0.);
        AnchorPane.setBottomAnchor(paneToSet, 0.);

        this.scenePicker.setActiveScene(index);
    }

    private void loadOnStart(){
        int active = scenePicker.getActiveScene();
        if(active >= 0) {
            selectScene(active);
        }
    }
    @FXML
    public void showMailSett(ActionEvent actionEvent) {
        InfoWindow infoWindow = new InfoWindow();
        infoWindow.getStage().show();
    }

    @FXML
    void showDbToolsWindow(ActionEvent event) {

    }

    @FXML
    void loadNewestUser(ActionEvent event) throws NamingException, SQLException {
        LDAPParser ldapParser = new LDAPParser();
        ldapParser.setLdap_URL(Cfg.getInstance().retrieveProp(Cfg.LDAP_URL));
        ldapParser.setLdap_port(Cfg.getInstance().retrieveProp(Cfg.LDAP_PORT));
        ldapParser.setAd_adminUser(Cfg.getInstance().retrieveProp(Cfg.LDAP_ACC));
        ldapParser.setAd_adminPass(PHolder.ldap);
        ldapParser.initializeLdapContext();
        ldapParser.queryLdap();

        DbCon dbCon = new DbCon(ldapParser);
        dbCon.setDbUrl(Cfg.getInstance().retrieveProp(Cfg.DB_URL));
        dbCon.setDbUser(Cfg.getInstance().retrieveProp(Cfg.DB_LOGIN));
        dbCon.setDbPass(PHolder.db);
        dbCon.initConnection();

        dbCon.getNewUsers();

        UserHolder.setCurrentUser(dbCon.getNewestUser());

        this.labelUsername.setText(UserHolder.getCurrentUser().getDisplayName());
        this.labelCity.setText(UserHolder.getCurrentUser().getCity());

        MailerWindow w = (MailerWindow) scenePicker.getScene(0);
        w.setUserName(UserHolder.getCurrentUser().getDisplayName());
    }

    public void setStatusBarText(String text){
        this.statusBar.setVanishingText(text);
    }

}
