package com.ttzv.itmg.ui.sceneControl;

import com.ttzv.itmg.properties.Cfg;
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
    }

    public void add (Pane pane){
        this.scenes.add(pane);
    }

    public Pane getScene(int index){
        this.activeScene = index;
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
