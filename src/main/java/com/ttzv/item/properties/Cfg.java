package com.ttzv.item.properties;

//this is apparently Singleton pattern, Bill Pugh pattern was used
 public class Cfg extends Propsicl {

    //Property keys below
     public static final String APP_NAME = "appName";
     //window final
     public static final String ACTIVE_WINDOW = "ActiveWindow";
     public static final String MSG_LIST = "MsgTemplateList";
     public static final String SMS_LIST = "SmsTemplateList";
     //mail final
     public static final String SMTP_HOST = "smtpHost";
     public static final String SMTP_PORT = "smtpStartTLS";
     public static final String SMTP_TLS = "smtpPort";
     public static final String SMTP_LOGIN = "smtpLogin";
     public static final String SAVEPASS = "savePass";
     public static final String AUTOFILL_LOGIN = "autoFillLogin";
     public static final String USER_REGEX = "userRegexFilter";
     public static final String LOGIN_REGEX = "loginRegexFilter";
     //ldap final
     public static final String LDAP_URL = "ldapUrl";
     public static final String LDAP_PORT = "ldapPort";
     public static final String LDAP_ACC = "ldapAcc";
     public static final String LDAP_SEARCH_BASE = "searchBase";
     //db
     public static final String DB_URL = "dbUrl";
     public static final String DB_LOGIN = "dbLogin";
     public static final String DB_USER_QTY = "dbLoadedUserQuantity";
     public static final String DB_DRIVER = "dbDriver";
     //signature
     public static final String SIGN_LOC = "signatureLocation";
     public static final String SIGN_TARGETPATH = "signatureSavingDirectory";
     public static final String DIR_ALWAYSOPEN = "dirAlwaysOpen";
     //password generator
     public static final String PASS_GEN_METHOD = "passGenMethod";
     public static final String PROPERTY_PASS_RANDOM = "random";
     public static final String PROPERTY_PASS_PATTERN = "pattern";
     public static final String PASS_GEN_PATTERN = "passGenPattern";
     public static final String PASS_FILES = "passFiles";
     //textfields regex
     public static final String LTF_NAME_ONLY = "ltfNameOnly";
     public static final String LTF_RESTRICT_SYMBOLS = "ltfRestrictSymbols";
     //theme
     public static final String THEME = "theme";
     //SMS
     public static final String SMSAPI_LOGIN = "smsApiLogin";
     public static final String SMSAPI_SENDER = "smsApiSenderName";
     public static final String SMSAPI_KEY = "smsApiKey";





     @Override
     public void defaultPropsVals() {
         defPropSet(APP_NAME, "ITem");
         defPropSet(ACTIVE_WINDOW, String.valueOf(0));
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
