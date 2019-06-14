package com.ttzv.itmg.pass;

import com.ttzv.itmg.file.Loader;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WordListPasswordGenerator {

    private final String SYMBOLS = "!@#$%^&*?+";
    private char[] pattern;
    private Loader loader;
    private List<File> wordLists;
    private String generatedString;
    private final char WORD = 'W';
    private final char NUMBER = 'N';
    private final char SYMBOL = 'S';
    private int wordFileNo = 0;

    public WordListPasswordGenerator(String pattern /*, List<File> wordLists*/){
        this.pattern = pattern.toCharArray();

        //temporary
        this.wordLists = new ArrayList<File>();
        try {
            this.wordLists.add(new File(getClass().getResource("/text/adjectives.txt").toURI()));
            this.wordLists.add(new File(getClass().getResource("/text/nouns.txt").toURI()));
        }catch (URISyntaxException e) {
            e.printStackTrace();
        }
        /*this.wordLists.add(new File("C:\\Users\\tzwak\\Dysk Google\\JavaWork\\jProjects\\Mailer\\src\\main\\resources\\text\\nouns.txt"));*/
        //temporary

        this.loader = new Loader();
    }

    private void buildString(){

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < pattern.length; i++) {

            if(pattern[i] == WORD){
                if(wordFileNo >= wordLists.size()){
                    wordFileNo = new Random().nextInt(wordLists.size());
                }
                String word = addWord(wordFileNo);
                word = word.substring(0,1).toUpperCase() + word.substring(1).toLowerCase();
                stringBuilder.append(word);
                wordFileNo++;
            }
            if(pattern[i] == NUMBER){
                stringBuilder.append(addDigit());
            }
            if(pattern[i] == SYMBOL){
                stringBuilder.append(addSymbol());
            }
        }

        this.generatedString = stringBuilder.toString();

    }

    private String addWord(int listNo){
        loader.load(wordLists.get(listNo));
        ArrayList<String> wordList = loader.contentToArray();
        return wordList.get(new Random().nextInt(wordList.size()));
    }

    private String addDigit(){
        return Integer.toString(new Random().nextInt(10));
    }

    private String addSymbol(){
        return Character.toString( SYMBOLS.toCharArray()[ new Random().nextInt( SYMBOLS.length() ) ] );
    }

    public String getGeneratedString() {
        buildString();
        return generatedString;
    }
}
