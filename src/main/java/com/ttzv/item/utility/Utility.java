package com.ttzv.item.utility;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {


    public static String DEFAULT_ENTITY_SEPARATOR = "=:";

    public static String restrictedSymbols = "%_/\\=-+?*";

    public static String fileSeparator = System.getProperty("file.separator");

    public static String replaceAccents(String text){
        String[] polArrFind = {"ą", "ć", "ę", "ż", "ń", "ó", "ł", "ź", "ś"};//, "Ą", "Ę", "Ć", "Ż", "Ł", "Ś", "Ź"};
        String[] replArr = {"a", "c", "e", "z", "n", "o", "l", "z", "s"};//, "A", "E", "C", "Z", "L", "S", "Z"};

        ArrayList<String> polList = new ArrayList<>(Arrays.asList(polArrFind));
        ArrayList<String> npolList = new ArrayList<>(Arrays.asList(replArr));

        for(String c : polArrFind){
            if(text.contains(c)){
                text = text.replace(c, npolList.get(polList.indexOf(c)));
            }
        }

        return text;
    }

    public static String reformatUserInput(String input){
        if(input.contains("-")){
            input = input.substring(0, input.indexOf("-"));
        }
        String reformatted = input.trim().replace(" ", ".").toLowerCase();

        return replaceAccents(reformatted);
    }

    public static ArrayList<String> stringToArray(String string){
        return new ArrayList<>(Arrays.asList(string.substring(1, string.length() - 1).replaceAll(",\\s", ",").split(",")));
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
        return prefixZeros((int) objectGUID[3] & 0xFF) +
                prefixZeros((int) objectGUID[2] & 0xFF) +
                prefixZeros((int) objectGUID[1] & 0xFF) +
                prefixZeros((int) objectGUID[0] & 0xFF) +
                "-" +
                prefixZeros((int) objectGUID[5] & 0xFF) +
                prefixZeros((int) objectGUID[4] & 0xFF) +
                "-" +
                prefixZeros((int) objectGUID[7] & 0xFF) +
                prefixZeros((int) objectGUID[6] & 0xFF) +
                "-" +
                prefixZeros((int) objectGUID[8] & 0xFF) +
                prefixZeros((int) objectGUID[9] & 0xFF) +
                "-" +
                prefixZeros((int) objectGUID[10] & 0xFF) +
                prefixZeros((int) objectGUID[11] & 0xFF) +
                prefixZeros((int) objectGUID[12] & 0xFF) +
                prefixZeros((int) objectGUID[13] & 0xFF) +
                prefixZeros((int) objectGUID[14] & 0xFF) +
                prefixZeros((int) objectGUID[15] & 0xFF);
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

    public static SimpleDateFormat ldapDateFormat() {
        return new SimpleDateFormat("yyyyMMddHHmmss");
    }

    public static SimpleDateFormat globalDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    public static String formatLdapDate(String date) {
        Date d = parseDate(date, ldapDateFormat());
        return globalDateFormat().format(d);
    }

    public static Date parseDate (String date, SimpleDateFormat simpleDateFormat) {
        try {
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    //todo: remove
    public static String cityNameToId(String city){
        switch (city){
            case "Cieszyn":
                return "c001";
            case "Katowice":
                return "c002";
            case "Krakow":
                return "c003";
            case "Warszawa":
                return "c004";
            case "Poznan":
                return "c005";
            case "Gdansk":
                return "c006";
            case "Wroclaw":
                return "c007";
            case "Lodz":
                return "c008";
            default:
                return "ERVAL";
        }
    }

    /**
     * Replaces word given in parameter to Polish equivalent
     * @param word word that should be replaced
     * @return Polonized word, with polish symbols
     */ //todo: move words database to external storage if there will ever be need for more words
    public static String polonize(String word){
        switch (word){
            case "Krakow":
                return "Kraków";
            case "Wroclaw":
                return "Wrocław";
            case "Lodz":
                return "Łódź";
            case "Gdansk":
                return "Gdańsk";
            case "Poznan":
                return "Poznań";
            default:
                return word;
        }
    }

    /**
     * Return nested list if list in parameter contains only one element - utility method for resultset
     */
    public static List<String> unNestList(List<List<String>> list){
        if(list.size() == 1){
            return list.get(0);
        } else {
            System.err.println("List contains more than one elements or is empty" +
                    "\n" + list);
            return null;
        }

    }

    public static boolean mapContainsSubString(String text, Map<String, String> map){
        return map.entrySet()
                .stream().anyMatch(entry -> entry.getValue()
                        .toLowerCase()
                        .contains(text.trim()
                                .toLowerCase()));
    }

   /* public static String ldapStringToDate(String date) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmss.0Z");
        Date tdate = simpleDateFormat.parse(date);
        SimpleDateFormat formatted = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        formatted.format(tdate);
        return formatted;
    }*/
}
