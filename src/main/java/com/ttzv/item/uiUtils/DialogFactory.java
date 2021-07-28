package com.ttzv.item.uiUtils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import java.io.IOException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class DialogFactory {

    private Window ownerWindow;
    private static DialogFactory dialogFactory;

    private DialogFactory (Window ownerWindow) {
        this.ownerWindow = ownerWindow;
    }

    public static void initFactory(Window window) {
        if(dialogFactory == null){
            dialogFactory = new DialogFactory(window);
        }
    }

    public static void showWindow(String file, Modality modality, String title) throws IOException {
        ResourceBundle langResourceBundle = ResourceBundle.getBundle("lang");
        FXMLLoader loader = new FXMLLoader(DialogFactory.class.getResource(String.format("/fxml/%s.fxml", file)),langResourceBundle);
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.initOwner(dialogFactory.ownerWindow);
        stage.initModality(modality);
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.show();
    }

    public static Stage showWindow(String file) throws IOException {
        ResourceBundle langResourceBundle = ResourceBundle.getBundle("lang");
        FXMLLoader loader = new FXMLLoader(DialogFactory.class.getResource(String.format("/fxml/%s.fxml", file)),langResourceBundle);
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.initOwner(dialogFactory.ownerWindow);
        stage.setScene(new Scene(root));
        return stage;
    }

    public static void showAlert(Alert.AlertType alertType, String s) {
        Alert alert = new Alert(alertType);
        alert.initOwner(dialogFactory.ownerWindow);
        alert.setContentText(s);
        alert.showAndWait();
    }

    public static Alert buildAlert(Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.initOwner(dialogFactory.ownerWindow);
        return alert;
    }

    public static Stage getWaitWindow() throws IOException {
        Stage infoWindowStage = DialogFactory.showWindow("progressWindow");
        infoWindowStage.initOwner(dialogFactory.ownerWindow);
        infoWindowStage.initModality(Modality.APPLICATION_MODAL);
        infoWindowStage.setTitle("Wait");
        infoWindowStage.initStyle(StageStyle.UNDECORATED);
        return infoWindowStage;
    }

    public static <T> Dialog<T> buildDialog() {
        Dialog<T> dialog = new Dialog<>();
        dialog.initOwner(dialogFactory.ownerWindow);
        return dialog;
    }

    public static String textInputDialog(String title, String label){
        TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.initOwner(dialogFactory.ownerWindow);
        textInputDialog.setTitle(title);
        textInputDialog.setHeaderText(null);
        textInputDialog.setContentText(label);
        TextField textField = new TextField();
        textInputDialog.getDialogPane().getChildren().add(textField);
        Node okBtn = textInputDialog.getDialogPane().lookupButton(ButtonType.OK);
        okBtn.setDisable(true);
        textInputDialog.getEditor().textProperty().addListener((observable, oldValue, newValue) ->
                okBtn.setDisable(!newValue.matches("\\w+\\.\\w+"))
        );
        Optional<String> result = textInputDialog.showAndWait();
        if(result.isPresent()){
            return textInputDialog.getEditor().getText();
        }
        return null;
    }
}
