package com.ttzv.item.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class SmartClipboarsdScn extends AnchorPane {

    public SmartClipboarsdScn() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/smartclipscn.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
