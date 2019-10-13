package com.ttzv.item.ui.mainAppWindow.popups;

import com.ttzv.item.ad.DbCell;
import com.ttzv.item.ad.User;
import com.ttzv.item.ad.UserHolder;
import com.ttzv.item.db.DbCon;
import com.ttzv.item.db.PgStatement;
import com.ttzv.item.ui.mainAppWindow.MainWindow;
import com.ttzv.item.uiUtils.DatabaseBoundTextField;
import com.ttzv.item.uiUtils.UiObjectsWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
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
    private DatabaseBoundTextField<DbCell> txtUserPos;

    @FXML
    private DatabaseBoundTextField<DbCell> txtUserPhone;

    @FXML
    private DatabaseBoundTextField<DbCell> txtUserMPhone;

    @FXML
    private Label labelUsername;

    @FXML
    private DatabaseBoundTextField<DbCell> txtfPhonePin;

    @FXML
    private DatabaseBoundTextField<DbCell> txtfPhonePUK;

    @FXML
    private DatabaseBoundTextField<DbCell> txtfUserEmailAddress;

    @FXML
    private DatabaseBoundTextField<DbCell> txtfUserEmailInitPass;

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
        String table = "users";

        DbCon dbCon = new DbCon();
        dbCon.loadCfgCredentials();
        dbCon.initConnection();

        this.txtUserPos.
                setDbCell(new DbCell(table, "position", "userguid=" + PgStatement.apostrophied(user.getUserGUID()), dbCon));

        this.txtUserPhone.
                setDbCell(new DbCell(table, "userphone", "userguid=" + PgStatement.apostrophied(user.getUserGUID()), dbCon));

        this.txtUserMPhone.
                setDbCell(new DbCell(table, "usermphone", "userguid=" + PgStatement.apostrophied(user.getUserGUID()), dbCon));

        this.txtfUserEmailAddress.
                setDbCell(new DbCell(table, "mail", "userguid=" + PgStatement.apostrophied(user.getUserGUID()), dbCon));

        this.txtfUserEmailInitPass.
                setDbCell(new DbCell(table, "initmailpass", "userguid=" + PgStatement.apostrophied(user.getUserGUID()), dbCon));


        String position = this.txtUserPos.getText();
        if(!position.equals(UserHolder.getCurrentUser().getPosition())) {
           this.txtUserPos.getDbCell().update(position);
        }

        String phone = this.txtUserPhone.getText();
        if(!phone.equals(UserHolder.getCurrentUser().getUserPhone())) {
            this.txtUserPhone.getDbCell().update(phone);
        }

        String mPhone = this.txtUserMPhone.getText();
        if(!mPhone.equals(UserHolder.getCurrentUser().getUserMPhone())) {
            this.txtUserMPhone.getDbCell().update(mPhone);
        }

        String email = this.txtfUserEmailAddress.getText();
        if(!email.equals(UserHolder.getCurrentUser().getMail())) {
            this.txtfUserEmailAddress.getDbCell().update(email);
        }

        String emailPass = this.txtfUserEmailInitPass.getText();
        if(!emailPass.equals(UserHolder.getCurrentUser().getInitMailPass())) {
            this.txtfUserEmailInitPass.getDbCell().update(emailPass);
        }

        User currentUser = UserHolder.getCurrentUser();
        UserHolder.setCurrentUser(dbCon.reloadUser(currentUser));
        MainWindow mainWindow = (MainWindow) uiObjectsWrapper.retrieveObject(uiObjectsWrapper.MainWindow);
        mainWindow.changeUser();
    }
}
