package com.ttzv.item.entity;

public class City {

    private String name;
    private String landLineNumber;
    private String faxNumber;
    private String postalCode;

    private DynamicEntity cityEntity;

    public City(DynamicEntity cityEntity) {
        this.cityEntity = cityEntity;
    }

    public DynamicEntity getCityEntity() {
        return cityEntity;
    }

    public String getName() {
        return this.cityEntity.getValue(CityData.name.toString());
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLandLineNumber() {
        return this.cityEntity.getValue(CityData.landLineNumber.toString());
    }

    public void setLandLineNumber(String landLineNumber) {
        this.landLineNumber = landLineNumber;
    }

    public String getFaxNumber() {
        return this.cityEntity.getValue(CityData.faxNumber.toString());
    }

    public void setFaxNumber(String faxNumber) {
        this.faxNumber = faxNumber;
    }

    public String getPostalCode() {
        return this.cityEntity.getValue(CityData.postalCode.toString());
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}
