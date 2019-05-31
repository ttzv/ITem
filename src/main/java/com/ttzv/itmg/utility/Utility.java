package com.ttzv.itmg.utility;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {

    public static String restrictedSymbols = "%_/\\=-+?*";

    public static String fileSeparator = System.getProperty("file.separator");

    public static String reformatUserInput(String input){
        if(input.contains("-")){
            input = input.substring(0, input.indexOf("-"));
        }
        String[] polArrFind = {"ą", "ć", "ę", "ż", "ń", "ó", "ł", "ź", "ś"};//, "Ą", "Ę", "Ć", "Ż", "Ł", "Ś", "Ź"};
        String[] replArr = {"a", "c", "e", "z", "n", "o", "l", "z", "s"};//, "A", "E", "C", "Z", "L", "S", "Z"};

        ArrayList<String> polList = new ArrayList<>(Arrays.asList(polArrFind));
        ArrayList<String> npolList = new ArrayList<>(Arrays.asList(replArr));

        String reformatted = input.trim().replace(" ", ".").toLowerCase();
       // System.out.println(reformatted);

        for(String c : polArrFind){
            if(reformatted.contains(c)){
                reformatted = reformatted.replace(c, npolList.get(polList.indexOf(c)));
                //System.out.println("replaced: " + c + " with " + npolList.get(polList.indexOf(c)) + " word is: " + reformatted);
            }
        }
        return reformatted;
    }

    public static ArrayList<String> stringToArray(String string){
        return new ArrayList<>(Arrays.asList(string.substring(1, string.length() - 1).replaceAll("\\s", "").split(",")));
    }

    public static String extractCityFromDn(String dn){
        Pattern pattern = Pattern.compile(",OU=(.*?),");
        Matcher matcher = pattern.matcher(dn);
        if(matcher.find()){
            return matcher.group(1);
        }
        return "";
    }

    public static String formatObjectGUID(Object guid){

        return convertToDashedString( (byte[]) guid);
    }

    public static String convertToDashedString(byte[] objectGUID) {
        StringBuilder displayStr = new StringBuilder();
        displayStr.append(prefixZeros((int) objectGUID[3] & 0xFF));
        displayStr.append(prefixZeros((int) objectGUID[2] & 0xFF));
        displayStr.append(prefixZeros((int) objectGUID[1] & 0xFF));
        displayStr.append(prefixZeros((int) objectGUID[0] & 0xFF));
        displayStr.append("-");
        displayStr.append(prefixZeros((int) objectGUID[5] & 0xFF));
        displayStr.append(prefixZeros((int) objectGUID[4] & 0xFF));
        displayStr.append("-");
        displayStr.append(prefixZeros((int) objectGUID[7] & 0xFF));
        displayStr.append(prefixZeros((int) objectGUID[6] & 0xFF));
        displayStr.append("-");
        displayStr.append(prefixZeros((int) objectGUID[8] & 0xFF));
        displayStr.append(prefixZeros((int) objectGUID[9] & 0xFF));
        displayStr.append("-");
        displayStr.append(prefixZeros((int) objectGUID[10] & 0xFF));
        displayStr.append(prefixZeros((int) objectGUID[11] & 0xFF));
        displayStr.append(prefixZeros((int) objectGUID[12] & 0xFF));
        displayStr.append(prefixZeros((int) objectGUID[13] & 0xFF));
        displayStr.append(prefixZeros((int) objectGUID[14] & 0xFF));
        displayStr.append(prefixZeros((int) objectGUID[15] & 0xFF));
        return displayStr.toString();
    }


    private static String prefixZeros(int value) {
        if (value <= 0xF) {
            StringBuilder sb = new StringBuilder("0");
            sb.append(Integer.toHexString(value));

            return sb.toString();

        } else {
            return Integer.toHexString(value);
        }
    }

    public static String ldapStringToDate(String date) throws ParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDate localDate = LocalDate.parse(date, formatter);
        return localDate.toString();
    }

   /* public static String ldapStringToDate(String date) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmss.0Z");
        Date tdate = simpleDateFormat.parse(date);
        SimpleDateFormat formatted = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        formatted.format(tdate);
        return formatted;
    }*/
}
