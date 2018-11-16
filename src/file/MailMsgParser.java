package file;

import java.io.*;
import java.util.Scanner;

/**
 * Parses given HTML file and detects flagged lines. Current flags are: <!L></!L> - for Login and <!P></!P> for Password
 * Both can be changed.
 */
public class MailMsgParser {

    private BufferedReader bufferedReader;
    private String flagLoginRegex;
    private String flagPasswordRegex;
    private String flagLoginStart;
    private String flagLoginEnd;
    private String flagPasswordStart;
    private String flagPasswordEnd;
    private String flaggedLogin;
    private String flaggedPassword;
    private String flagTopicStart;
    private String flagTopicEnd;
    private String flaggedTopic;
    private StringBuilder stringBuilder;
    private int flagLoginStartOffset;
    private int flagLoginEndOffset;
    private int flagPassStartOffset;
    private int flagPassEndOffset;

    /**
     *
     * @param stringBuilder content received from Loader
     */
    public MailMsgParser(StringBuilder stringBuilder) {
        this.stringBuilder = stringBuilder;

        flagLoginStart = "<!L>";
        flagLoginEnd = "</!L>";

        flagPasswordStart = "<!P>";
        flagPasswordEnd = "</!P>";

        flagTopicStart = "<!T>";
        flagTopicEnd = "</!T>";

        flagLoginRegex = "(<!L>.*)(.*</!L>.*)";
        flagPasswordRegex = "(<!P>.*)(.*</!P>.*)";

    }


    /*public static void main(String[] args) throws Exception {
        Loader loader = new Loader("powitanie.html");
        MailMsgParser mailMsgParser = new MailMsgParser(loader.readContent());
        mailMsgParser.reparse();

        System.out.println(mailMsgParser.getOutputString());
        System.out.println("login: " + mailMsgParser.getFlaggedLogin());
        System.out.println("pass: " + mailMsgParser.getFlaggedPassword());
        Scanner reader = new Scanner(System.in);

        System.out.println("new Login: ");
        mailMsgParser.setFlaggedLogin(reader.nextLine());
        System.out.println("new pass: ");
        mailMsgParser.setFlaggedPassword(reader.nextLine());

        System.out.println("login: " + mailMsgParser.getFlaggedLogin());
        System.out.println("pass: " + mailMsgParser.getFlaggedPassword());

        mailMsgParser.reparse();

        System.out.println("reparsed:");
        System.out.println(mailMsgParser.getOutputString());

    }*/

    public String getOutputString(){
        return this.stringBuilder.toString();
    }

    public String getFlaggedLogin() {
        return flaggedLogin;
    }

    public void setFlaggedLogin(String flaggedLogin) {
        this.flaggedLogin = flaggedLogin;
    }

    public String getFlaggedPassword() {
        return flaggedPassword;
    }

    public void setFlaggedPassword(String flaggedPassword) {
        this.flaggedPassword = flaggedPassword;
    }

    /**
     * Read topic from html message and delete it from final content
     */
    public void parseFlaggedTopic(){
        int flagTopicStartOffset, flagTopicEndOffset;
        flagTopicStartOffset = stringBuilder.lastIndexOf(flagTopicStart) + flagTopicStart.length();
        flagTopicEndOffset = stringBuilder.lastIndexOf(flagTopicEnd);

       // System.out.println(flagTopicStartOffset + " | " + flagTopicEndOffset);

        String flaggedTopic = stringBuilder.substring(flagTopicStartOffset, flagTopicEndOffset);
        stringBuilder.replace(flagTopicStartOffset - flagTopicStart.length(), flagTopicEndOffset + flagTopicEnd.length(), "");

        this.flaggedTopic = flaggedTopic;

    }

    public String getFlaggedTopic(){
        return this.flaggedTopic;
    }

    /**
     * Allows to reparse text with modified flagged values, usually best to use after modifying flaggedLogin or flaggedPassword
     */
    public void reparse(){
        flagLoginStartOffset = stringBuilder.lastIndexOf(flagLoginStart) + flagLoginStart.length();
        flagLoginEndOffset = stringBuilder.lastIndexOf(flagLoginEnd);
        //System.out.println(flagLoginStartOffset + " | " + flagLoginEndOffset);

        this.stringBuilder.replace(flagLoginStartOffset, flagLoginEndOffset, "");
        this.stringBuilder.insert(flagLoginStartOffset, flaggedLogin);

        flagPassStartOffset = stringBuilder.lastIndexOf(flagPasswordStart) + flagPasswordStart.length();
        flagPassEndOffset = stringBuilder.lastIndexOf(flagPasswordEnd);
        //System.out.println(flagPassStartOffset + " | " + flagPassEndOffset);

        this.stringBuilder.replace(flagPassStartOffset, flagPassEndOffset, "");
        this.stringBuilder.insert(flagPassStartOffset, flaggedPassword);

        //System.out.println(getFlaggedTopic());


    }
}
