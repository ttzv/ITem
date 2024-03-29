package com.ttzv.item.uiUtils;

import com.ttzv.item.properties.Cfg;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ScenePicker {

    private List<Pane> scenes;
    private Integer activeScene;
    private final Cfg AppConfiguration = Cfg.getInstance();

    public ScenePicker() {
        activeScene = Integer.parseInt(AppConfiguration.retrieveProp(Cfg.ACTIVE_WINDOW));

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

    public Pane pickScene(int index){
        this.activeScene = index;
        return this.scenes.get(index);
    }

    public int getActiveScene(){
        return activeScene;
    }

    public void setActiveScene(int as){
        this.activeScene = as;
            AppConfiguration.setProperty(Cfg.ACTIVE_WINDOW, String.valueOf(activeScene));
        try {
            AppConfiguration.saveFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Pane> getScenes() {
        return scenes;
    }
}
