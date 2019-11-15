package com.ttzv.item.entity;

public enum PhoneData implements Mappable{

    ownerid("ownerid"),
    number("number"),
    model("model"),
    imei("imei"),
    pin("pin"),
    puk("puk");

    String name;

    PhoneData(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public String getDbKey() {
        return getDbKey(new KeyMapper(KeyMapper.KEY_MAP_JSON_PATH, Phone.class));
    }

    @Override
    public String getDbKey(KeyMapper keyMapper) {
        return keyMapper.getCorrespondingMapping(this.toString(), KeyMapper.DBKEY);
    }

    @Override
    public String getLdapKey() {
        return getDbKey(new KeyMapper(KeyMapper.KEY_MAP_JSON_PATH, Phone.class));
    }

    @Override
    public String getLdapKey(KeyMapper keyMapper) {
        return keyMapper.getCorrespondingMapping(this.toString(), KeyMapper.LDAPKEY);
    }
}
