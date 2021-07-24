package com.ttzv.item.uiUtils;

import com.ttzv.item.properties.Cfg;
import com.ttzv.item.utility.Utility;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MsgFileChooser {

    private FileChooser fileChooser;
    private List<Path> filesPathList;
    private String listNameProperty;

    public MsgFileChooser(String listName) {
        this.fileChooser = new FileChooser();
        this.listNameProperty = listName;

        //set initial dir if property exists
        String initProp = Cfg.getInstance().retrieveProp(listNameProperty);
        if(!initProp.isEmpty() && !initProp.equals("[]")) {
            filesPathList = Utility.stringToArray(initProp).stream().map(s -> Paths.get(s)).collect(Collectors.toList());
            Path initDir = filesPathList.get(filesPathList.size() - 1).getParent(); //takes latest element in list
            if (Files.exists(initDir)) {
                this.fileChooser.setInitialDirectory(initDir.toFile());
            }
        } else {
            filesPathList = new ArrayList<>();
        }
    }

    public void show() throws IOException {
        List<File> filesList = fileChooser.showOpenMultipleDialog(null);
        if(filesList != null) {
            buildFileList(filesList);
        }
    }

    public List<Path> getFilesPathList() {
        return filesPathList;
    }

    public void clearList(){
        this.filesPathList.clear();
    }

    public List<File> getFilesList(){
        return filesPathList.stream().map(path -> {
            if(Files.exists(path)) return path.toFile();
            return null;
        }).filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public void removePath(Path p) throws IOException {
        filesPathList.remove(p);
        saveToProperties();
    }

    /**
     * Method allowing to return all filenames stored in list
     * @return filenames stored in list
     */
    public List<String> getFileNames(){
        if(this.filesPathList != null && !this.filesPathList.isEmpty())
            return filesPathList.stream().map(f -> f.getFileName().toString()).collect(Collectors.toList());
        return null;
    }

    /**
     * save list of files chosen in this directory, if directory stays the same new values are added to existing, if directory changes list is erased.
     * @param filesList list list of files which paths should be stored
     */
    private void buildFileList(List<File> filesList) throws IOException {
        for (File f : filesList) {
            Path p = f.toPath();
            if(!filesPathList.contains(p)) {
                filesPathList.add(f.toPath());
            }
        }
        this.fileChooser.setInitialDirectory(filesPathList.get(filesPathList.size() - 1).getParent().toFile());
        saveToProperties();
    }

    private void saveToProperties() throws IOException {
        filesPathList.removeIf(p -> !Files.exists(p));
        Cfg.getInstance().setProperty(listNameProperty, filesPathList.toString());
        Cfg.getInstance().saveFile();
    }



}
