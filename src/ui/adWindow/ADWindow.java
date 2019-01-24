package ui.adWindow;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class ADWindow extends AnchorPane {
    public ADWindow() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("adwdw.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
