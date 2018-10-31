package window;
import file.Loader;
import file.Parser;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import window.controls.ButtonControls;
import window.inputFields.Inputs;
import window.parsedMsgWindow.MessageWindow;
import window.parsedMsgWindow.MsgWindowUpdater;

public class MainWindow extends Application {



    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane borderPane = new BorderPane();
        primaryStage.setScene(new Scene(borderPane, 800, 600));

        Loader loader = new Loader("powitanie.html");
        Parser parser = new Parser(loader.readContent());
        Inputs inputs = new Inputs();
        MessageWindow msgWindow = new MessageWindow(parser.getOutputString());
        ButtonControls buttonControls = new ButtonControls();
        MsgWindowUpdater windowUpdater = new MsgWindowUpdater(parser, msgWindow, inputs, buttonControls);//TODO: split parsing from file and detecting flags
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
