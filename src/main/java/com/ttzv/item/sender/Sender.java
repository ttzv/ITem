package com.ttzv.item.sender;

import com.ttzv.item.properties.Cfg;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.MimeMessage;
import javafx.scene.control.Alert;


import java.util.Date;
import java.util.Properties;

public class Sender {

    private final Cfg AppConfiguration = Cfg.getInstance();

    private Session session;
    private Transport transport;
    private boolean validConn;

    private String smtpHost;
    private String smtpStartTLS;
    private String smtpPort;
    private String senderAddress;
    private char[] senderPassword;
    private String receiverAddress;
    private Alert alert;

    private MimeMessage msg;
    private String msgSubject;


    private String[] domainSuffix = {"atal.pl", "gmail.com"};
    private String addressPrefix;
    private String addressSuffix;


    public Sender() {

        //data parsed from properties.
        smtpHost = AppConfiguration.retrieveProp(Cfg.SMTP_HOST);
        smtpStartTLS = AppConfiguration.retrieveProp(Cfg.SMTP_TLS);
        smtpPort = AppConfiguration.retrieveProp(Cfg.SMTP_PORT);
        senderAddress = AppConfiguration.retrieveProp(Cfg.SMTP_LOGIN);

        receiverAddress = "";
        msgSubject = "";

        addressPrefix = "";
        addressSuffix = "";

        this.validConn = false;
    }

    public void initSession() {
        Properties props = new Properties();
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.starttls.enable", smtpStartTLS);
        props.put("mail.smtp.port", smtpPort);

        session = Session.getInstance(props, null);

        msg = new MimeMessage(session);
    }

    public void sendMail() {

        //System.out.println("sender settings = [" + smtpHost + "\n" + smtpStartTLS + "\n" + smtpPort + "\n" + senderAddress + "\n" + "******(password)");

        try{
            initConnection();
            // transport.connect(senderAddress, senderPassword);
            transport.sendMessage(msg, msg.getAllRecipients());
            //Transport.send(msg, senderAddress, senderPassword);
        } catch( MessagingException mex) {
            System.err.println("send failed, exception: " + mex);
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(mex.toString());
            alert.showAndWait();
        }

}

    public void initConnection() throws MessagingException {

            msg.setFrom(senderAddress);
            msg.setRecipients(Message.RecipientType.TO,
                    receiverAddress);
            msg.setSubject(msgSubject, "utf-8");
            msg.setSentDate(new Date());
            transport = session.getTransport("smtp");
            msg.saveChanges();
            transport.connect(senderAddress, new String(senderPassword));
            validConn = true;

    }

    public String validate() {
        String validationResult = "";
        if (smtpHost.isEmpty()) {
            validationResult += "Host field empty | ";
        }
        if (smtpStartTLS.isEmpty()) {
            validationResult += "TLS field empty | ";
        }
        if (smtpPort.isEmpty()) {
            validationResult += "SMTP field empty | ";
        }
        if (senderAddress.isEmpty()) {
            validationResult += "Login field empty | ";
        }
        if (senderPassword.length <= 0) {
            validationResult += "Pass field empty | ";
        }
        if (receiverAddress.isEmpty()) {
            validationResult += "Address field empty | ";
        }
        if (msgSubject.isEmpty()) {
            validationResult += "Subject field empty | ";
        }
        if (validationResult.isEmpty()) {
            return "ok";
        }
        return validationResult;
    }

    public void setSmtpHost(String smtpHost) {
        this.smtpHost = smtpHost;
    }

    public void setSmtpStartTLS(String smtpStartTLS) {
        this.smtpStartTLS = smtpStartTLS;
    }

    public void setSmtpPort(String smtpPort) {
        this.smtpPort = smtpPort;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public void setSenderPassword(char[] senderPassword) {
        this.senderPassword = senderPassword;
    }

    public void setAddressPrefix(String addressPrefix) {
        this.addressPrefix = addressPrefix;
    }

    public void setAddressSuffix(String addressSuffix) {
        this.addressSuffix = addressSuffix;
    }

    public void setMsg(String msg) throws MessagingException{
       if(!msg.isEmpty()) this.msg.setContent(msg, "text/html; charset=utf-8");
    }

    public void setMsgSubject(String msgSubject) {
        this.msgSubject = msgSubject;
    }

    public String getMsgSubject() {
        return msgSubject;
    }

    public String getSmtpHost() {
        return smtpHost;
    }

    public String getSmtpStartTLS() {
        return smtpStartTLS;
    }

    public String getSmtpPort() {
        return smtpPort;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public char[] getSenderPassword() {
        return senderPassword;
    }

    public String getReceiverAddress() {
        this.receiverAddress = addressPrefix + "@" +
                addressSuffix;
        return receiverAddress;
    }


    public String[] getDomainSuffix() {
        return domainSuffix;
    }

    public boolean isValidConn() {
        return validConn;
    }
}

