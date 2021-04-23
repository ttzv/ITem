module com.ttzv.item {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.naming;
    requires java.sql;
    requires java.desktop;
    requires jdk.charsets;
    requires jakarta.mail;
    requires org.update4j;
    requires ttzv.uiUtils;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires smsapi.lib;
    requires spring.context;
    requires spring.beans;

    opens com.ttzv.item to javafx.fxml, spring.core;
    opens com.ttzv.item.ui to javafx.fxml;
    opens com.ttzv.item.utility to spring.beans;

    exports com.ttzv.item;
    exports com.ttzv.item.entity;
    exports com.ttzv.item.uiUtils;

}