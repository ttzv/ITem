package window;

import file.ConfigHandler;
import file.Vals;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
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
import window.statusBar.StatusBar;
import utility.BorderedTitledPane.BorderedTitledPane;
import utility.Utility;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainWindow extends Application {

    private List<File> msgList;

    @Override
    public void start(Stage primaryStage) throws Exception {

        BorderPane borderPane = new BorderPane();
        primaryStage.setScene(new Scene(borderPane, 800, 600));
        primaryStage.setTitle("Mailer");

        StatusBar statusBar = new StatusBar();

        borderPane.setBottom(statusBar.getStatusBar());

        //File icoFile = new File( "img" + Utility.fileSeparator + "atl.png");

        Image scnIco = new Image("img/atl.png");
        primaryStage.getIcons().add(scnIco);
        Sender sender = new Sender();

        ConfigHandler configHandler = new ConfigHandler(sender);
        configHandler.firstRun();

        primaryStage.setOnCloseRequest(event -> {
            try {
                configHandler.saveCustomProperties();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        MsgMenuBar msgMenuBar = new MsgMenuBar();
        borderPane.setTop(msgMenuBar.getMenuBar());

        msgMenuBar.getItemFromMenu("Ustawienia", "Opcje").setOnAction(event -> {
            SettingsWindow settingsWindow = new SettingsWindow(sender, configHandler);
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
        ButtonControls buttonControls = new ButtonControls(msgTabPane, sender, inputs, statusBar);
        MsgWindowUpdater windowUpdater = new MsgWindowUpdater(msgTabPane, inputs, buttonControls, msgWindow);
        windowUpdater.bindInputsHandlers();



        if(configHandler.getProperties().containsKey(Vals.LIST_MSG.toString()) && configHandler.getProperties().containsKey(Vals.LOCATION_MSG.toString())){
           msgList = new ArrayList<>();
           ArrayList<String> fileNamesList = Utility.stringToArray(configHandler.getProperties().getProperty(Vals.LIST_MSG.toString()));
           String parentDirPath = configHandler.getProperties().getProperty(Vals.LOCATION_MSG.toString());
           File parentDir = new File(parentDirPath);
           String fileSeparator = System.getProperty("file.separator");

           if(parentDir.exists()) {
               for (String s : fileNamesList) {
                   File msg = new File (parentDirPath + fileSeparator + s);
                   if(msg.exists()) {
                       msgList.add(msg);
                   } else {
                       System.out.println(s + " does not exist ");
                   }
               }
               msgTabPane.loadFileList(msgList);
               System.out.println("Loaded files" + msgTabPane.getFileArrayList());
               msgTabPane.buildTabPane();
               windowUpdater.bindTabHandlers();
               windowUpdater.update();
           }
        }


        msgMenuBar.getItemFromMenu("Dodaj", "Opcje").setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            if(configHandler.getProperties().containsKey(Vals.LOCATION_MSG.toString())) {
                if (!configHandler.getProperties().getProperty(Vals.LOCATION_MSG.toString()).isEmpty()) {
                    String parentDirPath = configHandler.getProperties().getProperty(Vals.LOCATION_MSG.toString());
                    File parentDir = new File(parentDirPath);
                    if(parentDir.exists()) {
                        fileChooser.setInitialDirectory(new File(configHandler.getProperties().getProperty(Vals.LOCATION_MSG.toString())));
                    }
                }
            }
            msgList = fileChooser.showOpenMultipleDialog(primaryStage);

            System.out.println(msgList);
            if( !msgList.isEmpty() ) {
                msgTabPane.loadFileList(msgList);
                System.out.println("Loaded files" + msgTabPane.getFileArrayList());
                msgTabPane.buildTabPane();
                windowUpdater.bindTabHandlers();
                windowUpdater.update();
            }
            configHandler.getProperties().put(Vals.LOCATION_MSG.toString(), msgList.get(msgList.size()-1).getParent());
            ArrayList<String> fileMsgList = new ArrayList<>();
            for (File f : msgList){
                fileMsgList.add(f.getName());
            }
            configHandler.getProperties().put(Vals.LIST_MSG.toString(), fileMsgList.toString());
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

