package com.ttzv.item.entity;

public interface Mappable {

    String getDbKey(KeyMapper keyMapper);
    String getLdapKey(KeyMapper keyMapper);
}
