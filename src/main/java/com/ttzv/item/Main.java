package com.ttzv.item;


import com.ttzv.item.dao.DbSession;
import com.ttzv.item.properties.Cfg;
import com.ttzv.item.ui.controller.MainWindowController;
import com.ttzv.item.uiUtils.DialogFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.service.spi.ServiceException;

import java.io.IOException;
import java.util.ResourceBundle;

public class Main extends Application {

    public void start(Stage primaryStage) throws IOException {

        initCfg();

        ResourceBundle langResourceBundle = ResourceBundle.getBundle("lang");
        //check if connection with database can be established, otherwise fall back to embedded db
        Session dbSession = null;
        try{
            dbSession = DbSession.openSession();
        } catch (ServiceException se){
            DialogFactory.showAlert(Alert.AlertType.ERROR, "No connection to database, application will use embedded database instead.");
            Cfg.getInstance().setProperty(Cfg.DB_EMBEDDED, "true");
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"), langResourceBundle);
        Parent root = loader.load();
        MainWindowController mainWindowController = loader.getController();
        mainWindowController.referenceRoot(root);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("ITem");

        DialogFactory.initFactory(scene.getWindow());
        mainWindowController.addPrimaryTableViewRightClickHandler();
        mainWindowController.loadTheme();

        primaryStage.show();
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

