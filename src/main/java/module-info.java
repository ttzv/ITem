module com.ttzv.itmg {
    requires annotations;
    requires javafx.graphics;
    requires java.naming;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.web;
    requires java.desktop;
    requires javax.mail.api;

    opens com.ttzv.itmg;

    exports com.ttzv.itmg.ui.mainAppWindow;
}