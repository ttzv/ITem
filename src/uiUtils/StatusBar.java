package uiUtils;

import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;

import java.util.Timer;

public class StatusBar extends Label {

    private int invCnt;
    private long upperLimit;
    private CountDown countDown;

    public StatusBar() {
        invCnt = 0;
        this.setStyle("-fx-border-color: silver;");
        this.setPrefHeight(20);
        this.setPrefWidth(1080);
        this.setText("");

        this.upperLimit = 5000;
    }

    /**
     * Set text in statusbar that vanishes after some time.
     * @param text - text to set in statusbar
     */
    public void setVanishingText(String text) {
        invCnt++;
        this.setText(text);
        if(invCnt < 2) {
            countDown = new CountDown(5);
            countDown.begin();
            waitForClear();
        } else {
            countDown.restart();
        }

    }

    private void clear(){
        this.setText("");
        invCnt = 0;
    }
    private void waitForClear(){
        Task task = new Task() {
            @Override
            protected Void call() throws Exception {
                while(true){
                    System.out.println(countDown.getCurrentCount());
                    if(countDown.getCurrentCount() >= upperLimit){
                        Platform.runLater(() -> clear());
                        this.cancel();
                    }
                    Thread.sleep(500);
                    Platform.runLater(() -> appendText(". "));
                }
            }
        };
        new Thread(task).start();
    }

    public void appendText(String text){
        String ctext = this.getText();
        ctext += text;
        this.setText(ctext);
    }

}
