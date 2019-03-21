package ui.mainAppWindow;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import properties.Cfg;
import uiUtils.UiObjectsWrapper;

import java.io.IOException;
import java.sql.SQLException;

public class Main extends Application {

    public void start(Stage primaryStage) throws Exception {
        UiObjectsWrapper uiObjectsWrapper = new UiObjectsWrapper();
        MainWindow mw = new MainWindow(uiObjectsWrapper);
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

