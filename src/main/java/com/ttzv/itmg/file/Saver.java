package com.ttzv.itmg.file;

import sun.security.util.Password;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class Saver {

private String stringToSave;
private Path targetPath;
private String fileName;
private String extension;

public final String HTML = ".html";
public final String TXT = ".txt";


    public Saver(String stringToSave) {
        this.stringToSave = stringToSave;
    }

    public void setTargetPath(Path targetPath) {
        this.targetPath = targetPath;
    }

    public void setExtension (String extension){
        this.extension = extension;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void saveFile() throws IOException {
        initializeFile();

        String fileName = this.fileName + this.extension;

       // System.out.println(fileName);

        Path filePath = this.targetPath.resolve(fileName);

       // System.out.println(filePath.toAbsolutePath().toString());

        //BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(filePath.toFile()));

        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath.toFile())));

        //System.out.println(filePath.toFile().getAbsolutePath());

        bufferedWriter.write(stringToSave);

        bufferedWriter.close();

        System.out.println(getClass().getSimpleName() + ": Succesfully saved file: " + filePath.toAbsolutePath());

    }

    private void initializeFile() throws IOException {
        if(this.targetPath == null){
            this.targetPath = Paths.get(this.getClass().getSimpleName());
        }
        if(!Files.exists(this.targetPath)){
            Files.createDirectories(this.targetPath);
        }
        if(this.extension == null){
            this.extension = "";
        }
        if(this.fileName == null){
            Calendar calendar = Calendar.getInstance();
            this.fileName = calendar.getTime().toString().replaceAll("\\s", "").replaceAll(":","");
        }
    }

}