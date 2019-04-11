package com.ttzv.itmg.uiUtils;

import java.util.HashMap;

public class UiObjectsWrapper {

    public final Integer MainWindow = 1;
    public final Integer MailerWindow = 2;


    private HashMap<Integer, Object> map;

    public UiObjectsWrapper() {
        map = new HashMap<>();
    }

    public void registerObject(Integer hash, Object object){
        map.put(hash,object);
    }

    public Object retrieveObject(Integer hash){
        if(map.keySet().contains(hash)){
            return map.get(hash);
        }
        return null;
    }
}
