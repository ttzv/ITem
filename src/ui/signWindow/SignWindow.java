package ui.signWindow;

import file.Loader;
import file.SignatureParser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

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

        textFieldEventBind(this.txtfName, 1);
        textFieldEventBind(this.txtfPos, 3);
        textFieldEventBind(this.txtfPhone, 5);
        textFieldEventBind(this.txtfMPhone, 7);
        textFieldEventBind(this.txtfCityPhone, 13);
        textFieldEventBind(this.txtfCityFax, 15);
        txtfCityEvent();
        comBoxCityTypeAction();

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
    private void load(){
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        this.webViewSignature.getEngine().load(file.toURI().toString());
        Loader loader = new Loader();
        loader.load(file);
        signatureParser = new SignatureParser(loader.readContent());
        System.out.println( signatureParser.getOutputString() );

    }

    @FXML
    private void gethtml() {
        String html = (String) webViewSignature.getEngine().executeScript("document.documentElement.outerHTML");
        System.out.println("html:" + html);
    }

    @FXML
    private void loadLine() {
        this.webViewSignature.getEngine().loadContent(this.htmlLine.getText());
    }

    @FXML
    private void btnDeleteFax(ActionEvent event) {

    }

    @FXML
    private void btnDeleteMPhone(ActionEvent event) {

    }

    @FXML
    private void btnDeletePhone(ActionEvent event) {
        signatureParser.deleteRow(5);
        signatureParser.deleteRow(6);
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
