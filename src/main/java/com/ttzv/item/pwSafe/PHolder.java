package com.ttzv.item.pwSafe;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

public class PHolder {

    public static String MAIL_FILEID = "mCr";
    public static String LDAP_FILEID = "lCr";
    public static String DB_FILEID = "dCr";
    public static String SMS_FILEID = "sCr";

    private static Map<String, Crypt> cryptMap;

    public static char[] Mail = new char[0];
    public static char[] Ldap = new char[0];
    public static char[] Db = new char[0];
    public static char[] Sms = new char[0];

    public static char[] Mail() throws GeneralSecurityException, IOException {
        char[] p = new Crypt(MAIL_FILEID).read();
        return (p.length > 0) ? p : Mail;
    }
    public static char[] Ldap() throws GeneralSecurityException, IOException {
        char[] p = new Crypt(LDAP_FILEID).read();
        return (p.length > 0) ? p : Ldap;
    }
    public static char[] Db() throws GeneralSecurityException, IOException {
        char[] p = new Crypt(DB_FILEID).read();
        return (p.length > 0)? p : Db;
    }
    public static char[] Sms() throws GeneralSecurityException, IOException {
        char[] p = new Crypt(SMS_FILEID).read();
        return (p.length > 0) ? p : Sms;
    }

    public static Crypt getCrypt(String fileid){
        if(cryptMap == null) cryptMap = new HashMap<>();
        if(cryptMap.get(fileid) == null) cryptMap.put(fileid, Crypt.newCrypt(fileid));
        return cryptMap.get(fileid);
    }

}
