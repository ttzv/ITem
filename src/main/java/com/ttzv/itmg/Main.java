package com.ttzv.itmg;


import com.ttzv.itmg.properties.Cfg;
import com.ttzv.itmg.ui.mainAppWindow.MainWindow;
import com.ttzv.itmg.uiUtils.UiObjectsWrapper;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public void start(Stage primaryStage) {
        initCfgWithParam();

        UiObjectsWrapper uiObjectsWrapper = new UiObjectsWrapper();
        MainWindow mw = new MainWindow(uiObjectsWrapper);
        Parent root = mw.getFxmlLoader().getRoot();
        primaryStage.setTitle("Mailer");
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

    private void initCfgWithParam() {
        Parameters parameters = getParameters();
        String cfgDir = parameters.getNamed().get("cacheDir");
        try {
            Cfg.getInstance().init(cfgDir);
        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}

