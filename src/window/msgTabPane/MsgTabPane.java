package window.msgTabPane;

import file.Loader;
import file.MailMsgParser;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.web.WebView;
import window.parsedMsgWindow.MsgWindowUpdater;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MsgTabPane {

    private TabPane tabPane;
    private List<File> fileArrayList;
    private HashMap<String, File> fileHashMap;
    private HashMap<String, Tab> tabHashMap;
    private HashMap<String, MailMsgParser> idToParserMap;
    private MsgWindowUpdater msgWindowUpdater;

    public MsgTabPane(){
        this.tabPane = new TabPane();
        this.tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        this.fileHashMap = new HashMap<>();
        this.tabHashMap = new HashMap<>();
        this.idToParserMap = new HashMap<>();
        this.fileArrayList = new ArrayList<>();
        this.msgWindowUpdater = msgWindowUpdater;
    }

    public void addTab(String name){
        Tab tab = new Tab(name);
        tab.setId(name);
        tab.setOnSelectionChanged(event -> {
            String id = getSelectedTabID();
            System.out.println(id);
           /* if(!id.isEmpty()){
                msgWindowUpdater.changeSelectedParser(idToParserMap.get(id));
            }*/

        });
        tabHashMap.put(name, tab);
        this.tabPane.getTabs().add(tab);
        System.out.println("added tab: " + name);
    }

    public void addAllTabs(ArrayList<String> names){
        for (String n : names){
            addTab(n);
        }
    }

    public void loadFileList(List<File> fileList){
        fileArrayList = fileList;
    }

    public ArrayList<String> getFileNames(){
        ArrayList<String> fileNames = new ArrayList<>();
        int i = 0;
        for (File f : fileArrayList){
            fileNames.add(f.getName());
            fileHashMap.put(f.getName(), f);
        }
        return fileNames;
    }

    public Tab getTab(String name){
        return tabHashMap.get(name);
    }

    public TabPane getTabPane() {
        return tabPane;
    }

    public void addContentToTab(String tabName, Node content){
        tabHashMap.get(tabName).setContent(content);
    }

    public void buildTabPane(){
        addAllTabs(getFileNames());

        for (String k : tabHashMap.keySet()){
            Loader loader = new Loader(fileHashMap.get(k));
            MailMsgParser mailMsgParser = new MailMsgParser(loader.readContent());
            idToParserMap.put(k, mailMsgParser);
            WebView webView = new WebView();
            webView.getEngine().loadContent(mailMsgParser.getOutputString());
            addContentToTab(k, webView);
            System.out.println(k + " added content to tab");
        }
        System.out.println(idToParserMap);
    }

    public List<File> getFileArrayList() {
        return fileArrayList;
    }

    public String getSelectedTabID(){
        String ID = "";
        for(Tab t : tabHashMap.values()){
            if (t.isSelected()){
                ID = t.getId();
            }
        }
        return ID;
    }


}
