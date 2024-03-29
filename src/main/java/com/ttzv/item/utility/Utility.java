package com.ttzv.item.utility;


import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {


    public static String DEFAULT_ENTITY_SEPARATOR = ":";

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

    // https://miromannino.com/blog/convert-a-sid-to-string-with-java/
    public static String convertSidToStringSid(byte[] sid) {
        int offset, size;
        // sid[0] is the Revision, we allow only version 1, because it's the
        // only that exists right now.
        if (sid[0] != 1)
            throw new IllegalArgumentException("SID revision must be 1");
        StringBuilder stringSidBuilder = new StringBuilder("S-1-");
        // The next byte specifies the numbers of sub authorities (number of
        // dashes minus two)
        int subAuthorityCount = sid[1] & 0xFF;
        // IdentifierAuthority (6 bytes starting from the second) (big endian)
        long identifierAuthority = 0;
        offset = 2;
        size = 6;
        for (int i = 0; i < size; i++) {
            identifierAuthority |= (long) (sid[offset + i] & 0xFF) << (8 * (size - 1 - i));
            // The & 0xFF is necessary because byte is signed in Java
        }
        if (identifierAuthority < Math.pow(2, 32)) {
            stringSidBuilder.append(Long.toString(identifierAuthority));
        } else {
            stringSidBuilder.append("0x").append(
                    Long.toHexString(identifierAuthority).toUpperCase());
        }
        // Iterate all the SubAuthority (little-endian)
        offset = 8;
        size = 4; // 32-bits (4 bytes) for each SubAuthority
        for (int i = 0; i < subAuthorityCount; i++, offset += size) {
            long subAuthority = 0;
            for (int j = 0; j < size; j++) {
                subAuthority |= (long) (sid[offset + j] & 0xFF) << (8 * j);
                // The & 0xFF is necessary because byte is signed in Java
            }
            stringSidBuilder.append("-").append(subAuthority);
        }
        return stringSidBuilder.toString();
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

    public static LocalDateTime convertToLocalDateTimeViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public static DateTimeFormatter ldapDateFormat() {
        return DateTimeFormatter.ofPattern("uuuuMMddHHmmss[,S][.S]X");
    }

    public static DateTimeFormatter globalDateFormat() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

    public static String formatLdapDate(String date) {
        LocalDateTime d = parseDate(date, ldapDateFormat());
        return globalDateFormat().format(d);
    }

    public static LocalDateTime parseDate (String date, DateTimeFormatter dateTimeFormatter) {
        if(!date.equals("0") && !date.isEmpty()){
            return LocalDateTime.parse(date, dateTimeFormatter);
        }
        return null;
    }

    public static LocalDateTime parseLockoutTimestamp(Long timestamp){
        if (timestamp > 0) {
            Instant zero = Instant.parse("1601-01-01T00:00:00Z");
            Duration duration = Duration.of(timestamp / 10, ChronoUnit.MICROS).plus(timestamp % 10 * 100, ChronoUnit.NANOS);
            LocalDateTime localDateTime = LocalDateTime.ofInstant(zero.plus(duration), ZoneId.systemDefault());
            return localDateTime.truncatedTo(ChronoUnit.SECONDS);
        }
        return null;
    }

    public static LocalDateTime parseLockoutTimestamp(String timestamp){
        if(!timestamp.isBlank()){
            return parseLockoutTimestamp(Long.parseLong(timestamp));
        }
        return null;
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

    public static String formatObjectSid(Object sid) {
        return convertSidToStringSid( (byte[]) sid );
    }

   /* public static String ldapStringToDate(String date) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmss.0Z");
        Date tdate = simpleDateFormat.parse(date);
        SimpleDateFormat formatted = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        formatted.format(tdate);
        return formatted;
    }*/
}
