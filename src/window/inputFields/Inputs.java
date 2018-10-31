package window.inputFields;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import window.parsedMsgWindow.MessageWindow;

public class Inputs extends VBox {

    private TextField loginField;
    private TextField passField;
    private MessageWindow messageWindow;

    public Inputs() {
        this.messageWindow = messageWindow;
        HBox hBoxLogin = new HBox();
        Label labelLogin = new Label("Login: ");
        loginField = new TextField();
        hBoxLogin.getChildren().addAll(labelLogin, loginField);

        HBox hBoxPass = new HBox();
        Label labelPass = new Label("Pass: ");
        passField = new TextField();
        hBoxPass.getChildren().addAll(labelPass, passField);

        this.getChildren().addAll(hBoxLogin, hBoxPass);

    }

    public TextField getLoginField() {
        return loginField;
    }

    public void setLoginField(TextField loginField) {
        this.loginField = loginField;
    }

    public TextField getPassField() {
        return passField;
    }

    public void setPassField(TextField passField) {
        this.passField = passField;
    }

   /* public void addRefresher(TextField textField){
        textField.setOnAction(event -> {
            messageWindow.updateWindowContent();
        });
       }
    */
}