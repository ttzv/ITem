package file;

import java.io.*;
import java.util.Scanner;

/**
 * Parses given HTML file and detects flagged lines. Current flags are: <!L></!L> - for Login and <!P></!P> for Password
 * Both can be changed.
 */
public class Parser {

    private BufferedReader bufferedReader;
    private String flagLoginRegex;
    private String flagPasswordRegex;
    private String flagLoginStart;
    private String flagLoginEnd;
    private String flagPasswordStart;
    private String flagPasswordEnd;
    private String flaggedLogin;
    private String flaggedPassword;
    private StringBuilder stringBuilder;
    private int flagLoginStartOffset;
    private int flagLoginEndOffset;
    private int flagPassStartOffset;
    private int flagPassEndOffset;

    /**
     *
     * @param stringBuilder content receiver from Loader
     * @throws IOException
     */
    public Parser (StringBuilder stringBuilder) throws IOException {
        this.stringBuilder = stringBuilder;

        flagLoginStart = "<!L>";
        flagLoginEnd = "</!L>";
        flagPasswordStart = "<!P>";
        flagPasswordEnd = "</!P>";
        String htmlEndline = "<br>";
        flagLoginRegex = "(<!L>.*)(.*</!L>.*)";
        flagPasswordRegex = "(<!P>.*)(.*</!P>.*)";

    }


    public static void main(String[] args) throws Exception {
        Loader loader = new Loader("powitanie.html");
        Parser parser = new Parser(loader.readContent());
        parser.reparse();

        System.out.println(parser.getOutputString());
        System.out.println("login: " + parser.getFlaggedLogin());
        System.out.println("pass: " + parser.getFlaggedPassword());
        Scanner reader = new Scanner(System.in);

        System.out.println("new Login: ");
        parser.setFlaggedLogin(reader.nextLine());
        System.out.println("new pass: ");
        parser.setFlaggedPassword(reader.nextLine());

        System.out.println("login: " + parser.getFlaggedLogin());
        System.out.println("pass: " + parser.getFlaggedPassword());

        parser.reparse();

        System.out.println("reparsed:");
        System.out.println(parser.getOutputString());

    }

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
     * Allows to reparse text with modified flagged values, usually best to use after modifying flaggedLogin or flaggedPassword
     */
    public void reparse(){
        flagLoginStartOffset = ( stringBuilder.lastIndexOf(flagLoginStart) ) + flagLoginStart.length();
        flagLoginEndOffset = ( stringBuilder.lastIndexOf(flagLoginEnd) );
        System.out.println(flagLoginStartOffset + " | " + flagLoginEndOffset);

        this.stringBuilder.replace(flagLoginStartOffset, flagLoginEndOffset, "");
        this.stringBuilder.insert(flagLoginStartOffset, flaggedLogin);

        flagPassStartOffset = ( stringBuilder.lastIndexOf(flagPasswordStart) ) + flagPasswordStart.length();
        flagPassEndOffset = ( stringBuilder.lastIndexOf(flagPasswordEnd) );
        System.out.println(flagPassStartOffset + " | " + flagPassEndOffset);

        this.stringBuilder.replace(flagPassStartOffset, flagPassEndOffset, "");
        this.stringBuilder.insert(flagPassStartOffset, flaggedPassword);
    }
}
