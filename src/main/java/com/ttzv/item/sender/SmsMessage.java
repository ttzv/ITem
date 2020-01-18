package com.ttzv.item.sender;

/**
 * This class provides support for sending SMS using smsapi powered by LinkMobility (smsapi.pl)
 */
public class SmsMessage implements ItemMessage {
    @Override
    public String getReceiverAddress() {
        return null;
    }

    @Override
    public void setReceiverAddress(String rAddress) {

    }

    @Override
    public String getContentPreview() {
        return null;
    }

    @Override
    public String getTopic() {
        return null;
    }

    @Override
    public void setTopic(String topic) {

    }
}
