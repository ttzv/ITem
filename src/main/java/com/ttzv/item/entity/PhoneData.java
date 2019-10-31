package com.ttzv.item.entity;

public enum PhoneData {

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
}
