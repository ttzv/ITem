package window.controls;

import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import window.parsedMsgWindow.MessageWindow;


public class ButtonControls extends VBox {

    private Button button;

    public ButtonControls() {
        button = new Button("DoSomething");
        // button.setPrefSize(50, 50);
        this.getChildren().add(button);
    }

    public Button getButton() {
        return button;
    }
}
