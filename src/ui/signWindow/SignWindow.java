package ui.signWindow;

import file.Loader;
import file.SignatureParser;
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
import javafx.stage.FileChooser;
import properties.Cfg;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class SignWindow extends AnchorPane {

    private SignatureParser signatureParser;

    public SignWindow() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("signwdw.fxml"));
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

        textFieldEventBind(this.txtfName, SignatureParser.NAME);
        textFieldEventBind(this.txtfPos, SignatureParser.POSITION);
        textFieldEventBind(this.txtfPhone, SignatureParser.PHONE);
        textFieldEventBind(this.txtfMPhone, SignatureParser.MPHONE);
        textFieldEventBind(this.txtfCityPhone, SignatureParser.CITYPHONE);
        textFieldEventBind(this.txtfCityFax, SignatureParser.CITYFAX);
        txtfCityEvent();
        comBoxCityTypeAction();

        String signFilePath = Cfg.getInstance().retrieveProp(Cfg.SIGN_LOC);
        if(!signFilePath.isEmpty()){
            Loader loader = new Loader();
            loader.load(new File(signFilePath));
            signatureParser = new SignatureParser(loader.readContent());
            reload();
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
        loader.load(file);
        signatureParser = new SignatureParser(loader.readContent());
        reload();

        Cfg.getInstance().setProperty(Cfg.SIGN_LOC, file.getPath());
        Cfg.getInstance().saveFile();
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
    private void btnDeleteFaxAction(ActionEvent event) {
        if(!btnDeleteFax.isSelected()){
            signatureParser.showRows(19, 18);
        } else {
            signatureParser.hideRows(19, 18);
        }
        reload();
    }

    @FXML
    private void btnDeleteMPhoneAction(ActionEvent event) {
        if(!btnDeleteMPhone.isSelected()){
            signatureParser.showRows(10, 11);
        } else {
            signatureParser.hideRows(10, 11);
        }
        reload();
    }

    @FXML
    private void btnDeletePhoneAction(ActionEvent event) {
        if(!btnDeletePhone.isSelected()){
            signatureParser.showRows(7, 8);
        } else {
            signatureParser.hideRows(7, 8);
        }
        reload();
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

    private void reload(){
        webViewSignature.getEngine().loadContent(signatureParser.getOutputString());
    }


}
