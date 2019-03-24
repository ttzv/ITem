package com.ttzv.itmg.ui.crmWindow;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class CrmWindow extends AnchorPane {
    public CrmWindow() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/crmwdw.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
