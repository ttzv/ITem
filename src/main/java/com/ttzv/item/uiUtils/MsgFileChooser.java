package com.ttzv.item.uiUtils;

import com.ttzv.item.properties.Cfg;
import com.ttzv.item.utility.Utility;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class MsgFileChooser {

    private FileChooser fileChooser;
    private List<File> msgs;

    public MsgFileChooser() {
        this.fileChooser = new FileChooser();
        this.msgs = new ArrayList<>();

        //set initial dir if property exists
        String initProp = Cfg.getInstance().retrieveProp(Cfg.MsgParentPath);
        if(!initProp.isEmpty()) {
            File initDir = new File(initProp);
            if (initDir.exists()) {
                this.fileChooser.setInitialDirectory(initDir);
            }
        }
    }

    public void show(){
        msgs = fileChooser.showOpenMultipleDialog(null);
        if(msgs != null) {
            //saveParentPath();
            saveMsgList();
        } else
            msgs = new ArrayList<>(0);
    }

    public List<File> getMsgs() {
        return msgs;
    }

    //this takes first element from the list and saves parent dir in properties
    public void saveParentPath(){
        if(this.msgs.size() > 0) {
            String path = this.msgs.get(0).getParent();
            Cfg.getInstance().setProperty(Cfg.MsgParentPath, path);
            try {
                Cfg.getInstance().saveFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //returns list of file names
    public List<String> getFileNames(){
        List<String> msgNames = new ArrayList<>();
        if(this.msgs.size() > 0) {
            for (File f : msgs) {
                msgNames.add(f.getName());
            }
        }
        return msgNames;
    }

    /**
     * save list of files chosen in this directory, if directory stays the same new values are added to existing, if directory changes list is erased.
     */
    public void saveMsgList() {
        List<String> fileNames;
        //if msglist property doesnt exist or is empty and parentPath from properties is the same as parentpath of loaded file
        if ((Cfg.getInstance().retrieveProp(Cfg.MsgList) != null && !Cfg.getInstance().retrieveProp(Cfg.MsgList).isEmpty()) && Cfg.getInstance().retrieveProp(Cfg.MsgParentPath).equals(msgs.get(0).getParent()))
        {
            fileNames = Utility.stringToArray(Cfg.getInstance().retrieveProp(Cfg.MsgList));
            for (String fn : getFileNames()){
                if(!fileNames.contains(fn)){
                    fileNames.add(fn);
                }
            }
        } else {
            fileNames = getFileNames();
        }
            if (!fileNames.isEmpty()) {
                Cfg.getInstance().setProperty(Cfg.MsgList, fileNames.toString());
            }
            saveParentPath();
        }


    /**
     * If properties related to messages are present use this method to get List of files, only files that exists are placed in the list
     * @return List of files or empty list if properties were not present
     */
        public List<File> getInitialFileList(){
            //this works assuming all files are in the same directory, multiple diferent directories are not supported atm
        List<File> msgFiles = new ArrayList<>();

        String parentDir = Cfg.getInstance().retrieveProp(Cfg.MsgParentPath);
        String msgListString = Cfg.getInstance().retrieveProp(Cfg.MsgList);

        if(!parentDir.isEmpty() && !msgListString.isEmpty()){
            Path parentPath = FileSystems.getDefault().getPath(parentDir);
            ArrayList<String> msgList = Utility.stringToArray(msgListString);
            for (String s : msgList) {
                Path p = parentPath.resolve(FileSystems.getDefault().getPath(s));
                if(Files.exists(p)){
                    msgFiles.add(new File(p.toUri()));
                }
            }
            return msgFiles;
        }
        return msgFiles;
    }


}
