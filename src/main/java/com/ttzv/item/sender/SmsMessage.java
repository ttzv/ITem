package com.ttzv.item.sender;

/**
 * This class provides support for sending SMS using smsapi powered by LinkMobility (smsapi.pl)
 */
public class SmsMessage implements ItemMessage {

    private String text;
    private String recipientAddress;
    private String sender;


    @Override
    public String getRecipientAddress() {
        return this.recipientAddress;
    }

    @Override
    public void setRecipientAddress(String rAddress) {
        this.recipientAddress = rAddress;
    }

    @Override
    public String getContentPreview() {
        return null;
    }

    @Override
    public String getText() {
        return this.text;
    }

    @Override
    public String getTopic() {
        return null;
    }

    @Override
    public void setTopic(String topic) {}

    @Override
    public String getSender() {
        return this.sender;
    }

    @Override
    public void setSender(String sender) {
        this.sender = sender;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }
}
