package ui.mainAppWindow.popups;

import ad.User;
import ad.UserHolder;
import db.DbCon;
import db.PgStatement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.SQLException;

public class UserEdit extends AnchorPane {

    private Stage stage;

    public UserEdit() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("useredit.fxml"));
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
    void btnSaveAndClose(ActionEvent event) throws SQLException {
        updateDatabase(UserHolder.getCurrentUser());
        stage.close();
    }

    private void preloadInfo(User user){
        this.txtUserPos.setText(user.getPosition());
        this.txtUserPhone.setText(user.getUserPhone());
        this.txtUserMPhone.setText(user.getUserMPhone());
    }

    private void updateDatabase(User user) throws SQLException {
        DbCon dbCon = new DbCon();
        dbCon.loadCfgCredentials();
        dbCon.initConnection();


        String position = this.txtUserPos.getText();
        String queryPos = "";
        if(!position.isEmpty()) {
            queryPos = PgStatement.update("users", "position", PgStatement.apostrophied(position), "samaccountname='" + user.getSamAccountName() + "'");
        }

        String phone = this.txtUserPhone.getText();
        String queryPhone = "";
        if(!phone.isEmpty()) {
            queryPhone = PgStatement.update("users", "userphone", PgStatement.apostrophied(phone), "samaccountname='" + user.getSamAccountName() + "'");
        }

        String mPhone = this.txtUserMPhone.getText();
        String queryMPhone = "";
        if(!mPhone.isEmpty()) {
            queryMPhone = PgStatement.update("users", "usermphone", PgStatement.apostrophied(mPhone), "samaccountname='" + user.getSamAccountName() + "'");
        }


        //System.out.println(queryPos + "\n" + queryPhone + "\n" + queryMPhone);

        dbCon.customQuery(queryPos, queryPhone, queryMPhone);
    }



}
