package com.ttzv.itmg.uiUtils;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.UnaryOperator;

public class LimitableTextField extends TextField {

    public static String NAME_ONLY = "[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]*|\\s|[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]*\\s+[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]*";
    public static String RESTRICT_SYMBOLS = "[A-Za-z]*|\\.|\\d|[A-Za-z]+\\.\\w+|[-]";

    private final int MAX_THREADS = 4;
    private ThreadPoolExecutor exec;

    private String originalStyle;
    private String regexFilter;

    public LimitableTextField() {
        exec = (ThreadPoolExecutor) Executors.newFixedThreadPool(MAX_THREADS, runnable ->{
            Thread thread = new Thread(runnable);
            thread.setDaemon(true);
            return thread;
        });
        this.originalStyle = this.getStyle();
        this.setTextFormatter(selectTextFormatter());
        this.regexFilter = "";
    }

    public void setRegexFilter(String regexFilter) {
        this.regexFilter = regexFilter;
    }

    private void setRejectBorderStyle(){
        Platform.runLater(() -> {
            this.setStyle("-fx-focus-color: red;\n" +
                    "    -fx-faint-focus-color: #ff000022;");
            if(!this.isFocused()) {
                this.setStyle("-fx-border-color: red;" +
                        "-fx-faint-focus-color: #ff000022;");
            }
        });
    }

    private void restoreOriginalBorderStyle(){
        Platform.runLater(() -> {
            this.setStyle(originalStyle);
        });
    }


    private TextFormatter<String> selectTextFormatter(){
        UnaryOperator<TextFormatter.Change> filter = getFilter();
        TextFormatter<String> textFormatter = new TextFormatter<String>(filter);
        return textFormatter;
    }


    private UnaryOperator<TextFormatter.Change> getFilter(){
        return change -> {
            String text = change.getText();
            //System.out.println("text from change: " + text);

            if(text.matches(regexFilter)){
                return change;
            } else if(change.isContentChange()){
                blinkError(3);
                //System.out.println("rejected " + change);
            }
            return null;
        };
    }

    private void blinkError(int repeats){
       // System.out.println( exec.getActiveCount());
        int delay = 100;
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                for (int i = 0; i < repeats; i++) {
                    setRejectBorderStyle();
                    Thread.sleep(delay);
                    restoreOriginalBorderStyle();
                    Thread.sleep(delay);
                }
                return null;
            }
        };
        if(exec.getActiveCount() < MAX_THREADS) {
            exec.execute(new Thread(task));
        }

    }
}
