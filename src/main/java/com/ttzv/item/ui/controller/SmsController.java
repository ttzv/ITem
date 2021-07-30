package com.ttzv.item.ui.controller;

import com.ttzv.item.entity.UserHolder;
import com.ttzv.item.file.Loader;
import com.ttzv.item.properties.Cfg;
import com.ttzv.item.sender.SmsMessage;
import com.ttzv.item.sms.SmsApiClient;
import com.ttzv.item.uiUtils.DialogFactory;
import com.ttzv.item.uiUtils.FileNodeWrapper;
import com.ttzv.item.uiUtils.MsgFileChooser;
import com.ttzv.item.utility.Utility;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import pl.smsapi.exception.SmsapiException;

import java.io.IOException;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.util.regex.Pattern;

public class SmsController extends AnchorPane {

    private final Cfg AppConfiguration = Cfg.getInstance();
    private final int ACCENTED_CHAR_PENALTY = 90;
    private final int MESSAGE_CHAR_LIMIT = 160;
    private final int MESSAGE_ACCENTED_LIMIT = MESSAGE_CHAR_LIMIT - ACCENTED_CHAR_PENALTY;
    private final int MAX_SMS_NO = 3;
    private final String ACCENTED_CHARS = "^.*[ą|ć|ę|ł|ń|ó|ś|ź|ż|Ą|Ć|Ę|Ł|Ń|Ó|Ś|Ź|Ż].*$";

    private Pattern pattern;
    private Integer currentChars = 0;
    private Integer remChars = MESSAGE_CHAR_LIMIT;
    private Integer msgNo = 0;
    private MsgFileChooser msgFileChooser;
    private Loader loader;

    public void refreshAccountInfo(){
        SmsApiClient smsApiClient;
        try {
            smsApiClient = new SmsApiClient();
            setLabel_points(smsApiClient.getPoints().toString());
        } catch (IOException | GeneralSecurityException | SmsapiException e) {
            e.printStackTrace();
            DialogFactory.showAlert(Alert.AlertType.WARNING, e.toString());
            setLabel_points("-");
        }
    }

    void updateSender(){
        setTextfield_smsSender(AppConfiguration.retrieveProp(Cfg.SMSAPI_SENDER));
    }

    void updateUserLabels(UserHolder userHolder){
        setTextfield_smsRecipient(userHolder.getCurrentUser().getDisplayName());
        setTextfield_smsRecipientNumber(userHolder.getCurrentUser().getPhoneNumber());
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

    private void loadTemplateHandler(){
        this.cbox_template.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            loader.load(t1.getPath());
            this.textarea_smscontent.setText(loader.readContent().toString());
        });
    }

    private void buildComboBox(){
        cbox_template.getItems().clear();
        if(!msgFileChooser.getFilesPathList().isEmpty())
        for (Path p :
                msgFileChooser.getFilesPathList()) {
            cbox_template.getItems().add(new FileNodeWrapper(p, p.getFileName().toString()));
        }
    }

    private void clearComboBox(){
        cbox_template.getItems().clear();
        msgFileChooser.clearList();
    }

    @FXML
    private TextField textfield_smsSender;

    @FXML
    private TextField textfield_smsRecipient;

    @FXML
    private TextField textfield_smsRecipientNumber;

    @FXML
    private ComboBox<FileNodeWrapper> cbox_template;

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
        loader = new Loader();
        pattern = Pattern.compile(ACCENTED_CHARS);
        this.msgFileChooser = new MsgFileChooser(Cfg.SMS_LIST);
        addTextAreaListener();
        updateSender();
        buildComboBox();
        loadTemplateHandler();
    }

    @FXML
    void btnA_Send(ActionEvent event) throws IOException {
        Stage waitWindow = DialogFactory.getWaitWindow();

        Task<Boolean> wait = new Task<>() {
            @Override
            protected Boolean call(){
            SmsMessage smsMessage = new SmsMessage();
            smsMessage.setText(textarea_smscontent.getText());
            smsMessage.setRecipientAddress(textfield_smsRecipientNumber.getText());
            smsMessage.setSender(textfield_smsSender.getText());
            try {
                SmsApiClient smsApiClient = new SmsApiClient();
                smsApiClient.sendSMS(smsMessage);
                Platform.runLater(() -> refreshAccountInfo());
            } catch (IOException | GeneralSecurityException | SmsapiException e) {
                e.printStackTrace();
                DialogFactory.showAlert(Alert.AlertType.ERROR, e.toString());
            }
            return Boolean.TRUE;
            }
        };
        wait.setOnRunning(workerStateEvent -> {
            waitWindow.show();
        });
        wait.setOnSucceeded(workerStateEvent -> {
            waitWindow.close();
        });
        wait.setOnFailed(workerStateEvent -> {
            waitWindow.close();
            DialogFactory.showAlert(Alert.AlertType.ERROR, wait.getException().toString());
        });
        new Thread(wait).start();


    }

    @FXML
    void btnA_addToQue(ActionEvent event) {

    }

    @FXML
    void refreshAccountInfoEvent(ActionEvent event) {
        refreshAccountInfo();
    }

    @FXML
    void btn_removeAccents(ActionEvent event) {
        this.textarea_smscontent.setText(Utility.replaceAccents(textarea_smscontent.getText()));
    }

    @FXML
    void btnA_addTemplates(ActionEvent event) {
        try {
            msgFileChooser.show();
        } catch (IOException e) {
            e.printStackTrace();
            DialogFactory.showAlert(Alert.AlertType.WARNING, e.toString());
        }

        buildComboBox();
    }

    @FXML
    void btnA_clearTemplates(ActionEvent event) {
        clearComboBox();
    }

}
