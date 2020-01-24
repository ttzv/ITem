package com.ttzv.item.ui;

import com.ttzv.item.dao.UserComboWrapper;
import com.ttzv.item.entity.UserHolder;
import com.ttzv.item.properties.Cfg;
import com.ttzv.item.sender.SmsMessage;
import com.ttzv.item.sms.SmsApiClient;
import com.ttzv.item.utility.Utility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import pl.smsapi.exception.SmsapiException;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ResourceBundle;
import java.util.logging.LoggingMXBean;
import java.util.regex.Pattern;

public class SmsScn extends AnchorPane {

    private final int ACCENTED_CHAR_PENALTY = 90;
    private final int MESSAGE_CHAR_LIMIT = 160;
    private final int MESSAGE_ACCENTED_LIMIT = MESSAGE_CHAR_LIMIT - ACCENTED_CHAR_PENALTY;
    private final int MAX_SMS_NO = 3;
    private final String ACCENTED_CHARS = "^.*[ą|ć|ę|ł|ń|ó|ś|ź|ż|Ą|Ć|Ę|Ł|Ń|Ó|Ś|Ź|Ż].*$";

    private final UserComboWrapper userCombowrapper;
    private UserHolder userHolder;
    private Cfg cfg;
    private Pattern pattern;
    private Integer currentChars = 0;
    private Integer remChars = MESSAGE_CHAR_LIMIT;
    private Integer msgNo = 0;

    public SmsScn(UserHolder userHolder, UserComboWrapper userComboWrapper) {
        ResourceBundle langResourceBundle = ResourceBundle.getBundle("lang");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/smsscn.fxml"), langResourceBundle);
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        cfg = Cfg.getInstance();
        this.userHolder = userHolder;
        this.userCombowrapper = userComboWrapper;
        this.pattern = Pattern.compile(ACCENTED_CHARS, Pattern.MULTILINE);
    }

    void refreshAccountInfo(){
        SmsApiClient smsApiClient;
        try {
            smsApiClient = new SmsApiClient();
            setLabel_points(smsApiClient.getPoints().toString());
        } catch (IOException | GeneralSecurityException | SmsapiException e) {
            e.printStackTrace();
            WarningDialog.showAlert(Alert.AlertType.WARNING, e.toString());
            setLabel_points("-");
        }
    }

    void updateSender(){
        setTextfield_smsSender(cfg.retrieveProp(Cfg.SMSAPI_SENDER));
    }

    void updateUserLabels(UserHolder userHolder){
        setTextfield_smsRecipient(userHolder.getCurrentUser().getDisplayName());
        setTextfield_smsRecipientNumber(userCombowrapper.getPhoneOf(userHolder.getCurrentUser()).getNumber());
    }

    private void updateMsgLabels(){
        this.label_cchars.setText(currentChars.toString());
        this.label_remchars.setText(remChars.toString());
        this.label_msgno.setText(msgNo.toString());
    }

    public void setTextfield_smsSender(String textfield_smsSender) {
        this.textfield_smsSender.setText(textfield_smsSender);
    }

    public void setTextfield_smsRecipient(String textfield_smsRecipient) {
        this.textfield_smsRecipient.setText(textfield_smsRecipient);
    }

    public void setTextfield_smsRecipientNumber(String textfield_smsRecipientNumber) {
        this.textfield_smsRecipientNumber.setText(textfield_smsRecipientNumber);
    }

    public void setTextfield_smsVariable(String textfield_smsVariable) {
        this.textfield_smsVariable.setText(textfield_smsVariable);
    }

    public void setLabel_points(String label_points) {
        this.label_points.setText(label_points);
    }

    private void addTextAreaListener(){
        textarea_smscontent.textProperty().addListener((observableValue, s, t1) -> {
            this.currentChars = t1.length();
            if(pattern.matcher(t1).find()) {
                if(this.currentChars > (MESSAGE_ACCENTED_LIMIT) * MAX_SMS_NO){
                    textarea_smscontent.setText(t1.substring(0, MESSAGE_ACCENTED_LIMIT));
                } else {
                    this.msgNo = currentChars / (MESSAGE_ACCENTED_LIMIT) + 1;
                    this.remChars = (MESSAGE_ACCENTED_LIMIT) - currentChars % (MESSAGE_ACCENTED_LIMIT);
                }
            } else {
                if(this.currentChars > MESSAGE_CHAR_LIMIT * MAX_SMS_NO){
                    textarea_smscontent.setText(t1.substring(0, MESSAGE_CHAR_LIMIT));
                } else {
                    this.msgNo = currentChars / (MESSAGE_CHAR_LIMIT) + 1;
                    this.remChars = MESSAGE_CHAR_LIMIT - currentChars % MESSAGE_CHAR_LIMIT;
                }
            }
            updateMsgLabels();
        });
    }

    private void buildComboBox(){
        this.cbox_template.getItems().add("Dodaj...");
        this.cbox_template.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            //todo finish
        });
    }

    @FXML
    private TextField textfield_smsSender;

    @FXML
    private TextField textfield_smsRecipient;

    @FXML
    private TextField textfield_smsRecipientNumber;

    @FXML
    private TextField textfield_smsVariable;

    @FXML
    private ComboBox<String> cbox_template;

    @FXML
    private Label label_points;

    @FXML
    private Label label_cchars;

    @FXML
    private Label label_remchars;

    @FXML
    private Label label_msgno;

    @FXML
    private TextArea textarea_smscontent;

    @FXML
    public void initialize(){
        refreshAccountInfo();
        addTextAreaListener();
        updateSender();
    }

    @FXML
    void btnA_Send(ActionEvent event) {
        SmsMessage smsMessage = new SmsMessage();
        smsMessage.setText(this.textarea_smscontent.getText());
        smsMessage.setRecipientAddress(this.textfield_smsRecipientNumber.getText());
        smsMessage.setSender(this.textfield_smsSender.getText());

        try {
            SmsApiClient smsApiClient = new SmsApiClient();
            smsApiClient.sendSMS(smsMessage);
            refreshAccountInfo();
        } catch (IOException | GeneralSecurityException | SmsapiException e) {
            e.printStackTrace();
            WarningDialog.showAlert(Alert.AlertType.ERROR, e.toString());
        }
    }

    @FXML
    void btnA_addToQue(ActionEvent event) {

    }

    @FXML
    void refreshAccountInfo(ActionEvent event) {
        refreshAccountInfo();
    }

    @FXML
    void btn_removeAccents(ActionEvent event) {
        this.textarea_smscontent.setText(Utility.replaceAccents(textarea_smscontent.getText()));
    }

}
