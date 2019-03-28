package com.ttzv.itmg.file;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Loader {

    private BufferedReader bufferedReader;

    public Loader(){

    }

    public Loader(File file){
        load(file);
    }

    public boolean load (File file){
        if(file.exists() && file != null) {
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(
                        new FileInputStream(file.getPath()), StandardCharsets.UTF_8));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return true;
        } else {
            System.err.println("File: " + file.toString() + "  was not found.");
            return false;
        }
    }

    public StringBuilder readContent() {
        StringBuilder stringBuilder = new StringBuilder();
        String stringLine;
        try {
            while ((stringLine = bufferedReader.readLine()) != null) {
                stringBuilder.append(stringLine + "\n");
            }
            return stringBuilder;
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return null;
    }
}
