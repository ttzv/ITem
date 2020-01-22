package com.ttzv.item.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.ResourceBundle;

public class SmsScn extends AnchorPane {

    public SmsScn() {
        ResourceBundle langResourceBundle = ResourceBundle.getBundle("lang");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/smsscn.fxml"), langResourceBundle);
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
