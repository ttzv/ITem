package window.infoWindow;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class InfoWindow {

    private Stage stage;
    private GridPane gridPane;

    public InfoWindow (){
        this.stage = new Stage();
        this.stage.setTitle("Informacje");
        gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(1);
        //gridPane.setGridLinesVisible(true);
        gridPane.setPadding(new Insets(20, 20, 20, 20));
        stage.setScene(new Scene(gridPane));
    }

    public Stage getStage() {
        createInfo();
        return stage;
    }

    public void createInfo(){
        Label info = new Label("Dostępne flagi. Umieść w wiadomości html przed załadowaniem do programu.");
        Label loginLabel = new Label("Login");
        Label loginLabelContent = new Label("<!L></!L>");
        Label passLabel = new Label("Hasło");
        Label passLabelContent = new Label("<!P></!P>");
        Label topicLabel = new Label("Temat");
        Label topicLabelContent = new Label("<!T></!T>");
        gridPane.add(info, 0, 0, 2, 1);
        gridPane.add(loginLabel, 0, 1);
        gridPane.add(loginLabelContent, 1, 1);
        gridPane.add(passLabel, 0, 2);
        gridPane.add(passLabelContent, 1, 2);
        gridPane.add(topicLabel, 0, 3);
        gridPane.add(topicLabelContent, 1, 3);
    }
}
