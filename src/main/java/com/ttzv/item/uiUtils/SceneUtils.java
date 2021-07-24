package com.ttzv.item.uiUtils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.ResourceBundle;

public class SceneUtils {

    public static void showWindow(String file, Modality modality, String title) throws IOException {
        ResourceBundle langResourceBundle = ResourceBundle.getBundle("lang");
        FXMLLoader loader = new FXMLLoader(SceneUtils.class.getResource(String.format("/fxml/%s.fxml", file)),langResourceBundle);
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.initModality(modality);
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.show();
    }

    public static Stage showWindow(String file) throws IOException {
        ResourceBundle langResourceBundle = ResourceBundle.getBundle("lang");
        FXMLLoader loader = new FXMLLoader(SceneUtils.class.getResource(String.format("/fxml/%s.fxml", file)),langResourceBundle);
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        return stage;
    }

    public static Stage getWaitWindow() throws IOException {
        Stage infoWindowStage = SceneUtils.showWindow("progressWindow");
        infoWindowStage.initModality(Modality.APPLICATION_MODAL);
        infoWindowStage.setTitle("Wait");
        infoWindowStage.initStyle(StageStyle.UNDECORATED);
        return infoWindowStage;
    }
}
