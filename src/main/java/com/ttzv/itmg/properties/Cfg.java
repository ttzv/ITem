package com.ttzv.itmg.properties;

//this is apparently Singleton pattern, Bill Pugh pattern was used
 public class Cfg extends Propsicl {



    //Property keys below
     //window
     public static String ActiveWindow = "ActiveWindow";
     public static String MsgParentPath = "MsgParentPath";
     public static String MsgList = "MsgList";
     //mail
     public static String SMTP_HOST = "smtpHost";
     public static String SMTP_PORT = "smtpStartTLS";
     public static String SMTP_TLS = "smtpPort";
     public static String SMTP_LOGIN = "smtpLogin";
     public static String SAVEPASS = "savePass";
     public static String AUTOFILL_LOGIN = "autoFillLogin";
     public static String USER_REGEX = "userRegexFilter";
     public static String LOGIN_REGEX = "loginRegexFilter";
     //ldap
     public static String LDAP_URL = "ldapUrl";
     public static String LDAP_PORT = "ldapPort";
     public static String LDAP_ACC = "ldapAcc";
     //db
     public static String DB_URL = "dbUrl";
     public static String DB_LOGIN = "dbLogin";
     public static String DB_USER_QTY = "dbLoadedUserQuantity";
     //signature
     public static String SIGN_LOC = "signatureLocation";
     public static String SIGN_TARGETPATH = "signatureSavingDirectory";
     public static String DIR_ALWAYSOPEN = "dirAlwaysOpen";
     //password generator
     public static String PASS_GEN_METHOD = "passGenMethod";
     public static String PROPERTY_PASS_RANDOM = "random";
     public static String PROPERTY_PASS_PATTERN = "pattern";
     public static String PASS_GEN_PATTERN = "passGenPattern";
     public static String PASS_FILES = "passFiles";
     //textfields regex
     public static String LTF_NAME_ONLY = "ltfNameOnly";
     public static String LTF_RESTRICT_SYMBOLS = "ltfRestrictSymbols";




     @Override
     public void defaultPropsVals() {
         defPropSet(ActiveWindow, String.valueOf(0));
         defPropSet(SMTP_HOST, "smtp.gmail.com");
         defPropSet(SMTP_PORT,"587");
         defPropSet(SMTP_TLS, "true");
         defPropSet(PASS_GEN_PATTERN, "WWNS");
         defPropSet(LTF_NAME_ONLY, "[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ-]*|\\s|[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]*\\s+[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ-]*");
         defPropSet(LTF_RESTRICT_SYMBOLS, "[A-Za-z]*|\\.|\\d|[A-Za-z]+\\.\\w+|[-]");
     }

     public static Cfg getInstance(){
         return CfgHolder.INSTANCE;
     }

     private static class CfgHolder{
         private static final Cfg INSTANCE = new Cfg();
     }

}
