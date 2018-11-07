package window.inputFields;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import window.parsedMsgWindow.MessageWindow;

public class Inputs extends GridPane {

    private TextField loginField;
    private TextField passField;
    private MessageWindow messageWindow;

    public Inputs() {
        Label labelLogin = new Label("Login: ");
        loginField = new TextField();

        Label labelPass = new Label("Pass: ");
        passField = new TextField();

        setHgap(5);
        setPadding(new Insets(10, 5, 10, 5));
        add(labelLogin, 0, 0);
        add(loginField, 1, 0);
        add(labelPass, 0, 1);
        add(passField, 1, 1);

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