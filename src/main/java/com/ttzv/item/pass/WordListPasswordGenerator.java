package com.ttzv.item.pass;

import com.ttzv.item.file.Loader;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WordListPasswordGenerator {

    private final String SYMBOLS = "!@#$%^&*?+";
    private final char WORD = 'W';
    private final char NUMBER = 'N';
    private final char SYMBOL = 'S';
    private char[] pattern;
    private Loader loader;
    private List<URL> wordLists;
    private String generatedString;
    private int wordFileNo = 0;

    public WordListPasswordGenerator(String pattern){
        this.pattern = pattern.toCharArray();
        //temporary
        this.wordLists = new ArrayList<>();

        URL url = this.getClass().getResource("/text/adjectives.txt");
        this.wordLists.add(url);

        url = this.getClass().getResource("/text/nouns.txt");
        this.wordLists.add(url);
        //temporary

        this.loader = new Loader();
    }

    private void buildString() throws IOException {

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

    private String addWord(int listNo) throws IOException {
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

    public String getGeneratedString() throws IOException {
        buildString();
        return generatedString;
    }
}
