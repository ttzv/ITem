package com.ttzv.item.entity;

public class CommandItem implements DynamicEntityCompatible {

    private String commandTitle;
    private String commandContents;
    private String tags;
    private String uid;

    private DynamicEntity commantItemEntity;

    public CommandItem(String uid) {
        this.commantItemEntity = new DynamicEntity();
        this.setUid(uid);
    }

    public CommandItem(String uid, String commandTitle, String commandContents, String tags) {
        this.commantItemEntity = new DynamicEntity();
        this.setCommandTitle(commandTitle);
        this.setCommandContents(commandContents);
        this.setTags(tags);
        this.setUid(uid);
    }

    public CommandItem(DynamicEntity commantItemEntity) {
        this.commantItemEntity = commantItemEntity;
    }

    public String getCommandTitle() {
        return this.commantItemEntity.getValue(CommandItemData.title.toString());
    }

    public void setCommandTitle(String commandTitle) {
        if(!this.commantItemEntity.setValue(CommandItemData.title.toString(), commandTitle)){
            this.commantItemEntity.add(CommandItemData.title.toString(), commandTitle);
        }
    }

    public String getCommandContents() {
        return this.commantItemEntity.getValue(CommandItemData.content.toString());
    }

    public void setCommandContents(String commandContents) {
        if(!this.commantItemEntity.setValue(CommandItemData.content.toString(), commandContents)){
            this.commantItemEntity.add(CommandItemData.content.toString(), commandContents);
        }
    }

    public String getTags() {
        return this.commantItemEntity.getValue(CommandItemData.tags.toString());
    }

    public void setTags(String tags) {
        if(!this.commantItemEntity.setValue(CommandItemData.tags.toString(), tags)){
            this.commantItemEntity.add(CommandItemData.tags.toString(), tags);
        }
    }

    public String getUid() {
        return this.commantItemEntity.getValue(CommandItemData.uid.toString());
    }

    public void setUid(String uid) {
        if(!this.commantItemEntity.setValue(CommandItemData.uid.toString(), uid)){
            this.commantItemEntity.add(CommandItemData.uid.toString(), uid);
        }
    }

    @Override
    public DynamicEntity getEntity() {
        return this.commantItemEntity;
    }

    @Override
    public String getUniqueIdentifier() {
        return this.getCommandTitle();
    }
}
