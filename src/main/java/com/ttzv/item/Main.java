package com.ttzv.item;


import com.ttzv.item.dao.*;
import com.ttzv.item.entity.*;
import com.ttzv.item.properties.Cfg;
import com.ttzv.item.ui.mailerWindow.MailerWindow;
import com.ttzv.item.ui.mainAppWindow.MainWindow;
import com.ttzv.item.ui.mainAppWindow.SearchWindow;
import com.ttzv.item.ui.settingsWindow.SettingsWindow;
import com.ttzv.item.ui.signWindow.SignWindow;
import com.ttzv.item.uiUtils.UiObjectsWrapper;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javax.naming.NamingException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.SQLException;

public class Main extends Application {

    public void start(Stage primaryStage){
        initCfg();

        EntityDAO<User> entityDAOuserdb = null;
        EntityDAO<UserDetail> entityDAOuserdetdb = null;
        EntityDAO<Phone> entityDAOphonedb = null;
        EntityDAO<City> entityDAOcitydb = null;
        try {
            entityDAOuserdb = new UserDaoDatabaseImpl();
            entityDAOuserdetdb = new UserDetailDaoDatabaseImpl();
            entityDAOphonedb = new PhoneDaoDatabaseImpl();
            entityDAOcitydb = new CityDaoDatabaseImpl();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.WARNING,"Błąd połączenia z bazą danych \n" +
                    e);
        }

        UserHolder userHolder = null;
        try {
            userHolder = new UserHolder(entityDAOuserdb);
        } catch (SQLException | IOException | NamingException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.WARNING,"Błąd połączenia z bazą danych \n" +
                    e);
        }

        UserComboWrapper userComboWrapper = null;
        try {
            userComboWrapper = new UserComboWrapper(entityDAOcitydb, entityDAOphonedb, entityDAOuserdetdb);
        } catch (SQLException | IOException | NamingException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.WARNING,"Błąd połączenia z bazą danych \n" +
                    e);
        }


        UiObjectsWrapper uiObjectsWrapper = new UiObjectsWrapper();
        MailerWindow mailerWindow = new MailerWindow(uiObjectsWrapper, userHolder);
        SignWindow signWindow = new SignWindow(userHolder);
        SettingsWindow settingsWindow = new SettingsWindow(uiObjectsWrapper, userHolder);
        Pane smsScene = new Pane();//todo
        Pane clipboardScene = null;//todo
        SearchWindow searchWindow = new SearchWindow(uiObjectsWrapper, userHolder);

        //UserEdit userEdit = new UserEdit(uiObjectsWrapper);
        //CityEdit cityEdit = new CityEdit(uiObjectsWrapper);

        MainWindow mw = new MainWindow(uiObjectsWrapper, userHolder, userComboWrapper);

        mw.addSubScenes(mailerWindow, signWindow, smsScene, clipboardScene, settingsWindow); //todo: loadOnStart requires scene in Initialize, decouple somehow?

        Parent root = mw.getFxmlLoader().getRoot();
        primaryStage.setTitle(Cfg.getInstance().retrieveProp(Cfg.APP_NAME));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> System.exit(0));

        mw.updateMainWindowAssets();
        mw.loadOnStart();


        EntityDAO<User> entityDAOuserLdap = null;
        try {
            entityDAOuserLdap = new UserDaoLdapImpl();
        } catch (NamingException | UnknownHostException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Błąd połączenia z katalogiem LDAP \n" +
                    e);
            alert.showAndWait();
        }

        if(entityDAOuserLdap != null) {
            try {
                entityDAOuserdb.syncDataSourceWith(entityDAOuserLdap);
            } catch (SQLException | NamingException | IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.WARNING,e.toString());
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Baza użytkowników nie została zsynchronizowana z katalogiem");
            alert.showAndWait();
        }

    }

    public static void main(String[] args) {
        launch(args);

    }


    /*@Override
    public void init() throws Exception {
        super.init();
        Parameters parameters = getParameters();
        System.out.println("ParamsPassed: ");
        System.out.println(parameters.getNamed());
        String cfgDir = parameters.getNamed().get("cacheDir");
        try {
            Cfg.getInstance().init(cfgDir);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }*/

    private void initCfg() {
        try {
            Cfg.getInstance().init(null);
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    private void showAlert(Alert.AlertType alertType, String text){
        Alert alert = new Alert(alertType);
        alert.setContentText(text);
        alert.showAndWait();
    }

}

