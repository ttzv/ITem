package ui.gSuiteWindow;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class GSuiteWindow extends AnchorPane {

    public GSuiteWindow() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("gswdw.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
