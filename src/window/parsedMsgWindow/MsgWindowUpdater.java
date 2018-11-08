package window.parsedMsgWindow;

import file.MailMsgParser;
import javafx.scene.control.Tab;
import window.controls.buttonControls.ButtonControls;
import window.inputFields.Inputs;
import window.msgTabPane.MsgTabPane;

public class MsgWindowUpdater {

    private final Inputs inputs;
    private final ButtonControls buttonControls;
    private MsgTabPane msgTabPane;
    private MessageWindow messageWindow;

    public MsgWindowUpdater(MsgTabPane msgTabPane, Inputs inputs, ButtonControls buttonControls, MessageWindow messageWindow){
        this.inputs = inputs;
        this.buttonControls = buttonControls;
        this.msgTabPane = msgTabPane;
        this.messageWindow = messageWindow;
    }

    public void update(){
        MailMsgParser mailMsgParser = msgTabPane.getMsgParserOfSelectedTab();
        mailMsgParser.setFlaggedLogin(inputs.getLoginField().getText());
        System.out.println("loaded");
        mailMsgParser.setFlaggedPassword(inputs.getPassField().getText());
        mailMsgParser.reparse();
        msgTabPane.updateSelectedTabContext();
        messageWindow.setParsedTitle(mailMsgParser.getFlaggedTopic());
    }

    public void bindInputsHandlers(){
        this.inputs.getLoginField().textProperty().addListener((observable, oldValue, newValue) -> {
            update();
        });
        this.inputs.getPassField().textProperty().addListener((observable, oldValue, newValue) -> {
            update();
        });
    }

    public void bindTabHandlers(){
        for(Tab t : msgTabPane.getTabHashMap().values()){
            t.setOnSelectionChanged(event -> {
                if(t.isSelected()) {
                    System.out.println("selected " + t.getId());
                    update();
                }
            });
        }
    }
}
