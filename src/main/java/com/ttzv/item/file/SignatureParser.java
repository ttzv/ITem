package com.ttzv.item.file;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class SignatureParser implements FileParser {
    private StringBuilder stringBuilder;
    private ArrayList<String> splitSign;
    private ArrayList<String> defaults;
    private String name;
    private String position;
    private String phone;
    private String mPhone;
    private String city;
    private String cityPhone;
    private String cityFax;

    public static final int NAME = 1;
    public static final int POSITION = 4;
    public static final int PHONE = 7;
    public static final int MPHONE = 10;
    public static final int CITY = 15;
    public static final int CITYPHONE = 17;
    public static final int CITYFAX = 19;

    private String phoneConstantValue = "tel. (+48) ";
    private String mphoneConstantValue = "kom. (+48) ";
    private String cityPhoneConstantValue = "Tel. (+48) ";
    private String cityFaxConstantValue = "Fax. (+48) ";
    private ArrayList<Integer> availableLines;

    private HashSet<Integer> hiddenRows;

    public SignatureParser (StringBuilder stringBuilder){
        splitSign = new ArrayList<String>(Arrays.asList(stringBuilder.toString().split("\\r?\\n")));
        defaults = new ArrayList<>(splitSign);
        this.name = splitSign.get(NAME);
        this.position = splitSign.get(POSITION);
        this.phone = splitSign.get(PHONE);
        this.mPhone = splitSign.get(MPHONE);
        this.city = splitSign.get(CITY);

        Integer[] avail = {NAME, POSITION, PHONE, MPHONE, CITY, CITYPHONE, CITYFAX};
        availableLines = new ArrayList<Integer>(Arrays.asList(avail));

        hiddenRows = new HashSet<>();
    }

    /**
     * Set desired text in chosen line
     * @param text text to set
     * @param line line in which text should be inserted
     */
    public void setLine(String text, int line){
        if(availableLines.contains(line)) {
            if(line == PHONE){
                this.splitSign.set(line, phoneConstantValue + text);
            } else if (line == MPHONE) {
                this.splitSign.set(line, mphoneConstantValue + text);
            } else if (line == CITYPHONE) {
                this.splitSign.set(line, cityPhoneConstantValue + text);
            }else if(line == CITYFAX){
                this.splitSign.set(line, cityFaxConstantValue + text);
            }else{
                this.splitSign.set(line, text);
            }
            if(text == null || text.isEmpty()){
                this.splitSign.set(line, defaults.get(line));
            }
        }
    }

    /**
     * Use this method to hide unneeded information from signature.
     * @param rows - rows to delete hide in signature
     */
    public void hideRows(int... rows){
        for (int r : rows){
            hiddenRows.add(r);
        }
    }

    public void showRows(Integer... rows){
        for (Integer r : rows) {
            hiddenRows.remove(r);
        }
    }

    public void setName(String name){
        this.name = name;
    }

    public void setPosition(String position){
        this.position = position;
    }

    public void setPhone(String phone) {
        this.phone = phoneConstantValue + phone;
    }

    public void setmPhone(String mPhone) {
        this.mPhone = mphoneConstantValue + mPhone;
    }

    public void setCity(String city) {
        if(city.isEmpty()){
            this.splitSign.set(CITY, defaults.get(CITY));
        }
        this.splitSign.set(CITY, city);
    }

    public void setCityPhone(String cityPhone) {
        if(city.isEmpty()){
            this.splitSign.set(CITYPHONE, defaults.get(CITYPHONE));
        }
        this.splitSign.set(CITYPHONE, cityPhone);
    }

    public void setCityFax(String cityFax) {
        if(city.isEmpty()){
            this.splitSign.set(CITYFAX, defaults.get(CITYFAX));
        }
        this.splitSign.set(CITYFAX, cityFax);
    }

    @Override
    public String getOutputString() {
        String result = "";
        for (int i = 0; i < splitSign.size(); i++) {
            if(!hiddenRows.contains(i)){
                result = result.concat(splitSign.get(i) + "\n");
            }
        }
        return result;
    }
}
