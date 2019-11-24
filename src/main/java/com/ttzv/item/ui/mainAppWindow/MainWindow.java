package com.ttzv.item.ui.mainAppWindow;

import com.ttzv.item.dao.UserComboWrapper;
import com.ttzv.item.entity.*;
import com.ttzv.item.properties.Cfg;
import com.ttzv.item.ui.mailerWindow.MailerWindow;
import com.ttzv.item.ui.mainAppWindow.popups.CityEdit;
import com.ttzv.item.ui.mainAppWindow.popups.UserEdit;
import com.ttzv.item.ui.sceneControl.ScenePicker;
import com.ttzv.item.ui.signWindow.SignWindow;
import com.ttzv.item.uiUtils.TableViewCreator;
import com.ttzv.item.uiUtils.UiObjectsWrapper;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
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
import java.sql.SQLException;
import java.util.Map;

public class MainWindow extends AnchorPane {

    private ScenePicker scenePicker;

    private FXMLLoader fxmlLoader;

    private UserEdit userEditPop;
    private CityEdit cityEditPop;
    private SearchWindow searchWindow;
    private boolean infoBarAssetsVisible;
    private UiObjectsWrapper uiObjectsWrapper;
    private UserHolder userHolder;
    private UserComboWrapper userComboWrapper;


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
    private TableView<Map> primaryUserTableView;

    @FXML
    private TableView<Map> msgQTableView;

    @FXML
    private AnchorPane contentPane;

    @FXML
    private StatusBar statusBar;

    @FXML
    private SideBar sidebartest;

    @FXML
    private Button sidebartogglebtn;

    @FXML
    private ActionableTextField actionabletest1;


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

        userEditPop = null;//new UserEdit(uiObjectsWrapper);
        cityEditPop = null;//new CityEdit(uiObjectsWrapper);
        searchWindow = new SearchWindow(uiObjectsWrapper, userHolder);

        TableViewCreator tableViewCreator = new TableViewCreator(primaryUserTableView);
        tableViewCreator.createFromMap(TableViewCreator.builder()
                .addColumns(userHolder.getNewest(1).get(0))
                .addRows(userHolder.getAllUsers())
        );

        sidebartest.setToggler(sidebartogglebtn);
        sidebartest.setPrefWidth(0.0);
        sidebartest.childrenVisible(false);
        sidebartest.applyAnchors(0.0);

        actionabletest1.hideButtons();

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

    private void loadOnStart() {
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

    /*@FXML
        //not used currently
    void loadNewestUser(ActionEvent event) throws NamingException, SQLException {
        LDAPParser ldapParser = new LDAPParser();
        ldapParser.loadCfgCredentials();
        ldapParser.initializeLdapContext();

        DbCon dbCon = new DbCon(ldapParser);
        dbCon.loadCfgCredentials();
        dbCon.setDbPass(PHolder.db);
        dbCon.initConnection();

        dbCon.updateUsersTable();

        UserHolder.setCurrentUser(dbCon.getNewestUser());

        changeUser();
    }*/

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
    void findAction(ActionEvent event) {
        searchWindow.show();
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


    public boolean isInfoBarAssetsVisible() {
        return infoBarAssetsVisible;
    }

    public void changeUser() {
        this.labelUsername.setText(userHolder.getCurrentUser().getDisplayName());
        this.labelCity.setText(userHolder.getCurrentUser().getCity());


        MailerWindow mw = (MailerWindow) scenePicker.getScene(0);
        mw.setUserName(userHolder.getCurrentUser().getDisplayName());

        SignWindow sw = (SignWindow) scenePicker.getScene(1);
        User user = userHolder.getCurrentUser();
        City city = userComboWrapper.getCityOf(user);
        Phone phone = userComboWrapper.getPhoneOf(user);
        UserDetail userDetail = userComboWrapper.getDetailOf(user);
        sw.setTxtfName(user.getDisplayName());
        sw.setTxtfCity(userHolder.getCurrentUser().getCity());
        sw.setTxtfCityPhone(city.getLandLineNumber());
        sw.setTxtfCityFax(city.getFaxNumber());
        sw.setTxtfPos(userDetail.getPosition());
        sw.setTxtfPhone(userDetail.getLandLineNumber());
        sw.setTxtfMPhone(phone.getNumber());
        String cType = city.getType();
        if (cType.equals("Filia")) {
            sw.selectComboxVal(1);
        } else if (cType.equals("Centrala")) {
            sw.selectComboxVal(0);
        }
        sw.reload();

        //System.out.println("performing change");
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




        /*final double startWidth = 0.0;
        final double targetWidth = 250.0;
        final Animation hideSidebar = new Transition() {
            {
                setCycleDuration(Duration.millis(1000));
            }
            protected void interpolate(double frac) {
                final double curWidth = targetWidth * (1.0 - frac);
                sidebar.setPrefWidth(curWidth);
                System.out.println(curWidth);
            }
        };
        hideSidebar.onFinishedProperty().set(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                sidebar.setVisible(false);
                sidebartogglebtn.setText("Show");
            }
        });

        final Animation showSidebar = new Transition() {
            {
                setCycleDuration(Duration.millis(1000));
            }

            protected void interpolate(double frac) {
                final double curWidth = targetWidth * frac;
                sidebar.setPrefWidth(curWidth);
                System.out.println(curWidth);
            }
        };
        showSidebar.onFinishedProperty().set(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                sidebartogglebtn.setText("Hide");
                for (Node n:sidebar.getChildren()) {
                    n.setManaged(true);
                    n.setVisible(true);
                }
            }
        });

        if (showSidebar.statusProperty().get() == Animation.Status.STOPPED && hideSidebar.statusProperty().get() == Animation.Status.STOPPED) {
            if (sidebar.isVisible()) {
                hideSidebar.play();
                for (Node n:sidebar.getChildren()) {
                    n.setManaged(false);
                    n.setVisible(false);
                }
            } else {
                showSidebar.play();
                sidebar.setVisible(true);
            }
        }
            */
    }
}
