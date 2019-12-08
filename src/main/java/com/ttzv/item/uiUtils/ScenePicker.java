package com.ttzv.item.uiUtils;

import com.ttzv.item.properties.Cfg;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ScenePicker {

    private List<Pane> scenes;
    private Integer activeScene;


    public ScenePicker() {

        activeScene = Integer.parseInt(Cfg.getInstance().retrieveProp(Cfg.ActiveWindow));

        this.scenes = new ArrayList<>();
    }

    public void addAll (Pane... panes){
        for(Pane p : panes){
            if(!this.scenes.contains(p)){
                this.scenes.add(p);
            }
        }
        System.out.println();
    }

    public void add (Pane pane){
        this.scenes.add(pane);
    }

    public Pane getScene(int index){
        this.activeScene = index;
        System.out.println(this.scenes);
        return this.scenes.get(index);
    }

    public int getActiveScene(){
        return activeScene;
    }

    public void setActiveScene(int as){
        this.activeScene = as;
            Cfg.getInstance().setProperty(Cfg.ActiveWindow, String.valueOf(activeScene));
        try {
            Cfg.getInstance().saveFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
