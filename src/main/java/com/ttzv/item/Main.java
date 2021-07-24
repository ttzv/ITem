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

