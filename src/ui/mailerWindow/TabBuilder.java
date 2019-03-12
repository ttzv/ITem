package ui.mailerWindow;

import file.Loader;
import file.MailMsgParser;

import java.io.File;
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

        this.msgFileChooser = new MsgFileChooser();

        if(!msgFileChooser.getInitialFileList().isEmpty()){
            this.preload = true;
        }
        viewTabList = new ArrayList<>();
    }

    public void promptForChooser(){
        this.msgFileChooser.show();
        builderLoop(msgFileChooser.getMsgs());
    }

    public void build() {
       builderLoop(msgFileChooser.getMsgs());
    }

    public void preload(){
        if(preload){
            builderLoop(msgFileChooser.getInitialFileList());
        }
    }

    public void builderLoop(List<File> list){
        for (File f : list) {
            loader.load(f);
            ViewTab viewTab = new ViewTab(f.getName(), new MailMsgParser(loader.readContent()));
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

}
