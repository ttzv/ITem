package com.ttzv.item.entity;

public class Phone {

    private String ownerid;
    private String number;
    private String model;
    private String imei;
    private String pin;
    private String puk;

    private DynamicEntity phoneEntity;

    public Phone(DynamicEntity phoneEntity) {
        this.phoneEntity = phoneEntity;
    }

    public DynamicEntity getPhoneEntity() {
        return phoneEntity;
    }

    public String getOwnerid() {
        return this.phoneEntity.getValue(PhoneData.ownerid.toString());
    }

    public void setOwnerid(String ownerid) {
        this.ownerid = ownerid;
    }

    public String getNumber() {
        return this.phoneEntity.getValue(PhoneData.number.toString());
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getModel() {
        return this.phoneEntity.getValue(PhoneData.model.toString());
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getImei() {
        return this.phoneEntity.getValue(PhoneData.imei.toString());
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getPin() {
        return this.phoneEntity.getValue(PhoneData.pin.toString());
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getPuk() {
        return this.phoneEntity.getValue(PhoneData.puk.toString());
    }

    public void setPuk(String puk) {
        this.puk = puk;
    }
}
