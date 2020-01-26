package com.ttzv.item.uiUtils;

import com.ttzv.item.file.Loader;
import com.ttzv.item.file.MailMsgParser;
import com.ttzv.item.properties.Cfg;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TabBuilder {

    private Loader loader;
    private MsgFileChooser msgFileChooser;
    private List<ViewTab> viewTabList;

    //used to determine if anything can be preloaded from properties, default false
    private boolean preload;

    public TabBuilder(){
        this.preload = false;

        this.loader = new Loader();

        this.msgFileChooser = new MsgFileChooser(Cfg.MSG_LIST);

        if(!msgFileChooser.getFilesList().isEmpty()){
            this.preload = true;
        }
        viewTabList = new ArrayList<>();
    }

    public void promptForChooser() throws IOException {
        this.msgFileChooser.show();
        builderLoop(msgFileChooser.getFilesList());
    }

    public void build() {
       builderLoop(msgFileChooser.getFilesList());
    }

    public void preload(){
        if(preload){
            builderLoop(msgFileChooser.getFilesList());
        }
    }

    public void builderLoop(List<File> list){
        for (File f : list) {
            loader.load(f);
            ViewTab viewTab = new ViewTab(f.getName(), new MailMsgParser(loader.readContent()), Paths.get(f.getPath()));
            if(!viewTabList.contains(viewTab)){
                viewTabList.add(viewTab);
            }
        }
    }

    public List<ViewTab> getViewTabList() {
        return viewTabList;
    }

    public boolean isReady(){
        return preload;
    }

    public ViewTab getSelectedTab(){
        for (ViewTab vt : getViewTabList()) {
            if(vt.isSelected()){
                return vt;
            }
        }
        return null;
    }

    public MsgFileChooser getMsgFileChooser() {
        return msgFileChooser;
    }
}
