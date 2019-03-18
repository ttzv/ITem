package ui.mainAppWindow;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class CityEdit extends AnchorPane{

    private Stage stage;

    public CityEdit() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("cityedit.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            AnchorPane root = fxmlLoader.load();
            stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));
        }catch(Exception e) {
            e.printStackTrace();
        }
        stage.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue){
                stage.close();
            }
        });

    }

    public void showAt(double x, double y){
        stage.setX(x);
        stage.setY(y);
        System.out.println("Opened at: " + x + " " + y);
        stage.show();
    }

    @FXML
    private void initialize(){

    }

    @FXML
    private TextField labCityPhone;

    @FXML
    private TextField labCityFax;

    @FXML
    private Label labelCityName;

    @FXML
    void btnSaveAndClose(ActionEvent event) {

    }
}
