package com.ttzv.item.entity;

public class Phone implements DynamicEntityCompatible, Comparable<Phone>
{

    private String ownerid; //guid
    private String number;
    private String model;
    private String imei;
    private String pin;
    private String puk;

    private DynamicEntity phoneEntity;

    public Phone(DynamicEntity phoneEntity) {
        this.phoneEntity = phoneEntity;
    }

    @Override
    public String getUniqueIdentifier() {
        return getOwnerid();
    }

    @Override
    public DynamicEntity getEntity() {
        return phoneEntity;
    }

    public String getOwnerid() {
        return this.phoneEntity.getValue(PhoneData.ownerid.toString());
    }

    public void setOwnerid(String ownerid) {
        this.phoneEntity.setValue(PhoneData.ownerid.toString(), ownerid);
    }

    public String getNumber() {
        return this.phoneEntity.getValue(PhoneData.number.toString());
    }

    public void setNumber(String number) {
        this.phoneEntity.setValue(PhoneData.number.toString(), number);
    }

    public String getModel() {
        return this.phoneEntity.getValue(PhoneData.model.toString());
    }

    public void setModel(String model) {
        this.phoneEntity.setValue(PhoneData.model.toString(), model);
    }

    public String getImei() {
        return this.phoneEntity.getValue(PhoneData.imei.toString());
    }

    public void setImei(String imei) {
        this.phoneEntity.setValue(PhoneData.imei.toString(), imei);
    }

    public String getPin() {
        return this.phoneEntity.getValue(PhoneData.pin.toString());
    }

    public void setPin(String pin) {
        this.phoneEntity.setValue(PhoneData.pin.toString(), pin);
    }

    public String getPuk() {
        return this.phoneEntity.getValue(PhoneData.puk.toString());
    }

    public void setPuk(String puk) {
        this.phoneEntity.setValue(PhoneData.puk.toString(), puk);
    }

    @Override
    public int compareTo(Phone o) {
        return this.getNumber().compareTo(o.getNumber());
    }
}
