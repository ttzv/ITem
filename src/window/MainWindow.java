package window;
import file.Loader;
import file.MailMsgParser;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sender.Sender;
import window.controls.buttonControls.ButtonControls;
import window.infoWindow.InfoWindow;
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
        primaryStage.setTitle("Mailer");
        Sender sender = new Sender();

        MsgMenuBar msgMenuBar = new MsgMenuBar();
        borderPane.setTop(msgMenuBar.getMenuBar());

        msgMenuBar.getItemFromMenu("Ustawienia", "Opcje").setOnAction(event -> {
            SettingsWindow settingsWindow = new SettingsWindow(sender);
            settingsWindow.getStage().show();
        });
        msgMenuBar.getItemFromMenu("Informacje", "Inne").setOnAction(event -> {
            InfoWindow infoWindow = new InfoWindow();
            infoWindow.getStage().show();
        });

        MsgTabPane msgTabPane = new MsgTabPane();
        MessageWindow msgWindow = new MessageWindow();
        msgWindow.addTabPane(msgTabPane.getTabPane());

        Inputs inputs = new Inputs();
        ButtonControls buttonControls = new ButtonControls(msgTabPane, sender, inputs);
        MsgWindowUpdater windowUpdater = new MsgWindowUpdater(msgTabPane, inputs, buttonControls, msgWindow);
        windowUpdater.bindInputsHandlers();

        //msgWindow.setParsedTitle(mailMsgParser.getFlaggedTopic());
        msgMenuBar.getItemFromMenu("Dodaj", "Opcje").setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            msgList = fileChooser.showOpenMultipleDialog(primaryStage);
            /*for (File f : msgList){
                if(!msgList.contains(f)){
                    msgTabPane.loadFileList(msgList);
                }
            }*/
            if( !msgList.isEmpty() ) {
                msgTabPane.loadFileList(msgList);
                System.out.println("Loaded files" + msgTabPane.getFileArrayList());
                msgTabPane.buildTabPane();
                windowUpdater.bindTabHandlers();
                windowUpdater.update();
            }
        });


        BorderedTitledPane msgWindowContainer = new BorderedTitledPane("Wiadomość", msgWindow);
        borderPane.setCenter(msgWindowContainer);

        VBox rightPaneVBox = new VBox();
        rightPaneVBox.getChildren().addAll(inputs, buttonControls);
        BorderedTitledPane inputsPaneContainer = new BorderedTitledPane("Dane", rightPaneVBox);
        borderPane.setRight(inputsPaneContainer);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}

