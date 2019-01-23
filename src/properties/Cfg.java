package properties;

//this is apparently Singleton pattern, Bill Pugh pattern was used
 public class Cfg extends Propsicl {

     //Property keys below
     public static String ActiveWindow = "ActiveWindow";
     public static String MsgParentPath = "MsgParentPath";
     public static String MsgList = "MsgList";


     @Override
     public void defaultPropsVals() {
         defPropSet(ActiveWindow, String.valueOf(0));
     }

     static public Cfg getInstance(){
         return CfgHolder.INSTANCE;
     }

     static private class CfgHolder{
         static private final Cfg INSTANCE = new Cfg();
     }

}
