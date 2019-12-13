package com.ttzv.item.ui;

import com.ttzv.item.dao.UserComboWrapper;
import com.ttzv.item.dao.UserDaoLdapImpl;
import com.ttzv.item.entity.*;
import com.ttzv.item.properties.Cfg;
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
import ttzv.uiUtils.ActionableTextField;
import ttzv.uiUtils.SideBar;
import ttzv.uiUtils.StatusBar;

import javax.naming.NamingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.Map;

public class MainWindow extends AnchorPane {

    private ScenePicker scenePicker;

    private FXMLLoader fxmlLoader;
    private boolean infoBarAssetsVisible;
    private UiObjectsWrapper uiObjectsWrapper;
    private UserHolder userHolder;
    private UserComboWrapper userComboWrapper;
    private TableViewCreator tableViewCreator;


    @FXML
    private Button scene1;

    @FXML
    private Button scene2;

    @FXML
    private Button scene3;

    @FXML
    private Button scene4;

    @FXML
    private Button scene5;

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
    private SideBar sidebartest;

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



    public MainWindow(UiObjectsWrapper uiObjectsWrapper, UserHolder userHolder, UserComboWrapper userComboWrapper) {
        this.uiObjectsWrapper = uiObjectsWrapper;
        this.userHolder = userHolder;
        this.userComboWrapper = userComboWrapper;

        uiObjectsWrapper.registerObject(uiObjectsWrapper.MainWindow, this);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/mainWindow.fxml"));
        this.fxmlLoader = fxmlLoader;
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MainWindow getMainWindow() {
        return this;
    }

    public FXMLLoader getFxmlLoader() {
        return fxmlLoader;
    }

    @FXML
    public void initialize() {
        labelCity.setText("");
        labelUsername.setText("");

        User userHolderFirst = userHolder.getFirst();
        if(userHolderFirst != null) {
            userHolder.setCurrentUser(userHolderFirst);
        }

        tableViewCreator = new TableViewCreator(primaryUserTableView);
        tableViewCreator.createFromMap(tableViewCreator.builder()
                .addColumns(userHolder.getCurrentUser())
                .addRows(userHolder.getAllUsers())
        );

        addPrimarytableViewDoubleClickHandler();

        sidebartest.setToggler(sidebartogglebtn);
        sidebartest.setPrefWidth(0.0);
        sidebartest.childrenVisible(false);
        sidebartest.applyAnchors(0.0);

        hideTxtfActControls();

        //updateMainWindowAssets();

        addTxtfActListeners();

        addSearchFieldAction();

    }


    public void goScn1(ActionEvent actionEvent) {
        selectScene(0);
        //statusBar.setVanishingText("Mailing - wysyłanie szablonów wiadomości z danymi dostępowymi");
    }

    @FXML
    public void goScn2(ActionEvent actionEvent) {
        selectScene(1);
        //statusBar.setVanishingText("Selected Scene 2");
    }

    @FXML
    public void goScn3(ActionEvent actionEvent) {
        selectScene(2);
        //statusBar.setVanishingText("Selected Scene 3");
    }

    @FXML
    public void goScn4(ActionEvent actionEvent) {
        selectScene(3);
        //statusBar.setVanishingText("Selected Scene 4");
    }

    @FXML
    public void goScn5(ActionEvent actionEvent) {
        selectScene(4);
        //statusBar.setVanishingText("Ustawienia połączeń z serwerem poczty, katalogiem LDAP, bazą danych itp.");
    }

    private void selectScene(int index) {

        Pane paneToSet = scenePicker.getScene(index);
        this.contentPane.getChildren().setAll(paneToSet);
        AnchorPane.setRightAnchor(paneToSet, 0.);
        AnchorPane.setLeftAnchor(paneToSet, 0.);
        AnchorPane.setTopAnchor(paneToSet, 0.);
        AnchorPane.setBottomAnchor(paneToSet, 0.);

        this.scenePicker.setActiveScene(index);
    }

    public void loadOnStart() {
        int active = scenePicker.getActiveScene();
        if (active >= 0) {
            selectScene(active);
        }
    }

    @FXML
    public void showMailSett(ActionEvent actionEvent) throws IOException {
        //TODO: create utility method for quick initialization of new windows from fxml files, something like *showWindow(String file)*
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/infoWindow.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Pomoc");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void performSaveUserProperties() throws IOException, SQLException {
        User user = userHolder.getCurrentUser();
        UserDetail userDetail = userComboWrapper.getDetailOf(user);
        City city = userComboWrapper.getCityOf(user);
        Phone phone = userComboWrapper.getPhoneOf(user);
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
    void loadNewUsers() throws NamingException, SQLException {
        int userQtyToLoad = Integer.parseInt(Cfg.getInstance().retrieveProp(Cfg.DB_USER_QTY));
    }

    @FXML
    void imgBtnNextUser(MouseEvent event) {
        //UserHolder.next();
        changeUser();
    }

    @FXML
    void imgBtnPrevUser(MouseEvent event) {
        //UserHolder.previous();
        changeUser();
    }

    @FXML
    void exitAction(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void imgCityEditAction(MouseEvent event) {
        //cityEditPop.showAt(event.getScreenX(), event.getScreenY());
    }

    @FXML
    void imgUserEditAction(MouseEvent event) {
        //userEditPop.showAt(event.getScreenX(), event.getScreenY());
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
        Task sync = new Task<Boolean>(){
            @Override
            protected Boolean call() throws Exception {
                EntityDAO<User> userEntityDAOldap = new UserDaoLdapImpl();
                userHolder.syncAndRefresh(userEntityDAOldap);
                return Boolean.TRUE;
            }
        };
        sync.setOnRunning(workerStateEvent -> {
            this.prgList.setVisible(true);
            this.prgList.setProgress(-1);
        });
        sync.setOnSucceeded(workerStateEvent -> {
            this.prgList.setVisible(false);
            Platform.runLater(() -> {
                tableViewCreator = new TableViewCreator(primaryUserTableView);
                tableViewCreator.createFromMap(tableViewCreator.builder()
                        .addColumns(userHolder.getNewest(1).get(0))
                        .addRows(userHolder.getAllUsers()));
            });
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
        User user = userHolder.getCurrentUser();
        City city = userComboWrapper.getCityOf(user);
        Phone phone = userComboWrapper.getPhoneOf(user);
        UserDetail userDetail = userComboWrapper.getDetailOf(user);

        MailerWindow mw = (MailerWindow) scenePicker.getScene(0);

        SignWindow sw = (SignWindow) scenePicker.getScene(1);

        if (user != null) {
            this.labelUsername.setText(userHolder.getCurrentUser().getDisplayName());
            this.labelCity.setText(userHolder.getCurrentUser().getCity());
            mw.setUserName(userHolder.getCurrentUser().getDisplayName());
            sw.setTxtfName(user.getDisplayName());
        }

        if(city != null) {
            sw.setTxtfCity(city.getName());
            sw.setTxtfCityPhone(city.getLandLineNumber());
            sw.setTxtfCityFax(city.getFaxNumber());
            String cType = city.getType();
            if (cType.equals("Filia")) {
                sw.selectComboxVal(1);
            } else if (cType.equals("Centrala")) {
                sw.selectComboxVal(0);
            }
        }

        if(userDetail != null) {
            sw.setTxtfPos(userDetail.getPosition());
            sw.setTxtfPhone(userDetail.getLandLineNumber());
        }

        if(phone != null) {
            sw.setTxtfMPhone(phone.getNumber());
        }

        sw.reload();
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


    public void addSubScenes(Pane... scenes) {
        scenePicker = new ScenePicker();
        scenePicker.addAll(scenes);
    }

    @FXML
    void sidebartoggle(ActionEvent event) {
        sidebartest.animatePane();
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
        User user = userHolder.getCurrentUser();
        UserDetail userDetail = userComboWrapper.getDetailOf(user);
        City city = userComboWrapper.getCityOf(user);
        Phone phone = userComboWrapper.getPhoneOf(user);
        //user and user detail
        if(user != null){
            txtfActLogin.setText(user.getSamAccountName());
            txtfActName.setText(user.getGivenName());
            txtfActSn.setText(user.getSn());
            txtfActMail.setText(user.getMail());
            txtfActCity.setText(user.getCity());
            txtfActDispN.setText(user.getDisplayName());
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

}
