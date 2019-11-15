package com.ttzv.item.entity;

import java.util.stream.Stream;

/**
 * UserData enum stores all possible user attributes that can be set inside application. This is not necessarily a must-have,
 * but it eases development a bit and allows compatibility with existing logic.
 */
public enum UserData implements Mappable{

    objectGUID("guid"),
    samaccountname("samaccountname"),
    givenname("givenName"),
    sn("sn"),
    displayname("displayname"),
    useraccountcontrol("useraccountcontrol"),
    mail("mail"),
    whenCreated("whenCreated"),
    distinguishedName("distinguishedName"),
    city("City"),
    whenChanged("whenChanged");

    String name;
            
    UserData(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public String getDbKey() {
        return getDbKey(new KeyMapper(KeyMapper.KEY_MAP_JSON_PATH, User.class));
    }

    @Override
    public String getDbKey(KeyMapper keyMapper) {
        return keyMapper.getCorrespondingMapping(this.toString(), KeyMapper.DBKEY);
    }

    @Override
    public String getLdapKey() {
        return getDbKey(new KeyMapper(KeyMapper.KEY_MAP_JSON_PATH, User.class));
    }

    @Override
    public String getLdapKey(KeyMapper keyMapper) {
        return keyMapper.getCorrespondingMapping(this.toString(), KeyMapper.LDAPKEY);
    }
}
