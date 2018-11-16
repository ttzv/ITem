package window.statusBar;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

public class StatusBar {
    private Label statusLabel;
    private HBox statushBox;

    public StatusBar() {
        this.statusLabel = new Label ( "");
        this.statusLabel.setPadding(new Insets(0, 0, 0, 5));
        this.statushBox = new HBox();
        this.statushBox.getChildren().add(statusLabel);
        statushBox.getStylesheets().add(getClass().getResource("statusbar.css").toExternalForm());
        statushBox.getStyleClass().add("statusbar");
    }

    public void set (String text){
        this.statusLabel.setText(text);
    }

    public HBox getStatusBar(){
        return this.statushBox;
    }

    public void clear(){
        this.statusLabel.setText("");
    }
}
