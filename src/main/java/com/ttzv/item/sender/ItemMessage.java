package com.ttzv.item.sender;

public interface ItemMessage {

    public String getRecipientAddress();
    public void setRecipientAddress(String rAddress);
    public String getContentPreview();
    public String getText();
    public String getTopic();
    public void setTopic(String topic);
    public String getSender();
    public void setSender(String sender);
    public void setText(String text);
}
