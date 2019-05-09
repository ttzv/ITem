package com.ttzv.itmg.ad;

public enum UserColumns {

            samaccountname("samaccountname"),
            givenname("givenname"),
            sn("sn"),
            displayname("displayname"),
            useraccountcontrol("useraccountcontrol"),
            mail("mail"),
            whenCreated("whenCreated"),
            initmailpass("initmailpass"),
            city("city"),
            name("name"),
            type("type"),
            phone("phone"),
            fax("fax"),
            position("position"),
            userphone("userphone"),
            usermphone("usermphone");

            private final String colName;

    UserColumns(String colName) {
        this.colName = colName;
    }

    @Override
    public String toString() {
        return this.colName;
    }

    //    public String[] getAll
}
