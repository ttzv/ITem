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
        UnaryOperator<TextFormatter.Change> filter = getFilter(converter);
        TextFormatter<String> textFormatter = new TextFormatter<String>(filter);
        return textFormatter;
    }

    private UnaryOperator<TextFormatter.Change> getFilter(int converter){
            return change -> {
                if(change.isContentChange()) {
                    String newText = change.getControlNewText();
                    String cText = change.getControlText();
                    if (newText.matches("\\d{9}")) {
                        String text = change.getControlNewText();
                        if(converter == FORMAT_MOBILE_NUMBER) {
                            text = insertSpacesAt(text, 3, 6);
                        } else if(converter == FORMAT_PHONE_NUMBER){
                            text = insertSpacesAt(text, 2, 5, 7);
                        }
                        System.out.println("text: " + text + "l:" + newText.length() + " | " + change.getRangeEnd());
                        change.setRange(0, change.getRangeEnd());
                        change.setText(text);
                        change.setAnchor(text.length());
                        change.setCaretPosition(text.length());
//                        TextField textField = (TextField) change.getControl();
//                        textField.setText(text);
                        System.out.println(change + " | " + change.getCaretPosition());
//                        return change;
                    } else if(cText.matches("(\\d{3}\\s){2}\\d{3}") || cText.matches("(\\d{2}\\s)\\d{3}(\\s\\d{2}){2}")){
                        String text = newText.replaceAll(" ", "");
                        change.setRange(0, change.getRangeEnd());
                        change.setText(text);
                        change.setAnchor(text.length());
                        change.setCaretPosition(text.length());
                    } else {
                        System.out.println(change);
                        return change;
                    }
                }
                return change;
            };
    }

    private String insertSpacesAt(String text, int... index){
        StringBuilder stringBuilder = new StringBuilder(text);
        for (int i = 0; i < index.length; i++) {
            stringBuilder.insert(index[i] + i, phoneSeparator);
        }
        return stringBuilder.toString();
    }

    //not used
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
