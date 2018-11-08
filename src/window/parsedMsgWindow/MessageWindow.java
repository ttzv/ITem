package window.parsedMsgWindow;


import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.web.WebView;
import window.utility.BorderedTitledPane.BorderedTitledPane;

import java.io.IOException;

public class MessageWindow extends VBox {

    private WebView webView;
    private String content;
    private Label parsedTitle;

    public MessageWindow() {

        parsedTitle = new Label();
        BorderedTitledPane topicBorderedLabel = new BorderedTitledPane("Temat", parsedTitle);
        this.setPadding(new Insets(15, 15, 15, 15));
        setManaged(true);

        this.getChildren().add(topicBorderedLabel);

    }

    public Label getParsedTitle() {
        return parsedTitle;
    }

    public void setParsedTitle(String title) {
        this.parsedTitle.setText(title);
    }

    public void addTabPane (TabPane tabPane){
        this.getChildren().add(tabPane);
    }
}
