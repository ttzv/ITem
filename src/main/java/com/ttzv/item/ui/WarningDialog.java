package com.ttzv.item.ui;

import javafx.scene.control.Alert;

public class WarningDialog {

    public WarningDialog() {
    }

    public static void showAlert(Alert.AlertType alertType, String text) {
        Alert alert = new Alert(alertType);
        alert.setContentText(text);
        alert.showAndWait();
    }
}
