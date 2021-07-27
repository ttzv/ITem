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
    requires jakarta.activation;
    requires ttzv.uiUtils;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires smsapi.lib;
    requires org.hibernate.orm.core;
    requires org.hibernate.commons.annotations;
    requires java.persistence;
    requires net.bytebuddy;
    requires com.sun.xml.bind;
    requires com.fasterxml.classmate;

    opens com.ttzv.item to javafx.fxml;
    opens com.ttzv.item.entity to org.hibernate.orm.core;
    opens com.ttzv.item.ui to javafx.fxml;

    exports com.ttzv.item;
    exports com.ttzv.item.entity;
    exports com.ttzv.item.uiUtils;
    opens com.ttzv.item.ui.controller to javafx.fxml;

}