package com.ttzv.item.sender;

public interface ItemMessage {

    public String getReceiverAddress();
    public void setReceiverAddress(String rAddress);
    public String getContentPreview();
    public String getTopic();
    public void setTopic(String topic);
}
