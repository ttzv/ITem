package ui.MailerWindow;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class MailerWindow extends AnchorPane {

    @FXML

    public Label lab1;

    @FXML
    private TextField txt1;

    @FXML
    private TextField txt2;


    public MailerWindow() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("mailerWindow.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        txt1.textProperty().bindBidirectional(txt2.textProperty());
    }

    public Label getLab1() {
        return lab1;
    }

    public void setLab1(Label lab1) {
        this.lab1.setText("abc");
    }


}
