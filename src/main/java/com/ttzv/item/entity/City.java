package com.ttzv.item.entity;

public class City implements DynamicEntityCompatible, Comparable<City>{

    private String name;
    private String type;
    private String landLineNumber;
    private String faxNumber;
    private String postalCode;

    private DynamicEntity cityEntity;

    /**
     * Default constructor for entities created by-hand during application runtime.
     * Use setters to add new pairs to it's underlying DynamicEntity
     * @param name unique identifier, must not be null so it's enforced in constructor.
     */
    public City(String name){
        this.cityEntity = new DynamicEntity();
        this.setName(name);
    }

    /**
     * Constructor for Entities created by-hand during application runtime.
     * Allows to set all entity values at once during creation.
     */
    public City(String name, String landLineNumber, String faxNumber, String postalCode) {
        this.cityEntity = new DynamicEntity();
        this.setName(name);
        this.setLandLineNumber(landLineNumber);
        this.setFaxNumber(faxNumber);
        this.setPostalCode(postalCode);
    }

    /**
     * Constructor for Entities created automatically.
     * Not recommended to use this for by-hand creation
     * @param cityEntity DynamicEntity object created when pulling information from datasource.
     */
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
        if(!this.cityEntity.setValue(CityData.name.toString(), name)){
            this.cityEntity.add(CityData.name.toString(), name);
        }
    }

    public String getType() {
        return this.cityEntity.getValue(CityData.type.toString());
    }

    public void setType(String type) {
        if(!this.cityEntity.setValue(CityData.type.toString(), type)){
            this.cityEntity.add(CityData.type.toString(), type);
        }
    }

    public String getLandLineNumber() {
        return this.cityEntity.getValue(CityData.landLineNumber.toString());
    }

    public void setLandLineNumber(String landLineNumber) {
        if(!this.cityEntity.setValue(CityData.landLineNumber.toString(), landLineNumber)){
            this.cityEntity.add(CityData.landLineNumber.toString(), landLineNumber);
        }
    }

    public String getFaxNumber() {
        return this.cityEntity.getValue(CityData.faxNumber.toString());
    }

    public void setFaxNumber(String faxNumber) {
        if(!this.cityEntity.setValue(CityData.faxNumber.toString(), faxNumber)){
            this.cityEntity.add(CityData.faxNumber.toString(), faxNumber);
        }
    }

    public String getPostalCode() {
        return this.cityEntity.getValue(CityData.postalCode.toString());
    }

    public void setPostalCode(String postalCode) {
        if(!this.cityEntity.setValue(CityData.postalCode.toString(), postalCode)){
            this.cityEntity.add(CityData.postalCode.toString(), postalCode);
        }
    }

    @Override
    public int compareTo(City o) {
        return this.getName().compareTo(o.getName());
    }

    @Override
    public String toString() {
        return this.cityEntity.getList().toString();
    }
}
