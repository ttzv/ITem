package com.ttzv.itmg.ui.signWindow;

import com.ttzv.itmg.ad.UserHolder;
import com.ttzv.itmg.file.Loader;
import com.ttzv.itmg.file.Saver;
import com.ttzv.itmg.file.SignatureParser;
import com.ttzv.itmg.uiUtils.TextFieldFormatters;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import com.ttzv.itmg.properties.Cfg;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SignWindow extends AnchorPane {

    private SignatureParser signatureParser;

    public SignWindow() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/signwdw.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize(){
        this.comBoxCityType.getItems().addAll("Centrala","Filia");
        this.comBoxCityType.getSelectionModel().select(1);

        TextFieldFormatters textFieldFormatters = new TextFieldFormatters();

        this.txtfMPhone.setTextFormatter(textFieldFormatters.selectTextFormatter(textFieldFormatters.FORMAT_MOBILE_NUMBER));

        textFieldEventBind(this.txtfName, SignatureParser.NAME);
        textFieldEventBind(this.txtfPos, SignatureParser.POSITION);
        textFieldEventBind(this.txtfPhone, SignatureParser.PHONE);
        textFieldEventBind(this.txtfMPhone, SignatureParser.MPHONE);
        textFieldEventBind(this.txtfCityPhone, SignatureParser.CITYPHONE);
        textFieldEventBind(this.txtfCityFax, SignatureParser.CITYFAX);
        txtfCityEvent();
        comBoxCityTypeAction();
        btnDeletePhoneAction();
        btnDeleteMPhoneAction();
        btnDeleteFaxAction();

        String signFilePath = Cfg.getInstance().retrieveProp(Cfg.SIGN_LOC);
        if(!signFilePath.isEmpty()){
            Loader loader = new Loader();
            boolean loaded = loader.load(new File(signFilePath));
            if(loaded) {
                signatureParser = new SignatureParser(loader.readContent());
                reload();
            }
        }




    }

    @FXML
    private WebView webViewSignature;

    @FXML
    private TextField txtfName;

    @FXML
    private TextField txtfPos;

    @FXML
    private TextField txtfPhone;

    @FXML
    private TextField txtfMPhone;

    @FXML
    private TextField txtfCityPhone;

    @FXML
    private TextField txtfCityFax;

    @FXML
    private ComboBox<String> comBoxCityType;

    @FXML
    private TextField txtfCity;

    @FXML
    private TextField htmlLine;

    @FXML
    private ToggleButton btnDeleteMPhone;

    @FXML
    private ToggleButton btnDeletePhone;

    @FXML
    private ToggleButton btnDeleteFax;

    @FXML
    private void load() throws IOException {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        Loader loader = new Loader();
        if ( loader.load(file) ) {
            signatureParser = new SignatureParser(loader.readContent());
            reload();
            Cfg.getInstance().setProperty(Cfg.SIGN_LOC, file.getPath());
            Cfg.getInstance().saveFile();
        }
    }

    @FXML
    private void btnCopyContentAction() {

        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.put(DataFormat.RTF, webViewSignature.getEngine().executeScript("document.selection.createrange()"));
        clipboard.setContent(content);


    }

    @FXML
    private void btnCopyHTMLAction() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.putString(signatureParser.getOutputString());
        clipboard.setContent(clipboardContent);
    }

    @FXML
    void btnOpenHtmlDirAction(ActionEvent event) throws IOException {
       openTargetDir();
    }

    private void openTargetDir() throws IOException {
        String cfgTargetPath = Cfg.getInstance().retrieveProp(Cfg.SIGN_TARGETPATH);
        if(!cfgTargetPath.isEmpty()) {
            Path path = Paths.get(cfgTargetPath, UserHolder.getCurrentUser().getDisplayName());
            if(Files.exists(path)) {
                Desktop desktop = Desktop.getDesktop();
                desktop.open(new File(path.toUri()));
            }
        }
    }

    @FXML
    void btnSaveHtmlFileAction(ActionEvent event) throws IOException {
        if (UserHolder.getCurrentUser() != null){
            Saver saver = new Saver(signatureParser.getOutputString());
            System.out.println(signatureParser.getOutputString());
            saver.setExtension(saver.HTM);
            saver.setFileName(UserHolder.getCurrentUser().getSamAccountName());

            String cfgTargetPath = Cfg.getInstance().retrieveProp(Cfg.SIGN_TARGETPATH);
            File targetPath;

            if(cfgTargetPath.isEmpty() || !Files.exists(Paths.get(cfgTargetPath)) ) {
                DirectoryChooser directoryChooser = new DirectoryChooser();
                targetPath = directoryChooser.showDialog(null);
                Cfg.getInstance().setProperty(Cfg.SIGN_TARGETPATH, targetPath.getPath());
                Cfg.getInstance().saveFile();

            } else {
                targetPath = new File(cfgTargetPath);
            }
            Path finalSavePath = targetPath.toPath().resolve(UserHolder.getCurrentUser().getDisplayName());
            if(!Files.exists(finalSavePath)){
                Files.createDirectory(finalSavePath);
            }

            saver.setTargetPath(finalSavePath);
            saver.saveFile();

            if(Cfg.getInstance().retrieveProp(Cfg.DIR_ALWAYSOPEN).equals("true")){
                openTargetDir();
            }
        }
    }


    private void btnDeleteFaxAction() {
        btnDeleteFax.setOnAction(event ->{
            if(btnDeleteFax.isSelected()){
                hideFax(true);
            } else {
                hideFax(false);
            }
            reload();
        });
    }


    private void btnDeleteMPhoneAction() {
        btnDeleteMPhone.setOnAction(event -> {
            if (btnDeleteMPhone.isSelected()) {
                hideMPhone(true);
            } else {
                hideMPhone(false);
            }
            reload();
        });
    }


    private void btnDeletePhoneAction() {
        btnDeletePhone.setOnAction(event -> {
            if (btnDeletePhone.isSelected()) {
                hidePhone(true);
            } else {
                hidePhone(false);
            }
            reload();
        });
    }


    private void comBoxCityTypeAction() {
        this.comBoxCityType.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            String cType = comBoxCityType.getSelectionModel().getSelectedItem();
            String city = txtfCity.getText();
            if (!cType.isEmpty() && !city.isEmpty()){
                signatureParser.setCity(cType + " " + city);
                reload();
            }
        });
    }

    private void textFieldEventBind(TextField textField, int bindTo){
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            signatureParser.setLine(textField.getText(), bindTo);
            reload();
        });
    }

    private void txtfCityEvent(){
        this.txtfCity.textProperty().addListener((observable, oldValue, newValue) -> {
            String cType = comBoxCityType.getSelectionModel().getSelectedItem();
            if(!cType.isEmpty()) {
                signatureParser.setCity(cType + " " + txtfCity.getText());
                reload();
            }
        });
    }

    public void reload(){
        webViewSignature.getEngine().loadContent(signatureParser.getOutputString());
    }

    public void setTxtfName(String txtfName) {
        this.txtfName.setText(txtfName);
    }

    public void setTxtfPos(String txtfPos) {
        this.txtfPos.setText(txtfPos);
    }

    public void setTxtfPhone(String txtfPhone) {
        String phone="";
        if(!txtfPhone.isEmpty()){
            phone = txtfPhone;
            hidePhone(false);
            btnDeletePhone.setSelected(false);
        } else {
            hidePhone(true);
            btnDeletePhone.setSelected(true);
        }
        this.txtfPhone.setText(phone);
    }

    public void setTxtfMPhone(String txtfMPhone) {
        String mPhone="";
        if(!txtfMPhone.isEmpty()){
            mPhone = txtfMPhone;
            hideMPhone(false);
            btnDeleteMPhone.setSelected(false);
        } else {
            hideMPhone(true);
            btnDeleteMPhone.setSelected(true);
        }
        this.txtfMPhone.setText(mPhone);
    }

    public void setTxtfCityPhone(String txtfCityPhone) {
        this.txtfCityPhone.setText(txtfCityPhone);
    }

    public void setTxtfCityFax(String txtfCityFax) {
        String cityFax = "";
        if(!txtfCityFax.isEmpty()) {
            cityFax = txtfCityFax;
            hideFax(false);
            btnDeleteFax.setSelected(false);
        } else {
            hideFax(true);
            btnDeleteFax.setSelected(true);
        }
        this.txtfCityFax.setText(cityFax);

    }

    public void setTxtfCity(String  txtfCity) {
        this.txtfCity.setText(txtfCity);
    }

    public void selectComboxVal(int val){
        this.comBoxCityType.getSelectionModel().select(val);
    }

    private void hidePhone(boolean hide){
        if(hide){
            signatureParser.hideRows(6, 7, 8);
        } else {
            signatureParser.showRows(6, 7, 8);
        }
    }
    private void hideMPhone(boolean hide){
        if(hide){
            signatureParser.hideRows(9, 10, 11);
        } else {
            signatureParser.showRows(9, 10, 11);
        }
    }
    private void hideFax(boolean hide){
        if(hide){
            signatureParser.hideRows(19, 18);
        } else {
            signatureParser.showRows(19, 18);
        }
    }
}
