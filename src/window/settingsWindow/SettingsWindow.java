package window.settingsWindow;

import file.ConfigHandler;
import file.Vals;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sender.Sender;

import java.util.ArrayList;

public class SettingsWindow {

    private Stage stage;
    private GridPane sceneGrid;
    private Sender sender;

    private TextField smtpHost;
    private TextField smtpTLS;
    private TextField smtpPort;
    private TextField senderLogin;
    private PasswordField senderPass;

    private ArrayList<Node> labelsList;
    private ArrayList<Node> inputsList;
    private ConfigHandler cfgh;

    public SettingsWindow(Sender sender, ConfigHandler cfgh){
        this.cfgh = cfgh;
        this.stage = new Stage();
        this.stage.setTitle("Settings");
        sceneGrid = new GridPane();
        sceneGrid.setPadding(new Insets(25, 25, 25, 25));
        sceneGrid.setHgap(10);
        System.out.println(sceneGrid.isResizable());
        this.stage.setScene(new Scene(sceneGrid));
        this.sender = sender;

        this.smtpHost = new TextField(sender.getSmtpHost());
        this.smtpTLS = new TextField(sender.getSmtpStartTLS());
        this.smtpPort = new TextField(sender.getSmtpPort());
        this.senderLogin = new TextField(sender.getSenderAddress());
        this.senderPass = new PasswordField();
        senderPass.setText(sender.getSenderPassword());

        this.inputsList = new ArrayList<>();
        this.labelsList = new ArrayList<>();
    }

    public Stage getStage(){
        buildStage();
        return this.stage;
    }

    private VBox labelsBox(){
        VBox vBox = new VBox();
        vBox.getChildren().addAll(
                new Label("SMTP Host:"),
                new Label("Use TLS"),
                new Label("SMTP Port"),
                new Label("Login"),
                new Label("Password")
        );
        this.labelsList.addAll(vBox.getChildren());
        return vBox;

    }

    private VBox inputsBox(){
        VBox vBox = new VBox();
        vBox.getChildren().addAll(
                smtpHost,
                smtpTLS,
                smtpPort,
                senderLogin,
                senderPass
                );
        this.inputsList.addAll(vBox.getChildren());
        return vBox;
    }

    private Button createSettBtn(){
        Button button = new Button("OK");
        button.setOnAction(event -> {
            updateSettings();
            this.stage.close();
        });
        return button;
    }

    public void buildStage(){
        inputsBox();
        labelsBox();

        for (int i = 0; i < labelsList.size(); i++) {
            this.sceneGrid.add(labelsList.get(i), 0, i);
        }
        for (int i = 0; i < inputsList.size(); i++) {
            this.sceneGrid.add(inputsList.get(i), 1, i);
        }

        this.sceneGrid.add(createSettBtn(), 0, labelsList.size());
    }


    public void updateSettings(){
        sender.setSmtpHost(this.smtpHost.getText());
        sender.setSmtpStartTLS(this.smtpTLS.getText());
        sender.setSmtpPort(this.smtpPort.getText());
        sender.setSenderAddress(this.senderLogin.getText());
        sender.setSenderPassword(this.senderPass.getText());

        cfgh.getProperties().put(Vals.SMTP_HOST.toString(), sender.getSmtpHost());
        cfgh.getProperties().put(Vals.SMTP_TLS.toString(), sender.getSmtpStartTLS());
        cfgh.getProperties().put(Vals.SMTP_PORT.toString(), sender.getSmtpPort());
        cfgh.getProperties().put(Vals.SMTP_LOGIN.toString(), sender.getSenderAddress());
        cfgh.getProperties().put(Vals.SMTP_PASS.toString(), sender.getSenderPassword());
    }
}
