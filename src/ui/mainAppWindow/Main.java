package ui.mainAppWindow;

import db.DbCon;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import properties.Cfg;
import pwSafe.Crypt;
import ui.settingsWindow.PHolder;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;

public class Main extends Application {

    public void start(Stage primaryStage) throws Exception {
        MainWindow mw = new MainWindow();
        Parent root = mw.getFxmlLoader().getRoot();
        primaryStage.setTitle("Mailer");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> {
            System.exit(0);
        });
    }

    public static void main(String[] args) throws SQLException {
        try {
            Cfg.getInstance().init();
        } catch (IOException e) {
            e.printStackTrace();
        }

        launch(args);
    }



}

