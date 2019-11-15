package com.ttzv.item.entity;

public class City implements DynamicEntityCompatible, Comparable<City>{

    private String name;
    private String landLineNumber;
    private String faxNumber;
    private String postalCode;

    private DynamicEntity cityEntity;

    public City(DynamicEntity cityEntity) {
        this.cityEntity = cityEntity;
    }

    @Override
    public String getUniqueIdentifier() {
        return getName();
    }

    @Override
    public DynamicEntity getEntity() {
        return cityEntity;
    }

    public String getName() {
        return this.cityEntity.getValue(CityData.name.toString());
    }

    public void setName(String name) {
        this.cityEntity.setValue(CityData.name.toString(), name);
    }

    public String getLandLineNumber() {
        return this.cityEntity.getValue(CityData.landLineNumber.toString());
    }

    public void setLandLineNumber(String landLineNumber) {
        this.cityEntity.setValue(CityData.landLineNumber.toString(), landLineNumber);
    }

    public String getFaxNumber() {
        return this.cityEntity.getValue(CityData.faxNumber.toString());
    }

    public void setFaxNumber(String faxNumber) {
        this.cityEntity.setValue(CityData.faxNumber.toString(), faxNumber);
    }

    public String getPostalCode() {
        return this.cityEntity.getValue(CityData.postalCode.toString());
    }

    public void setPostalCode(String postalCode) {
        this.cityEntity.setValue(CityData.postalCode.toString(), postalCode);
    }

    @Override
    public int compareTo(City o) {
        return this.getName().compareTo(o.getName());
    }


}
