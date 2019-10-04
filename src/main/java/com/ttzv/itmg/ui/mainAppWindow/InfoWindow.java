/**
 * Sample Skeleton for 'infoWindow.fxml' Controller Class
 */

package com.ttzv.itmg.ui.mainAppWindow;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

public class InfoWindow {

    @FXML // fx:id="l1"
    private Label l1; // Value injected by FXMLLoader

    @FXML // fx:id="l2"
    private Label l2; // Value injected by FXMLLoader

    @FXML // fx:id="l3"
    private Label l3; // Value injected by FXMLLoader

    @FXML // fx:id="b1"
    private Button b1; // Value injected by FXMLLoader

    @FXML // fx:id="b2"
    private Button b2; // Value injected by FXMLLoader

    @FXML // fx:id="b3"
    private Button b3; // Value injected by FXMLLoader

    public InfoWindow() {
    }

    public void initialize () {
        textToClipboard(b1, l1);
        textToClipboard(b2, l2);
        textToClipboard(b3, l3);
    }

    private void textToClipboard(Button b, Label l){
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent clipboardContent = new ClipboardContent();
        b.setOnAction(actionEvent -> {
            clipboardContent.putString(l.getText());
            clipboard.setContent(clipboardContent);
        });

    }
}
