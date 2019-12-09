package com.ttzv.item.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class SmsScn extends AnchorPane {

    public SmsScn() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/smsscn.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
