package com.ttzv.item;


import com.ttzv.item.properties.Cfg;
import com.ttzv.item.ui.mainAppWindow.MainWindow;
import com.ttzv.item.uiUtils.UiObjectsWrapper;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class Main extends Application {

    public void start(Stage primaryStage) throws SQLException {
        initCfg();

        UiObjectsWrapper uiObjectsWrapper = new UiObjectsWrapper();
        MainWindow mw = new MainWindow(uiObjectsWrapper);
        Parent root = mw.getFxmlLoader().getRoot();
        primaryStage.setTitle(Cfg.getInstance().retrieveProp(Cfg.APP_NAME));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> System.exit(0));

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
}

