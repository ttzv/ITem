package com.ttzv.item.ui.controller;

import com.ttzv.item.dao.DbSession;
import com.ttzv.item.dao.UserComboWrapper;
import com.ttzv.item.dao.UserDaoLdapImpl;
import com.ttzv.item.entity.*;
import com.ttzv.item.properties.Cfg;
import com.ttzv.item.service.AdUserService;
import com.ttzv.item.uiUtils.ScenePicker;
import com.ttzv.item.uiUtils.TableViewCreator;
import com.ttzv.item.uiUtils.UiObjectsWrapper;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.hibernate.Session;
import ttzv.uiUtils.ActionableTextField;
import ttzv.uiUtils.SideBar;
import ttzv.uiUtils.StatusBar;

import javax.naming.NamingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class MainWindowController extends AnchorPane {

    private final Cfg AppConfiguration = Cfg.getInstance();

    private Parent root;

    private Stage primaryStage;
    private UserComboWrapper userComboWrapper;
    private UserHolder userHolder;


    private TableViewCreator tableViewCreator;

    @FXML
    private CheckMenuItem mnuItemThemeSelectModena;

    @FXML
    private CheckMenuItem mnuItemThemeSelectDarkModena;

    @FXML
    private Label labelUsername;

    @FXML
    private Label labelCity;

    @FXML
    private Button sidebartogglebtn;

    @FXML
    private TableView<Map> primaryUserTableView;

    @FXML
    private TableView<Map> msgQTableView;

    @FXML
    private AnchorPane contentPane;

    @FXML
    private SideBar sideBar;

    @FXML
    private ActionableTextField txtfActLogin;

    @FXML
    private ActionableTextField txtfActName;

    @FXML
    private ActionableTextField txtfActSn;

    @FXML
    private ActionableTextField txtfActMail;

    @FXML
    private ActionableTextField txtfActCity;

    @FXML
    private ActionableTextField txtfActDispN;

    @FXML
    private ActionableTextField txtfActCtName;

    @FXML
    private ActionableTextField txtfActCtPhone;

    @FXML
    private ActionableTextField txtfActPos;

    @FXML
    private ActionableTextField txtfActInitpass;

    @FXML
    private ActionableTextField txtfActUsrLandLine;

    @FXML
    private ActionableTextField txtfActCtType;

    @FXML
    private ActionableTextField txtfActCtFax;

    @FXML
    private ActionableTextField txtfActPhNumber;

    @FXML
    private ActionableTextField txtfActPhModel;

    @FXML
    private ActionableTextField txtfActPhImei;

    @FXML
    private ActionableTextField txtfActPhPin;

    @FXML
    private ActionableTextField txtfActPhPuk;

    @FXML
    private Button saveUserProperties;

    @FXML
    private StatusBar statusBar;

    @FXML
    private ProgressIndicator prgList;

    @FXML
    private TextField txtfSearchUsers;

    //Sms view + controller
    @FXML
    AnchorPane smsView;
    @FXML
    SmsController smsViewController;

    //Signatures view + controller
    @FXML
    AnchorPane signaturesView;
    @FXML
    SignaturesController signaturesViewController;

    //Settings view + controller
    @FXML
    AnchorPane settingsView;
    @FXML
    SettingsController settingsViewController;

    //Mailer view + controller
    @FXML
    AnchorPane mailerView;
    @FXML
    MailerController mailerViewController;

    //Settings view + controller
    @FXML
    AnchorPane commandBoxView;
    @FXML
    CommandBoxController commandBoxViewController;


    @FXML
    public void initialize() {
        labelCity.setText("");
        labelUsername.setText("");

//        ADUser ADUserHolderFirst = userHolder.getFirst();
//        if(ADUserHolderFirst != null) {
//            userHolder.setCurrentUser(ADUserHolderFirst);
//        }
//
//        tableViewCreator = new TableViewCreator(primaryUserTableView);
//        tableViewCreator.createFromMap(tableViewCreator.builder()
//                .addColumns(userHolder.getCurrentUser())
//                .addRows(userHolder.getAllUsers())
//        );

        addPrimarytableViewDoubleClickHandler();

        sideBar.setToggler(sidebartogglebtn);
        sideBar.setPrefWidth(0.0);
        sideBar.childrenVisible(false);
        sideBar.applyAnchors(0.0);

        hideTxtfActControls();

        addTxtfActListeners();

        addSearchFieldAction();

        //loadTheme();

        selectScene(mailerView);
    }

    private void selectScene(Pane paneToSet){
        this.contentPane.getChildren().setAll(paneToSet);
        AnchorPane.setRightAnchor(paneToSet, 0.);
        AnchorPane.setLeftAnchor(paneToSet, 0.);
        AnchorPane.setTopAnchor(paneToSet, 0.);
        AnchorPane.setBottomAnchor(paneToSet, 0.);
    }

    @FXML
    void btnRibbonCloseAction(MouseEvent event) {
        Platform.exit();
    }

    @FXML
    void btnRibbonMaximizeAction(MouseEvent event) {
        if(primaryStage.isMaximized())
            primaryStage.setMaximized(false);
        else
            primaryStage.setMaximized(true);
    }

    @FXML
    void btnRibbonMinimizeAction(MouseEvent event) {
        primaryStage.setIconified(true);
    }

    @FXML
    public void showMailSett(ActionEvent actionEvent) throws IOException {
        //TODO: create utility method for quick initialization of new windows from fxml files, something like *showWindow(String file)*
        ResourceBundle langResourceBundle = ResourceBundle.getBundle("lang");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/infoWindow.fxml"),langResourceBundle);
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Pomoc");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void performSaveUserProperties() throws IOException, SQLException {
        ADUser ADUser = userHolder.getCurrentUser();
        UserDetail userDetail = userComboWrapper.getDetailOf(ADUser);
        City city = userComboWrapper.getCityOf(ADUser);
        Phone phone = userComboWrapper.getPhoneOf(ADUser);
        //userdetail
        if (userDetail != null) {
            userDetail.setPosition(txtfActPos.getText());
            userDetail.setInitMailPass(txtfActInitpass.getText());

            userComboWrapper.updateUserDetail(userDetail);
        }
        //city
        if(city != null) {
            city.setName(txtfActCtName.getText());
            city.setType(txtfActCtType.getText());
            city.setLandLineNumber(txtfActCtPhone.getText());
            city.setFaxNumber(txtfActCtFax.getText());

            userComboWrapper.updateCity(city);
        }
        //phone
        if(phone != null) {
            phone.setNumber(txtfActPhNumber.getText());
            phone.setModel(txtfActPhModel.getText());
            phone.setImei(txtfActPhImei.getText());
            phone.setPin(txtfActPhPin.getText());
            phone.setPuk(txtfActPhPuk.getText());

            userComboWrapper.updatePhone(phone);
        }




    }

    @FXML
    void exitAction(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void pathToClipboard(ActionEvent event) {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.putString("%USERPROFILE%\\AppData\\Roaming\\Microsoft\\Signatures");
        clipboard.setContent(clipboardContent);
        statusBar.setVanishingText("Skopiowano do schowka");
    }

    @FXML
    void btnClearUserList(ActionEvent event) {
        primaryUserTableView.getItems().clear();
    }

    @FXML
    void btnRefreshUserList(ActionEvent event) throws SQLException, IOException, NamingException, GeneralSecurityException {
        AdUserService adUserService = new AdUserService();
        System.out.println(adUserService.getAll());
//        Task sync = new Task<Boolean>(){
//            @Override
//            protected Boolean call() throws Exception {
//                EntityDAO<ADUser> userEntityDAOldap = new UserDaoLdapImpl();
//                userHolder.syncAndRefresh(userEntityDAOldap);
//                return Boolean.TRUE;
//            }
//        };
//        sync.setOnRunning(workerStateEvent -> {
//            this.prgList.setVisible(true);
//            this.prgList.setProgress(-1);
//        });
//        sync.setOnSucceeded(workerStateEvent -> {
//            this.prgList.setVisible(false);
//            Platform.runLater(() -> {
//                tableViewCreator = new TableViewCreator(primaryUserTableView);
//                tableViewCreator.createFromMap(tableViewCreator.builder()
//                        .addColumns(userHolder.getNewest(1).get(0))
//                        .addRows(userHolder.getAllUsers()));
//            });
//        });
//        sync.setOnFailed(workerStateEvent -> {
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setContentText(sync.getException().toString());
//            alert.showAndWait();
//            this.prgList.setVisible(false);
//        });
//        new Thread(sync).start();
    }


    private void addSearchFieldAction(){
        this.txtfSearchUsers.textProperty().addListener((observableValue, oldValue, newValue) ->  {
                tableViewCreator.filter(newValue);
        });
    }

    public void changeUser() {
        ADUser ADUser = userHolder.getCurrentUser();
        City city = userComboWrapper.getCityOf(ADUser);
        Phone phone = userComboWrapper.getPhoneOf(ADUser);
        UserDetail userDetail = userComboWrapper.getDetailOf(ADUser);

        if (ADUser != null) {
            this.labelUsername.setText(userHolder.getCurrentUser().getDisplayName());
            this.labelCity.setText(userHolder.getCurrentUser().getCity());
            mailerViewController.setUserName(userHolder.getCurrentUser().getDisplayName());
            signaturesViewController.setTxtfName(ADUser.getDisplayName());
            smsViewController.updateUserLabels(userHolder);
        }

        if(city != null) {
            signaturesViewController.setTxtfCity(city.getName());
            signaturesViewController.setTxtfCityPhone(city.getLandLineNumber());
            signaturesViewController.setTxtfCityFax(city.getFaxNumber());
            String cType = city.getType();
            if (cType.equals("Filia")) {
                signaturesViewController.selectComboxVal(1);
            } else if (cType.equals("Centrala")) {
                signaturesViewController.selectComboxVal(0);
            }
        }

        if(userDetail != null) {
            signaturesViewController.setTxtfPos(userDetail.getPosition());
            signaturesViewController.setTxtfPhone(userDetail.getLandLineNumber());
        }

        if(phone != null) {
            signaturesViewController.setTxtfMPhone(phone.getNumber());
        }

        signaturesViewController.reload();


    }

    public void setStatusBarText(String text) {
        this.statusBar.setVanishingText(text);
    }

    public void setLabelUsername(String labelUsername) {
        this.labelUsername.setText(labelUsername);
    }

    public void setLabelCity(String labelCity) {
        this.labelCity.setText(labelCity);
    }

    @FXML
    void sidebartoggle(ActionEvent event) {
        sideBar.animatePane();
    }

    @FXML
    void mnuItemThemeSelectDarkModenaAction(ActionEvent event) throws IOException {
        if(this.mnuItemThemeSelectModena.isSelected()) {
            this.mnuItemThemeSelectModena.setSelected(false);
        }

        root.getStylesheets().add(getClass().getResource("/style/dark-modena2.css").toExternalForm());
        this.mnuItemThemeSelectDarkModena.setSelected(true);
        AppConfiguration.setProperty(Cfg.THEME, this.mnuItemThemeSelectDarkModena.getText());
        AppConfiguration.saveFile();
    }

    @FXML
    void mnuItemThemeSelectModenaAction(ActionEvent event) throws IOException {
        if(this.mnuItemThemeSelectDarkModena.isSelected()){
            this.mnuItemThemeSelectDarkModena.setSelected(false);
        }
        root.getStylesheets().remove(getClass().getResource("/style/dark-modena2.css").toExternalForm());
        this.mnuItemThemeSelectModena.setSelected(true);
        AppConfiguration.setProperty(Cfg.THEME, this.mnuItemThemeSelectModena.getText());
        AppConfiguration.saveFile();
    }


    private void addPrimarytableViewDoubleClickHandler(){
        this.primaryUserTableView.setOnMouseClicked(mouseEvent -> {
            if(mouseEvent.getClickCount() == 2){
                String id = primaryUserTableView.getSelectionModel()
                        .getSelectedItem()
                        .get(UserData.objectGUID.toString()).toString();
                userHolder.setCurrentUser(userHolder.getUser(id));
                updateMainWindowAssets();
            }
        });
    }

    public void updateMainWindowAssets(){
        setTxtfActValues();
        changeUser();
    }

    private void hideTxtfActControls(){
        txtfActLogin.hideButtons();
        txtfActName.hideButtons();
        txtfActSn.hideButtons();
        txtfActMail.hideButtons();
        txtfActCity.hideButtons();
        txtfActDispN.hideButtons();
        txtfActCtName.hideButtons();
        txtfActCtPhone.hideButtons();
        txtfActPos.hideButtons();
        txtfActInitpass.hideButtons();
        txtfActCtType.hideButtons();
        txtfActCtFax.hideButtons();
        txtfActPhNumber.hideButtons();
        txtfActPhModel.hideButtons();
        txtfActPhImei.hideButtons();
        txtfActPhPin.hideButtons();
        txtfActPhPuk.hideButtons();
        txtfActUsrLandLine.hideButtons();
    }

    private void addTxtfActListeners(){
        txtfActLogin.addButton1Action(txtfActLogin.COPY_ACTION());
        txtfActName.addButton1Action(txtfActName.COPY_ACTION());
        txtfActSn.addButton1Action(txtfActSn.COPY_ACTION());
        txtfActMail.addButton1Action(txtfActMail.COPY_ACTION());
        txtfActCity.addButton1Action(txtfActCity.COPY_ACTION());
        txtfActDispN.addButton1Action(txtfActDispN.COPY_ACTION());
        txtfActCtName.addButton1Action(txtfActCtName.COPY_ACTION());
        txtfActCtPhone.addButton1Action(txtfActCtPhone.COPY_ACTION());
        txtfActPos.addButton1Action(txtfActPos.COPY_ACTION());
        txtfActInitpass.addButton1Action(txtfActInitpass.COPY_ACTION());
        txtfActCtType.addButton1Action(txtfActCtType.COPY_ACTION());
        txtfActCtFax.addButton1Action(txtfActCtFax.COPY_ACTION());
        txtfActPhNumber.addButton1Action(txtfActPhNumber.COPY_ACTION());
        txtfActPhModel.addButton1Action(txtfActPhModel.COPY_ACTION());
        txtfActPhImei.addButton1Action(txtfActPhImei.COPY_ACTION());
        txtfActPhPin.addButton1Action(txtfActPhPin.COPY_ACTION());
        txtfActPhPuk.addButton1Action(txtfActPhPuk.COPY_ACTION());
        txtfActUsrLandLine.addButton1Action(txtfActUsrLandLine.COPY_ACTION());
    }

    private void setTxtfActValues(){
        ADUser ADUser = userHolder.getCurrentUser();
        UserDetail userDetail = userComboWrapper.getDetailOf(ADUser);
        City city = userComboWrapper.getCityOf(ADUser);
        Phone phone = userComboWrapper.getPhoneOf(ADUser);
        //user and user detail
        if(ADUser != null){
            txtfActLogin.setText(ADUser.getSamAccountName());
            txtfActName.setText(ADUser.getGivenName());
            txtfActSn.setText(ADUser.getSn());
            txtfActMail.setText(ADUser.getMail());
            txtfActCity.setText(ADUser.getCity());
            txtfActDispN.setText(ADUser.getDisplayName());
        }
        if(userDetail != null) {
            txtfActPos.setText(userDetail.getPosition());
            txtfActInitpass.setText(userDetail.getInitMailPass());
        }
        //city
        if(city != null) {
            txtfActCtName.setText(city.getName());
            txtfActCtPhone.setText(city.getLandLineNumber());
            txtfActCtType.setText(city.getType());
            txtfActCtFax.setText(city.getFaxNumber());
        }
        //phone
        if(phone != null) {
            txtfActPhNumber.setText(phone.getNumber());
            txtfActPhModel.setText(phone.getModel());
            txtfActPhImei.setText(phone.getImei());
            txtfActPhPin.setText(phone.getPin());
            txtfActPhPuk.setText(phone.getPuk());
        }
    }

    private void loadTheme(){ //todo: refactor
        String theme = AppConfiguration.retrieveProp(Cfg.THEME);
        String modena = "Modena";
        String darkModena = "Dark Modena";
        if(theme.equals(darkModena)){
            root.getStylesheets().add(getClass().getResource("/style/dark-modena2.css").toExternalForm());
            this.mnuItemThemeSelectDarkModena.setSelected(true);
        }
    }

    @FXML
    private void showMailerView(ActionEvent actionEvent) { selectScene(mailerView); }
    @FXML
    private void showSignatureView(ActionEvent actionEvent) {
        selectScene(signaturesView);
    }
    @FXML
    private void showSmsView(ActionEvent actionEvent) {
        selectScene(smsView);
    }
    @FXML
    private void showCmdbxView(ActionEvent actionEvent) {
        selectScene(commandBoxView);
    }
    @FXML
    private void showSettingsView(ActionEvent actionEvent) {
        selectScene(settingsView);
    }

    /**
     * Used to provide root reference to MainWindow controller
     * @param root reference to root
     */
    public void referenceRoot(Parent root){
        this.root = root;
    }
}
