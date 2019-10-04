module com.ttzv.itmg {
    requires javafx.graphics;
    requires java.naming;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.web;
    requires java.desktop;
    requires jakarta.mail;
    requires ttzv.uiUtils;

    opens com.ttzv.itmg to javafx.fxml;
    opens com.ttzv.itmg.ui.mainAppWindow to javafx.fxml;
    opens com.ttzv.itmg.ui.mainAppWindow.popups to javafx.fxml;
    opens com.ttzv.itmg.ui.crmWindow to javafx.fxml;
    opens com.ttzv.itmg.ui.dbToolsWindow to javafx.fxml;
    opens com.ttzv.itmg.ui.mailerWindow to javafx.fxml;
    opens com.ttzv.itmg.ui.gSuiteWindow to javafx.fxml;
    opens com.ttzv.itmg.ui.settingsWindow to javafx.fxml;
    opens com.ttzv.itmg.ui.signWindow to javafx.fxml;

    exports com.ttzv.itmg;
    exports com.ttzv.itmg.uiUtils;

}