package com.ttzv.item.file;


import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;

public class Loader {

    private BufferedReader bufferedReader;

    public Loader() {
    }

    public Loader(File file) {
        load(file);
    }

    public boolean load(Path path){
        return this.load(path.toFile());
    }

    public boolean load(URL url) throws IOException {
        this.load(url.openStream());
        return true;
    }

    public boolean load(InputStream inputStream){
        if(inputStream != null){
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            return true;
        } else {
            System.err.println("Stream not available or empty");
        }
        return false;
    }

    public boolean load(File file) {
        if(file != null) {
            if (file.exists()) {
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
        } else {
            System.err.println(this.getClass().getSimpleName() + ": No file received");
            return false;
        }
    }

    public StringBuilder readContent() {
        StringBuilder stringBuilder = new StringBuilder();
        String stringLine;
        try {
            while ((stringLine = bufferedReader.readLine()) != null) {
                stringBuilder.append(stringLine).append("\n");
            }
            bufferedReader.close();
            return stringBuilder;
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return null;
    }

    public ArrayList<String> contentToArray() {

        ArrayList<String> list = new ArrayList<>();
        String stringLine;
        try {
            while ((stringLine = bufferedReader.readLine()) != null) {
                list.add(stringLine);
            }
            bufferedReader.close();
            return list;
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return list;
    }
}


