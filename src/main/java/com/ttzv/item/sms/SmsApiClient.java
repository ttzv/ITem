package com.ttzv.item.sms;

import com.ttzv.item.properties.Cfg;
import com.ttzv.item.pwSafe.PHolder;
import com.ttzv.item.sender.SmsMessage;
import pl.smsapi.BasicAuthClient;
import pl.smsapi.Client;
import pl.smsapi.api.SmsFactory;
import pl.smsapi.api.UserFactory;
import pl.smsapi.api.action.sms.SMSSend;
import pl.smsapi.api.action.user.UserGetPoints;
import pl.smsapi.api.response.MessageResponse;
import pl.smsapi.api.response.PointsResponse;
import pl.smsapi.api.response.StatusResponse;
import pl.smsapi.exception.ClientException;
import pl.smsapi.exception.SmsapiException;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class SmsApiClient {

    private Client client;

    public SmsApiClient(String login, String passwordhash) throws SmsapiException {
        if(!passwordhash.isBlank()) {
            BasicAuthClient client = new BasicAuthClient(login, passwordhash);
            this.client = client;
            getPoints(client);
        } else {
            throw new ClientException("Password is blank.");
        }
    }

    //todo: limit creation of new client objects by checking if login or password has changed, if not use old valid client.
    public SmsApiClient() throws IOException, GeneralSecurityException, SmsapiException {
        this(Cfg.getInstance().retrieveProp(Cfg.SMSAPI_LOGIN),new String(PHolder.Sms()));
    }

    public Client getClient(String login, String passwordhash) throws ClientException {
        BasicAuthClient client = new BasicAuthClient(login, passwordhash);
        try {
            getPoints(client);
        } catch (SmsapiException e) {
            e.printStackTrace();
            client = null;
        }
        return client;
    }

    public Client getClient(){
        return client;
    }

    public Double getPoints() throws SmsapiException {
        return getPoints(this.client);
    }

    public double getPoints(Client client) throws SmsapiException {
        UserFactory user = new UserFactory(client);
        UserGetPoints points = user.actionGetPoints();
        PointsResponse pointsResponse = points.execute();
        return pointsResponse.getPoints();
    }

    public void sendSMS(SmsMessage smsMessage) throws SmsapiException {
        this.sendSMS(smsMessage, this.client);
    }

    public void sendSMS(SmsMessage smsMessage, Client client) throws SmsapiException {
        SmsFactory sms = new SmsFactory(client);
        SMSSend smsSend = sms.actionSend()
                .setSender(smsMessage.getSender())
                .setText(smsMessage.getText())
                .setTo(smsMessage.getRecipientAddress());
        StatusResponse response = smsSend.execute();
        for (MessageResponse status : response.getList()) {
            System.out.println(status.getNumber() + " " + status.getStatus());
        }
    }
}
