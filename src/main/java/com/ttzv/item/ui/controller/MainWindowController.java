package com.ttzv.item.ui.controller;

import com.ttzv.item.entity.*;
import com.ttzv.item.properties.Cfg;
import com.ttzv.item.service.ADUserService;
import com.ttzv.item.service.ADUserServiceImpl;
import com.ttzv.item.service.OfficeService;
import com.ttzv.item.service.OfficeServiceImpl;
import com.ttzv.item.uiUtils.OfficeFormatCell;
import com.ttzv.item.uiUtils.TableViewCreator;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ttzv.uiUtils.ActionableTextField;
import ttzv.uiUtils.SideBar;
import ttzv.uiUtils.StatusBar;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class MainWindowController extends AnchorPane {

    private final Cfg AppConfiguration = Cfg.getInstance();

    private Parent root;

    private UserHolder userHolder;
    private ADUserService adUserService;
    private OfficeService officeService;

    private TableViewCreator<ADUser_n> tableViewCreator;
    private Map<Label, TextField> storageNodesMap;

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
    private TableView<ADUser_n> primaryUserTableView;

    @FXML
    private TableView<ADUser_n> msgQTableView;

    @FXML
    private AnchorPane contentPane;

    @FXML
    private SideBar sideBar;

    @FXML
    private GridPane sidebarGrid;

    @FXML
    private ActionableTextField txtfActLogin;

    @FXML
    private ActionableTextField txtfActName;

    @FXML
    private ActionableTextField txtfActSn;

    @FXML
    private ActionableTextField txtfActMail;


    @FXML
    private ActionableTextField txtfActDispN;

    @FXML
    private ActionableTextField txtfActCtName;

    @FXML
    public ActionableTextField txtfActCtName_2;

    @FXML
    private ActionableTextField txtfActCtPhone;

    @FXML
    private ActionableTextField txtfActPos;

    @FXML
    public ActionableTextField txtfActCtLandline;

    @FXML
    public ActionableTextField txtfActCtPostalcode;

    @FXML
    public ActionableTextField txtfActCtLocation;

    @FXML
    public ActionableTextField txtfActCtLocation_2;

    @FXML
    private ActionableTextField txtfActCtFax;

    @FXML
    private ActionableTextField txtfActPhNumber;

    @FXML
    public ActionableTextField txtfActUsrLandLine;

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
    public ComboBox<Office> cbox_Offices;


    @FXML
    public void initialize() {
        storageNodesMap = new HashMap<>();
        labelCity.setText("");
        labelUsername.setText("");
        adUserService = new ADUserServiceImpl();
        List<ADUser_n> adUsers = adUserService.getAll();
        userHolder = UserHolder.getHolder();
        userHolder.appendADUsers(adUsers);

        tableViewCreator = new TableViewCreator<ADUser_n>(primaryUserTableView);
        tableViewCreator.builder()
                .editColumn(0, "lockoutTime")
                .editColumn(1, "displayName")
                .editColumn(2, "officeLocation")
                .editColumn(3, "email")
                .editColumn(4, "sAMAccountName")
                .editColumn(5, "position")
                .editColumn(6, "whenCreated")
                .editColumn(7, "objectGUID")
                .editColumn(8, "objectSid")
                .setItems(userHolder.getADUsers());

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

        officeService = new OfficeServiceImpl();
        ObservableList<Office> observableListOffices = FXCollections.observableList(officeService.getOffices());
        cbox_Offices.setItems(observableListOffices);
        cbox_Offices.setCellFactory(param -> new OfficeFormatCell());
        cbox_Offices.setButtonCell(cbox_Offices.getCellFactory().call(null));

    }

    private void selectScene(Pane paneToSet){
        this.contentPane.getChildren().setAll(paneToSet);
        AnchorPane.setRightAnchor(paneToSet, 0.);
        AnchorPane.setLeftAnchor(paneToSet, 0.);
        AnchorPane.setTopAnchor(paneToSet, 0.);
        AnchorPane.setBottomAnchor(paneToSet, 0.);
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
    void performSaveUserProperties() {
        ADUser_n adUser = userHolder.getCurrentUser();
        UserDetail_n userDetail = adUser.getDetail();
        adUser.setSAMAccountName(txtfActLogin.getText());
        userDetail.setPosition(txtfActPos.getText());
        adUser.setEmail(txtfActMail.getText());

        userDetail.setLandlineNumber(txtfActUsrLandLine.getText());
        userDetail.setOffice(cbox_Offices.getValue());
        userDetail.setPhoneNumber(txtfActPhNumber.getText());
        userDetail.setLandlineNumber(txtfActUsrLandLine.getText());

        Map<String, Object> newStorage = new HashMap<>();
        storageNodesMap.forEach((label, textField) -> {
            String value = textField.getText();
            if(!value.isEmpty()) {
                newStorage.put(label.getText(), textField.getText());
            }
        });
        userDetail.setStorage(newStorage);

        adUserService.updateADUser(adUser);
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
    void btnRefreshUserList(ActionEvent event) {
        Task<Boolean> sync = new Task<>(){
            @Override
            protected Boolean call() {
                userHolder.appendADUsers(adUserService.getAll());
                tableViewCreator.builder().setItems(userHolder.getADUsers());
                return Boolean.TRUE;
            }
        };
        sync.setOnRunning(workerStateEvent -> {
            this.prgList.setVisible(true);
            this.prgList.setProgress(-1);
        });
        sync.setOnSucceeded(workerStateEvent -> {
            this.prgList.setVisible(false);
        });
        sync.setOnFailed(workerStateEvent -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(sync.getException().toString());
            alert.showAndWait();
            this.prgList.setVisible(false);
        });
        new Thread(sync).start();
    }


    private void addSearchFieldAction(){
        this.txtfSearchUsers.textProperty().addListener((observableValue, oldValue, newValue) ->  {
                tableViewCreator.filter(newValue);
        });
    }

    public void changeUser() {
        ADUser_n adUser = userHolder.getCurrentUser();

        this.labelUsername.setText(adUser.getDisplayName());
        this.labelCity.setText(adUser.getCity());
        mailerViewController.setUserName(adUser.getDisplayName());
        signaturesViewController.setTxtfName(adUser.getDisplayName());
        smsViewController.updateUserLabels(userHolder);

        Office office = adUser.getOffice();
        if(office != null) {
            Office existingOffice = cbox_Offices.getItems().stream().filter(off -> off.equals(office)).findFirst().orElse(null);
            cbox_Offices.getSelectionModel().select(existingOffice);
            signaturesViewController.setTxtfCity(office.getName());
            signaturesViewController.setTxtfCityPhone(office.getPhonenumber());
            signaturesViewController.setTxtfCityFax(office.getFax());
            String cType = office.getName();
            if (cType.equals("Filia")) {
                signaturesViewController.selectComboxVal(1);
            } else if (cType.equals("Centrala")) {
                signaturesViewController.selectComboxVal(0);
            }
        }

        signaturesViewController.setTxtfPos(adUser.getPosition());
        signaturesViewController.setTxtfPhone(adUser.getLandLineNumber());
        signaturesViewController.setTxtfMPhone(adUser.getPhoneNumber());
        signaturesViewController.reload();

        sidebarGrid.getChildren().removeAll(storageNodesMap.keySet());
        sidebarGrid.getChildren().removeAll(storageNodesMap.values());
        storageNodesMap.clear();
        buildPasswordStorageInterface(adUser.getDetail());
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
                ADUser_n adUser = primaryUserTableView.getSelectionModel()
                        .getSelectedItem();
                if(adUser != null) {
                    userHolder.setCurrentUser(adUser);
                    updateMainWindowAssets();
                }
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
        txtfActDispN.hideButtons();
        txtfActCtName.hideButtons();
        txtfActCtName_2.hideButtons();
        txtfActCtLocation.hideButtons();
        txtfActCtLocation_2.hideButtons();
        txtfActCtPostalcode.hideButtons();
        txtfActCtLandline.hideButtons();
        txtfActCtPhone.hideButtons();
        txtfActPos.hideButtons();
        txtfActCtFax.hideButtons();
        txtfActPhNumber.hideButtons();
        txtfActUsrLandLine.hideButtons();
    }

    private void addTxtfActListeners(){
        txtfActLogin.addButton1Action(txtfActLogin.COPY_ACTION());
        txtfActName.addButton1Action(txtfActName.COPY_ACTION());
        txtfActSn.addButton1Action(txtfActSn.COPY_ACTION());
        txtfActMail.addButton1Action(txtfActMail.COPY_ACTION());
        txtfActDispN.addButton1Action(txtfActDispN.COPY_ACTION());
        txtfActCtName.addButton1Action(txtfActCtName.COPY_ACTION());
        txtfActCtName_2.addButton1Action(txtfActCtName_2.COPY_ACTION());
        txtfActCtPhone.addButton1Action(txtfActCtPhone.COPY_ACTION());
        txtfActPos.addButton1Action(txtfActPos.COPY_ACTION());
        txtfActCtLocation.addButton1Action(txtfActCtLocation.COPY_ACTION());
        txtfActCtLocation_2.addButton1Action(txtfActCtLocation_2.COPY_ACTION());
        txtfActCtPostalcode.addButton1Action(txtfActCtPostalcode.COPY_ACTION());
        txtfActCtLandline.addButton1Action(txtfActCtLandline.COPY_ACTION());
        txtfActCtFax.addButton1Action(txtfActCtFax.COPY_ACTION());
        txtfActPhNumber.addButton1Action(txtfActPhNumber.COPY_ACTION());
        txtfActUsrLandLine.addButton1Action(txtfActUsrLandLine.COPY_ACTION());
    }

    private void setTxtfActValues(){
        ADUser_n adUser= userHolder.getCurrentUser();

        txtfActLogin.setText(adUser.getSAMAccountName());
        txtfActName.setText(adUser.getGivenName());
        txtfActSn.setText(adUser.getSn());
        txtfActMail.setText(adUser.getEmail());
        txtfActDispN.setText(adUser.getDisplayName());

        txtfActPos.setText(adUser.getPosition());
            //txtfActInitpass.setText(userDetail.getInitMailPass());

        //city
        Office office = adUser.getOffice();
        if(office != null) {
            txtfActCtName.setText(office.getLocation());
            txtfActCtPhone.setText(office.getPhonenumber());
            txtfActCtFax.setText(office.getFax());
        }
        //phone
        txtfActPhNumber.setText(adUser.getPhoneNumber());

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

    public void cbox_Offices_Event(ActionEvent actionEvent) {
        System.out.println(cbox_Offices.getValue());

        Office selectedOffice = cbox_Offices.getValue();
        txtfActCtName.setText(selectedOffice.getName());
        txtfActCtName_2.setText(selectedOffice.getName2());
        txtfActCtLocation.setText(selectedOffice.getLocation());
        txtfActCtLocation_2.setText(selectedOffice.getLocation2());
        txtfActCtPostalcode.setText(selectedOffice.getPostalcode());
        txtfActCtLandline.setText(selectedOffice.getLandline());
        txtfActCtPhone.setText(selectedOffice.getPhonenumber());
        txtfActCtFax.setText(selectedOffice.getFax());
    }

    public void buildPasswordStorageInterface(UserDetail_n userDetail) {
        Map<String, Object> storage = userDetail.getStorage();
        System.out.println(sidebarGrid.getRowCount());

        if(storage != null){
            storage.forEach((k, v) -> {
                TextField textField = new TextField();
                Label label = new Label(k);
                storageNodesMap.put(label, textField);
                textField.setText(v.toString());
                int lastRow = sidebarGrid.getRowCount();
                sidebarGrid.add(label, 0, lastRow);
                sidebarGrid.add(textField, 1, lastRow);
            });
        }
    }
}
