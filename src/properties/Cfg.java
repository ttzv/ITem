package properties;

//this is apparently Singleton pattern, Bill Pugh pattern was used
 public class Cfg extends Propsicl {

     //Property keys below
     public static String ActiveWindow = "ActiveWindow";
     public static String MsgParentPath = "MsgParentPath";
     public static String MsgList = "MsgList";
     public static String SMTP_HOST = "smtpHost";
     public static String SMTP_PORT = "smtpStartTLS";
     public static String SMTP_TLS = "smtpPort";
     public static String SMTP_LOGIN = "smtpLogin";
     public static String SMTP_PASS = "smtpPass";



     @Override
     public void defaultPropsVals() {
         defPropSet(ActiveWindow, String.valueOf(0));
         defPropSet(SMTP_HOST, "smtp.gmail.com");
         defPropSet(SMTP_PORT,"587");
         defPropSet(SMTP_TLS, "true");
     }

     static public Cfg getInstance(){
         return CfgHolder.INSTANCE;
     }

     static private class CfgHolder{
         static private final Cfg INSTANCE = new Cfg();
     }

}
