package com.ttzv.item.ui.controller;

import com.ttzv.item.entity.ADUser_n;
import com.ttzv.item.entity.Office;
import com.ttzv.item.entity.UserDetail_n;
import com.ttzv.item.entity.UserHolder;
import com.ttzv.item.properties.Cfg;
import com.ttzv.item.pwSafe.PHolder;
import com.ttzv.item.service.*;
import com.ttzv.item.uiUtils.DialogFactory;
import com.ttzv.item.uiUtils.OfficeFormatCell;
import com.ttzv.item.uiUtils.TableViewCreator;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ttzv.uiUtils.ActionableTextField;
import ttzv.uiUtils.SideBar;
import ttzv.uiUtils.StatusBar;

import javax.naming.NamingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;

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

    public void postInitSmsController(){
        smsViewController.refreshAccountInfo();
    }

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
    public void initialize() throws GeneralSecurityException, IOException {
        storageNodesMap = new HashMap<>();
        labelCity.setText("");
        labelUsername.setText("");
        adUserService = new ADUserServiceImpl();
        List<ADUser_n> adUsers = adUserService.getAll();
        userHolder = UserHolder.getHolder();
        userHolder.setADUsers(adUsers);

        tableViewCreator = new TableViewCreator<>(primaryUserTableView);
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

        addPrimaryTableViewDoubleClickHandler();

        sideBar.setToggler(sidebartogglebtn);
        sideBar.setPrefWidth(0.0);
        sideBar.childrenVisible(false);
        sideBar.applyAnchors(0.0);

        hideTxtfActControls();

        addTxtfActListeners();

        addSearchFieldAction();

        selectScene(mailerView);

        officeService = new OfficeServiceImpl();
        ObservableList<Office> observableListOffices = FXCollections.observableList(officeService.getOffices());
        cbox_Offices.setItems(observableListOffices);
        cbox_Offices.setCellFactory(param -> new OfficeFormatCell());
        cbox_Offices.setButtonCell(cbox_Offices.getCellFactory().call(null));
        Office office = new Office();
        cbox_Offices.getItems().add(office);
        if(PHolder.Ldap().length > 0){
            syncTask();
        }

        userHolder.setCurrentUser(userHolder.getADUsers().stream().findFirst().orElse(null));
        if(userHolder.getCurrentUser() != null) updateMainWindowAssets();
    }

    public void addPrimaryTableViewRightClickHandler() throws IOException {
        Stage progressWindow = DialogFactory.getWaitWindow();
        ContextMenu cm = new ContextMenu();
        MenuItem unlock = new MenuItem(ResourceBundle.getBundle("lang").getString("tableaction.unlock"));
        cm.getItems().add(unlock);
        cm.setOnAction(event -> {
            ADUser_n selectedUser = primaryUserTableView.getSelectionModel().getSelectedItem();
            if(selectedUser != null && selectedUser.getLockoutTime() != null){
                Task<Boolean> unlockTask = new Task<Boolean>() {
                    @Override
                    protected Boolean call() throws Exception {
                    LdapService ldapService = new LdapServiceImpl();
                    ldapService.unlockADUser(selectedUser);
                    return Boolean.TRUE;
                    }
                };
                unlockTask.setOnRunning(action -> {
                    progressWindow.show();
                });
                unlockTask.setOnSucceeded(action -> {
                    progressWindow.close();
                    syncTask();
                });
                unlockTask.setOnFailed(action -> {
                    DialogFactory.showAlert(Alert.AlertType.ERROR, unlockTask.getException().toString());
                });
                new Thread(unlockTask).start();
            } else {
                cm.hide();
            }
        });
        primaryUserTableView.setContextMenu(cm);
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
        DialogFactory.showWindow("infoWindow", Modality.APPLICATION_MODAL, "Help");
    }

    @FXML
    void performSaveUserProperties() {
        ADUser_n adUser = userHolder.getCurrentUser();
        UserDetail_n userDetail = adUser.getDetail();
        adUser.setSAMAccountName(txtfActLogin.getText());
        userDetail.setPosition(txtfActPos.getText());
        adUser.setEmail(txtfActMail.getText());

        userDetail.setLandlineNumber(txtfActUsrLandLine.getText());
        Office selectedOffice;
        if (cbox_Offices.getSelectionModel().getSelectedItem() != null && cbox_Offices.getSelectionModel().getSelectedItem().getName() == null){
            selectedOffice = createOffice();
        } else {
            selectedOffice = cbox_Offices.getValue();
        }
        if (selectedOffice != null) {
            userDetail.setOffice(selectedOffice);
            selectedOffice.setName(txtfActCtName.getText());
            selectedOffice.setName2(txtfActCtName_2.getText());
            selectedOffice.setLocation(txtfActCtLocation.getText());
            selectedOffice.setLocation2(txtfActCtLocation_2.getText());
            selectedOffice.setPostalcode(txtfActCtPostalcode.getText());
            selectedOffice.setLandline(txtfActCtLandline.getText());
            selectedOffice.setPhonenumber(txtfActCtPhone.getText());
            selectedOffice.setFax(txtfActCtFax.getText());
            officeService.updateOffice(selectedOffice);
        }

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

    private Office createOffice(){
        Office office = new Office();
        office.setName(txtfActCtName.getText());
        office.setName2(txtfActCtName_2.getText());
        office.setLocation(txtfActCtLocation.getText());
        office.setLocation2(txtfActCtLocation_2.getText());
        office.setPostalcode(txtfActCtPostalcode.getText());
        office.setLandline(txtfActCtLandline.getText());
        office.setPhonenumber(txtfActCtPhone.getText());
        office.setFax(txtfActCtFax.getText());
        if(office.getName() != null){
            officeService.saveOffice(office);
            cbox_Offices.getItems().add(0, office);
            return office;
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Cannot create an Office without a name.");
        }
        return null;
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
        syncTask();
    }

    private void syncTask(){
        Task<Boolean> sync = new Task<>(){
            @Override
            protected Boolean call() throws GeneralSecurityException, NamingException, IOException {
                LdapService ldapService = new LdapServiceImpl();
                List<ADUser_n> ldapUsers = ldapService.getAll();
                Map<String, List<ADUser_n>> logSync = adUserService.updateTableFrom(ldapUsers);
                Map<String, List<ADUser_n>> logBind = adUserService.autoBindOffices(officeService.getOffices());
                System.out.println("LDAP Sync task:\nCreated: " + logSync.get("Created").size() + " Updated: " + logSync.get("Updated").size() + " Deleted: " + logSync.get("Deleted").size() + "\n"
                 + "Office bind task:\nUpdated: " + logBind.get("Updated").size());
                userHolder.setADUsers(adUserService.getAll());
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
            DialogFactory.showAlert(Alert.AlertType.ERROR,sync.getException().toString());
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
            signaturesViewController.setTxtfCity(office.getName2());
            String phoneNumber = office.getLandline();
            if (phoneNumber == null || phoneNumber.isBlank()) {
                phoneNumber = office.getPhonenumber();
                signaturesViewController.setOfficeMobileFormatter();
            } else {
                signaturesViewController.setOfficeDefaultFormatter();
            }
            signaturesViewController.setTxtfCityPhone(phoneNumber);
            signaturesViewController.setTxtfCityFax(office.getFax());
            String cName = office.getName();
            if (cName != null) {
                signaturesViewController.selectComboxVal(cName);
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


    private void addPrimaryTableViewDoubleClickHandler(){
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
        ADUser_n adUser = userHolder.getCurrentUser();

        txtfActLogin.setText(adUser.getSAMAccountName());
        txtfActName.setText(adUser.getGivenName());
        txtfActSn.setText(adUser.getSn());
        txtfActMail.setText(adUser.getEmail());
        txtfActDispN.setText(adUser.getDisplayName());
        txtfActPos.setText(adUser.getPosition());

        //city
        Office office = adUser.getOffice();
        if(office != null) {
            txtfActCtName.setText(office.getName());
            txtfActCtName_2.setText(office.getName2());
            txtfActCtLocation.setText(office.getLocation());
            txtfActCtLocation_2.setText(office.getLocation2());
            txtfActCtPostalcode.setText(office.getPostalcode());
            txtfActCtLandline.setText(office.getLandline());
            txtfActCtPhone.setText(office.getPhonenumber());
            txtfActCtFax.setText(office.getFax());
        }
        //phone
        txtfActPhNumber.setText(adUser.getPhoneNumber());
        txtfActUsrLandLine.setText(adUser.getLandLineNumber());

    }

    public void loadTheme(){
        String theme = AppConfiguration.retrieveProp(Cfg.THEME);
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
        if(selectedOffice != null) {
            txtfActCtName.setText(selectedOffice.getName());
            txtfActCtName_2.setText(selectedOffice.getName2());
            txtfActCtLocation.setText(selectedOffice.getLocation());
            txtfActCtLocation_2.setText(selectedOffice.getLocation2());
            txtfActCtPostalcode.setText(selectedOffice.getPostalcode());
            txtfActCtLandline.setText(selectedOffice.getLandline());
            txtfActCtPhone.setText(selectedOffice.getPhonenumber());
            txtfActCtFax.setText(selectedOffice.getFax());
        }
    }

    public void buildPasswordStorageInterface(UserDetail_n userDetail) {
        Map<String, Object> storage = userDetail.getStorage();
        System.out.println(storage);

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

    public void deleteOffice(ActionEvent actionEvent) {
        Office selectedOffice = cbox_Offices.getSelectionModel().getSelectedItem();
        if(selectedOffice != null && selectedOffice.getName() != null){
            Dialog<ButtonType> dialog = DialogFactory.<ButtonType>buildDialog();
            dialog.setContentText("Are you sure you want to delete Office: " + selectedOffice + " ?");
            dialog.getDialogPane().getButtonTypes().add(ButtonType.YES);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.NO);
            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.YES) {
                selectedOffice.getUserDetails().forEach(detail -> {
                    detail.setOffice(null);
                    adUserService.updateADUser(detail.getAdUser()); //todo ugh
                });
                officeService.deleteOffice(selectedOffice);
                cbox_Offices.getItems().remove(selectedOffice);
            }
        }
    }

}
