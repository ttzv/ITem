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
    
    private Label parsedTitle;
    private Label inputtedReceiverAddress;
    private String inputDomain;
    private String inputAddress;

    public MessageWindow() {

        this.setPadding(new Insets(15, 15, 15, 15));

        inputDomain = "";
        inputAddress = "";

        HBox hBoxHeadInfo = new HBox();
        parsedTitle = new Label();
        BorderedTitledPane topicBorderedLabel = new BorderedTitledPane("Temat", parsedTitle);
        topicBorderedLabel.setPadding(new Insets(0, 10, 0, 10));


        inputtedReceiverAddress = new Label();
        BorderedTitledPane addressBorderedLabel = new BorderedTitledPane("Odbiorca", inputtedReceiverAddress);
        addressBorderedLabel.setPadding(new Insets(0, 10, 0, 10));
        
        hBoxHeadInfo.getChildren().addAll(topicBorderedLabel, addressBorderedLabel);

        this.getChildren().add(hBoxHeadInfo);

    }

    public Label getParsedTitle() {
        return parsedTitle;
    }

    public void setParsedTitle(String title) {
        this.parsedTitle.setText(title);
    }

    public void setInputAddress(String inputAddress) {
        this.inputAddress = inputAddress;
    }

    public void setInputDomain(String inputDomain) {
        this.inputDomain = inputDomain;
    }
    
    public void refreshReceiverAddress(){
        this.inputtedReceiverAddress.setText(this.inputAddress + this.inputDomain);
    }

    public Label getInputtedReceiverAddress() {
        return inputtedReceiverAddress;
    }

    public void addTabPane (TabPane tabPane){
        this.getChildren().add(tabPane);
    }
}
