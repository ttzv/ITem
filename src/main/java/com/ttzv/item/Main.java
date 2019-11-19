package com.ttzv.item;


import com.ttzv.item.dao.*;
import com.ttzv.item.entity.*;
import com.ttzv.item.properties.Cfg;
import com.ttzv.item.ui.mailerWindow.MailerWindow;
import com.ttzv.item.ui.mainAppWindow.MainWindow;
import com.ttzv.item.ui.mainAppWindow.SearchWindow;
import com.ttzv.item.ui.mainAppWindow.popups.CityEdit;
import com.ttzv.item.ui.mainAppWindow.popups.UserEdit;
import com.ttzv.item.ui.settingsWindow.SettingsWindow;
import com.ttzv.item.ui.signWindow.SignWindow;
import com.ttzv.item.uiUtils.UiObjectsWrapper;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.naming.NamingException;
import java.io.IOException;
import java.sql.SQLException;

public class Main extends Application {

    public void start(Stage primaryStage) throws SQLException, NamingException, IOException {
        initCfg();

        EntityDAO<User> entityDAOuserdb = new UserDaoDatabaseImpl();
        EntityDAO<UserDetail> entityDAOuserdetdb = new UserDetailDaoDatabaseImpl();
        EntityDAO<Phone> entityDAOphonedb = new PhoneDaoDatabaseImpl();
        EntityDAO<City> entityDAOcitydb = new CityDaoDatabaseImpl();
        //EntityDAO<User> entityDAOuserLdap = new UserDaoLdapImpl();

        UserHolder userHolder = new UserHolder(entityDAOuserdb);

        UserComboWrapper userComboWrapper = new UserComboWrapper(entityDAOcitydb, entityDAOphonedb, entityDAOuserdetdb);


        UiObjectsWrapper uiObjectsWrapper = new UiObjectsWrapper();
        MailerWindow mailerWindow = new MailerWindow(uiObjectsWrapper, userHolder);
        SignWindow signWindow = new SignWindow(userHolder);
        SettingsWindow settingsWindow = new SettingsWindow(uiObjectsWrapper, userHolder);
        SearchWindow searchWindow = new SearchWindow(uiObjectsWrapper, userHolder);
        //UserEdit userEdit = new UserEdit(uiObjectsWrapper);
        //CityEdit cityEdit = new CityEdit(uiObjectsWrapper);

        MainWindow mw = new MainWindow(uiObjectsWrapper, userHolder, userComboWrapper);
        mw.addSubScenes(mailerWindow, signWindow, settingsWindow);
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

