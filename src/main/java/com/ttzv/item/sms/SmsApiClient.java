package com.ttzv.item.sms;

import com.ttzv.item.properties.Cfg;
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

public class SmsApiClient {

    private Client client;

    public SmsApiClient(String login, String passwordhash) throws ClientException {
        BasicAuthClient client = new BasicAuthClient(login, passwordhash);
        this.client = client;
    }

    public Client getClient(String login, String passwordhash) throws ClientException {
        return new BasicAuthClient(login, passwordhash);
    }

    public Client getClient(){
        return client;
    }

    public double getPoints() throws SmsapiException {
        UserFactory user = new UserFactory(client);
        UserGetPoints points = user.actionGetPoints();
        PointsResponse pointsResponse = points.execute();
        return pointsResponse.getPoints();
    }

    public void sendSMS(SmsMessage smsMessage) throws SmsapiException {
        SmsFactory sms = new SmsFactory(client);
        SMSSend smsSend = sms.actionSend()
                .setText(smsMessage.getText())
                .setTo(smsMessage.getReceiverAddress());
        StatusResponse response = smsSend.execute();
        for (MessageResponse status : response.getList()) {
            System.out.println(status.getNumber() + " " + status.getStatus());
        }
    }


    public static void main(String[] args) throws SmsapiException, IOException {
        Cfg.getInstance().init(null);

        SmsApiClient smsApiClient = new SmsApiClient(Cfg.getInstance().retrieveProp(Cfg.SMSAPI_LOGIN), Cfg.getInstance().retrieveProp(Cfg.SMSAPI_KEY));

        Client client = smsApiClient.getClient();

        UserFactory user = new UserFactory(client);
        UserGetPoints points = user.actionGetPoints();

        PointsResponse pointsResponse = points.execute();

        System.out.println("Points left: " + pointsResponse.getPoints());


/*
            SmsFactory smsApi = new SmsFactory(client);
            String phoneNumber = "609290066";
            SMSSend action = smsApi.actionSend()
                    .setText("test")
                    .setTo(phoneNumber);

            StatusResponse result = action.execute();

            for (MessageResponse status : result.getList() ) {
                System.out.println(status.getNumber() + " " + status.getStatus());
            }
        } catch (ClientException e) {
            //
            //101 Niepoprawne lub brak danych autoryzacji.
            //102 Nieprawidłowy login lub hasło
            //103 Brak punków dla tego użytkownika
            //105 Błędny adres IP
            //110 Usługa nie jest dostępna na danym koncie
            //1000 Akcja dostępna tylko dla użytkownika głównego
            //1001 Nieprawidłowa akcja
            //
            e.printStackTrace();
        } catch (SmsapiException e) {
            e.printStackTrace();
        }*/

    }
}
