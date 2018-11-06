package window.parsedMsgWindow;

import file.MailMsgParser;
import window.controls.ButtonControls;
import window.inputFields.Inputs;

public class MsgWindowUpdater {

    private MailMsgParser mailMsgParser;
    private MessageWindow messageWindow;
    private final Inputs inputs;
    private final ButtonControls buttonControls;

    public MsgWindowUpdater(MailMsgParser mailMsgParser, MessageWindow messageWindow , Inputs inputs, ButtonControls buttonControls){
        this.mailMsgParser = mailMsgParser;
        this.messageWindow = messageWindow;
        this.inputs = inputs;
        this.buttonControls = buttonControls;

    }

    public void update(){
        mailMsgParser.setFlaggedLogin(inputs.getLoginField().getText());
        mailMsgParser.setFlaggedPassword(inputs.getPassField().getText());
        mailMsgParser.reparse();
        messageWindow.updateWindowContent(mailMsgParser.getOutputString());
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
