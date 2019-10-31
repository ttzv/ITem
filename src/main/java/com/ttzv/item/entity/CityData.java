package com.ttzv.item.entity;

public enum CityData {

    name("name"),
    landLineNumber("landLineNumber"),
    faxNumber("faxNumber"),
    postalCode("postalCode");

    String udname;

    CityData(String udname) {
        this.udname = udname;
    }
}
