package com.ttzv.itmg.uiUtils;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;

import java.util.function.UnaryOperator;

public class TextFieldFormatters {

    public final int FORMAT_MOBILE_NUMBER = 1;
    public final int FORMAT_PHONE_NUMBER = 2;
    private final String phoneSeparator = " ";

    public TextFormatter<String> selectTextFormatter(int converter) {
        UnaryOperator<TextFormatter.Change> filter = getFilter();
        TextFormatter<String> textFormatter = new TextFormatter<String>(filter);
        return textFormatter;
    }

    private UnaryOperator<TextFormatter.Change> getFilter(){
            return change -> {
                if(change.isContentChange()) {
                    String newText = change.getControlNewText();
                    String cText = change.getControlText();
                    if (newText.matches("\\d{9}")) {
                        String text = change.getControlNewText();
                        text = insertSpacesAt(text, 3, 6);
                        System.out.println("text: " + text + "l:" + newText.length() + " | " + change.getRangeEnd());
                        change.setRange(0, change.getRangeEnd());

                        change.setText(text);
                        change.setAnchor(text.length());
                        change.setCaretPosition(text.length());
//                        TextField textField = (TextField) change.getControl();
//                        textField.setText(text);
                        System.out.println(change + " | " + change.getCaretPosition());
                        return change;
                    } else if(cText.matches("(\\d{3}\\s){2}\\d{3}")){
                        String text = newText.replaceAll(" ", "");
                        change.setRange(0, change.getRangeEnd());
                        change.setText(text);
                        change.setAnchor(text.length());
                        change.setCaretPosition(text.length());
                        return change;
                    } else {
                        return change;
                    }
                }
                return null;
            };
    }

    private String insertSpacesAt(String text, int... index){
        StringBuilder stringBuilder = new StringBuilder(text);
        for (int i = 0; i < index.length; i++) {
            stringBuilder.insert(index[i] + i, phoneSeparator);
        }
        return stringBuilder.toString();
    }

    private StringConverter<String> getStringConverter(int converter){
        if(converter == FORMAT_MOBILE_NUMBER){
            return new StringConverter<String>() {
                @Override
                public String toString(String object) {
                    if (object.length() >= 9){
                        return insertSpacesAt(object, 2, 5);
                    } else {
                        return object;
                    }
                }

                @Override
                public String fromString(String string) {
                    return null;
                }
            };
        }
        if(converter == FORMAT_PHONE_NUMBER){
            return new StringConverter<String>() {
                @Override
                public String toString(String object) {
                    return null;
                }

                @Override
                public String fromString(String string) {
                    return null;
                }
            };
        }
        return null;
    }

}
