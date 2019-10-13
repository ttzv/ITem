package com.ttzv.item.ui.dbToolsWindow;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class DbTools {

    private Stage stage;

    public DbTools() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/DbTools.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.load();
        Parent root = fxmlLoader.getRoot();
        this.stage = new Stage();
        this.stage.setTitle("Narzędzia DB");
        this.stage.setScene(new Scene(root));
    }

    public Stage getStage() {
        return stage;
    }

}
