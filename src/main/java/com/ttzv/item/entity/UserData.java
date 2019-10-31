package com.ttzv.item.entity;

import java.util.stream.Stream;

/**
 * UserData enum stores all possible user attributes that can be set inside application. This is not necessarily a must-have,
 * but it eases development a bit and allows compatibility with existing logic.
 */
public enum UserData {

            objectGUID("objectGUID"),
            samaccountname("samaccountname"),
            givenname("givenName"),
            sn("sn"),
            displayname("displayname"),
            useraccountcontrol("useraccountcontrol"),
            mail("mail"),
            whenCreated("whenCreated"),
            distinguishedName("distinguishedName"),
            whenChanged("whenChanged"),
            initmailpass("initmailpass"),
            cityName("city"),
            cityType("cityType"),
            cityPhone("cityPhone"),
            cityfax("cityFax"),
            position("position"),
            userphone("userphone"),
            usermphone("usermphone"),
            userlandline("userlandline");


            String name;
            
    UserData(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
