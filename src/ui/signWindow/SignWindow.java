package ui.signWindow;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class SignWindow extends AnchorPane {
    public SignWindow() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("signwdw.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize(){
    }


    @FXML
    private WebView webViewSignature;
    @FXML
    private TextField htmlLine;

    @FXML
    private void load(){
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        //this.webViewSignature.getEngine().load(file.toURI().toString());
        this.webViewSignature.getEngine().loadContent("<img src=\"http://file.atal.pl/stopka/mail_stopka.png\">");
    }

    @FXML
    private void gethtml() {
        String html = (String) webViewSignature.getEngine().executeScript("document.documentElement.outerHTML");
        System.out.println("html:" + html);
    }

    @FXML
    private void loadLine() {
        this.webViewSignature.getEngine().loadContent(this.htmlLine.getText());
    }


}
