package window;
import file.Loader;
import file.MailMsgParser;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sender.Sender;
import window.controls.ButtonControls;
import window.controls.menuBar.MenuBarControl;
import window.inputFields.Inputs;
import window.parsedMsgWindow.MessageWindow;
import window.parsedMsgWindow.MsgWindowUpdater;
import window.settingsWindow.SettingsWindow;

public class MainWindow extends Application {



    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane borderPane = new BorderPane();
        primaryStage.setScene(new Scene(borderPane, 800, 600));
        Sender sender = new Sender();
        primaryStage.setTitle("Mailer");

        MenuBarControl menuBarControl = new MenuBarControl();
        borderPane.setTop(menuBarControl.getMenuBar());
        menuBarControl.getItemFromMenu("Settings", "Options").setOnAction(event -> {
            SettingsWindow settingsWindow = new SettingsWindow(sender);
            settingsWindow.getStage().show();
        });
        Loader loader = new Loader("powitanie.html");
        MailMsgParser mailMsgParser = new MailMsgParser(loader.readContent());
        Inputs inputs = new Inputs();
        MessageWindow msgWindow = new MessageWindow(mailMsgParser.getOutputString());
        ButtonControls buttonControls = new ButtonControls();
        MsgWindowUpdater windowUpdater = new MsgWindowUpdater(mailMsgParser, msgWindow, inputs, buttonControls);
        windowUpdater.bindHandlers();

        borderPane.setCenter(msgWindow);

        VBox rightPaneVBox = new VBox();
        rightPaneVBox.getChildren().addAll(inputs, buttonControls);
        borderPane.setRight(rightPaneVBox);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
