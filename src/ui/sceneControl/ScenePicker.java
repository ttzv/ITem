package ui.sceneControl;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.util.*;

public class ScenePicker {

    private List<Pane> scenes;
    private Integer activeScene;

    public ScenePicker(){
        this.scenes = new ArrayList<>();
        activeScene = -1;
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

    public Pane getPane(int index){
        this.activeScene = index;
        return this.scenes.get(index);
    }

}
