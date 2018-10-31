package window.parsedMsgWindow;

import file.Parser;
import window.controls.ButtonControls;
import window.inputFields.Inputs;

public class MsgWindowUpdater {

    private Parser parser;
    private MessageWindow messageWindow;
    private final Inputs inputs;
    private final ButtonControls buttonControls;

    public MsgWindowUpdater(Parser parser, MessageWindow messageWindow , Inputs inputs, ButtonControls buttonControls){
        this.parser = parser;
        this.messageWindow = messageWindow;
        this.inputs = inputs;
        this.buttonControls = buttonControls;

    }

    public void update(){
        parser.setFlaggedLogin(inputs.getLoginField().getText());
        parser.setFlaggedPassword(inputs.getPassField().getText());
        parser.reparse();
        messageWindow.updateWindowContent(parser.getOutputString());
    }

    public void bindHandlers(){
        this.inputs.getLoginField().textProperty().addListener((observable, oldValue, newValue) -> {
            update();
        });
        this.inputs.getPassField().textProperty().addListener((observable, oldValue, newValue) -> {
            update();
        });
        this.buttonControls.getButton().setOnAction(event -> {
            update();
        });
    }
}
