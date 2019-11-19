package com.ttzv.item.entity;

public enum CityData implements Mappable{

    name("name"),
    landLineNumber("landLineNumber"),
    faxNumber("faxNumber"),
    postalCode("postalCode"),
    type("Type");

    String udname;

    CityData(String udname) {
        this.udname = udname;
    }

    @Override
    public String toString() {
        return udname;
    }

    @Override
    public String getDbKey() {
        return getDbKey(new KeyMapper(KeyMapper.KEY_MAP_JSON_PATH, City.class));
    }

    @Override
    public String getDbKey(KeyMapper keyMapper) {
        return keyMapper.getCorrespondingMapping(this.toString(), KeyMapper.DBKEY);
    }

    @Override
    public String getLdapKey() {
        return getDbKey(new KeyMapper(KeyMapper.KEY_MAP_JSON_PATH, City.class));
    }

    @Override
    public String getLdapKey(KeyMapper keyMapper) {
        return keyMapper.getCorrespondingMapping(this.toString(), KeyMapper.LDAPKEY);
    }
}
