package com.ttzv.item.file;

import java.io.BufferedReader;

/**
 * Parses given HTML file and detects flagged lines. Current flags are: <!L></!L> - for Login and <!P></!P> for Password
 * Both can be changed.
 */
public class MailMsgParser implements FileParser {

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

        flaggedLogin = "";
        flaggedPassword = "";
    }

    @Override
    public String getOutputString() {
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
    public void parseFlaggedTopic() {
        int flagTopicStartOffset, flagTopicEndOffset;
        flagTopicStartOffset = stringBuilder.lastIndexOf(flagTopicStart);
        flagTopicEndOffset = stringBuilder.lastIndexOf(flagTopicEnd);
        if (flagTopicStartOffset >= 0 && flagTopicEndOffset >= 0) {
            flagTopicStartOffset += flagTopicStart.length();
            //System.out.println(flagTopicStartOffset + " | " + flagTopicEndOffset);

            String flaggedTopic = stringBuilder.substring(flagTopicStartOffset, flagTopicEndOffset);
            stringBuilder.replace(flagTopicStartOffset - flagTopicStart.length(), flagTopicEndOffset + flagTopicEnd.length(), "");

            this.flaggedTopic = flaggedTopic;
        } else {
            this.flaggedTopic = "";
        }

    }

    public String getFlaggedTopic() {
        return this.flaggedTopic;
    }

    /**
     * Allows to reparse text with modified flagged values, usually best to use after modifying flaggedLogin or flaggedPassword,
     * if no flags are present in text nothing changes in document.
     */
    public void reparse() {
        flagLoginStartOffset = stringBuilder.lastIndexOf(flagLoginStart);
        flagLoginEndOffset = stringBuilder.lastIndexOf(flagLoginEnd);

        if (flagLoginStartOffset >= 0 && flagLoginEndOffset >= 0) {
            flagLoginStartOffset += flagLoginStart.length();
            this.stringBuilder.replace(flagLoginStartOffset, flagLoginEndOffset, "");
            this.stringBuilder.insert(flagLoginStartOffset, flaggedLogin);
        }
        //System.out.println(flagLoginStartOffset + " | " + flagLoginEndOffset);


        flagPassStartOffset = stringBuilder.lastIndexOf(flagPasswordStart);
        flagPassEndOffset = stringBuilder.lastIndexOf(flagPasswordEnd);
        //System.out.println(flagPassStartOffset + " | " + flagPassEndOffset);
        if (flagPassStartOffset >= 0 && flagPassEndOffset >= 0) {
            flagPassStartOffset += flagPasswordStart.length();
            this.stringBuilder.replace(flagPassStartOffset, flagPassEndOffset, "");
            this.stringBuilder.insert(flagPassStartOffset, flaggedPassword);
        }

        //System.out.println(getFlaggedTopic());


    }
}
