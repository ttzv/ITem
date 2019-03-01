package uiUtils;

import javafx.concurrent.Task;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import java.util.function.UnaryOperator;

public class LimitableTextField extends TextField {

    public static String NAME_ONLY = "[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]*|\\s|[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]+\\s\\w+";
    public static String RESTRICT_SYMBOLS = "[A-Za-z]*|\\.|\\d|[A-Za-z]+\\.\\w+|[-]";


    private String originalStyle;
    private String regexFilter;

    public LimitableTextField() {
        this.originalStyle = this.getStyle();
        this.setTextFormatter(selectTextFormatter());
        this.regexFilter = "";
    }

    public void setRegexFilter(String regexFilter) {
        this.regexFilter = regexFilter;
    }

    private void setRejectBorderStyle(){
        this.setStyle("-fx-focus-color: red;\n" +
                "    -fx-faint-focus-color: #ff000022;");
    }

    private void restoreOriginalBorderStyle(){
        this.setStyle(originalStyle);
    }


    private TextFormatter<String> selectTextFormatter(){
        UnaryOperator<TextFormatter.Change> filter = getFilter();
        TextFormatter<String> textFormatter = new TextFormatter<String>(filter);
        return textFormatter;
    }


    private UnaryOperator<TextFormatter.Change> getFilter(){
        return change -> {
            String text = change.getText();
            System.out.println("text from change: " + text);

            if(text.matches(regexFilter)){
                return change;
            } else if(change.isContentChange()){
                blinkError(3);
                System.out.println("rejected " + change);
            }
            return null;
        };
    }

    private void blinkError(int repeats){
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
        new Thread(task).start();
    }
}
