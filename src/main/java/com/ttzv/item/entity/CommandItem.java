package com.ttzv.item.entity;
import java.util.Set;

public class CommandItem implements DynamicEntityCompatible {

    private String commandTitle;
    private String commandContents;
    private String tags;

    private DynamicEntity commantItemEntity;

    public CommandItem(String commandTitle) {
        this.commantItemEntity = new DynamicEntity();
        this.setCommandTitle(commandTitle);
    }

    public CommandItem(String commandTitle, String commandContents, String tags) {
        this.commantItemEntity = new DynamicEntity();
        this.setCommandTitle(commandTitle);
        this.setCommandContents(commandContents);
        this.setTags(tags);
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

    @Override
    public DynamicEntity getEntity() {
        return this.commantItemEntity;
    }

    @Override
    public String getUniqueIdentifier() {
        return this.getCommandTitle();
    }
}
