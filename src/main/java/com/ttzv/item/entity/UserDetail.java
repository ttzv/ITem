package com.ttzv.item.entity;

public class UserDetail implements DynamicEntityCompatible{

    private DynamicEntity entity;

    private String guid;
    private String position;
    private String initMailPass;
    private String notes;

    public UserDetail(DynamicEntity entity) {
        this.entity = entity;
    }

    @Override
    public DynamicEntity getEntity() {
        return null;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getInitMailPass() {
        return initMailPass;
    }

    public void setInitMailPass(String initMailPass) {
        this.initMailPass = initMailPass;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
