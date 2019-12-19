package com.ttzv.item;


import com.ttzv.item.dao.*;
import com.ttzv.item.entity.*;
import com.ttzv.item.properties.Cfg;
import com.ttzv.item.ui.*;
import com.ttzv.item.uiUtils.UiObjectsWrapper;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.naming.NamingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;

public class Main extends Application {

    public void start(Stage primaryStage){
        initCfg();

        EntityDAO<User> entityDAOuserdb = null;
        EntityDAO<UserDetail> entityDAOuserdetdb = null;
        EntityDAO<Phone> entityDAOphonedb = null;
        EntityDAO<City> entityDAOcitydb = null;
        EntityDAO<CommandItem> entityDAOcmddb = null;

        try {
            entityDAOuserdb = new UserDaoDatabaseImpl();
            entityDAOuserdetdb = new UserDetailDaoDatabaseImpl();
            entityDAOphonedb = new PhoneDaoDatabaseImpl();
            entityDAOcitydb = new CityDaoDatabaseImpl();
            entityDAOcmddb = new CommandBoxDatabaseImpl();
        } catch (SQLException | IOException | GeneralSecurityException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.WARNING,"Błąd połączenia ze źródłem danych \n" +
                    e);
        }

        EntityDAO<User> entityDAOuserLdap = null;
        try {
            entityDAOuserLdap = new UserDaoLdapImpl();
        } catch (NamingException | IOException | GeneralSecurityException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Błąd połączenia z katalogiem LDAP \n" +
                    e);
            alert.showAndWait();
        }

        if(entityDAOuserLdap != null) {
            try {
                if (entityDAOuserdb != null)
                    entityDAOuserdb.syncDataSourceWith(entityDAOuserLdap);
            } catch (SQLException | NamingException | IOException | GeneralSecurityException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.WARNING, "Błąd synchronizacji\n" +
                        e);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Baza użytkowników nie została zsynchronizowana z katalogiem");
            alert.showAndWait();
        }



        UserHolder userHolder = null;
        try {
            //userHolder stores only User data (immutable, atleast at the moment)
            userHolder = new UserHolder(entityDAOuserdb);
        } catch (SQLException | IOException | NamingException | GeneralSecurityException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.WARNING,"Błąd połączenia z bazą danych \n" +
                    e);
        }

        UserComboWrapper userComboWrapper = null;
        try {
            //userComboWrapper stores all supporting data for User entity
            userComboWrapper = new UserComboWrapper(entityDAOcitydb, entityDAOphonedb, entityDAOuserdetdb);
        } catch (SQLException | IOException | NamingException | GeneralSecurityException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.WARNING,"Błąd połączenia z bazą danych \n" +
                    e);
        }

        CommandBox commandBox = null;

        try {
            commandBox = new CommandBox(entityDAOcmddb);
        } catch (SQLException | NamingException | GeneralSecurityException | IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.WARNING,"Błąd połączenia z bazą danych \n" +
                    e);
        }

        UiObjectsWrapper uiObjectsWrapper = new UiObjectsWrapper();
        MailerWindow mailerWindow = new MailerWindow(uiObjectsWrapper, userHolder, userComboWrapper);
        SignWindow signWindow = new SignWindow(userHolder);
        SettingsWindow settingsWindow = new SettingsWindow(uiObjectsWrapper, userHolder);
        SmsScn smsScene = new SmsScn();
        CommandBoxScn commandBoxScn = new CommandBoxScn(commandBox);

        MainWindow mw = new MainWindow(uiObjectsWrapper, userHolder, userComboWrapper, primaryStage);
        mw.addSubScenes(mailerWindow, signWindow, smsScene, commandBoxScn, settingsWindow);

        Parent root = mw.getFxmlLoader().getRoot();
        primaryStage.setTitle(Cfg.getInstance().retrieveProp(Cfg.APP_NAME));
        primaryStage.setScene(new Scene(root));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> System.exit(0));

        mw.updateMainWindowAssets();
        mw.loadOnStart();


    }

    public static void main(String[] args) {
        launch(args);

    }

    private void initCfg() {
        try {
            Cfg.getInstance().init(null);
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    private void showAlert(Alert.AlertType alertType, String text) {
        Alert alert = new Alert(alertType);
        alert.setContentText(text);
        alert.showAndWait();
    }

}

