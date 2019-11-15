package com.ttzv.item.entity;

public interface Mappable {

    String getDbKey();
    String getDbKey (KeyMapper keyMapper);
    String getLdapKey();
    String getLdapKey(KeyMapper keyMapper);
}
