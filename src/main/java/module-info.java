module com.ttzv.item {
    requires javafx.graphics;
    requires java.naming;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.web;
    requires java.desktop;
    requires jdk.charsets;
    requires jakarta.mail;
    requires org.update4j;
    requires ttzv.uiUtils;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires ttzv.propsicl; //todo: exclude from configHandler because it is already loaded in itemBootstrap

    opens com.ttzv.item to javafx.fxml;
    opens com.ttzv.item.ui to javafx.fxml;

    exports com.ttzv.item;
    exports com.ttzv.item.entity;
    exports com.ttzv.item.uiUtils;

}