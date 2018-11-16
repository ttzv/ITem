package window.controls.buttonControls;

import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import jdk.net.SocketFlow;
import sender.Sender;
import window.inputFields.Inputs;
import window.msgTabPane.MsgTabPane;
import window.parsedMsgWindow.MessageWindow;
import window.statusBar.StatusBar;

import javax.mail.MessagingException;

public class ButtonControls extends VBox {

    private final MsgTabPane msgTabPane;
    private final Sender sender;
    private Inputs inputs;
    private StatusBar statusBar;
    private Button button;

    public ButtonControls(MsgTabPane msgTabPane, Sender sender, Inputs inputs, StatusBar statusBar) {
        this.msgTabPane = msgTabPane;
        this.sender = sender;
        this.inputs = inputs;
        this.statusBar = statusBar;
        button = new Button("Wyślij");
        // button.setPrefSize(50, 50);
        this.getChildren().add(button);


        setButtonActions();
    }


    public void setButtonActions(){

        this.button.setOnAction(event -> {
            sender.initSession();
            sender.setMsgSubject(msgTabPane.getMsgParserOfSelectedTab().getFlaggedTopic());
            String address = inputs.getFinalRecAddress();
            sender.setReceiverAddress(address);
            System.out.println("msg will be sent to " + address);
            System.out.println("Topic loaded...");
            //System.out.println(msgTabPane.getMsgParserOfSelectedTab().getOutputString());
            try {
                sender.setMsg(msgTabPane.getMsgParserOfSelectedTab().getOutputString());
            } catch (MessagingException me){
                me.printStackTrace();
                System.out.println("failure");
            }
            String val = sender.validate();
            if(!val.equals("ok")) {
                System.out.println(val);
                statusBar.set(val);
            } else {
                sender.sendMail();
                statusBar.set("Wysłano do " + address);
                System.out.println("message sent");
            }
        });


    }
}

