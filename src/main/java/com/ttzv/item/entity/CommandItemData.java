package com.ttzv.item.entity;

public enum CommandItemData implements Mappable{

    title("title"),
    content("content"),
    tags("tags"),
    uid("uid");

    String name;

    CommandItemData(String name) {
        this.name = name;
    }

    @Override
    public String getDbKey() {
        return this.name;
    }

    @Override
    public String getDbKey(KeyMapper keyMapper) {
        return null;
    }

    @Override
    public String getLdapKey() {
        return null;
    }

    @Override
    public String getLdapKey(KeyMapper keyMapper) {
        return null;
    }
}
