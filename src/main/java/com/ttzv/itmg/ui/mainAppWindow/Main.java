package com.ttzv.itmg.ui.mainAppWindow;

import com.sun.javafx.application.ParametersImpl;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.ttzv.itmg.properties.Cfg;
import com.ttzv.itmg.uiUtils.UiObjectsWrapper;

import javax.xml.bind.SchemaOutputResolver;
import java.io.IOException;
import java.sql.SQLException;

public class Main extends Application {

    public void start(Stage primaryStage) {
        initCfgWithParam();

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

    public static void main(String[] args) {
        for (String a : args
        ) {
            System.out.println(a);

        }
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

