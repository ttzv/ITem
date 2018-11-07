package window;
import file.Loader;
import file.MailMsgParser;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sender.Sender;
import window.controls.buttonControls.ButtonControls;
import window.inputFields.Inputs;
import window.menuBar.MsgMenuBar;
import window.msgTabPane.MsgTabPane;
import window.parsedMsgWindow.MessageWindow;
import window.parsedMsgWindow.MsgWindowUpdater;
import window.settingsWindow.SettingsWindow;
import window.utility.BorderedTitledPane.BorderedTitledPane;

import java.io.File;
import java.util.List;

public class MainWindow extends Application {

    private List<File> msgList;

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane borderPane = new BorderPane();
        primaryStage.setScene(new Scene(borderPane, 800, 600));
        Sender sender = new Sender();
        primaryStage.setTitle("Mailer");



        MsgMenuBar msgMenuBar = new MsgMenuBar();
        borderPane.setTop(msgMenuBar.getMenuBar());

        msgMenuBar.getItemFromMenu("Ustawienia", "Opcje").setOnAction(event -> {
            SettingsWindow settingsWindow = new SettingsWindow(sender);
            settingsWindow.getStage().show();
        });

        MsgTabPane msgTabPane = new MsgTabPane(); //todo: add parser here
        Inputs inputs = new Inputs();
        MessageWindow msgWindow = new MessageWindow();

        //msgWindow.setParsedTitle(mailMsgParser.getFlaggedTopic());

        msgMenuBar.getItemFromMenu("Dodaj", "Opcje").setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            msgList = fileChooser.showOpenMultipleDialog(primaryStage);
            msgTabPane.loadFileList(msgList);
            System.out.println("Loaded files" + msgTabPane.getFileArrayList());
            msgTabPane.buildTabPane();
        });


        msgWindow.addTabPane(msgTabPane.getTabPane());
        ButtonControls buttonControls = new ButtonControls();

        MsgWindowUpdater windowUpdater = new MsgWindowUpdater(msgWindow, inputs, buttonControls);//todo: deleto from here
        windowUpdater.bindHandlers();
        BorderedTitledPane msgWindowContainer = new BorderedTitledPane("Wiadomość", msgWindow);
        borderPane.setCenter(msgWindowContainer);

        VBox rightPaneVBox = new VBox();
        rightPaneVBox.getChildren().addAll(inputs, buttonControls);
        borderPane.setRight(rightPaneVBox);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}

