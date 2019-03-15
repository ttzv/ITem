package file;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public final int NAME = 1;
    public final int POSITION = 3;
    public final int PHONE = 5;
    public final int MPHONE = 7;
    public final int CITY = 11;
    public final int CITYPHONE = 13;
    public final int CITYFAX = 15;

    private String phoneConstantValue = "tel. (+48) ";
    private String mphoneConstantValue = "kom. (+48) ";
    private String cityPhoneConstantValue = "Tel. ";
    private String cityFaxConstantValue = "Fax. ";
    private ArrayList<Integer> availableLines;

    private int removed;
    private int lastIndexModified;

    public SignatureParser (StringBuilder stringBuilder){
        splitSign = new ArrayList<String>(Arrays.asList(stringBuilder.toString().split("\\r?\\n")));
        defaults = new ArrayList<>(splitSign);
        System.out.println(splitSign);
        this.name = splitSign.get(NAME);
        this.position = splitSign.get(POSITION);
        this.phone = splitSign.get(PHONE);
        this.mPhone = splitSign.get(MPHONE);
        this.city = splitSign.get(CITY);

        Integer[] avail = {NAME, POSITION, PHONE, MPHONE, CITY, CITYPHONE, CITYFAX};
        availableLines = new ArrayList<Integer>(Arrays.asList(avail));

        removed = 0;
        lastIndexModified = -1;
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
            if(text.isEmpty()){
                this.splitSign.set(line, defaults.get(line));
            }
        }
    }

    /**
     * Use this method to delete unneeded information from signature.
     * @param row - row to delete from signature
     */
    public void deleteRow(int row){
        lastIndexModified = row;
        removed++;
        this.splitSign.remove(row);
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
        for (String s : splitSign) {
            result = result.concat(s + "\n");
        }
        return result;
    }
}
