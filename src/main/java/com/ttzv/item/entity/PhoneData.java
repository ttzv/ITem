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
    public String getDbKey(KeyMapper keyMapper) {
        return keyMapper.getCorrespondingMapping(this.toString(), KeyMapper.DBKEY);
    }

    @Override
    public String getLdapKey(KeyMapper keyMapper) {
        return keyMapper.getCorrespondingMapping(this.toString(), KeyMapper.LDAPKEY);
    }
}
