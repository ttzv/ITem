package com.ttzv.itmg.ad;

import java.util.stream.Stream;

public enum UserData {

            userGUID(           "userGUID",             0),
            samaccountname(     "samaccountname",       1),
            givenname(          "givenname",            2),
            sn(                 "sn",                   3),
            displayname(        "displayname",          4),
            useraccountcontrol( "useraccountcontrol",   5),
            mail(               "mail",                 6),
            whenCreated(        "whenCreated",          7),
            cityId(             "city",                 8),
            whenChanged(        "whenChanged",          9),
            initmailpass(       "initmailpass",         10),
            cityName(           "name",                 11),
            type(               "type",                 12),
            phone(              "phone",                13),
            fax(                "fax",                  14),
            position(           "position",             15),
            userphone(          "userphone",            16),
            usermphone(         "usermphone",           17);

            private final String colName;
            private final int id;

    UserData(String colName, int id) {
        this.colName = colName;
        this.id = id;
    }

    @Override
    public String toString() {
        return this.colName;
    }

    public int getId() {
        return id;
    }

    public String getColName() {
        return colName;
    }

    public static String getNameByID(int id){
        for (UserData uc : values()){
            if(uc.getId() == id){
                return uc.getColName();
            }
        }
        return null;
    }

    public static UserData getById(int id){
        for (UserData uc : values()){
            if(uc.getId() == id){
                return uc;
            }
        }
        return null;
    }

    /*public static String [] getAllColumns(){
        String[] columns = new String[values().length];
        for (int i = 0; i < values().length; i++) {
            columns[i] = values()[i].toString();
        }

        return columns;
    }*/

    public static String[] getAllColumns(){
        return Stream.of(UserData.values()).map(UserData::getColName).toArray(String[]::new);
    }
}
