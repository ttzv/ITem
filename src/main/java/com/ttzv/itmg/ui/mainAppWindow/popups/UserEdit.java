package com.ttzv.itmg.ui.mainAppWindow.popups;

import com.ttzv.itmg.ad.User;
import com.ttzv.itmg.ad.UserHolder;
import com.ttzv.itmg.db.DbCon;
import com.ttzv.itmg.db.PgStatement;
import com.ttzv.itmg.ui.mainAppWindow.MainWindow;
import com.ttzv.itmg.uiUtils.UiObjectsWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.SQLException;

public class UserEdit extends AnchorPane {

    private Stage stage;
    private UiObjectsWrapper uiObjectsWrapper;

    public UserEdit(UiObjectsWrapper uiObjectsWrapper) {
        this.uiObjectsWrapper = uiObjectsWrapper;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/useredit.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            AnchorPane root = fxmlLoader.load();
            stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));
        }catch(Exception e) {
            e.printStackTrace();
        }
        stage.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue){
                stage.close();
            }
        });

    }

    public void showAt(double x, double y){
        this.labelUsername.setText(UserHolder.getCurrentUser().getDisplayName());
        preloadInfo(UserHolder.getCurrentUser());

        stage.setX(x);
        stage.setY(y);
        stage.show();
    }

    @FXML
    private void initialize(){
        this.txtUserMPhone.setText("");
        this.txtUserPhone.setText("");
        this.txtUserPos.setText("");
        //System.out.println("initialized");
    }

    @FXML
    private TextField txtUserPos;

    @FXML
    private TextField txtUserPhone;

    @FXML
    private TextField txtUserMPhone;

    @FXML
    private Label labelUsername;

    @FXML
    private TextField txtfPhonePin;

    @FXML
    private TextField txtfPhonePUK;

    @FXML
    private TextField txtfUserEmailAddress;

    @FXML
    private TextField txtfUserEmailInitPass;

    @FXML
    void btnSaveAndClose(ActionEvent event) throws SQLException {
        updateDatabase(UserHolder.getCurrentUser());
        stage.close();
    }

    private void preloadInfo(User user){
        this.txtUserPos.setText(user.getPosition());
        this.txtUserPhone.setText(user.getUserPhone());
        this.txtUserMPhone.setText(user.getUserMPhone());
        this.txtfUserEmailAddress.setText(user.getMail());
        this.txtfUserEmailInitPass.setText(user.getInitMailPass());
    }

    private void updateDatabase(User user) throws SQLException {
        DbCon dbCon = new DbCon();
        dbCon.loadCfgCredentials();
        dbCon.initConnection();


        String position = this.txtUserPos.getText();
        String queryPos = "";
        if(!position.isEmpty() || !position.equals(UserHolder.getCurrentUser().getPosition())) {
            queryPos = PgStatement.update("users", "position", PgStatement.apostrophied(position), "samaccountname='" + user.getSamAccountName() + "'");
        }

        String phone = this.txtUserPhone.getText();
        String queryPhone = "";
        if(!phone.isEmpty() || !phone.equals(UserHolder.getCurrentUser().getUserPhone())) {
            queryPhone = PgStatement.update("users", "userphone", PgStatement.apostrophied(phone), "samaccountname='" + user.getSamAccountName() + "'");
        }

        String mPhone = this.txtUserMPhone.getText();
        String queryMPhone = "";
        if(!mPhone.isEmpty() || !mPhone.equals(UserHolder.getCurrentUser().getUserMPhone())) {
            queryMPhone = PgStatement.update("users", "usermphone", PgStatement.apostrophied(mPhone), "samaccountname='" + user.getSamAccountName() + "'");
        }

        String email = this.txtfUserEmailAddress.getText();
        String queryEmail = "";
        if(!email.isEmpty() || !email.equals(UserHolder.getCurrentUser().getMail())) {
            queryEmail = PgStatement.update("users", "mail", PgStatement.apostrophied(email), "samaccountname='" + user.getSamAccountName() + "'");
        }

        String emailPass = this.txtfUserEmailInitPass.getText();
        String queryEmailPass = "";
        if(!emailPass.isEmpty() || !emailPass.equals(UserHolder.getCurrentUser().getInitMailPass())) {
            queryEmailPass = PgStatement.update("users", "initmailpass", PgStatement.apostrophied(emailPass), "samaccountname='" + user.getSamAccountName() + "'");
        }


        //System.out.println(queryPos + "\n" + queryPhone + "\n" + queryMPhone);

        dbCon.customStatement(queryPos, queryPhone, queryMPhone, queryEmail, queryEmailPass);

        User currentUser = UserHolder.getCurrentUser();
        UserHolder.setCurrentUser(dbCon.reloadUser(currentUser));
        MainWindow mainWindow = (MainWindow) uiObjectsWrapper.retrieveObject(uiObjectsWrapper.MainWindow);
        mainWindow.changeUser();
    }







}
