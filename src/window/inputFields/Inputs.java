package window.inputFields;

import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class Inputs extends GridPane {

    private TextField loginField;
    private TextField passField;
    private TextField userField;
    private ComboBox<String> userDomaincBox;

    private String finalRecAddress;

    public Inputs() {
        finalRecAddress = "";

        Label labelLogin = new Label("Login");
        loginField = new TextField();

        Label labelPass = new Label("Pass");
        passField = new TextField();

        Label labelUser = new Label("User");
        userField = new TextField();

        Label labelDomain = new Label("Domena");
        userDomaincBox = new ComboBox<>();
        userDomaincBox.getItems().add("@atal.pl");
        userDomaincBox.getItems().add("@gmail.com");
        userDomaincBox.getSelectionModel().selectFirst();

        setHgap(10);
        setVgap(5);
        setPadding(new Insets(10, 5, 10, 5));

        add(labelUser, 0, 0);
        add(userField, 1, 0);
        add(new Separator(), 0, 1, 2, 1);
        add(labelLogin, 0, 2);
        add(loginField, 1, 2);
        add(labelPass, 0, 3);
        add(passField, 1, 3);
        add(labelDomain, 0, 4);
        add(userDomaincBox, 1, 4);

    }

    public TextField getLoginField() {
        return loginField;
    }

    public void setLoginField(String loginField) {
        this.loginField.setText(loginField);
    }

    public TextField getPassField() {
        return passField;
    }

    public void setPassField(String passField) {
        this.passField.setText(passField);
    }

    public TextField getUserField() {
        return userField;
    }

    public void setUserField(TextField userField) {
        this.userField = userField;
    }

    public ComboBox<String> getUserDomaincBox() {
        return userDomaincBox;
    }

    public String getFinalRecAddress() {
        return finalRecAddress;
    }

    public void setFinalRecAddress(String finalRecAddress) {
        this.finalRecAddress = finalRecAddress;
    }

    /* public void addRefresher(TextField textField){
        textField.setOnAction(event -> {
            messageWindow.updateWindowContent();
        });
       }
    */
}