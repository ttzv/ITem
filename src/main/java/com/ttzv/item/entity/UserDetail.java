package com.ttzv.item.entity;

public class UserDetail implements DynamicEntityCompatible{

    private DynamicEntity userDetailEntity;

    private String guid;
    private String position;
    private String initMailPass;
    private String notes;

    public UserDetail(DynamicEntity entity) {
        this.userDetailEntity = entity;
    }

    @Override
    public String getUniqueIdentifier() {
        return getGuid();
    }

    @Override
    public DynamicEntity getEntity() {
        return this.userDetailEntity;
    }

    public String getGuid() {
        return this.userDetailEntity.getValue(UserDetailData.guid.toString()); }

    public String getPosition() {
        return this.userDetailEntity.getValue(UserDetailData.position.toString());
    }

    public void setPosition(String position) {
        this.userDetailEntity.setValue(UserDetailData.position.toString(), position);
    }

    public String getInitMailPass() {
        return this.userDetailEntity.getValue(UserDetailData.initMailPass.toString());
    }

    public void setInitMailPass(String initMailPass) {
        this.userDetailEntity.setValue(UserDetailData.initMailPass.toString(), initMailPass);
    }

    public String getNotes() {
        return this.userDetailEntity.getValue(UserDetailData.notes.toString());
    }

    public void setNotes(String notes) {
        this.userDetailEntity.setValue(UserDetailData.notes.toString(), notes);
    }
}
