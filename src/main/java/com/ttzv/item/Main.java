package com.ttzv.item;


import com.ttzv.item.properties.Cfg;
import com.ttzv.item.ui.controller.MainWindowController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;

public class Main extends Application {

    public void start(Stage primaryStage) throws IOException {

        initCfg();

        ResourceBundle langResourceBundle = ResourceBundle.getBundle("lang");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"), langResourceBundle);
        Parent root = loader.load();
        MainWindowController mainWindowController = loader.getController();
        mainWindowController.referenceRoot(root);
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("taikutsu");
        primaryStage.show();

//
//
//        EntityDAO<User> entityDAOuserdb = null;
//        EntityDAO<UserDetail> entityDAOuserdetdb = null;
//        EntityDAO<Phone> entityDAOphonedb = null;
//        EntityDAO<City> entityDAOcitydb = null;
//        EntityDAO<CommandItem> entityDAOcmddb = null;
//
//        try {
//            entityDAOuserdb = new UserDaoDatabaseImpl();
//            entityDAOuserdetdb = new UserDetailDaoDatabaseImpl();
//            entityDAOphonedb = new PhoneDaoDatabaseImpl();
//            entityDAOcitydb = new CityDaoDatabaseImpl();
//            entityDAOcmddb = new CommandBoxDatabaseImpl();
//        } catch (SQLException | IOException | GeneralSecurityException e) {
//            e.printStackTrace();
//            WarningDialog.showAlert(Alert.AlertType.WARNING,"Błąd połączenia ze źródłem danych \n" +
//                    e);
//        }
//
//        EntityDAO<User> entityDAOuserLdap = null;
//        try {
//            entityDAOuserLdap = new UserDaoLdapImpl();
//        } catch (NamingException | IOException | GeneralSecurityException e) {
//            e.printStackTrace();
//            Alert alert = new Alert(Alert.AlertType.WARNING);
//            alert.setContentText("Błąd połączenia z katalogiem LDAP \n" +
//                    e);
//            alert.showAndWait();
//        }
//
//        if(entityDAOuserLdap != null) {
//            try {
//                if (entityDAOuserdb != null)
//                    entityDAOuserdb.syncDataSourceWith(entityDAOuserLdap);
//            } catch (SQLException | NamingException | IOException | GeneralSecurityException e) {
//                e.printStackTrace();
//                WarningDialog.showAlert(Alert.AlertType.WARNING, "Błąd synchronizacji\n" +
//                        e);
//            }
//        } else {
//            Alert alert = new Alert(Alert.AlertType.WARNING);
//            alert.setContentText("Baza użytkowników nie została zsynchronizowana z katalogiem");
//            alert.showAndWait();
//        }
//
//
//
//        UserHolder userHolder = null;
//        try {
//            //userHolder stores only User data (immutable, atleast at the moment)
//            userHolder = new UserHolder(entityDAOuserdb);
//        } catch (SQLException | IOException | NamingException | GeneralSecurityException e) {
//            e.printStackTrace();
//            WarningDialog.showAlert(Alert.AlertType.WARNING,"Błąd połączenia z bazą danych \n" +
//                    e);
//        }
//
//        UserComboWrapper userComboWrapper = null;
//        try {
//            //userComboWrapper stores all supporting data for User entity
//            userComboWrapper = new UserComboWrapper(entityDAOcitydb, entityDAOphonedb, entityDAOuserdetdb);
//        } catch (SQLException | IOException | NamingException | GeneralSecurityException e) {
//            e.printStackTrace();
//            WarningDialog.showAlert(Alert.AlertType.WARNING,"Błąd połączenia z bazą danych \n" +
//                    e);
//        }
//
//        com.ttzv.item.entity.CommandBox commandBox = null;
//
//        try {
//            commandBox = new com.ttzv.item.entity.CommandBox(entityDAOcmddb);
//        } catch (SQLException | NamingException | GeneralSecurityException | IOException e) {
//            e.printStackTrace();
//            WarningDialog.showAlert(Alert.AlertType.WARNING,"Błąd połączenia z bazą danych \n" +
//                    e);
//        }
//
//        UiObjectsWrapper uiObjectsWrapper = new UiObjectsWrapper();
//        MailerController mailerController = new MailerController(uiObjectsWrapper, userHolder, userComboWrapper);
//        SignaturesController signaturesController = new SignaturesController(userHolder);
//        SettingsController settingsController = new SettingsController(uiObjectsWrapper, userHolder);
//        SmsController smsScene = new SmsController(userHolder, userComboWrapper);
//        CommandBoxController commandBoxControllerScn = new CommandBoxController(commandBox);
//
//        MainWindowController mw = new MainWindowController(uiObjectsWrapper, userHolder, userComboWrapper, primaryStage);
//        mw.addSubScenes(mailerController, signaturesController, smsScene, commandBoxControllerScn, settingsController);
//
//        Parent root = mw.getFxmlLoader().getRoot();
//        primaryStage.setTitle(Cfg.getInstance().retrieveProp(Cfg.APP_NAME));
//        primaryStage.setScene(new Scene(root));
//
//        primaryStage.setOnCloseRequest(event -> System.exit(0));
//
//        mw.updateMainWindowAssets();
//        mw.loadOnStart();
//

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



}

