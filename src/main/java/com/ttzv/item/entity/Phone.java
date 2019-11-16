package com.ttzv.item.entity;

public class Phone implements DynamicEntityCompatible, Comparable<Phone>
{
    private DynamicEntity phoneEntity;

    private String ownerid; //guid
    private String number;
    private String model;
    private String imei;
    private String pin;
    private String puk;

    public Phone(String ownerid) {
        this.phoneEntity = new DynamicEntity();
        this.setOwnerid(ownerid);
    }

    public Phone(String ownerid, String number, String model, String imei, String pin, String puk) {
        this.phoneEntity = new DynamicEntity();
        this.setOwnerid(ownerid);
        this.setNumber(number);
        this.setModel(model);
        this.setImei(imei);
        this.setPin(pin);
        this.setPuk(puk);
    }

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
        if(!this.phoneEntity.setValue(PhoneData.ownerid.toString(), ownerid)){
            this.phoneEntity.add(PhoneData.ownerid.toString(), ownerid);
        }
    }

    public String getNumber() {
        return this.phoneEntity.getValue(PhoneData.number.toString());
    }

    public void setNumber(String number) {
        if(!this.phoneEntity.setValue(PhoneData.number.toString(), number)){
            this.phoneEntity.add(PhoneData.number.toString(), number);
        }
    }

    public String getModel() {
        return this.phoneEntity.getValue(PhoneData.model.toString());
    }

    public void setModel(String model) {
        if(!this.phoneEntity.setValue(PhoneData.model.toString(), model)){
            this.phoneEntity.add(PhoneData.model.toString(), model);
        }
    }

    public String getImei() {
        return this.phoneEntity.getValue(PhoneData.imei.toString());
    }

    public void setImei(String imei) {
        if(!this.phoneEntity.setValue(PhoneData.imei.toString(), imei)){
            this.phoneEntity.add(PhoneData.imei.toString(), imei);
        }
    }

    public String getPin() {
        return this.phoneEntity.getValue(PhoneData.pin.toString());
    }

    public void setPin(String pin) {
        if(!this.phoneEntity.setValue(PhoneData.pin.toString(), pin)){
            this.phoneEntity.add(PhoneData.pin.toString(), pin);
        }
    }

    public String getPuk() {
        return this.phoneEntity.getValue(PhoneData.puk.toString());
    }

    public void setPuk(String puk) {
        if(!this.phoneEntity.setValue(PhoneData.puk.toString(), puk)){
            this.phoneEntity.add(PhoneData.puk.toString(), puk);
        }
    }

    @Override
    public int compareTo(Phone o) {
        return this.getNumber().compareTo(o.getNumber());
    }

    @Override
    public String toString() {
        return this.phoneEntity.getList().toString();
    }
}
